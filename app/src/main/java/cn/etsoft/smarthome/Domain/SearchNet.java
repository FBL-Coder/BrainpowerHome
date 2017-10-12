package cn.etsoft.smarthome.Domain;

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
     * rcu_rows : [{"uid":"39ffd505484d303408650743","pass":"08650743","name":"52435530373433","IpAddr":"192.168.0.103","SubMask":"255.255.255.0","Gateway":"192.168.0.1","centerServ":"123.206.104.89","roomNum":"","macAddr":"00502a040506","SoftVersion":"","HwVersion":"","bDhcp":0}]
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
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

    public static class RcuRowsBean {
        /**
         * uid : 39ffd505484d303408650743
         * pass : 08650743
         * name : 52435530373433
         * IpAddr : 192.168.0.103
         * SubMask : 255.255.255.0
         * Gateway : 192.168.0.1
         * centerServ : 123.206.104.89
         * roomNum :
         * macAddr : 00502a040506
         * SoftVersion :
         * HwVersion :
         * bDhcp : 0
         */

        private String uid;
        private String pass;
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
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
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
