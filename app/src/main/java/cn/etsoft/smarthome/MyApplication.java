package cn.etsoft.smarthome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.Domain.Weather_All_Bean;
import cn.etsoft.smarthome.Domain.Weather_Bean;
import cn.etsoft.smarthome.NetMessage.UDPServer;
import cn.etsoft.smarthome.NetMessage.WebSocket_Client;
import cn.etsoft.smarthome.Utils.CityDB;
import cn.etsoft.smarthome.Utils.Data_Cache;
import cn.etsoft.smarthome.Utils.WratherUtil;

/**
 * Author：FBL  Time： 2017/6/12.
 */
public class MyApplication extends com.example.abc.mybaseactivity.MyApplication.MyApplication {

    /**
     * websocket 数据通信对象
     */
    private WebSocket_Client wsClient;

    /**
     * UDPServer  udp数据接受对象
     */
    private UDPServer udpServer;
    /**
     * self
     */
    public static MyApplication mApplication;
    /**
     * websocket  UDp 连接以及数据返回监听Handler
     */
    private APPHandler handler = new APPHandler(this);
    //websocket数据回调WHAT
    public int WS_OPEN_OK = 100;
    public int WS_DATA_OK = 101;
    public int WS_CLOSE = 102;
    public int WS_Error = 103;
    //UDP数据回调成功WHAT
    public int UDP_DATA_OK = 1000;
    //UDP数据发送失败
    public int UDP_NOSEND = 1003;
    //UDP接收数据失败
    public int UDP_NORECEIVE = 1004;
    //数据发送返回超时
    public int UDP_NOBACK = 5000;
    //心跳包监听返回码-局域网断开
    public int HEARTBEAT_STOP = 8000;
    //心跳包监听返回码-局域网运行
    public int HEARTBEAT_RUN = 8080;
    //全局数据
    private static WareData mWareData;

    /**
     * 天气返回数据
     */
    private Weather_All_Bean mWrather_results;
    public List<Weather_Bean> mWeathers_list;//天气图标集合
    public CityDB mCityDB;


    /**
     * 局域网内连接状态
     */
    private boolean Isheartting = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = MyApplication.this;

        //初始化天气数据
        new WratherUtil();

        //初始化SoServer
        new Thread(new Runnable() {
            @Override
            public void run() {
                jniUtils.udpServer();
            }
        }).start();

        //初始化WebSocket
        wsClient = new WebSocket_Client();
        try {
            wsClient.initSocketClient(handler);
            wsClient.connect();
        } catch (URISyntaxException e) {
            ToastUtil.showText("webSocket连接失败");
        }
        // 开启UDP数据接收服务器
        ExecutorService exec = Executors.newCachedThreadPool();
        udpServer = new UDPServer(handler);
        exec.execute(udpServer);
        //启动心跳包定时监听
        udpServer.heartBeat();


