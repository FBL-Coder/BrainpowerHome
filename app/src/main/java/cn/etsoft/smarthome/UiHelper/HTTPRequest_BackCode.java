package cn.etsoft.smarthome.UiHelper;

import java.util.regex.Pattern;

/**
 * Author：FBL  Time： 2017/6/16.
 */

public class HTTPRequest_BackCode {

    public static Pattern id_rule = Pattern.compile("(^(13\\d|15[^4\\D]|17[13678]|18\\d)\\d{8}|170[^346\\D]\\d{7})$");
    public static Pattern pass_rule = Pattern.compile("\\w{6,12}");


    //登陆
    public static final int LOGIN_OK = 0;
    public static final int USER_NOTHING = 10005;
    public static final int LOGIN_ERROR = 10002 | 10001;
    //注册
    public static final int REGISTER_OK = 0;
    public static final int REGISTER_EXIST = 10006;
    //添加/删除联网模块
    public static final int RCUINFO_OK = 0;
    public static final int RCUINFO_ERROR = 10002 | 10001 | 10000;
    public static final int RCUINFO_VERIFY_ERROR = 10006;

}
