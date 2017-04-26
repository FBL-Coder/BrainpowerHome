package cn.etsoft.smarthome.pullmi.app;

import android.content.Context;

import cn.etsoft.smarthome.MyApplication;

public class GlobalVars {

    private static Context context;
    private static int sn;
    private static String devid, devpass;
    private static String dstip;

    public static void setContext(Context context) {
        GlobalVars.context = context;
    }

    public static int getSn() {
        return sn;
    }

    public static void setSn(int sn) {
        GlobalVars.sn = sn;
    }

    public static String getDstip() {
        if (dstip == null || "".equals(dstip))
            return MyApplication.LOCAL_IP;
        return dstip;
    }

    public static void setDstip(String dstip) {
        GlobalVars.dstip = dstip;
    }

    public static void init(Context c) {
        context = c;
    }

    public static Context getContext() {
        return context;
    }

    public static String getDevid() {
        if (devid == null)
            return "";
        return devid;
    }

    public static void setDevid(String devid) {
        GlobalVars.devid = devid;
    }

    public static String getDevpass() {
        return devpass;
    }

    public static void setDevpass(String devpass) {
        GlobalVars.devpass = devpass;
    }

}
