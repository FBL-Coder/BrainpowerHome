package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Say GoBay on 2017/7/25.
 */
public class SetGroupData implements Serializable{

    /**
     * devUnitID : 39ffd505484d303408650743
     * datType : 66
     * subType1 : 2
     * subType2 : 1
     * secs_trigger_rows : [{"triggerName":"d7e9bacfb4a5b7a2c6f7303f","triggerSecs":31,"triggerId":0,"valid":0,"devCnt":3,"reportServ":0,"run_dev_item":[{"canCpuID":"36ffd4054842373532790843","devID":0,"devType":0,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0},{"canCpuID":"36ffd4054842373532790843","devID":2,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0},{"canCpuID":"36ffd4054842373532790843","devID":2,"devType":4,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]}]
     * itemCnt : 1
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int itemCnt;
    /**
     * triggerName : d7e9bacfb4a5b7a2c6f7303f
     * triggerSecs : 31
     * triggerId : 0
     * valid : 0
     * devCnt : 3
     * reportServ : 0
     * run_dev_item : [{"canCpuID":"36ffd4054842373532790843","devID":0,"devType":0,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0},{"canCpuID":"36ffd4054842373532790843","devID":2,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0},{"canCpuID":"36ffd4054842373532790843","devID":2,"devType":4,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]
     */

    private List<SecsTriggerRowsBean> secs_trigger_rows;

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

    public List<SecsTriggerRowsBean> getSecs_trigger_rows() {
        return secs_trigger_rows;
    }

    public void setSecs_trigger_rows(List<SecsTriggerRowsBean> secs_trigger_rows) {
        this.secs_trigger_rows = secs_trigger_rows;
    }

    public static class SecsTriggerRowsBean implements Serializable{
        private String triggerName;
        private int triggerSecs;
        private int triggerId;
        private int valid;
        private int devCnt;
        private int reportServ;
        /**
         * canCpuID : 36ffd4054842373532790843
         * devID : 0
         * devType : 0
         * lmVal : 0
         * rev2 : 0
         * rev3 : 0
         * bOnOff : 1
         * param1 : 0
         * param2 : 0
         */

        private List<RunDevItemBean> run_dev_item;

        public String getTriggerName() {
            return triggerName;
        }

        public void setTriggerName(String triggerName) {
            this.triggerName = triggerName;
        }

        public int getTriggerSecs() {
            return triggerSecs;
        }

        public void setTriggerSecs(int triggerSecs) {
            this.triggerSecs = triggerSecs;
        }

        public int getTriggerId() {
            return triggerId;
        }

        public void setTriggerId(int triggerId) {
            this.triggerId = triggerId;
        }

        public int getValid() {
            return valid;
        }

        public void setValid(int valid) {
            this.valid = valid;
        }

        public int getDevCnt() {
            return devCnt;
        }

        public void setDevCnt(int devCnt) {
            this.devCnt = devCnt;
        }

        public int getReportServ() {
            return reportServ;
        }

        public void setReportServ(int reportServ) {
            this.reportServ = reportServ;
        }

        public List<RunDevItemBean> getRun_dev_item() {
            return run_dev_item;
        }

        public void setRun_dev_item(List<RunDevItemBean> run_dev_item) {
            this.run_dev_item = run_dev_item;
        }

        public static class RunDevItemBean {
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
