package cn.etsoft.smarthome.Utils;

import android.annotation.SuppressLint;
import android.graphics.ColorMatrixColorFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import cn.etsoft.smarthome.Domain.GlobalVars;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

//import com.example.smarthouse.MyApplication;
//import com.example.smarthouse.R.string;

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getName();

    public static String mLocalAddr = "";
    public static String mRemoteAddr = "";
    public static String mRemoteIp = "";
    public static String mLocalIp = "";
    public static int m_bSoftVerOld = 0;

    public static void setRemoteAddr(String addr) {
        mRemoteAddr = addr;
    }

    public static void setRemoteIp(String Ip) {
        mRemoteIp = Ip;
    }

    public static String byteToBit(byte b) {
        return "" + ((b >> 7) & 0x1) + ((b >> 6) & 0x1) + ((b >> 5) & 0x1) + ((b >> 4) & 0x1) + ((b >> 3) & 0x1)
                + ((b >> 2) & 0x1) + ((b >> 1) & 0x1) + ((b >> 0) & 0x1);
    }

    public static boolean isAsk(String str) {
        String s1 = str.substring(str.length() - 2, str.length() - 1);
        String s2 = str.substring(str.length() - 1, str.length());

        return ("0".equals(s1) && "1".equals(s2)) ? true : false;
    }

    public static boolean isReply(String str) {
        String s1 = str.substring(str.length() - 2, str.length() - 1);
        String s2 = str.substring(str.length() - 1, str.length());

        return ("1".equals(s1) && "0".equals(s2)) ? true : false;
    }

    public static void CommonUtils_init() {
        @SuppressLint("WifiManagerLeak") WifiManager wifi = (WifiManager) GlobalVars.getContext().getSystemService(GlobalVars.getContext().WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipStr = (ip & 0xff) + "." + ((ip >> 8) & 0xff) + "." + ((ip >> 16) & 0xff) + "." + ((ip >> 24) & 0xff);
        CommonUtils.mLocalIp = ipStr;
    }

    public static String getLocalIp() {
        return mLocalIp;
    }

    public static String getLocalAddr() {
        // return "S0001010802000000000";
        return mLocalAddr;
    }

    public static String getRemoteAddr() {
        // return "M0001010";
        return mRemoteAddr;
    }

    public static String getRemoteIp() {
        return mRemoteIp;
    }

    public static final OnTouchListener TouchDark = new OnTouchListener() {

        public final float[] BT_SELECTED = new float[]{1, 0, 0, 0, -50, 0, 1, 0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0};
        public final float[] BT_NOT_SELECTED = new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
                v.setBackgroundDrawable(v.getBackground());
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
                v.setBackgroundDrawable(v.getBackground());
            }
            return false;
        }
    };

    public static final OnTouchListener TouchLight = new OnTouchListener() {

        public final float[] BT_SELECTED = new float[]{1, 0, 0, 0, 50, 0, 1, 0, 0, 50, 0, 0, 1, 0, 50, 0, 0, 0, 1, 0};
        public final float[] BT_NOT_SELECTED = new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
                v.setBackgroundDrawable(v.getBackground());
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
                v.setBackgroundDrawable(v.getBackground());
            }
            return false;
        }
    };

    public static byte[] getZerobyte() {

        byte[] data = new byte[0];

        return data;
    }

    public static String getGBstr(String str) {
        try {
            return new String(str.getBytes(), "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getGBstr(byte[] bytes) {
        try {
            return new String(bytes, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

//    public static void sendMsg(final String msg) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DatagramPacket packet;
//                try {
//                    packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
//                            InetAddress.getByName("127.0.0.1"), 8400);
//
//                    MyApplication.mInstance.getSocket().send(packet);
//                } catch (UnknownHostException e) {
//
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    public static String printHexString(byte[] b) {
        String a = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            a = a + hex;
        }

        return a;
    }

    /**
     * cast byte array into int value
     *
     * @param value
     * @return
     */
    public static int byteToInt(byte[] value) {
        int length = value.length;
        int intValue = value[length - 1] & 0xff;

        for (int i = length - 2; i >= 0; i--) {
            intValue = (intValue << 8 * (length - i - 1)) | (value[i] & 0xff);
        }

        return intValue;
    }

    public static void shortAry2byteAry(byte[] byteAry, short[] shortAry, int shortLen) {
        byte[] b = byteAry;
        short[] s = shortAry;
        int j = 0;
        for (int i = 0; i < shortLen; i++) {
            b[j] = (byte) (s[i] & 0x00ff);
            j++;
            b[j] = (byte) (s[i] >> 8);
            j++;
        }
    }

    /**
     * Highg bit is ahead
     *
     * @param value
     * @return
     */
    public static int byteToIntHB(byte[] value) {
        int length = value.length;
        int intValue = value[0] & 0xff;
        for (int i = 1; i < length; i++) {
            intValue = (intValue << 8 * i) | (value[i] & 0xff);
        }
        return intValue;
    }

    // 形如0001转换为1
    public static int byteCharToInt(byte[] value) {
        int length = value.length;
        int intValue = 0;
        for (int i = 0; i < length; i++) {
            intValue += (int) ((value[i] - 0x30) * (Math.pow(10, length - i - 1)));
        }
        return intValue;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * cast byte array to string
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Get str from byte
     *
     * @param data
     * @return
     */
    public static String getStr(byte[] data) {
        int dataLen = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                dataLen = i;
                break;
            }
        }
        try {
            return new String(data, 0, dataLen, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * create a UDP packet, cmd: NSORDER, CMD PROPER: ASK, total length 56 bytes
     *
     * @param localAddr
     * @param localIp
     * @param remoteAddr
     * @param remoteIp
     * @throws UnknownHostException
     */
    public static byte[] createNsorderAsk(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[56];

        // header
        String header = "XXXCID";
        byte[] headerData = header.getBytes();
        copyBytes(headerData, data, 0, 0, headerData.length);

        // cmd
        data[0 + 6] = (byte) (Constants.UDP_CMD.NSORDER.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);

        // local address
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 8, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(getLocalIp()).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 28, localIpBytes.length);

        // remote addr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 32 + 0, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = new byte[4];
        for (byte b : remoteIpBytes) {
            b = (byte) (0 & 0xff);
        }
        copyBytes(remoteIpBytes, data, 0, 52 + 0, remoteIpBytes.length);

        return data;
    }

    /**
     * create a reply UDP packet, CMD: NSORDER, CMD Property:REPLY, total length
     * 57 bytes
     *
     * @param localAddr
     * @param localIp
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createNsorderReply(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[57];

        // header
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // cmd
        data[0 + 6] = (byte) (Constants.UDP_CMD.NSORDER.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.REPLY.getValue() & 0xff);

        // remote addr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        Log.e(TAG, "" + bytesToHexString(remoteAddrBytes));
        copyBytes(remoteAddrBytes, data, 0, 0 + 8, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 28, remoteIpBytes.length);

        // room machine count
        data[0 + 32] = (byte) (1 & 0xff);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        Log.e(TAG, bytesToHexString(localAddrBytes));
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        Log.e(TAG, "remoteAddr: " + new String(remoteAddrBytes) + ", remote ip: " + new String(remoteIp)
                + ", localAddr: " + new String(localAddr) + ", localIp: " + new String(localIp));

        return data;
    }

    /**
     * create a UDP packet, CMD: VIDEOTALK, CMD Property: ASK, SUB_CDM:CALL,
     * with length 62 bytes
     *
     * @param localAddr
     * @param localIp
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideoTaklAskCall(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[62];

        // header
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // CMD property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub CMD
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALL.getValue() & 0xff);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 9, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 29, localIpBytes.length);

        // remote addr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 33, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = remoteIp.getBytes();
        copyBytes(remoteIpBytes, data, 0, 0 + 53, remoteIpBytes.length);

        // reserved byte
        data[0 + 57] = (byte) (0 & 0xff);

        // braocast group
        byte[] broadcastBytes = InetAddress.getByName("255.255.255.255").getAddress();
        copyBytes(broadcastBytes, data, 0, 0 + 58, broadcastBytes.length);

        return data;
    }

    /**
     * create as UDP packet, CMD: VIDEOTALK, Cmd property: ASK, SUB CMD: LINEUSE
     *
     * @param localAddr
     * @param
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideotalkAskLineuse(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[57];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.LINEUSE.getValue() & 0xff);

        // remoteAddr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 9, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 29, remoteIpBytes.length);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        return data;
    }

    /**
     * create as UDP packet, CMD: VIDEOTALK, Cmd property: ASK, SUB CMD:
     * CALLANSWER
     *
     * @param localAddr
     * @param
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideotalkAskCallanswer(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[62];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLANSWER.getValue() & 0xff);

        // remoteAddr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 9, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 29, remoteIpBytes.length);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        // reserve byte
        data[0 + 57] = (byte) (0 & 0xff);

        // broadcast group
        byte[] broadcastBytes = InetAddress.getByName("255.255.255.255").getAddress();
        copyBytes(broadcastBytes, data, 0, 0 + 58, broadcastBytes.length);

        return data;
    }

    /**
     * create as UDP packet, CMD: VIDEOTALK, Cmd property: ASK, SUB CMD:
     * CALLSTART
     *
     * @param localAddr
     * @param
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideotalkAskCallstart(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[57];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLSTART.getValue() & 0xff);

        // remoteAddr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 9, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 29, remoteIpBytes.length);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        return data;
    }

    /**
     * create as UDP packet, CMD: VIDEOTALK, Cmd property: REPLY, SUB CMD:
     * CALLSTART
     *
     * @param localAddr
     * @param
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideotalkReplyCallstart(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[57];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.REPLY.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLSTART.getValue() & 0xff);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 9, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 29, localIpBytes.length);

        // remote addr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 33, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 53, remoteIpBytes.length);

        return data;
    }

    /**
     * create a UDP packet, CMD: VIDEOTALK, Cmd property: ASK, SUB CMD:
     * CALLCONFIRM, with length 61 bytes
     *
     * @param localAddr
     * @param localIp
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideotalkAskCallconfirm(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[61];

        // header
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // cmd
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLCONFIRM.getValue() & 0xff);

        // remote addr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 9, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 29, remoteIpBytes.length);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        // reserved
        byte[] confSns = new byte[4];
        for (byte b : confSns) {
            b = (byte) (0 & 0xff);
        }
        copyBytes(confSns, data, 0, 0 + 57, confSns.length);

        return data;

    }

    /**
     * create a UDP packet, send callconfirm reply packet
     *
     * @param localAddr
     * @param localIp
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideotalkReplyCallconfirm(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[61];

        // header
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // cmd
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.REPLY.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLCONFIRM.getValue() & 0xff);

        // remote addr
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 9, remoteAddrBytes.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 29, remoteIpBytes.length);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        // reserved
        byte[] confSns = new byte[4];
        for (byte b : confSns) {
            b = (byte) (0 & 0xff);
        }
        copyBytes(confSns, data, 0, 0 + 57, confSns.length);

        return data;

    }

    /**
     * create a UDP packet, reply callend
     *
     * @param localAddr
     * @param localIp
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideotalkReplyCallend(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[57];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.REPLY.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLEND.getValue() & 0xff);

        // remote addr
        byte[] remoteAddrByte = remoteAddr.getBytes();
        copyBytes(remoteAddrByte, data, 0, 0 + 9, remoteAddrByte.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 29, remoteIpBytes.length);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        return data;
    }

    public static byte[] createVideoWatchAskCallend(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[57];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        // copyBytes(headerBytes, data, 0, 0, headerBytes.length);
        System.arraycopy(headerBytes, 0, data, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOWATCH.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLEND.getValue() & 0xff);

        // remote addr
        byte[] remoteAddrByte = remoteAddr.getBytes();
        // copyBytes(remoteAddrByte, data, 0, 0+32, remoteAddrByte.length);
        System.arraycopy(remoteAddrByte, 0, data, 0 + 9, remoteAddrByte.length);

        // remote ip
        byte[] remoteIpByte = InetAddress.getByName(remoteIp).getAddress();
        // copyBytes(localIpByte, data, 0, 0+28, localIpByte.length);
        System.arraycopy(remoteIpByte, 0, data, 0 + 29, remoteIpByte.length);

        // local addr
        byte[] localAddrByte = localAddr.getBytes();
        // copyBytes(localAddrByte, data, 0, 0+8, localAddrByte.length);
        System.arraycopy(localAddrByte, 0, data, 0 + 33, localAddrByte.length);

        // local ip
        byte[] localIpByte = InetAddress.getByName(localIp).getAddress();
        // copyBytes(localIpByte, data, 0, 0+28, localIpByte.length);
        System.arraycopy(localIpByte, 0, data, 0 + 53, localIpByte.length);

        return data;
    }

    public static byte[] createVideoWatchNsOrderAsk(String localAddr, String localIp, String remoteAddr)
            throws UnknownHostException {
        byte[] data = new byte[56];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        // copyBytes(headerBytes, data, 0, 0, headerBytes.length);
        System.arraycopy(headerBytes, 0, data, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.NSORDER.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);

        // local addr
        byte[] localAddrByte = localAddr.getBytes();
        // copyBytes(localAddrByte, data, 0, 0+8, localAddrByte.length);
        System.arraycopy(localAddrByte, 0, data, 0 + 8, localAddrByte.length);

        // local ip
        byte[] localIpByte = InetAddress.getByName(localIp).getAddress();
        // copyBytes(localIpByte, data, 0, 0+28, localIpByte.length);
        System.arraycopy(localIpByte, 0, data, 0 + 28, localIpByte.length);

        // remote addr
        byte[] remoteAddrByte = remoteAddr.getBytes();
        // copyBytes(remoteAddrByte, data, 0, 0+32, remoteAddrByte.length);
        System.arraycopy(remoteAddrByte, 0, data, 0 + 32, remoteAddrByte.length);

        data[0 + 52] = (byte) (0);
        data[0 + 53] = (byte) (0);
        data[0 + 54] = (byte) (0);
        data[0 + 55] = (byte) (0);

        return data;
    }

    public static byte[] createVideoWatchAskCall(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[62];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        System.arraycopy(headerBytes, 0, data, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOWATCH.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALL.getValue() & 0xff);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 9, localAddrBytes.length);

        // local ip byte
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 29, localIpBytes.length);

        // remote addr byte
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 33, remoteAddrBytes.length);

        // remote ip byte
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 53, remoteIpBytes.length);

        // reserved
        data[0 + 57] = (byte) (0 & 0xff);

        // group bytes
        data[0 + 58] = (byte) (236 & 0xff);
        data[0 + 59] = (byte) (168 & 0xff);
        data[0 + 60] = (byte) (3 & 0xff);
        data[0 + 61] = (byte) (119 & 0xff);

        return data;
    }

    public static byte[] createVideoWatchAskCallconfirm(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[61];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        System.arraycopy(headerBytes, 0, data, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOWATCH.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLCONFIRM.getValue() & 0xff);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 9, localAddrBytes.length);

        // local ip byte
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 29, localIpBytes.length);

        // remote addr byte
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 33, remoteAddrBytes.length);

        // remote ip byte
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 53, remoteIpBytes.length);

        data[0 + 57] = (byte) (0 & 0xff);
        data[0 + 58] = (byte) (0 & 0xff);
        data[0 + 59] = (byte) (0 & 0xff);
        data[0 + 60] = (byte) (0 & 0xff);

        return data;
    }

    public static byte[] createVideoWatchReplyCallconfirm(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[61];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(data, headerBytes, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOWATCH.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.REPLY.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLCONFIRM.getValue() & 0xff);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 9, localAddrBytes.length);

        // local ip byte
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 29, localIpBytes.length);

        // remote addr byte
        byte[] remoteAddrBytes = remoteAddr.getBytes();
        copyBytes(remoteAddrBytes, data, 0, 0 + 33, remoteAddrBytes.length);

        // remote ip byte
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 53, remoteIpBytes.length);

        data[0 + 57] = (byte) (0 & 0xff);
        data[0 + 58] = (byte) (0 & 0xff);
        data[0 + 59] = (byte) (0 & 0xff);
        data[0 + 60] = (byte) (0 & 0xff);

        return data;
    }

    public static int createVideoTalkAskCalldown(byte[] buff, String localAddr, String localIp, String remoteAddr,
                                                 String remoteIp, int timestamp, int dataType, int frameNo, int frameLen, int totalPackages, int currPackage,
                                                 int dataLen, int packLen, byte[] audioData) throws UnknownHostException {
        byte[] data = buff;// new byte[150];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLDOWN.getValue() & 0xff);

        // remote addr
        byte[] remoteAddrByte = remoteAddr.getBytes();
        copyBytes(remoteAddrByte, data, 0, 0 + 9, remoteAddrByte.length);
        // remote addr ip
        byte[] remoteIpByte = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpByte, data, 0, 0 + 29, remoteIpByte.length);
        // remote addr
        byte[] localAddrByte = localAddr.getBytes();
        copyBytes(localAddrByte, data, 0, 0 + 33, localAddrByte.length);
        // local ip
        byte[] localIpByte = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpByte, data, 0, 0 + 53, localIpByte.length);

        // timestamp
        /*
         * byte[] timestampByte = new byte[4]; timestampByte[3] =
		 * (byte)((timestamp>>24) & 0xff); timestampByte[2] =
		 * (byte)((timestamp>>16) & 0xff); timestampByte[1] =
		 * (byte)((timestamp>>8) & 0xff); timestampByte[0] = (byte)((timestamp)
		 * & 0xff); copyBytes(timestampByte, data, 0, 57, timestampByte.length);
		 */
        data[57] = (byte) ((timestamp) & 0xff);
        data[58] = (byte) ((timestamp >> 8) & 0xff);
        data[59] = (byte) ((timestamp >> 16) & 0xff);
        data[60] = (byte) ((timestamp >> 24) & 0xff);

        // data type
        /*
         * byte[] dataTypeByte = new byte[2]; dataTypeByte[1] = (byte)((dataType
		 * >> 8) & 0xff); dataTypeByte[0] = (byte)((dataType) & 0xff);
		 * copyBytes(dataTypeByte, data, 0, 61, dataTypeByte.length);
		 */
        data[61] = (byte) ((dataType) & 0xff);
        data[62] = (byte) ((dataType >> 8) & 0xff);

        // frame no
        /*
         * byte[] frameNoByte = new byte[2]; frameNoByte[1] = (byte)((frameNo >>
		 * 8) & 0xff); frameNoByte[0] = (byte)(frameNo & 0xff);
		 * copyBytes(frameNoByte, data, 0, 63, frameNoByte.length);
		 */
        data[63] = (byte) ((frameNo) & 0xff);
        data[64] = (byte) ((frameNo >> 8) & 0xff);

        // frame len
        /*
         * byte[] frameLenByte = new byte[4]; frameLenByte[3] = (byte)((frameLen
		 * >> 24) & 0xff); frameLenByte[2] = (byte)((frameLen >> 16) & 0xff);
		 * frameLenByte[1] = (byte)((frameLen >> 8) & 0xff); frameLenByte[0] =
		 * (byte)((frameLen) & 0xff); copyBytes(frameLenByte, data, 0, 65,
		 * frameLenByte.length);
		 */
        data[65] = (byte) ((frameLen) & 0xff);
        data[66] = (byte) ((frameLen >> 8) & 0xff);
        data[67] = (byte) ((frameLen >> 16) & 0xff);
        data[68] = (byte) ((frameLen >> 24) & 0xff);

        // total package
		/*
		 * byte[] totalPackageByte = new byte[2]; totalPackageByte[1] =
		 * (byte)((totalPackages >> 8) & 0xff); totalPackageByte[0] =
		 * (byte)((totalPackages) & 0xff); copyBytes(totalPackageByte, data, 0,
		 * 69, totalPackageByte.length);
		 */
        data[69] = (byte) ((totalPackages) & 0xff);
        data[70] = (byte) ((totalPackages >> 8) & 0xff);

        // curr package
		/*
		 * byte[] currpackageByte = new byte[2]; currpackageByte[1] =
		 * (byte)((currPackage >> 8) & 0xff); currpackageByte[0] =
		 * (byte)((currPackage) & 0xff); copyBytes(currpackageByte, data, 0, 71,
		 * currpackageByte.length);
		 */
        data[71] = (byte) ((currPackage) & 0xff);
        data[72] = (byte) ((currPackage >> 8) & 0xff);

        // data len
		/*
		 * byte[] dataLenByte = new byte[2]; dataLenByte[1] = (byte)((dataLen >>
		 * 8) & 0xff); dataLenByte[0] = (byte)((dataLen) & 0xff);
		 * copyBytes(dataLenByte, data, 0, 73, dataLenByte.length);
		 */
        data[73] = (byte) ((dataLen) & 0xff);
        data[74] = (byte) ((dataLen >> 8) & 0xff);

        // pack len
		/*
		 * byte[] packLenByte = new byte[2]; packLenByte[1] = (byte)((packLen >>
		 * 8) & 0xff); packLenByte[0] = (byte)((packLen) & 0xff);
		 * copyBytes(packLenByte, data, 0, 75, packLenByte.length);
		 */
        data[75] = (byte) ((packLen) & 0xff);
        data[76] = (byte) ((packLen >> 8) & 0xff);

        data[77] = (byte) (32 & 0xff);
        data[78] = (byte) (0 & 0xff);
        data[79] = (byte) (34 & 0xff);
        data[80] = (byte) (0 & 0xff);

        // audioData
        copyBytes(audioData, data, 0, 86, audioData.length);

        return 150;
    }

    public static byte[] createOpenLock(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[57];
        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.REMOTEOPENLOCK.getValue() & 0xff);

        // remote addr
        byte[] remoteAddrByte = remoteAddr.getBytes();
        copyBytes(remoteAddrByte, data, 0, 0 + 9, remoteAddrByte.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 29, remoteIpBytes.length);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local Ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        return data;
    }

    /**
     * create a UDP packet, ask callend
     *
     * @param localAddr
     * @param localIp
     * @param remoteAddr
     * @param remoteIp
     * @return
     * @throws UnknownHostException
     */
    public static byte[] createVideotalkAskCallend(String localAddr, String localIp, String remoteAddr, String remoteIp)
            throws UnknownHostException {
        byte[] data = new byte[57];

        // header bytes
        byte[] headerBytes = "XXXCID".getBytes();
        copyBytes(headerBytes, data, 0, 0, headerBytes.length);

        // CMD
        data[0 + 6] = (byte) (Constants.UDP_CMD.VIDEOTALK.getValue() & 0xff);
        // cmd property
        data[0 + 7] = (byte) (Constants.SUB_CMD.ASK.getValue() & 0xff);
        // sub cmd
        data[0 + 8] = (byte) (Constants.SUB_CMD.CALLEND.getValue() & 0xff);

        // remote addr
        byte[] remoteAddrByte = remoteAddr.getBytes();
        copyBytes(remoteAddrByte, data, 0, 0 + 9, remoteAddrByte.length);

        // remote ip
        byte[] remoteIpBytes = InetAddress.getByName(remoteIp).getAddress();
        copyBytes(remoteIpBytes, data, 0, 0 + 29, remoteIpBytes.length);

        // local addr
        byte[] localAddrBytes = localAddr.getBytes();
        copyBytes(localAddrBytes, data, 0, 0 + 33, localAddrBytes.length);

        // local ip
        byte[] localIpBytes = InetAddress.getByName(localIp).getAddress();
        copyBytes(localIpBytes, data, 0, 0 + 53, localIpBytes.length);

        return data;
    }

    /**
     * determine whether the header is XXXCID
     *
     * @param header
     * @return
     */
    public static boolean isHeaderRight(byte[] header) {

        if (header == null) {
            return false;
        } else if ("XXXCID".equals(new String(header))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * determine whether the header is head
     *
     * @param header
     * @return
     */
    public static boolean isControlHeaderRight(byte[] header) {

        if (header == null) {
            return false;
        } else if ("head".equals(new String(header))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * copy bytes from srcStart index for src bytes to dstStart index for dst
     * bytes with data of he length
     *
     * @param src
     * @param dst
     * @param srcStart
     * @param dstStart
     * @param length
     */
    public static void copyBytes(byte[] src, byte[] dst, int srcStart, int dstStart, int length) {
        for (int i = 0; i < length; i++) {
            dst[dstStart + i] = src[srcStart + i];
        }
    }
}
