package cn.etsoft.smarthome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.example.abc.mybaseactivity.NetWorkListener.AppNetworkMgr;
import com.example.abc.mybaseactivity.Notifications.NotificationUtils;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.etsoft.smarthome.Activity.SafetyHomeActivity;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.Domain.Safety_Data;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.Domain.Weather_Bean;
import cn.etsoft.smarthome.NetMessage.UDPServer;
import cn.etsoft.smarthome.NetMessage.WebSocket_Client;
import cn.etsoft.smarthome.Utils.CityDB;
import cn.etsoft.smarthome.Utils.Data_Cache;
import cn.etsoft.smarthome.Utils.GetIPAddress;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.Utils.WratherUtil;

/**
 * Author：FBL  Time： 2017/6/12.
 * 全局 MyApplication
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
    //loading Dialog
    public int DIALOG_DISMISS = 2222;
    //网络判断
    public int NONET = 5555;
    //全局数据
    private static WareData mWareData;

    //上次使用的联网模快ID;
    private static RcuInfo rcuInfo_Use;

    public List<Weather_Bean> mWeathers_list;//天气图标集合
    public CityDB mCityDB;

    //添加或者编辑设备名以及房间名
    public static String AddOrEditDevName;
    public static String AddOrEditRoomName;

    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID

    /**
     * 局域网内连接状态
     */
    private boolean Isheartting = false;

    /**
     * 情景控制页面是否可见；
     */
    private boolean SceneIsShow = false;

    //区分发82返回的0 0 1包还是发33返回的 0 0 1包，做标记
    private boolean isSeekNet = false;

    //游客登录标记
    private boolean IsVisitor = false;

    //主页对象
    private Activity mHomeActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = MyApplication.this;
        /**
         * 腾讯 bugly
         */
        CrashReport.initCrashReport(MyApplication.getContext(), "c8a123eb0c", false);
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

        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.key_sound, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        queryIP();
    }

    public static void queryIP() {
        //设置上次使用的联网模块ID；
        GlobalVars.setDevid((String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, ""));
        List<RcuInfo> rcuInfos = MyApplication.mApplication.getRcuInfoList();
        for (int i = 0; i < rcuInfos.size(); i++) {
            if (GlobalVars.getDevid().equals(rcuInfos.get(i).getDevUnitID())) {
                rcuInfo_Use = rcuInfos.get(i);
            }
        }
        int NETWORK = AppNetworkMgr.getNetworkState(MyApplication.mContext);
        String IPAddress = "";
        if (NETWORK == 0) {
            ToastUtil.showText("请检查网络连接");
        } else if (NETWORK != 0 && NETWORK < 10) {//数据流量
            IPAddress = GetIPAddress.getLocalIpAddress();
        } else {
            IPAddress = GetIPAddress.getWifiIP(MyApplication.mContext);
        }
        if ("".equals(IPAddress))
            GlobalVars.setIPisEqual(GlobalVars.NOCOMPARE);
        else {
            String rcuInfo_Use_ip = rcuInfo_Use.getIpAddr();
            if ("".equals(rcuInfo_Use_ip) || rcuInfo_Use_ip == null) {
                GlobalVars.setIPisEqual(GlobalVars.IPDIFFERENT);
                return;
            }
            rcuInfo_Use_ip = rcuInfo_Use_ip.substring(0, rcuInfo_Use_ip.lastIndexOf("."));

            IPAddress = IPAddress.substring(0, IPAddress.lastIndexOf("."));
            if (rcuInfo_Use_ip.equals(IPAddress)) {//ip前三位一样，即局域网内的；
                GlobalVars.setIPisEqual(GlobalVars.IPEQUAL);
                SendDataUtil.getNetWorkInfo();
            } else {//网段不一样，公网；
                GlobalVars.setIPisEqual(GlobalVars.IPDIFFERENT);
            }
        }
    }


    public SoundPool getSp() {
        if (sp == null) {
            sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        }
        return sp;
    }

    public int getMusic() {
        if (music == 0) {
            music = sp.load(this, R.raw.key_sound, 1);
        }
        return music;
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
     * 获取CityDB
     */
    public CityDB getmCityDB() {
        return mCityDB;
    }

    /**
     * 获取全局 Context
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

    public void setRcuInfoList(List<RcuInfo> list) {
        Gson gson = new Gson();
        String str = gson.toJson(list);
        AppSharePreferenceMgr.put(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, str);
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
    public synchronized static WareData getWareData() {
        if (mWareData == null) {
            if (!"".equals(AppSharePreferenceMgr.get(GlobalVars.USERID_SHAREPREFERENCE, "")))
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

    public Dialog getProgressDialog(Context context,boolean isCancelable) {
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_custom_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }

    /**
     * 加载框显示
     */
    public void showLoadDialog(Activity activity) {
        if (progressDialog == null)
            getProgressDialog(activity,false);
        if (!activity.isFinishing() && !progressDialog.isShowing())
            progressDialog.show();
        //加载数据进度条，5秒数据没加载出来自动消失
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        Message message = handler.obtainMessage();
                        message.what = DIALOG_DISMISS;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    System.out.println(this.getClass().getName() +"---"+ e );
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

    public static String getAddOrEditDevName() {
        return AddOrEditDevName;
    }

    public static void setAddOrEditDevName(String addOrEditDevName) {
        AddOrEditDevName = addOrEditDevName;
    }

    public static String getAddOrEditRoomName() {
        return AddOrEditRoomName;
    }

    public static void setAddOrEditRoomName(String addOrEditRoomName) {
        AddOrEditRoomName = addOrEditRoomName;
    }

    public boolean isSceneIsShow() {
        return SceneIsShow;
    }

    public void setSceneIsShow(boolean sceneIsShow) {
        SceneIsShow = sceneIsShow;
    }

    public boolean isSeekNet() {
        return isSeekNet;
    }

    public void setSeekNet(boolean seekNet) {
        isSeekNet = seekNet;
    }

    public boolean isVisitor() {
        return IsVisitor;
    }

    public void setVisitor(boolean visitor) {
        IsVisitor = visitor;
    }

    public Activity getmHomeActivity() {
        return mHomeActivity;
    }

    public void setmHomeActivity(Activity mHomeActivity) {
        this.mHomeActivity = mHomeActivity;
    }

    /**
     * 静态Handler WebSocket以及Udp连接，数据监听
     */


    static class APPHandler extends Handler {
        private WeakReference<MyApplication> weakReference;
        private MyApplication application;
        private boolean UdpIsHaveBackData = false;
        private boolean WSIsOpen = false;
        private int NotificationID = 10;
        private long time_WebSocket = 0;

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
//                if (System.currentTimeMillis() - time_WebSocket < 2000)
//                    return;
//                time_WebSocket = System.currentTimeMillis();
                Log.e("WSException", "链接关闭" + msg.obj);
                application.wsClient = new WebSocket_Client();
                try {
                    application.wsClient.initSocketClient(application.handler);
                    application.wsClient.connect();
                } catch (URISyntaxException e) {
                    Log.e("WSException", "WebSocket链接重启失败" + e);
                }
            }
            if (msg.what == application.WS_DATA_OK) {//WebSocket 数据
//                Log.i(TAG, "handleMessage: " + msg.obj);
                MyApplication.mApplication.getUdpServer().webSocketData((String) msg.obj);
            }
            if (msg.what == application.WS_Error) {
                Log.e("WSException", "数据异常" + msg.obj);
//                ToastUtil.showText("数据发送失败，与服务器连接已断开，请稍后再试！");
            }
            //UDP数据报
            if (msg.what == application.UDP_DATA_OK) {
                UdpIsHaveBackData = true;
                if ((int) msg.obj == 32 && msg.arg1 == 2) {
                    String contont = Safety_Baojing();
                    if ("".equals(contont)) {
                        return;
                    }
                    NotificationUtils.createNotif(
                            MyApplication.mApplication, R.drawable.notification, "报警",
                            "警报", contont, new Intent(MyApplication.mApplication, SafetyHomeActivity.class), NotificationID, 0);
                    NotificationID++;
                }
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
            //网络监听吐司
            if (msg.what == application.NONET) {
                ToastUtil.showText("没有可用网络，请检查",5000);
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

            //load 超时后自动消失
            if (msg.what == application.DIALOG_DISMISS) {
                if (application.progressDialog != null && application.progressDialog.isShowing()) {
                    application.progressDialog.dismiss();
                    ToastUtil.showText("发送超时");
                    application.progressDialog = null;
                }
            }
        }

        /**
         * 获取警报信息
         *
         * @return 用户可见数据
         */
        @SuppressLint("WrongConstant")
        public String Safety_Baojing() {
            //报警提示信息
            String message = "";
            //布防类型；
            int SAFETY_TYPE = 0;
            String index = Integer.toBinaryString(MyApplication.getWareData().getSafetyResult_alarm().getSecDat());
            StringBuffer index_sb = new StringBuffer(index).reverse();
//            index_sb = index_sb.reverse();
            for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
                if (index_sb.length() <= i)
                    index_sb.append("0");
            }
//            SAFETY_TYPE = MyApplication.getWareData().getSafetyResult_alarm().getSecStatus();
            SAFETY_TYPE = (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 255);
            if (SAFETY_TYPE == 255) {
                return "";
            }
            Safety_Data safetyData;
            try {
                safetyData = Data_Cache.readFile_safety(GlobalVars.getDevid(), false);
            } catch (Exception e) {
                safetyData = new Safety_Data();
            }
            try {
                for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
                    if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecType() == SAFETY_TYPE
                            && MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getValid() == 1) {
                        boolean IsContain = false;
                        for (int j = 0; j < index_sb.length(); j++) {
                            if (i == j && '1' == index_sb.charAt(j)) {
                                IsContain = true;
                            }
                        }
                        if (IsContain) {
                            message += MyApplication.getWareData().getResult_safety()
                                    .getSec_info_rows().get(i).getSecName() + "、";
                            if (safetyData == null)
                                safetyData = new Safety_Data();
                            Safety_Data.Safety_Time time = safetyData.new Safety_Time();
                            Calendar cal = Calendar.getInstance();
                            time.setYear(cal.get(Calendar.YEAR));
                            time.setMonth(cal.get(Calendar.MONTH) + 1);
                            time.setDay(cal.get(Calendar.DAY_OF_MONTH));
                            time.setH(cal.get(Calendar.HOUR_OF_DAY));
                            time.setM(cal.get(Calendar.MINUTE));
                            time.setS(cal.get(Calendar.SECOND));
                            time.setSafetyBean(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i));

                            safetyData.getSafetyTime().add(time);
                            Data_Cache.writeFile_safety(GlobalVars.getDevid(), safetyData);
                        }
                    }
                }
                if (!"".equals(message)) {
                    message = message.substring(0, message.lastIndexOf("、"));
                    return "名称：" + message + " 触发警报";
                } else
                    return "";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("MyApp" + "Safety_Baojing()" + e);
                return "";
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
