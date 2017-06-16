package cn.etsoft.smarthome.Domain;

import android.content.Context;


public class GlobalVars {


    /**
     * 存储联网模块列表
     */
    public static  final String RCUINFOLIST_SHAREPREFERENCE = "RCUINFOLIST";
    /**
     * 存储用户ID
     */
    public static  final String USERID_SHAREPREFERENCE = "USERID";
    /**
     * 存储用户PASSWORD
     */
    public static  final String USERPASSWORD_SHAREPREFERENCE = "USERPASSWORD";


    public static String LOCAL_IP = "127.0.0.1";
    private static Context context;
    private static int sn;
    private static String devid, devpass;
    private static String dstip;
    private static boolean isLAN = true;

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
            return LOCAL_IP;
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

    public static boolean isIsLAN() {
        return isLAN;
    }

    public static void setIsLAN(boolean isLAN) {
        GlobalVars.isLAN = isLAN;
    }
}
