package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

import cn.etsoft.smarthome.pullmi.common.CommonUtils;

/**
 * Created by Say GoBay on 2017/4/21.
 */
public class SetSafetyResult implements Serializable {

    /**
     * devUnitID : 39ffd505484d303408650743
     * datType : 32
     * subType1 : 4
     * subType2 : 255
     * sec_info_rows : [{"secName":"b7c0c7f83000000000000000","secType":1,"sceneId":0,"secDev":0,"run_dev_item":[{"canCpuID":"36ffd4054842373532790843","devID":4,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]},{"secName":"b7c0c7f83100000000000000","secType":255,"sceneId":0,"secDev":0,"run_dev_item":[]},{"secName":"b7c0c7f83200000000000000","secType":255,"sceneId":0,"secDev":0,"run_dev_item":[]},{"secName":"b7c0c7f83300000000000000","secType":255,"sceneId":0,"secDev":0,"run_dev_item":[]},{"secName":"b7c0c7f83400000000000000","secType":255,"sceneId":0,"secDev":0,"run_dev_item":[]},{"secName":"b7c0c7f83500000000000000","secType":255,"sceneId":0,"secDev":0,"run_dev_item":[]},{"secName":"b7c0c7f83600000000000000","secType":255,"sceneId":0,"secDev":0,"run_dev_item":[]},{"secName":"b7c0c7f83700000000000000","secType":255,"sceneId":0,"secDev":0,"run_dev_item":[]},{"secName":"b7c0c7f83800000000000000","secType":255,"sceneId":0,"secDev":0,"run_dev_item":[]}]
     * itemCnt : 9
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int itemCnt;
    /**
     * secName : b7c0c7f83000000000000000
     * secType : 1
     * sceneId : 0
     * secDev : 0
     * run_dev_item : [{"canCpuID":"36ffd4054842373532790843","devID":4,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]
     */

    private List<SecInfoRowsBean> sec_info_rows;

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

    public List<SecInfoRowsBean> getSec_info_rows() {
        return sec_info_rows;
    }

    public void setSec_info_rows(List<SecInfoRowsBean> sec_info_rows) {
        this.sec_info_rows = sec_info_rows;
    }

    public static class SecInfoRowsBean implements Serializable {
        private String secName;
        private int secType;
        private int sceneId;
        private int secDev;
        /**
         * canCpuID : 36ffd4054842373532790843
         * devID : 4
         * devType : 3
         * lmVal : 0
         * rev2 : 0
         * rev3 : 0
         * bOnOff : 1
         * param1 : 0
         * param2 : 0
         */

        private List<RunDevItemBean> run_dev_item;

        public String getSecName() {
            if (secName == null)
                return "";
            return CommonUtils.getGBstr(CommonUtils.hexStringToBytes(secName));
        }

        public void setSecName(String secName) {
            this.secName = secName;
        }

        public int getSecType() {
            return secType;
        }

        public void setSecType(int secType) {
            this.secType = secType;
        }

        public int getSceneId() {
            return sceneId;
        }

        public void setSceneId(int sceneId) {
            this.sceneId = sceneId;
        }

        public int getSecDev() {
            return secDev;
        }

        public void setSecDev(int secDev) {
            this.secDev = secDev;
        }

        public List<RunDevItemBean> getRun_dev_item() {
            return run_dev_item;
        }

        public void setRun_dev_item(List<RunDevItemBean> run_dev_item) {
            this.run_dev_item = run_dev_item;
        }

        public static class RunDevItemBean implements Serializable {
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
