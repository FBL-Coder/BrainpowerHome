package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fbl on 16-11-23.
 */
public class UserBean implements Serializable {

    /**
     * userName : 17089111219
     * passwd : 123456
     * devUnitID : 39ffd905484d303429620443
     * datType : 66
     * subType1 : 0
     * subType2 : 0
     * dev_rows : [{"canCpuID":"36ffda054842373525871943","devType":3,"devID":4}]
     */
    private String userName;
    private String passwd;
    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    /**
     * canCpuID : 36ffda054842373525871943
     * devType : 3
     * devID : 4
     */

    private List<DevRowsBean> dev_rows;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

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

    public List<DevRowsBean> getDev_rows() {
        return dev_rows;
    }

    public void setDev_rows(List<DevRowsBean> dev_rows) {
        this.dev_rows = dev_rows;
    }

    public static class DevRowsBean implements Serializable {
        private String canCpuID;
        private int devType;
        private int devID;

        public String getCanCpuID() {
            return canCpuID;
        }

        public void setCanCpuID(String canCpuID) {
            this.canCpuID = canCpuID;
        }

        public int getDevType() {
            return devType;
        }

        public void setDevType(int devType) {
            this.devType = devType;
        }

        public int getDevID() {
            return devID;
        }

        public void setDevID(int devID) {
            this.devID = devID;
        }
    }
}
