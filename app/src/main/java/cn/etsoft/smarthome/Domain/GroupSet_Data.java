package cn.etsoft.smarthome.Domain;

import cn.etsoft.smarthome.Utils.CommonUtils;

import java.io.Serializable;
import java.util.List;


/**
 * Created by F-B-L on 2017/5/23.
 */

public class GroupSet_Data implements Serializable {


    /**
     * devUnitID : 39ffd805484d303408580143
     * datType : 66
     * subType1 : 1
     * subType2 : 255
     * secs_trigger_rows : [{"triggerName":"ffffffffffffffffffffffff","triggerSecs":4294967295,"triggerId":255,"reportServ":255,"valid":255,"devCnt":255,"run_dev_item":[{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]},{"triggerName":"ffffffffffffffffffffffff","triggerSecs":4294967295,"triggerId":255,"reportServ":255,"valid":255,"devCnt":255,"run_dev_item":[]},{"triggerName":"ffffffffffffffffffffffff","triggerSecs":4294967295,"triggerId":255,"reportServ":255,"valid":255,"devCnt":255,"run_dev_item":[]},{"triggerName":"ffffffffffffffffffffffff","triggerSecs":4294967295,"triggerId":255,"reportServ":255,"valid":255,"devCnt":255,"run_dev_item":[]}]
     * itemCnt : 4
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int itemCnt;
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
        /**
         * triggerName : ffffffffffffffffffffffff
         * triggerSecs : 4294967295
         * triggerId : 255
         * reportServ : 255
         * valid : 255
         * devCnt : 255
         * run_dev_item : [{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]
         */

        private String triggerName;
        private long triggerSecs;
        private int triggerId;
        private int reportServ;
        private int valid;
        private int devCnt;
        private List<RunDevItemBean> run_dev_item;

        public String getTriggerName() {
            return CommonUtils.getGBstr(CommonUtils.hexStringToBytes(triggerName));
        }

        public void setTriggerName(String triggerName) {
            this.triggerName = triggerName;
        }

        public long getTriggerSecs() {
            return triggerSecs;
        }

        public void setTriggerSecs(long triggerSecs) {
            this.triggerSecs = triggerSecs;
        }

        public int getTriggerId() {
            return triggerId;
        }

        public void setTriggerId(int triggerId) {
            this.triggerId = triggerId;
        }

        public int getReportServ() {
            return reportServ;
        }

        public void setReportServ(int reportServ) {
            this.reportServ = reportServ;
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
