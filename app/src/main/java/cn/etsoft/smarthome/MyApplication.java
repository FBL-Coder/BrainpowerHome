package cn.etsoft.smarthome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.speech.asr.SpeechConstant;
import com.example.abc.mybaseactivity.NetWorkListener.AppNetworkMgr;
import com.example.abc.mybaseactivity.Notifications.NotificationUtils;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.etsoft.smarthome.Activity.SafetyHomeActivity;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.Domain.RoomTempBean;
import cn.etsoft.smarthome.Domain.Safety_Data;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.Domain.Weather_Bean;
import cn.etsoft.smarthome.NetMessage.UDPServer;
import cn.etsoft.smarthome.NetMessage.WebSocket_Client;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.Utils.CityDB;
import cn.etsoft.smarthome.Utils.Data_Cache;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.Utils.WratherUtil;
import cn.etsoft.smarthome.yuyin.control.MyRecognizer;
import cn.etsoft.smarthome.yuyin.control.MyWakeup;
import cn.etsoft.smarthome.yuyin.recognization.MessageStatusRecogListener;
import cn.etsoft.smarthome.yuyin.recognization.PidBuilder;
import cn.etsoft.smarthome.yuyin.recognization.StatusRecogListener;
import cn.etsoft.smarthome.yuyin.util.Logger;
import cn.etsoft.smarthome.yuyin.wakeup.IWakeupListener;
import cn.etsoft.smarthome.yuyin.wakeup.RecogWakeupListener;

