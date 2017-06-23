package com.example.abc.mybaseactivity.MyApplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    public static Context mContext;
    private static List<Activity> activityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        initOkHttp();
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * @Description 初始化OkHttp
     */
    private void initOkHttp() {
        File cache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;


        //注释掉的是配置HTTPS所需内容；

//        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));
//        Https.SSLParams sslParams = Https.getSslSocketFactory(null, null, null);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时(单位:秒)
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
                .readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
                .pingInterval(20, TimeUnit.SECONDS) //websocket轮训间隔(单位:秒)
//                .cache(new Cache(cache.getAbsoluteFile(), cacheSize))//设置缓存
//                .cookieJar(cookieJar)//Cookies持久化
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//https配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * @Description 添加Activity到activityList，在onCreate()中调用
     */
    public static void addActivity(Activity activity) {
        if (activityList != null && activityList.size() > 0) {
            if (!activityList.contains(activity)) {
                activityList.add(activity);
            }
        } else {
            activityList.add(activity);
        }
    }

    /**
     * @Description 结束Activity到activityList，在onDestroy()中调用
     */
    public static void finishActivity(Activity activity) {
        if (activity != null && activityList != null && activityList.size() > 0) {
            activityList.remove(activity);
        }
    }

    /**
     * @Description 结束Activity到activityList，在onDestroy()中调用
     */
    public static void finishActivityByName(String activityName) {
        if (!"".equals(activityName) && activityName != null && activityList != null && activityList.size() > 0) {

            for (int i = 0; i < activityList.size(); i++) {
                if (activityName.equals(activityList.get(i).getClass().getName())) {
                    activityList.get(i).finish();
                    activityList.remove(i);
                    Log.i("FINISHActivity", activityName);
                }
            }
        }
    }

    /**
     * @Description 结束所有Activity
     */
    public static void finishAllActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (null != activity) {
                    activity.finish();
                }
            }
        }
        activityList.clear();
    }
}
