package cn.etsoft.smarthome.Domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fbl on 16-11-23.
 */
public class DevControl_Result implements Serializable {

    /**
     * devUnitID : 37ffdb05424e323416702443
     * datType : 7
     * subType1 : 1
     * subType2 : 1
     * dev_rows : [{"canCpuID":"31ffdf054257313827502543","devName":"b5c636360000000000000000","roomName":"ceb4b6a8d2e5000000000000","devType":3,"devID":6,"bOnOff":0,"bTuneEn":0,"lmVal":0,"powChn":6}]
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    /**
     * canCpuID : 31ffdf054257313827502543
     * devName : b5c636360000000000000000
     * roomName : ceb4b6a8d2e5000000000000
     * devType : 3
     * devID : 6
     * bOnOff : 0
     * bTuneEn : 0
     * lmVal : 0
     * powChn : 6
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

    public static class DevRowsBean implements Serializable {
        private String canCpuID;
        private String devName;
        private String roomName;
        private int devType;
        private int devID;
        private int bOnOff;
        private int bTuneEn;
        private int lmVal;
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

        public int getBTuneEn() {
            return bTuneEn;
        }

        public void setBTuneEn(int bTuneEn) {
            this.bTuneEn = bTuneEn;
        }

        public int getLmVal() {
            return lmVal;
        }

        public void setLmVal(int lmVal) {
            this.lmVal = lmVal;
        }

        public int getPowChn() {
            return powChn;
        }

        public void setPowChn(int powChn) {
            this.powChn = powChn;
        }
    }
}
