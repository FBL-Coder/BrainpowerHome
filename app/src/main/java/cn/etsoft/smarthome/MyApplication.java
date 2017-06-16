package cn.etsoft.smarthome;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.etsoft.smarthome.domain.ChnOpItem_scene;
import cn.etsoft.smarthome.domain.City;
import cn.etsoft.smarthome.domain.Out_List_printcmd;
import cn.etsoft.smarthome.domain.SetSafetyResult;
import cn.etsoft.smarthome.domain.User;
import cn.etsoft.smarthome.domain.Weather_All_Bean;
import cn.etsoft.smarthome.domain.Weather_Bean;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.RcuInfo;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.ui.GroupActivity;
import cn.etsoft.smarthome.ui.WelcomeActivity;
import cn.etsoft.smarthome.utils.CityDB;

/**
 * 作者：FBL  时间： 2016/10/30.
 */
public class MyApplication extends Application implements udpService.Callback, NetBroadcastReceiver.NetEvent {

    /**
     * 网络状态是否改变的监听
     */
    public static NetBroadcastReceiver.NetEvent event;
    /**
     * 服务对象
     */
    private MyServiceConn conn;
    /**
     * Application 对象
     */
    public static MyApplication mInstance;
    /**
     * 全局数据
     */
    private static WareData wareData;
    /**
     * 数据变更handler
     */
    private Handler handler = null;
    /**
     * 启动服务Handler
     */
    private Handler mhandler = null;
    /**
     * udpService 对象
     */
    private udpService service;
    /**
     * 全局Context
     */
    private static Context context;
    /**
     * 欢迎页面   这里只是为了销毁
     */
    private WelcomeActivity activity;
    private GroupActivity groupActivity;
    private DatagramSocket socket = null;
    public static int local_server_flag = -1; //0 local 1 server
    /**
     * 数据中，设备房间的集合
     */
    private static List<String> room_list;

    /**
     * 主页的Activity对象
     */
    private static Activity mHomeActivity;
    /**
     * 联网模块大于一个的时候，保存最近使用的联网模块ID；
     */
    private String devUnitID;
    /**
     * 本地IP地址
     */
    public static String LOCAL_IP = "127.0.0.1";

    private Weather_All_Bean results;

    private List<Weather_Bean> weathers_list;
    private CityDB mCityDB;
    private List<City> mCityList;
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<City>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;
    private static final String FORMAT = "^[a-z,A-Z].*$";
    private static SharedPreferences sharedPreferences;
    private boolean readData = false;
    //是否处理35的包
    private boolean IsDispose35 = false;

    private RcuInfo rcuInfo;

    public void setRcuInfo(RcuInfo rcuInfo) {
        this.rcuInfo = rcuInfo;
    }

    public RcuInfo getRcuInfo() {
        return rcuInfo;
    }

    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID
    private int music1;//定义一个整型用load（）；来设置suondID
    //区分发82返回的0 0 1包还是发33返回的 0 0 1包，做标记
    private boolean isSearch;
    public boolean isSearch() {
        return isSearch;
    }
    public void setSearch(boolean search) {
        isSearch = search;
    }

    /**
     * 情景备用全局数据
     */
    private static WareData wareData_scene;

    public static WareData getWareData_Scene() {
        if (wareData_scene == null)
            return new WareData();
        return wareData_scene;
    }

    public static void setWareData_Scene(WareData wareData_scene) {
        MyApplication.wareData_scene = wareData_scene;
    }


    /**
     * 安防设置备用全局数据--添加设备
     */
    private List<WareDev> safety_data_dev;

    public List<WareDev> getSafety_data_dev() {
        if (safety_data_dev == null)
            return new ArrayList<>();
        return safety_data_dev;
    }

    public void setSafety_data_dev() {
        onGetWareDataListener_safety.upDataWareData();
        this.safety_data_dev = safety_data_dev;
    }

    /**
     * 联网模块--搜索
     */
    private List<WareDev> searchNet_data_dev;

    public List<WareDev> getSearchNet_data_dev() {
        return searchNet_data_dev;
    }

    public void setSearchNet_data_dev(List<WareDev> searchNet_data_dev) {
        this.searchNet_data_dev = searchNet_data_dev;
    }

    /**
     * 安防设置备用全局数据--安防界面
     */
    private SetSafetyResult setSafetyResult;

