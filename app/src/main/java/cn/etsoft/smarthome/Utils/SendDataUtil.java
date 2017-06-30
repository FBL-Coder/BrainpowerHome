package cn.etsoft.smarthome.Utils;

import java.io.UnsupportedEncodingException;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;

/**
 * Author：FBL  Time： 2017/6/9.
 */

public class SendDataUtil {


    public static void getGroupSetInfo() {
        String GETGROUPSETINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() + "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getGroupInfo.getValue() + "," + "\"subType1\": 0," + "\"subType2\": 255" + "}";
        MyApplication.mApplication.getUdpServer().send(GETGROUPSETINFO);
    }


    public static void getSecurityInfo() {
        String GETSECURITYINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() + "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_security_info.getValue() + "," + "\"subType1\": 3," + "\"subType2\": 255" + "}";
        MyApplication.mApplication.getUdpServer().send(GETSECURITYINFO);
    }


    public static void getConditionInfo() {
        String GETCONDITIONINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() + "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getEnvEvents.getValue() + "," + "\"subType1\": 0," + "\"subType2\": 0" + "}";
        MyApplication.mApplication.getUdpServer().send(GETCONDITIONINFO);
    }


    public static void getTimerInfo() {
        String GETTIMERINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() + "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getTimerEvents.getValue() + "," + "\"subType1\": 0," + "\"subType2\": 0" + "}";
        MyApplication.mApplication.getUdpServer().send(GETTIMERINFO);
    }


    public static void getInputBoardInfo() {
        String GETINPUTBOARDINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() + "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getBoards.getValue() + "," + "\"subType1\": 0," + "\"subType2\": 1" + "}";
        MyApplication.mApplication.getUdpServer().send(GETINPUTBOARDINFO);
    }


    public static void getOutBoardInfo() {
        String GETOUTBOARDINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() + "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getBoards.getValue() + "," + "\"subType1\": 1," + "\"subType2\": 0" + "}";
        MyApplication.mApplication.getUdpServer().send(GETOUTBOARDINFO);
    }


    public static void getSceneInfo() {
        String GETSCENEINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() + "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getSceneEvents.getValue() + "," + "\"subType1\": 0," + "\"subType2\": 0" + "}";
        MyApplication.mApplication.getUdpServer().send(GETSCENEINFO);
    }

    public static void getDevInfo() {
        String GETDEVINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() + "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getDevsInfo.getValue() + "," + "\"subType1\": 0," + "\"subType2\": 0" + "}";
        MyApplication.mApplication.getUdpServer().send(GETDEVINFO);
    }

    public static void getNetWorkInfo() {
        String GETNETWORKINFO = "{\"devUnitID\": \"" + GlobalVars.getDevid() +
                "\"," + "\"datType\": " + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getRcuInfo.getValue() +
                "," + "\"subType1\": 0," + "\"subType2\": 0" + "}";
        MyApplication.mApplication.getUdpServer().send(GETNETWORKINFO);
    }

    public static void controlDev(WareDev dev, int cmd) {
        String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"canCpuID\":\"" + dev.getCanCpuId() + "\"" +
                ",\"devType\":" + dev.getType() +
                ",\"devID\":" + dev.getDevId() +
                ",\"cmd\":" + cmd +
                "}";
        MyApplication.mApplication.getUdpServer().send(ctlStr);
    }

    public static void deleteDev(WareDev dev) {
        String str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_delDev.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":0," +
                "\"canCpuID\":\"" + dev.getCanCpuId() + "\"," +
                "\"devType\":" + dev.getType() + "," +
                "\"devID\":" + dev.getDevId() + "," +
                "\"cmd\":" + 1 + "}";
        MyApplication.mApplication.getUdpServer().send(str);
    }

    public static void executelScene(WareSceneEvent event) {
        String str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_exeSceneEvents.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"eventId\":" + event.getEventId() + "}";
        MyApplication.mApplication.getUdpServer().send(str);
    }

    public static void addscene(int sceneid, String name) {
        byte[] data = {0};
        try {
            data = name.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str_gb = CommonUtils.bytesToHexString(data);
        String str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"sceneName\":\"" + str_gb + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_addSceneEvents.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"eventId\":" + sceneid +
                ",\"devCnt\":" + 0 +
                ",\"itemAry\":[{" +
                "\"uid\":\"\"" +
                ",\"devType\":" + 0 +
                ",\"devID\":" + 0 +
                ",\"bOnOff\":" + 0 +
                ",\"lmVal\":0" +
                ",\"param1\":0" +
                ",\"param2\":0" +
                "}]}";
        MyApplication.mApplication.getUdpServer().send(str);
    }

    public static void deleteScene(WareSceneEvent event) {
        String name = CommonUtils.bytesToHexString(event.getSceneName().getBytes());
        String str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"sceneName\":\"" + name + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_delSceneEvents.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"eventId\":" + event.getEventId() +
                ",\"devCnt\":" + 0 +
                ",\"itemAry\":[{" +
                "\"uid\":\"\"" +
                ",\"devType\":" + 0 +
                ",\"devID\":" + 0 +
                ",\"bOnOff\":" + 0 +
                ",\"lmVal\":0" +
                ",\"param1\":0" +
                ",\"param2\":0" +
                "}]}";
        MyApplication.mApplication.getUdpServer().send(str);
    }

    public static void getKeyItemInfo(int key_index, String uid) {
        //uid  按键板所在的输入板id
        final String str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getKeyOpItems.getValue() + "," +
                "\"uid\":" + "\"" + uid + "\"," +
                "\"subType1\":0," +
                "\"subType2\":0," +
                "\"key_index\":" + key_index + "}";
        MyApplication.mApplication.getUdpServer().send(str);
    }

    public static void getChnItemInfo(String uid, WareDev dev) {
        //uid  设备所在输出板ID
        final String str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getChnOpItems.getValue() + "," +
                "\"uid\":" + "\"" + uid + "\"," +
                "\"subType1\":0," +
                "\"subType2\":0," +
                "\"devID\":" + dev.getDevId() + "," +
                "\"devType\":" + dev.getType() + "}";
        MyApplication.mApplication.getUdpServer().send(str);
    }


}