import static cn.etsoft.smarthome.yuyin.recognization.IStatus.STATUS_FINISHED;
import static cn.etsoft.smarthome.yuyin.recognization.IStatus.STATUS_WAKEUP_SUCCESS;
import static cn.semtec.community2.service.CloudCallServiceManager.TAG;

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
    //WebSocket 接受到数据
    public int WS_DATA_OK = 101;
    //是否是远程数据
    public int WS_CLOSE = 102;
    public int WS_Error = 103;
    //UDP数据回调成功WHAT
    public int UDP_DATA_OK = 1000;
    //UDP数据有返回
    public int UDP_HANR_DATA = 1010;
    //UDP数据发送失败
    public int UDP_NOSEND = 1003;
    //UDP接收数据失败
    public int UDP_NORECEIVE = 1004;
    //loading Dialog
    public int DIALOG_DISMISS = 2222;
    //网络判断
    public int NONET = 5555;
    //网络变化   远程-->局域网
    public int NETCHANGE_LAN = 5050;
    //网络变化   局域网-->远程
    public int NETCHANGE_LONG = 0505;
    //全局数据
    public static WareData mWareData;

    //搜索联网模块数据
    private List<RcuInfo> SeekRcuInfos;

    public List<Weather_Bean> mWeathers_list;//天气图标集合
    public CityDB mCityDB;

    //添加或者编辑设备名以及房间名
    public static String AddOrEditDevName;
    public static String AddOrEditRoomName;

    private SoundPool sp;//声明一个SoundPool
    private int music, music_wozai, music_ok, music_sorry, music_dome, music_noexist;
    //定义一个整型用load（）；来设置suondID

    //区分发82返回的0 0 1包还是发33返回的 0 0 1包，做标记
    private boolean isSeekNet = false;

    //游客登录标记
    private boolean IsVisitor = false;

    //是否可以切换联网模块
    private boolean CanChangeNet = true;

    //主页对象
    private Activity mHomeActivity;

    //设置界面密码装填
    public boolean isInputPass = false;

    //首页房间温度湿度
    private RoomTempBean mRoomTempBean;

    //软件启动时间是否足够
    public boolean isStartTimeOk = false;

    public MyWakeup getMyWakeup() {
        return myWakeup;
    }

    //语音唤醒
    public MyWakeup myWakeup;

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    public MyRecognizer myRecognizer;
    /**
     * 0: 方案1， 唤醒词说完后，直接接句子，中间没有停顿。
     * >0 : 方案2： 唤醒词说完后，中间有停顿，然后接句子。推荐4个字 1500ms
     * <p>
     * backTrackInMs 最大 15000，即15s
     */
    private int backTrackInMs = 2500;
    private int mFinalCount = 0;
    private int isback = 0;


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
        udpServer.UdpHeard();

        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.key_sound, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        music_ok = sp.load(getApplicationContext(), R.raw.ok, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        music_sorry = sp.load(getApplicationContext(), R.raw.sorry, 1);
        music_wozai = sp.load(getApplicationContext(), R.raw.wozai, 1);
        music_dome = sp.load(getApplicationContext(), R.raw.dome, 1);
        music_noexist = sp.load(getApplicationContext(), R.raw.scene_noecist, 1);

        //天气数据
        mRoomTempBean = new RoomTempBean();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                    isStartTimeOk = true;
                } catch (InterruptedException e) {

                }
            }
        }).start();
        //获取IP地址
        getIp();

        //百度语音唤醒以及语音识别
        Logger.setHandler(handler);

        StatusRecogListener recogListener = new MessageStatusRecogListener(handler);
        myRecognizer = new MyRecognizer(this, recogListener);

        IWakeupListener listener = new RecogWakeupListener(handler);
        myWakeup = new MyWakeup(this, listener);

        myWakeup.start();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
                //如果mFinalCount ==1，说明是从后台到前台
                Log.e("onActivityStarted", mFinalCount +"");
                if (mFinalCount == 1){
                    //说明从后台回到了前台
                    if (isback == 0)
                        myWakeup.start();
                    isback = 1;

                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                isback = 0;
                //如果mFinalCount ==0，说明是前台到后台
                Log.i("onActivityStopped", mFinalCount +"");
                if (mFinalCount == 0){
                    //说明从前台回到了后台
                    myWakeup.stop();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }


    public void getIp() {
        int NETWORK = AppNetworkMgr.getNetworkState(MyApplication.mContext);
        if (NETWORK == 9) {
            String hostIp = null;
            try {
                // 获取本地设备的所有网络接口
                Enumeration<NetworkInterface> enumerationNi = NetworkInterface
                        .getNetworkInterfaces();
                while (enumerationNi.hasMoreElements()) {
                    NetworkInterface networkInterface = enumerationNi.nextElement();
                    String interfaceName = networkInterface.getDisplayName();
                    Log.i("tag", "网络名字" + interfaceName);

                    // 如果是无线网卡
                    if (interfaceName.equals("wlan0")) {
                        Enumeration<InetAddress> enumIpAddr = networkInterface
                                .getInetAddresses();
                        while (enumIpAddr.hasMoreElements()) {
                            // 返回枚举集合中的下一个IP地址信息
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            // 不是回环地址，并且是ipv4的地址
                            if (!inetAddress.isLoopbackAddress()
                                    && inetAddress instanceof Inet4Address) {
                                Log.i("tag", inetAddress.getHostAddress() + "   ");
                                hostIp = inetAddress.getHostAddress();
                            }
                        }
                    }
                }
                GlobalVars.LOCAL_IP = hostIp;
            } catch (SocketException e) {
                Log.i(TAG, "getIp: 有线IP地址获取失败" + e);
            }
        } else if (NETWORK >= 10) {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            if (!ip.equals(GlobalVars.WIFI_IP)) {
                GlobalVars.WIFI_IP = ip;
            }
        }
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
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

    public int getMusic_wozai() {
        return music_wozai;
    }

    public int getMusic_ok() {
        return music_ok;
    }

    public int getMusic_sorry() {
        return music_sorry;
    }

    public int getMusic_dome() {
        return music_dome;
    }

    public int getMusic_noexist() {
        return music_noexist;
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
            udpServer.UdpHeard();
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

    public Dialog getProgressDialog(Context context, boolean isCancelable) {
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
        try {
            if (progressDialog == null)
                getProgressDialog(activity, true);
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
                        System.out.println(this.getClass().getName() + "---" + e);
                    }
                }
            }).start();
        } catch (Error e) {

        } catch (Exception ex) {

        }
    }

    /**
     * 加载框显示
     */
    public void showLoadDialog(Activity activity, boolean isCancelable) {
        try {
            getProgressDialog(activity, isCancelable);
            progressDialog.show();
            //加载数据进度条，5秒数据没加载出来自动消失
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            Message message = handler.obtainMessage();
                            message.what = DIALOG_DISMISS;
                            handler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        System.out.println(this.getClass().getName() + "---" + e);
                    }
                }
            }).start();
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏加载动画框
     */
    public void dismissLoadDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
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

    public boolean isCanChangeNet() {
        return CanChangeNet;
    }

    public void setCanChangeNet(boolean canChangeNet) {
        CanChangeNet = canChangeNet;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    CanChangeNet = true;
                } catch (InterruptedException e) {
                    CanChangeNet = false;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Activity getmHomeActivity() {
        return mHomeActivity;
    }

    public void setmHomeActivity(Activity mHomeActivity) {
        this.mHomeActivity = mHomeActivity;
    }

    public List<RcuInfo> getSeekRcuInfos() {
        if (SeekRcuInfos == null)
            SeekRcuInfos = new ArrayList<>();
        return SeekRcuInfos;
    }

    public RoomTempBean getRoomTempBean() {
        return mRoomTempBean;
    }


    public void setSeekRcuInfos(List<RcuInfo> seekRcuInfos) {
        SeekRcuInfos = seekRcuInfos;
    }




    /**
     * 静态Handler WebSocket以及Udp连接，数据监听
     */

    static class APPHandler extends Handler {
        private WeakReference<MyApplication> weakReference;
        private MyApplication application;
        private boolean WSIsOpen = false;
        private boolean WSIsAgainConnectRun;
        private int NotificationID = 10;
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private boolean isExist;

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
                WSIsOpen = false;
                WS_againConnect();
            }
            if (msg.what == application.WS_DATA_OK) {//WebSocket 数据
//                GlobalVars.setIsLAN(false);
                WSIsOpen = true;
                MyApplication.mApplication.getUdpServer().webSocketData((String) msg.obj);
            }
            if (msg.what == application.WS_Error) {
                Log.e("WSException", "数据异常" + msg.obj);
                WSIsOpen = false;
                WS_againConnect();
//                ToastUtil.showText("数据发送失败，与服务器连接已断开，请稍后再试！");
            }
            if (msg.what == STATUS_FINISHED) {
                Log.i("最终识别结果--：", msg.obj.toString());
                List<WareSceneEvent> list = WareDataHliper.initCopyWareData().getSceneControlData();
                isExist = false;
                for (int i = 0; i < list.size(); i++) {
                    if (msg.obj.toString().contains(list.get(i).getSceneName())) {
                        SendDataUtil.executelScene(list.get(i));
                        isExist = true;
                        MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic_ok(), 1, 1, 0, 0, 1);
                    }
                }
                if (!isExist) {
                    MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic_noexist(), 1, 1, 0, 0, 1);
                }
            }
            if (msg.what == STATUS_WAKEUP_SUCCESS) {
                // 此处 开始正常识别流程
                Map<String, Object> params = new LinkedHashMap<String, Object>();
                params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
                params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
                int pid = PidBuilder.create().model(PidBuilder.INPUT).toPId(); //如识别短句，不需要需要逗号，将PidBuilder.INPUT改为搜索模型PidBuilder.SEARCH
                params.put(SpeechConstant.PID, pid);
                if (application.backTrackInMs > 0) { // 方案1， 唤醒词说完后，直接接句子，中间没有停顿。
                    params.put(SpeechConstant.AUDIO_MILLS, System.currentTimeMillis() - application.backTrackInMs);
                }
                application.myRecognizer.cancel();
                application.myRecognizer.start(params);
            }


            //UDP数据报
            if (msg.what == application.UDP_DATA_OK) {
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
                if ((int) msg.obj == 32 && msg.arg1 == 1) {
                    AppSharePreferenceMgr.put(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, msg.arg2);
                }
                if (onGetWareDataListener != null)
                    onGetWareDataListener.upDataWareData((int) msg.obj, msg.arg1, msg.arg2);
            }
            //UDP
            if (msg.what == application.UDP_NOSEND)
                Log.e("UDPException", "UDP发送消息失败");
            if (msg.what == application.UDP_HANR_DATA)//UDP有数据返回
                GlobalVars.setIsLAN(true);
            //UDP接收数据异常
            if (msg.what == application.UDP_NORECEIVE)
                Log.e("UDPException", "UDP数据接收失败");
            //网络监听吐司
            if (msg.what == application.NONET) {
                ToastUtil.showText("没有可用网络，请检查", 5000);
            }
            if (msg.what == application.NETCHANGE_LONG) {//网络变化  局域网--》远程
//                Dialog();
                GlobalVars.setIsLAN(false);
            }
            if (msg.what == application.NETCHANGE_LAN) {//网络变化  远程 --》 局域网
//                Dialog();
                GlobalVars.setIsLAN(true);
                MyApplication.mApplication.getUdpServer().sendSeekNet(false);
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

        public void Dialog() {
            if (builder == null)
                builder = new AlertDialog.Builder(weakReference.get());
            if (dialog == null) {
                dialog = builder.setTitle("提示")
                        .setMessage("检测到网络改变，是否自动切换数据来源？")
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                GlobalVars.setIsLAN(true);
                            }
                        }).create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
            }
            if (!dialog.isShowing()) {//此时提示框未显示
                dialog.show();
            }
        }

        /**
         * WebSocket 重连
         */
        private void WS_againConnect() {
            if (WSIsAgainConnectRun) {
                return;
            }
            WSIsAgainConnectRun = true;
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.e("WSException", "WS尝试连接中...");
                    application.wsClient = new WebSocket_Client();
                    try {
                        application.wsClient.initSocketClient(application.handler);
                        application.wsClient.connect();
                    } catch (URISyntaxException e) {
                        Log.e("WSException", "WebSocket链接重启失败" + e);
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (; ; ) {
                        if (WSIsOpen) {
                            WSIsAgainConnectRun = false;
                            return;
                        }
                        try {
                            Thread.sleep(5000);
                            handler.sendMessage(handler.obtainMessage());
                        } catch (InterruptedException e) {
                            Log.e("WSException", "WebSocket链接重启失败" + e);
                        }
                    }
                }
            }).start();
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
                if (SAFETY_TYPE == 0) {
                    for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
                        if ((MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecType() == 0
                                || MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecType() == 1
                                || MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecType() == 2)
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
                } else {
                    for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
                        if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecType()
                                == SAFETY_TYPE
                                && MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getValid()
                                == 1) {
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
                }
                if (!"".equals(message)) {
                    message = message.substring(0, message.lastIndexOf("、"));
                    return "名称：" + message + " 触发警报";
                } else
                    return "";
            } catch (Exception e) {
//                e.printStackTrace();
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
        wsClient.closeConnect();
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