    public SetSafetyResult getSetSafetyResult() {
        if (setSafetyResult == null)
            return new SetSafetyResult();
        return setSafetyResult;
    }

    public void setSetSafetyResult(SetSafetyResult setSafetyResult) {
        this.setSafetyResult = setSafetyResult;
    }

    /**
     * 控制设置备用全局数据-输入；
     */
    private List<WareKeyOpItem> input_key_data;

    public List<WareKeyOpItem> getInput_key_data() {
        if (input_key_data == null)
            return new ArrayList<>();
        return input_key_data;
    }

    public void setInput_key_data(List<WareKeyOpItem> input_key_data) {
        this.input_key_data = input_key_data;
    }

    /**
     * 控制设置备用全局数据-按键情景；
     */
    private ChnOpItem_scene key_scene_data;

    public ChnOpItem_scene getKey_scene_data() {
        return key_scene_data;
    }

    public void setKey_scene_data(ChnOpItem_scene key_scene_data) {
        this.key_scene_data = key_scene_data;
    }


    /**
     * 控制设置备用全局数据-输出；
     */
    private List<Out_List_printcmd> out_key_data;

    public List<Out_List_printcmd> getOut_key_data() {
        return out_key_data;
    }

    public void setOut_key_data(List<Out_List_printcmd> out_key_data) {
        this.out_key_data = out_key_data;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 腾讯 bugly
         */
        CrashReport.initCrashReport(getApplicationContext(), "f623f31b48", false);
        /**
         * 初始化网络状态监听
         */
        event = MyApplication.this;
        /**
         * 初始化Application
         */
        mInstance = MyApplication.this;
        /**
         * 初始化上下文；
         */
        MyApplication.context = getApplicationContext();

        /**
         * GlobalVars 这也是全局变量，这里只是给全局Context
         */
        GlobalVars.init(this.getApplicationContext());

        /**
         * 初始化工具类
         */
        CommonUtils.CommonUtils_init();

        //初始化城市
        initCityList();
//        mLocationClient = new LocationClient(getApplicationContext());
        /**始化Socket端口
         * 初
         */
        try {
            socket = new DatagramSocket(8080);
        } catch (SocketException e) {
//            e.printStackTrace();
        }

        /**
         * 启动底层C库 服务
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                jniUtils.udpServer();
            }
        }).start();

        /**
         * 天气对应的图标id
         */
        weathers_list = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 43; i++) {
                    Weather_Bean bean = new Weather_Bean();
                    bean.setCode(i);
                    weathers_list.add(bean);
                }
            }
        }).start();

        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.key_sound, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        music1 = sp.load(this, R.raw.jingbao, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
    }

    public SoundPool getSp() {
        if (sp == null) {
            sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        }
        return sp;
    }

    public int getMusic() {
        if (music == 0) {
            music = sp.load(getActivity(), R.raw.key_sound, 1);
        }
        return music;
    }

    public int getMusic1() {
        if (music1 == 0) {
            music1 = sp.load(getActivity(), R.raw.jingbao, 1);
        }
        return music1;
    }

    private void initCityList() {
        mCityList = new ArrayList<City>();
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<City>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mCityDB = openCityDB();// 这个必须最先复制完,所以我放在单线程中处理
                prepareCityList();
            }
        }).start();
    }

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + MyApplication.getContext().getPackageName() + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        if (!db.exists()) {
            // L.i("db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }

    public boolean isReadData() {
        return readData;
    }

    public void setReadData(boolean readData) {
        this.readData = readData;
    }

    public boolean isDispose35() {
        return IsDispose35;
    }

    public void setDispose35(boolean dispose35) {
        IsDispose35 = dispose35;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                IsDispose35 = false;
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15000);
                    handler.sendMessage(handler.obtainMessage());
                } catch (Exception e) {
                    System.out.println(e + "");
                }
            }
        }).start();
    }

    private boolean prepareCityList() {
        mCityList = mCityDB.getAllCity();// 获取数据库中所有城市
        for (City city : mCityList) {
            String firstName = city.getFirstPY();// 第一个字拼音的第一个字母
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(city);
                } else {
                    mSections.add(firstName);
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put(firstName, list);
                }
            } else {
                if (mSections.contains("#")) {
                    mMap.get("#").add(city);
                } else {
                    mSections.add("#");
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put("#", list);
                }
            }
        }
        Collections.sort(mSections);// 按照字母重新排序
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }
        return true;
    }

    public synchronized CityDB getCityDB() {
        if (mCityDB == null)
            mCityDB = openCityDB();
        return mCityDB;
    }

    public String getDevUnitID() {
        return devUnitID;
    }

    public void setDevUnitID(String devUnitID) {
        this.devUnitID = devUnitID;
    }


    /**
     * 获取Socket对象
     *
     * @return
     */
    public DatagramSocket getSocket() {
        return socket;
    }

    public GroupActivity getGroupActivity() {
        return groupActivity;
    }

    public void setGroupActivity(GroupActivity groupActivity) {
        this.groupActivity = groupActivity;
    }

    /**
     * 获取欢迎页面的对象
     *
     * @return
     */
    public WelcomeActivity getActivity() {
        return activity;
    }

    /**
     * 赋值欢迎页面
     *
     * @param activity
     */
    public void setActivity(WelcomeActivity activity) {
        this.activity = activity;
    }

    /**
     * 获取全局Context
     *
     * @return
     */
    public static Context getContext() {
        return MyApplication.context;
    }


    /**
     * 数据服务的回调，接收到数据后触发此方法；
     *
     * @param what     根据what判断数据  What== datType
     * @param wareData 数据本身
     */
    @Override
    public void getWareData(int what, WareData wareData) {
        MyApplication.wareData = wareData;
        onGetWareDataListener.upDataWareData(what);

        if (what == 100) {
            Intent intent = new Intent();
            intent.setPackage("cn.etsoft.smarthome");
            intent.setAction("service.socket.com");
            startService(intent);
        }

        //在任何页面，触发安防警报要发出警报信息
        if (what == 32 && MyApplication.getWareData().getSafetyResult_alarm() != null && MyApplication.getWareData().getSafetyResult_alarm().getSubType1() == 2) {
            int SecDat = MyApplication.getWareData().getSafetyResult_alarm().getSecDat();
            //对SecDat进行二进制转码，数字为1的位置即为触发警报的防区
            String SecDatList = Integer.toBinaryString(SecDat);

            Intent intent = new Intent();
            if (!SecDatList.contains("1"))
                return;
            intent.putExtra("index", SecDatList);
            intent.setPackage("cn.etsoft.smarthome");
            intent.setAction("cc.test.com");
            startService(intent);
            MyApplication.getWareData().setSafetyResult_alarm(null);
        }
    }

    /**
     * 获取主页对象
     *
     * @return
     */
    public static Activity getmHomeActivity() {
        return mHomeActivity;
    }

    /**
     * 赋值主页对象
     *
     * @param mHomeActivity
     */
    public static void setmHomeActivity(Activity mHomeActivity) {
        MyApplication.mHomeActivity = mHomeActivity;
    }

    public Weather_All_Bean getResults() {
        return results;
    }

    public void setResults(Weather_All_Bean results) {
        this.results = results;
    }

    public List<Weather_Bean> getWeathers_list() {
        return weathers_list;
    }


    /**
     * 用户注册发送数据
     *
     * @param id  user id
     * @param pos user password
     */
    public static void addUserData(String id, String pos) {
        final String str = "{" +
                "\"userName\":\"" + id + "\"," +
                "\"passwd\":\"" + pos + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_regeditUser.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":0}";
        sendMsg(str);
    }

    /**
     * 用户登录发送数据
     *
     * @param id  user id
     * @param pos user password
     */
    public static void sendUserData(String id, String pos) {
        final String str = "{" +
                "\"userName\":\"" + id + "\"," +
                "\"passwd\":\"" + pos + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_loginUser.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":0}";
        sendMsg(str);
    }

    /**
     * 启动应用并且启动服务后  发送的数据包；
     */
    public static void setRcuDevIDtoLocal() {
        sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String appid = sharedPreferences.getString("appid", "");
        final String str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getBroadCast.getValue() + "," +
                "\"uuid\":\"" + appid + "\"," +
                "\"subType1\":0," +
                "\"subType2\":0}";
        if ("".equals(GlobalVars.getDevid()) || "".equals(GlobalVars.getDevpass()))
            return;
        sendMsg(str);
    }

    public static void getSceneInfo() {
        final String scene_str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getSceneEvents.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":0}";
        sendMsg(scene_str);
    }

    /**
     * 获取输入板对应设备的数据包
     *
     * @param key_index
     * @param uid
     */
    public static void getKeyItemInfo(int key_index, String uid) {
        final String key_str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getKeyOpItems.getValue() + "," +
                "\"uid\":" + "\"" + uid + "\"," +
                "\"subType1\":0," +
                "\"subType2\":0," +
                "\"key_index\":" + key_index + "}";
        sendMsg(key_str);
    }

    /**
     * 获取设备对应的输出板数据包
     *
     * @param uid
     * @param devType
     * @param devID
     */
    public static void getChnItemInfo(String uid, int devType, int devID) {
        final String chn_str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getChnOpItems.getValue() + "," +
                "\"uid\":" + "\"" + uid + "\"," +
                "\"subType1\":0," +
                "\"subType2\":0," +
                "\"devID\":" + devID + "," +
                "\"devType\":" + devType + "}";
        sendMsg(chn_str);
    }

    /**
     * 获取用户面板数据
     */
    public static void getUserData() {
//        {
//              "userName": "17089111219",
//              "passwd": "123456",
//              "devUnitID": "39ffd905484d303429620443",
//              "datType": 66,
//              "subType1": 0,
//              "subType2": 0
//        }
        sharedPreferences = context.getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        User user = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class);
        final String chn_str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"passwd\":\"" + user.getPass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getShortcutKey.getValue() + "," +
                "\"userName\":\"" + user.getId() + "\"," +
                "\"subType1\":0," +
                "\"subType2\":0" +
                "}";
        sendMsg(chn_str);
    }


    /**
     * 发送消息的方法；
     *
     * @param msg
     */

    static int count = 0;

    public static void sendMsg(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket packet = null;
                try {
                    Log.i("IPAdd", "命令的IP地址 : " + GlobalVars.getDstip() + "----发送的消息 : " + msg);
                    packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
                            InetAddress.getByName(GlobalVars.getDstip()), 8400);
                    if (packet != null)
                        MyApplication.mInstance.getSocket().send(packet);
                } catch (Exception e) {
                    Log.e("Exception", "" + e);
                }
            }
        }).start();

    }

    public static void setWareData(WareData wareData) {
        MyApplication.wareData = wareData;
    }


    public static List<String> getRoom_list() {
        if (room_list == null) {
            return new ArrayList<>();
        }
        return room_list;
    }

    public static void setRoom_list(List<String> room_list) {
        MyApplication.room_list = room_list;
    }


    public static WareData getWareData() {
        if (wareData == null) {
            return new WareData();
        }
        return wareData;
    }

    public Handler getAllHandler() {
        return handler;
    }

    public void setAllHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * 启动服务；
     */
    public void startSer() {
        conn = new MyServiceConn();
        bindService(new Intent(this, udpService.class), conn, BIND_AUTO_CREATE);
    }


    @Override
    public void onNetChange(int netMobile) {
        if (isAppRunning(this)) {
            //TODO  网络状态发生变化
            setRcuDevIDtoLocal();
        }
    }

    /**
     * 判断当前程序是否还在运行
     *
     * @param context
     * @return
     */
    private boolean isAppRunning(Context context) {
        String packageName = context.getPackageName();
        String topActivityClassName = getTopActivityName(context);

        if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    /**
     * 获取数据服务
     */
    public final class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // TODO Auto-generated method stub
            service = ((udpService.LocalBinder) binder).getService();
            service.runUdpServer();
            // 将当前activity添加到接口集合中
            service.addCallback(MyApplication.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            service = null;
        }
    }

    /**
     * 实现通知数据更新的接口；
     */
    private static OnGetWareDataListener onGetWareDataListener;

    public void setOnGetWareDataListener(OnGetWareDataListener Listener) {
        onGetWareDataListener = Listener;
    }

    public interface OnGetWareDataListener {
        void upDataWareData(int what);
    }

    /**
     * 防区模块的回调
     */
    private static OnGetWareDataListener_safety onGetWareDataListener_safety;

    public void setOnGetWareDataListener_safety(OnGetWareDataListener_safety Listener) {
        onGetWareDataListener_safety = Listener;
    }

    public interface OnGetWareDataListener_safety {
        void upDataWareData();
    }
}
