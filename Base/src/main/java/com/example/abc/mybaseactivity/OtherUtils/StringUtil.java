package com.example.abc.mybaseactivity.OtherUtils;

/**
 * @Description String类工具集合
 * @Author FBL   2017-5-2
 */

public class StringUtil {

    /**
     * @Description 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }
}
