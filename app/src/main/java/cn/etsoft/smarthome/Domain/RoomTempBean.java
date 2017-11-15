package cn.etsoft.smarthome.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：FBL  Time： 2017/11/15.
 */

public class RoomTempBean {


    /**
     * devUnitID : 39ffd805484d303408580143
     * datType : 68
     * subType1 : 0
     * subType2 : 0
     * rcu_rows : [{"uId":"56ff74067285495632462167","roomName":"ceb4b6a8d2e5","tempVal":0,"humidity":0,"pm25":3,"pm10":3}]
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
        if (rcu_rows==null)
            rcu_rows = new ArrayList<>();
        return rcu_rows;
    }

    public void setRcu_rows(List<RcuRowsBean> rcu_rows) {
        this.rcu_rows = rcu_rows;
    }

    public static class RcuRowsBean {
        /**
         * uId : 56ff74067285495632462167
         * roomName : ceb4b6a8d2e5
         * tempVal : 0
         * humidity : 0
         * pm25 : 3
         * pm10 : 3
         */

        private String uId;
        private String roomName;
        private int tempVal;
        private int humidity;
        private int pm25;
        private int pm10;

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public int getTempVal() {
            return tempVal;
        }

        public void setTempVal(int tempVal) {
            this.tempVal = tempVal;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public int getPm10() {
            return pm10;
        }

        public void setPm10(int pm10) {
            this.pm10 = pm10;
        }
    }
}
