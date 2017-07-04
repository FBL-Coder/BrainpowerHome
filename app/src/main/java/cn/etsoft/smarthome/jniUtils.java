package cn.etsoft.smarthome;

public class jniUtils {
    public static native String udpServer();

    static {
        System.loadLibrary("udp_sock");
    }
}
