package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Say GoBay on 2017/7/13.
 */
public class SaveDevControl_Result implements Serializable{

    /**
     * devUnitID : 39ffd505484d303408650743
     * datType : 6
     * subType1 : 1
     * subType2 : 1
     * dev_rows : [{"canCpuID":"36ffda054842373503711843","devName":"cec0c9fabce4313100000000","roomName":"cec0c9fabce4000000000000","devType":0,"devID":0,"bOnOff":0,"selMode":1,"selTemp":24,"selSpd":5,"selDirect":255,"rev1":24,"powChn":2}]
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    /**
     * canCpuID : 36ffda054842373503711843
     * devName : cec0c9fabce4313100000000
     * roomName : cec0c9fabce4000000000000
     * devType : 0
     * devID : 0
     * bOnOff : 0
     * selMode : 1
     * selTemp : 24
     * selSpd : 5
     * selDirect : 255
     * rev1 : 24
     * powChn : 2
     */

    private List<DevRowsBean> dev_rows;

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

    public static class DevRowsBean implements Serializable{
        private String canCpuID;
        private String devName;
        private String roomName;
        private int devType;
        private int devID;
        private int bOnOff;
        private int selMode;
        private int selTemp;
        private int selSpd;
        private int selDirect;
        private int rev1;
        private int powChn;

        public String getCanCpuID() {
            return canCpuID;
        }

        public void setCanCpuID(String canCpuID) {
            this.canCpuID = canCpuID;
        }

        public String getDevName() {
            return devName;
        }

        public void setDevName(String devName) {
            this.devName = devName;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
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

        public int getBOnOff() {
            return bOnOff;
        }

        public void setBOnOff(int bOnOff) {
            this.bOnOff = bOnOff;
        }

        public int getSelMode() {
            return selMode;
        }

        public void setSelMode(int selMode) {
            this.selMode = selMode;
        }

        public int getSelTemp() {
            return selTemp;
        }

        public void setSelTemp(int selTemp) {
            this.selTemp = selTemp;
        }

        public int getSelSpd() {
            return selSpd;
        }

        public void setSelSpd(int selSpd) {
            this.selSpd = selSpd;
        }

        public int getSelDirect() {
            return selDirect;
        }

        public void setSelDirect(int selDirect) {
            this.selDirect = selDirect;
        }

        public int getRev1() {
            return rev1;
        }

        public void setRev1(int rev1) {
            this.rev1 = rev1;
        }

        public int getPowChn() {
            return powChn;
        }

        public void setPowChn(int powChn) {
            this.powChn = powChn;
        }
    }
}
