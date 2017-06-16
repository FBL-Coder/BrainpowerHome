package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Say GoBay on 2017/6/13.
 */
public class SearchNet implements Serializable {

    /**
     * devUnitID : 39ffd505484d303408650743
     * datType : 0
     * subType1 : 0
     * subType2 : 1
     * rcu_rows : [{"canCpuID":"39ffd505484d303408650743","devUnitPass":"08650743���з�","name":"���з�","IpAddr":"192.168.0.252","SubMask":"255.255.0.0","Gateway":"192.168.0.1","centerServ":"192.168.0.100","roomNum":"0000","macAddr":"00502a040506","SoftVersion":"0000","HwVersion":"0000","bDhcp":0}]
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    /**
     * canCpuID : 39ffd505484d303408650743
     * devUnitPass : 08650743���з�
     * name : ���з�
     * IpAddr : 192.168.0.252
     * SubMask : 255.255.0.0
     * Gateway : 192.168.0.1
     * centerServ : 192.168.0.100
     * roomNum : 0000
     * macAddr : 00502a040506
     * SoftVersion : 0000
     * HwVersion : 0000
     * bDhcp : 0
     */

    private List<RcuRowsBean> rcu_rows;

    public String getDevUnitID() {
        return devUnitID;
    }

    public void setDevUnitID(String devUnitID) {
        this.devUnitID = devUnitID;
    }

    public int getDatType() {
        return datType;
    }

    public void setDatType(int datType) {
        this.datType = datType;
    }

    public int getSubType1() {
        return subType1;
    }

    public void setSubType1(int subType1) {
        this.subType1 = subType1;
    }

    public int getSubType2() {
        return subType2;
    }

    public void setSubType2(int subType2) {
        this.subType2 = subType2;
    }

    public List<RcuRowsBean> getRcu_rows() {
        return rcu_rows;
    }

    public void setRcu_rows(List<RcuRowsBean> rcu_rows) {
        this.rcu_rows = rcu_rows;
    }

    public static class RcuRowsBean implements Serializable{
        private String canCpuID;
        private String devUnitPass;
        private String name;
        private String IpAddr;
        private String SubMask;
        private String Gateway;
        private String centerServ;
        private String roomNum;
        private String macAddr;
        private String SoftVersion;
        private String HwVersion;
        private int bDhcp;

        public String getCanCpuID() {
            return canCpuID;
        }

        public void setCanCpuID(String canCpuID) {
            this.canCpuID = canCpuID;
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

        public void setIpAddr(String IpAddr) {
            this.IpAddr = IpAddr;
        }

        public String getSubMask() {
            return SubMask;
        }

        public void setSubMask(String SubMask) {
            this.SubMask = SubMask;
        }

        public String getGateway() {
            return Gateway;
        }

        public void setGateway(String Gateway) {
            this.Gateway = Gateway;
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

        public void setSoftVersion(String SoftVersion) {
            this.SoftVersion = SoftVersion;
        }

        public String getHwVersion() {
            return HwVersion;
        }

        public void setHwVersion(String HwVersion) {
            this.HwVersion = HwVersion;
        }

        public int getBDhcp() {
            return bDhcp;
        }

        public void setBDhcp(int bDhcp) {
            this.bDhcp = bDhcp;
        }
    }
}
