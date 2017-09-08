package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class RcuInfo implements Serializable {

//            "uid":	"39ffd505484d303408650743",
//            "pass":	"39ffd505",
//            "name":	"cef7b0b2d1d0b7a2",
//            "IpAddr":	"192.168.0.102",
//            "SubMask":	"255.255.255.0",
//            "Gateway":	"192.168.0.1",
//            "centerServ":	"192.168.0.104",
//            "roomNum":	"",
//            "macAddr":	"00502a040506",
//            "SoftVersion":	"",
//            "HwVersion":	"",
//            "bDhcp":	0

    private String devUnitID;
    private String devUnitPass;
    private String name;
    private String IpAddr;
    private String SubMask;
    private String GateWay;
    private String centerServ;
    private String roomNum;
    private String macAddr;
    private String canCpuName;
    private String SoftVersion;
    private String HwVversion;
    private int bDhcp;
    private int rev1;
    private int rev2;

    public int getbDhcp() {
        return bDhcp;
    }

    public void setbDhcp(int bDhcp) {
        this.bDhcp = bDhcp;
    }

    public RcuInfo() {}

    public String getDevUnitID() {
        return devUnitID;
    }

    public void setDevUnitID(String devUnitID) {
        this.devUnitID = devUnitID;
    }

    public String getDevUnitPass() {
        return devUnitPass;
    }

    public void setDevUnitPass(String devUnitPass) {
        this.devUnitPass = devUnitPass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddr() {
        return IpAddr;
    }

    public void setIpAddr(String ipAddr) {
        IpAddr = ipAddr;
    }

    public String getSubMask() {
        return SubMask;
    }

    public void setSubMask(String subMask) {
        SubMask = subMask;
    }

    public String getGateWay() {
        return GateWay;
    }

    public void setGateWay(String gateWay) {
        GateWay = gateWay;
    }

    public String getCenterServ() {
        return centerServ;
    }

    public void setCenterServ(String centerServ) {
        this.centerServ = centerServ;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getSoftVersion() {
        return SoftVersion;
    }

    public void setSoftVersion(String softVersion) {
        SoftVersion = softVersion;
    }

    public String getHwVversion() {
        return HwVversion;
    }

    public void setHwVversion(String hwVversion) {
        HwVversion = hwVversion;
    }

    public String getCanCpuName() {
        return canCpuName;
    }

    public void setCanCpuName(String canCpuName) {
        this.canCpuName = canCpuName;
    }
}
