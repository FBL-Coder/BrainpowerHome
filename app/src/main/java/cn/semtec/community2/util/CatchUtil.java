package cn.semtec.community2.util;

import cn.semtec.community2.MyApplication;

public class CatchUtil {

    public static void catchM(Throwable e) {
        if (MyApplication.isDebug) {
            e.printStackTrace();
        } else {
            //计划做成发邮件到邮箱
        }
    }

}
