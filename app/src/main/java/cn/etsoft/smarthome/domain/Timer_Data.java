package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

import cn.etsoft.smarthome.pullmi.common.CommonUtils;

/**
 * Created by F-B-L on 2017/5/19.
 */

public class Timer_Data implements Serializable{

    /**
     * devUnitID : 39ffd805484d303416600643
     * datType : 17
     * subType1 : 0
     * subType2 : 1
     * timerEvent_rows : [{"timSta":[17,27,0,16],"timEnd":[17,28,0,1],"timerName":"b6a8cab1c6f7300000000000","devCnt":1,"eventId":0,"valid":1,"rev3":0,"run_dev_item":[{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]},{"timSta":[0,0,0,0],"timEnd":[0,0,0,0],"timerName":"b6a8cab1c6f7310000000000","devCnt":0,"eventId":1,"valid":0,"rev3":0,"run_dev_item":[]},{"timSta":[0,0,0,0],"timEnd":[0,0,0,0],"timerName":"b6a8cab1c6f7320000000000","devCnt":0,"eventId":2,"valid":0,"rev3":0,"run_dev_item":[]},{"timSta":[0,0,0,0],"timEnd":[0,0,0,0],"timerName":"b6a8cab1c6f7330000000000","devCnt":0,"eventId":3,"valid":0,"rev3":0,"run_dev_item":[]},{"timSta":[0,0,0,0],"timEnd":[0,0,0,0],"timerName":"b6a8cab1c6f7340000000000","devCnt":0,"eventId":4,"valid":0,"rev3":0,"run_dev_item":[]},{"timSta":[0,0,0,0],"timEnd":[0,0,0,0],"timerName":"b6a8cab1c6f7350000000000","devCnt":0,"eventId":5,"valid":0,"rev3":0,"run_dev_item":[]},{"timSta":[0,0,0,0],"timEnd":[0,0,0,0],"timerName":"b6a8cab1c6f7360000000000","devCnt":0,"eventId":6,"valid":0,"rev3":0,"run_dev_item":[]},{"timSta":[0,0,0,0],"timEnd":[0,0,0,0],"timerName":"b6a8cab1c6f7370000000000","devCnt":0,"eventId":7,"valid":0,"rev3":0,"run_dev_item":[]}]
     * itemCnt : 8
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int itemCnt;
    private List<TimerEventRowsBean> timerEvent_rows;

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

    public int getItemCnt() {
        return itemCnt;
    }

    public void setItemCnt(int itemCnt) {
        this.itemCnt = itemCnt;
    }

    public List<TimerEventRowsBean> getTimerEvent_rows() {
        return timerEvent_rows;
    }

    public void setTimerEvent_rows(List<TimerEventRowsBean> timerEvent_rows) {
        this.timerEvent_rows = timerEvent_rows;
    }

    public static class TimerEventRowsBean implements Serializable {
        /**
         * timSta : [17,27,0,16]
         * timEnd : [17,28,0,1]
         * timerName : b6a8cab1c6f7300000000000
         * devCnt : 1
         * eventId : 0
         * valid : 1
         * rev3 : 0
         * run_dev_item : [{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]
         */

        private String timerName;
        private int devCnt;
        private int eventId;
        private int valid;
        private int rev3;
        private List<Integer> timSta;
        private List<Integer> timEnd;
        private List<RunDevItemBean> run_dev_item;

        public String getTimerName() {
            return CommonUtils.getGBstr(CommonUtils.hexStringToBytes(timerName));
        }

        public void setTimerName(String timerName) {
            this.timerName = timerName;
        }

        public int getDevCnt() {
            return devCnt;
        }

        public void setDevCnt(int devCnt) {
            this.devCnt = devCnt;
        }

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public int getValid() {
            return valid;
        }

        public void setValid(int valid) {
            this.valid = valid;
        }

        public int getRev3() {
            return rev3;
        }

        public void setRev3(int rev3) {
            this.rev3 = rev3;
        }

        public List<Integer> getTimSta() {
            return timSta;
        }

        public void setTimSta(List<Integer> timSta) {
            this.timSta = timSta;
        }

        public List<Integer> getTimEnd() {
            return timEnd;
        }

        public void setTimEnd(List<Integer> timEnd) {
            this.timEnd = timEnd;
        }

        public List<RunDevItemBean> getRun_dev_item() {
            return run_dev_item;
        }

        public void setRun_dev_item(List<RunDevItemBean> run_dev_item) {
            this.run_dev_item = run_dev_item;
        }

        public static class RunDevItemBean implements Serializable{
            /**
             * canCpuID : 36ffd7054842373507781843
             * devID : 8
             * devType : 3
             * lmVal : 0
             * rev2 : 0
             * rev3 : 0
             * bOnOff : 1
             * param1 : 0
             * param2 : 0
             */

            private String canCpuID;
            private int devID;
            private int devType;
            private int lmVal;
            private int rev2;
            private int rev3;
            private int bOnOff;
            private int param1;
            private int param2;

            public String getCanCpuID() {
                return canCpuID;
            }

            public void setCanCpuID(String canCpuID) {
                this.canCpuID = canCpuID;
            }

            public int getDevID() {
                return devID;
            }

            public void setDevID(int devID) {
                this.devID = devID;
            }

            public int getDevType() {
                return devType;
            }

            public void setDevType(int devType) {
                this.devType = devType;
            }

            public int getLmVal() {
                return lmVal;
            }

            public void setLmVal(int lmVal) {
                this.lmVal = lmVal;
            }

            public int getRev2() {
                return rev2;
            }

            public void setRev2(int rev2) {
                this.rev2 = rev2;
            }

            public int getRev3() {
                return rev3;
            }

            public void setRev3(int rev3) {
                this.rev3 = rev3;
            }

            public int getBOnOff() {
                return bOnOff;
            }

            public void setBOnOff(int bOnOff) {
                this.bOnOff = bOnOff;
            }

            public int getParam1() {
                return param1;
            }

            public void setParam1(int param1) {
                this.param1 = param1;
            }

            public int getParam2() {
                return param2;
            }

            public void setParam2(int param2) {
                this.param2 = param2;
            }
        }
    }
}
