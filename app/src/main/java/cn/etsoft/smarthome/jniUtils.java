package cn.etsoft.smarthome;

/**
 * Created by hwp on 16-9-22.
 */
public class jniUtils {
    public static native String udpServer();

    static {
        System.loadLibrary("udp_sock");
    }
}