        //设置上次使用的联网模块ID；
        GlobalVars.setDevid((String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, ""));
    }

    /**
     * 获取心跳状态
     */
    public boolean isIsheartting() {
        return Isheartting;
    }

    /**
     * 设置心跳状态
     */
    public void setIsheartting(boolean isheartting) {
        Isheartting = isheartting;
    }

    /**
     * 获取天气返回数据
     */
    public Weather_All_Bean getmWrather_results() {
        return mWrather_results;
    }

    /**
     * 设置天气数据
     */
    public void setmWrather_results(Weather_All_Bean mWrather_results) {
        this.mWrather_results = mWrather_results;
    }

    /**
     * 获取CityDB
     */
    public CityDB getmCityDB() {
        return mCityDB;
    }

    /**
     * 获取全局Context
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取联网模块列表
     */
    public List<RcuInfo> getRcuInfoList() {
        String rcuinfolist_json = (String) AppSharePreferenceMgr.get(this, GlobalVars.RCUINFOLIST_SHAREPREFERENCE, "");
        List<RcuInfo> json_rcuinfolist = new Gson().fromJson(rcuinfolist_json, new TypeToken<List<RcuInfo>>() {
        }.getType());

        if (json_rcuinfolist == null || "".equals(json_rcuinfolist))
            json_rcuinfolist = new ArrayList<>();
        return json_rcuinfolist;
    }

    /**
     * 获取网络数据Handler;
     */
    public Handler getDataHandler() {
        return handler;
    }

    /**
     * 获取udp接收器对象
     */
    public UDPServer getUdpServer() {
        if (udpServer == null) {
            // 开启UDP数据接收服务器
            ExecutorService exec = Executors.newCachedThreadPool();
            udpServer = new UDPServer(handler);
            exec.execute(udpServer);
        }
        return udpServer;
    }

    /**
     * 获取WebSocket对象
     */
    public WebSocket_Client getWsClient() {
        if (wsClient == null) {
            wsClient = new WebSocket_Client();
            try {
                wsClient.initSocketClient(handler);
                wsClient.connect();
            } catch (URISyntaxException e) {
                ToastUtil.showText("webSocket连接失败");
                return null;
            }
        }
        return wsClient;
    }

    /**
     * 重置所有数据
     */
    public static void setNewWareData() {
        mWareData = new WareData();
    }

    /**
     * 获取所有数据
     */
    public static WareData getWareData() {
        if (mWareData == null) {
            try {
                mWareData = (WareData) Data_Cache.readFile(GlobalVars.getDevid());
            } catch (Exception e) {
                Log.e("Exception", "MyApplication" + e);
                mWareData = new WareData();
            }
            if (mWareData == null)
                mWareData = new WareData();
        }
        return mWareData;
    }

    /**
     * 获取加载框Dialog；
     */
    Dialog progressDialog;

    public Dialog getProgressDialog(Context context) {
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_custom_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    /**
     * 加载框显示
     */
    public void showLoadDialog(Activity activity) {
        if (progressDialog == null)
            getProgressDialog(activity);
        if (!activity.isFinishing() && !progressDialog.isShowing())
            progressDialog.show();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        };
        //加载数据进度条，5秒数据没加载出来自动消失
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (progressDialog.isShowing()) {
                        handler.sendMessage(handler.obtainMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e + "");
                }
            }
        }).start();
    }

    /**
     * 隐藏加载动画框
     */
    public void dismissLoadDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 静态Handler WebSocket以及Udp连接，数据监听
     */
    static class APPHandler extends Handler {
        private WeakReference<MyApplication> weakReference;
        private MyApplication application;
        private boolean UdpIsHaveBackData = false;
        private boolean WSIsOpen = false;

        APPHandler(MyApplication application) {
            this.weakReference = new WeakReference<>(application);
        }

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (weakReference == null)
                return;
            application = weakReference.get();
            if (msg.what == application.WS_OPEN_OK) {
                WSIsOpen = true;
                Log.e("WebSiocket", "链接成功");
            }
            if (msg.what == application.WS_CLOSE) {
                Log.e("WSException", "链接关闭" + msg.obj);
                application.wsClient = new WebSocket_Client();
                try {
                    application.wsClient.initSocketClient(application.handler);
                    application.wsClient.connect();
                } catch (URISyntaxException e) {
                    Log.e("WSException", "WebSocket链接重启失败" + e);
                }
            }
            if (msg.what == application.WS_DATA_OK) {
//                NotificationUtils.createNotif(
//                        application, R.mipmap.ic_launcher, msg.obj + "",
//                        "标题", msg.obj + "", new Intent(application, HomeActivity.class), 0, 0);
                MyApplication.mApplication.getUdpServer().webSocketData((String) msg.obj);
            }
            if (msg.what == application.WS_Error) {
                Log.e("WSException", "数据异常" + msg.obj);
                ToastUtil.showText("数据发送失败，与服务器连接已断开，请稍后再试！");
            }
            //UDP数据报
            if (msg.what == application.UDP_DATA_OK) {
                UdpIsHaveBackData = true;
                if (onGetWareDataListener != null)
                    onGetWareDataListener.upDataWareData((int) msg.obj, msg.arg1, msg.arg2);
            }
            //UDP
            if (msg.what == application.UDP_NOSEND)
                Log.e("UDPException", "UDP发送消息失败");
            //UDP接收数据异常
            if (msg.what == application.UDP_NORECEIVE)
                Log.e("UDPException", "UDP数据接收失败");
            //心跳广播停止
            if (msg.what == application.HEARTBEAT_STOP) {
                GlobalVars.setIsLAN(false);
            }
            //心跳广播运行
            if (msg.what == application.HEARTBEAT_RUN) {
                GlobalVars.setIsLAN(true);
            }
            //udp发送数据后的回调
            if (msg.what == application.UDP_NOBACK) {

                final String data = (String) msg.obj;
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!UdpIsHaveBackData && WSIsOpen) {
                            if (onUdpgetDataNoBackListener != null) {
                                onUdpgetDataNoBackListener.WSSendDatd(data);
                            }
                            timer.cancel();
                        }
                    }
                }, 3000, 3000);
            }
        }
    }

    /**
     * 实现通知数据更新的接口；
     */
    private static OnGetWareDataListener onGetWareDataListener;

    public static void setOnGetWareDataListener(OnGetWareDataListener Listener) {
        onGetWareDataListener = Listener;
    }

    public interface OnGetWareDataListener {
        void upDataWareData(int datType, int subtype1, int subtype2);
    }

    /**
     * Udp发送数据后5秒五返回
     */
    private static OnUdpgetDataNoBackListener onUdpgetDataNoBackListener;

    public static void setOnUdpgetDataNoBackListener(OnUdpgetDataNoBackListener onUdpgetDataNoBackListener) {
        MyApplication.onUdpgetDataNoBackListener = onUdpgetDataNoBackListener;
    }

    public interface OnUdpgetDataNoBackListener {
        void WSSendDatd(String msg);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Data_Cache.writeFile(GlobalVars.getDevid(), MyApplication.getWareData());
        super.onTerminate();
        onCreate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Data_Cache.writeFile(GlobalVars.getDevid(), MyApplication.getWareData());
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Data_Cache.writeFile(GlobalVars.getDevid(), MyApplication.getWareData());
        super.onTrimMemory(level);
    }
}
