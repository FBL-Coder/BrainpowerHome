package cn.etsoft.smarthome.Utils;

import android.content.Context;

import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;


public class GlobalVars {


    /**
     * 存储联网模块列表
     */
    public static final String RCUINFOLIST_SHAREPREFERENCE = "RCUINFOLIST";
    /**
     * 存储使用中的联网模块ID
     */
    public static final String RCUINFOID_SHAREPREFERENCE = "RCUINFOID";
    /**
     * 存储用户ID
     */
    public static final String USERID_SHAREPREFERENCE = "USERID";
    /**
     * 存储用户PASSWORD
     */
    public static final String USERPASSWORD_SHAREPREFERENCE = "USERPASSWORD";
    /**
     * 布防类型
     */
    public static final String SAFETY_TYPE_SHAREPREFERENCE = "SAFETY_TYPE";
    /**
     * 登陆标记
     */
    public static final String LOGIN_SHAREPREFERENCE = "LOGIN_FLAG";
    /**
     * 用户配置密码
     */
    public static final String CONFIG_PASS_SHAREPREFERENCE = "CONFIG_PASS";


    public static String LOCAL_IP = "127.0.0.1";
    public static String WIFI_IP = "0.0.0.0";
    private static Context context;
    private static int sn;
    private static String userid, devid, devpass;
    private static String dstip;
    private static boolean isHeart = false;
    private static boolean isLAN = true;

    public static int IPDIFFERENT = 1,IPEQUAL = 2,NOCOMPARE = 0;
    private static int IPisEqual = NOCOMPARE;

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
        devid = (String) AppSharePreferenceMgr.get(RCUINFOID_SHAREPREFERENCE, "");
        return devid;
    }

    public static void setDevid(String devid) {
        GlobalVars.devid = devid;
    }

    public static String getUserid() {
        if (userid == null || userid == "")
            userid = (String) AppSharePreferenceMgr.get(USERID_SHAREPREFERENCE, "");
        return userid;

    }

    public static void setUserid(String userid) {
        GlobalVars.userid = userid;
    }

    public static String getDevpass() {
        if (devpass == null || devpass == "") {
            StringBuffer buffer = new StringBuffer((String) AppSharePreferenceMgr.get(RCUINFOID_SHAREPREFERENCE, ""));
            if (buffer == null || buffer.length() < 8) {
                return "";
            }
            String pass_reverse = buffer.reverse().toString().substring(0, 8);
            StringBuffer buffer_pass = new StringBuffer(pass_reverse);
            devpass = buffer_pass.reverse().toString();
        }
        return devpass;
    }

    public static void setDevpass(String devpass) {
        GlobalVars.devpass = devpass;
    }

    public static boolean isIsHeart() {
        return isHeart;
    }

    public static void setIsHeart(boolean isHeart) {
        GlobalVars.isHeart = isHeart;
    }

    public static boolean isIsLAN() {
        return isLAN;
    }

    public static void setIsLAN(boolean isLAN) {
        GlobalVars.isLAN = isLAN;
    }

    public static int getIPisEqual() {
        return IPisEqual;
    }

    public static void setIPisEqual(int IPisEqual) {
        GlobalVars.IPisEqual = IPisEqual;
    }
}
