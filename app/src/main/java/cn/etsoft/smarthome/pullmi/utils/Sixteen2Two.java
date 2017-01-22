package cn.etsoft.smarthome.pullmi.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fbl on 16-11-8.
 */
public class Sixteen2Two {

    public static List<Integer> decimal2Binary(int decimalSource) {
        BigInteger bi = new BigInteger(String.valueOf(decimalSource));  //转换成BigInteger类型
        char[] charArray = bi.toString(2).toCharArray(); //参数2指定的是转化成X进制，默认10进制
        List<Integer> Switch = new ArrayList<>();
        for (int i = 0; i < charArray.length; i++) {
            if ('1' == charArray[charArray.length - 1 - i]) {
                Switch.add(i);
            }
        }
        return Switch;
    }
}
