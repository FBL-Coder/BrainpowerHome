package cn.etsoft.smarthome.UiHelper;

import java.util.regex.Pattern;

/**
 * Author：FBL  Time： 2017/6/16.
 * Http请求返回码
 */

public class HTTPRequest_BackCode {

    //注册登陆 账号密码限制正则
    public static Pattern id_rule = Pattern.compile("(^(13\\d|15[^4\\D]|17[13678]|18\\d)\\d{8}|170[^346\\D]\\d{7})$");
    public static Pattern pass_rule = Pattern.compile("\\w{6,12}");

    //登陆
    public static final int LOGIN_OK = 0;
    public static final int LOGIN_USER_NOTFIND = 10005;
    public static final int LOGIN_ERROR = 10001;
    public static final int LOGIN_ERROR_Exception= 10002 ;

    //注册
    public static final int REGISTER_OK = 0;
    public static final int REGISTER_EXIST = 10001;

    //添加/删除联网模块
    public static final int RCUINFO_OK = 0;
    public static final int RCUINFO_ERROR = 10001;
    public static final int RCUINFO_ERROR_Exception = 10000;
    public static final int RCUINFO_NETERROR = 10002;
    public static final int RCUINFO_VERIFY_ERROR = 10006;

}
