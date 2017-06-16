package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

import cn.etsoft.smarthome.pullmi.common.CommonUtils;

/**
 * Created by Say GoBay on 2017/4/21.
 * 查询联网模块防区信息bean类
 */
public class SetSafetyResult implements Serializable {


    /**
     * devUnitID : 39ffd805484d303416600643
     * datType : 32
     * subType1 : 4
     * subType2 : 255
     * sec_info_rows : [{"secName":"313233367373730000000000","secCode":"0202000236ffd70548423735","secType":120,"secId":7,"sceneId":24,"secDev":67,"itemCnt":3,"valid":0,"run_dev_item":[{"canCpuID":"0b00000036ffd70548423735","devID":3,"devType":7,"lmVal":120,"rev2":24,"rev3":67,"bOnOff":0,"param1":0,"param2":0},{"canCpuID":"0700000036ffd70548423735","devID":3,"devType":7,"lmVal":120,"rev2":24,"rev3":67,"bOnOff":0,"param1":0,"param2":0},{"canCpuID":"0b00000036ffd70548423735","devID":3,"devType":7,"lmVal":120,"rev2":24,"rev3":67,"bOnOff":0,"param1":0,"param2":0}]},{"secName":"36ffd7054842373507781843","secCode":"030002000901000036ffd705","secType":66,"secId":72,"sceneId":55,"secDev":53,"itemCnt":7,"valid":67,"run_dev_item":[]},{"secName":"fe276d1470a55371ff276d14","secCode":"30abb9349fe4601402000000","secType":165,"secId":112,"sceneId":83,"secDev":113,"itemCnt":72,"valid":20,"run_dev_item":[]},{"secName":"9fe460140200000090fa3e71","secCode":"48276d1448276d1490fa3e71","secType":40,"secId":117,"sceneId":109,"secDev":20,"itemCnt":104,"valid":52,"run_dev_item":[]},{"secName":"48276d1450853671ff276d14","secCode":"68abb93417e3601448276d14","secType":0,"secId":105,"sceneId":0,"secDev":0,"itemCnt":0,"valid":0,"run_dev_item":[]},{"secName":"17e3601448276d1469000000","secCode":"00000000b7c0c7f836000000","secType":0,"secId":0,"sceneId":0,"secDev":0,"itemCnt":255,"valid":0,"run_dev_item":[]},{"secName":"bac300000000000000000000","secCode":"00ff000040a801cc077f0000","secType":168,"secId":64,"sceneId":1,"secDev":204,"itemCnt":7,"valid":0,"run_dev_item":[]}]
     * itemCnt : 7
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int itemCnt;
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

    public static class SecInfoRowsBean implements Serializable{
        /**
         * secName : 313233367373730000000000
         * secCode : 0202000236ffd70548423735
         * secType : 120
         * secId : 7
         * sceneId : 24
         * secDev : 67
         * itemCnt : 3
         * valid : 0
         * run_dev_item : [{"canCpuID":"0b00000036ffd70548423735","devID":3,"devType":7,"lmVal":120,"rev2":24,"rev3":67,"bOnOff":0,"param1":0,"param2":0},{"canCpuID":"0700000036ffd70548423735","devID":3,"devType":7,"lmVal":120,"rev2":24,"rev3":67,"bOnOff":0,"param1":0,"param2":0},{"canCpuID":"0b00000036ffd70548423735","devID":3,"devType":7,"lmVal":120,"rev2":24,"rev3":67,"bOnOff":0,"param1":0,"param2":0}]
         */

        private String secName;
        private String secCode;
        private int secType;
        private int secId;
        private int sceneId;
        private int secDev;
        private int devCnt;
        private int itemCnt;
        private int valid;
        private List<RunDevItemBean> run_dev_item;

        public String getSecName() {
            return CommonUtils.getGBstr(CommonUtils.hexStringToBytes(secName));
//            return secName;
        }

        public void setSecName(String secName) {
            this.secName = secName;
        }

        public String getSecCode() {
            return secCode;
        }

        public void setSecCode(String secCode) {
            this.secCode = secCode;
        }

        public int getSecType() {
            return secType;
        }

        public void setSecType(int secType) {
            this.secType = secType;
        }

        public int getSecId() {
            return secId;
        }

        public void setSecId(int secId) {
            this.secId = secId;
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

        public int getDevCnt() {
            return devCnt;
        }

        public void setDevCnt(int devCnt) {
            this.devCnt = devCnt;
        }

        public int getItemCnt() {
            return itemCnt;
        }

        public void setItemCnt(int itemCnt) {
            this.itemCnt = itemCnt;
        }

        public int getValid() {
            return valid;
        }

        public void setValid(int valid) {
            this.valid = valid;
        }

        public List<RunDevItemBean> getRun_dev_item() {
            for (int i = 0; i < run_dev_item.size(); i++) {
                if (run_dev_item.get(i).getCanCpuID() == null || run_dev_item.get(i).getCanCpuID().isEmpty())
                    run_dev_item.remove(i);
            }
            return run_dev_item;
        }

        public void setRun_dev_item(List<RunDevItemBean> run_dev_item) {
            this.run_dev_item = run_dev_item;
        }

        public static class RunDevItemBean implements Serializable{
            /**
             * canCpuID : 0b00000036ffd70548423735
             * devID : 3
             * devType : 7
             * lmVal : 120
             * rev2 : 24
             * rev3 : 67
             * bOnOff : 0
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
