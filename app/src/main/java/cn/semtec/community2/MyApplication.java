package cn.semtec.community2;

import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import cn.semtec.community2.database.DBhelper;
import cn.semtec.community2.entity.HouseProperty;
import cn.semtec.community2.util.SharedPreferenceUtil;
import cn.semtec.community2.util.Util;

/**
 * 每次打包： app 套图 更换位置： 欢迎页、登录页、注册第一步、注册第二部、 来电界面、门禁图标； 软件名 ..apk名..isdebug
 * ..allowI 版本号..时间 后台ID
 * 数据库version是否需要更改
 */
public class MyApplication extends cn.etsoft.smarthome.MyApplication {
    public static MyApplication instance;
    private static SharedPreferenceUtil preference;
    private static HttpUtils httpUtils;
    //是否登录后台成功
    public static boolean logined;
    // 是否审核通过
    public static String cellphone;
    public static SQLiteDatabase db;
    public static boolean isDebug;
    public static int display_width;
    public static int display_height;
    public static float density;
    public static ArrayList<HouseProperty> houseList;
    public static HouseProperty houseProperty;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        houseList = new ArrayList<>();
        // isDebug 控制catch输出
        isDebug = true;
        LogUtils.allowI = true;
        Util.audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static SharedPreferenceUtil getSharedPreferenceUtil() {
        if (preference == null) {
            preference = new SharedPreferenceUtil(mContext);
        }
        return preference;
    }

    public static HttpUtils getHttpUtils() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils(5000);
            httpUtils.configCurrentHttpCacheExpiry(3000);
        }
        return httpUtils;
    }

    public static SQLiteDatabase getDB() {
        if (db == null || !db.isOpen()) {
            DBhelper helper = new DBhelper(instance);
            db = helper.getWritableDatabase();
        }
        return db;
    }
}
