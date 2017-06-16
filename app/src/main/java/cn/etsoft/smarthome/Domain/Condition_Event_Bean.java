package cn.etsoft.smarthome.Domain;

import cn.etsoft.smarthome.Utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by F-B-L on 2017/5/19.
 */

public class Condition_Event_Bean implements Serializable {


    /**
     * devUnitID : 39ffd805484d303416600643
     * datType : 27
     * subType1 : 0
     * subType2 : 1
     * envEvent_rows : [{"uidSrc":"000000000000000000000000","eventName":"b4a5b7a2c6f7300000000000","valTh":22,"thType":0,"envType":2,"devCnt":1,"eventId":0,"valid":1,"rev":0,"run_dev_item":[{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]},{"uidSrc":"000000000000000000000000","eventName":"b4a5b7a2c6f7310000000000","valTh":0,"thType":0,"envType":0,"devCnt":0,"eventId":1,"valid":0,"rev":0,"run_dev_item":[]},{"uidSrc":"000000000000000000000000","eventName":"b4a5b7a2c6f7320000000000","valTh":0,"thType":0,"envType":0,"devCnt":0,"eventId":2,"valid":0,"rev":0,"run_dev_item":[]},{"uidSrc":"000000000000000000000000","eventName":"b4a5b7a2c6f7330000000000","valTh":0,"thType":0,"envType":0,"devCnt":0,"eventId":3,"valid":0,"rev":0,"run_dev_item":[]}]
     * itemCnt : 4
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int itemCnt;
    private List<EnvEventRowsBean> envEvent_rows;

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

    public List<EnvEventRowsBean> getenvEvent_rows() {
        if (envEvent_rows == null)
            return new ArrayList<>();
        return envEvent_rows;
    }

    public void setenvEvent_rows(List<EnvEventRowsBean> envEvent_rows) {
        this.envEvent_rows = envEvent_rows;
    }

    public static class EnvEventRowsBean implements Serializable{
        /**
         * uidSrc : 000000000000000000000000
         * eventName : b4a5b7a2c6f7300000000000
         * valTh : 22
         * thType : 0
         * envType : 2
         * devCnt : 1
         * eventId : 0
         * valid : 1
         * rev : 0
         * run_dev_item : [{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]
         */

        private String uidSrc;
        private String eventName;
        private int valTh;
        private int thType;
        private int envType;
        private int devCnt;
        private int eventId;
        private int valid;
        private int rev;
        private List<RunDevItemBean> run_dev_item;

        public String getUidSrc() {
            return uidSrc;
        }

        public void setUidSrc(String uidSrc) {
            this.uidSrc = uidSrc;
        }

        public String getEventName() {
            return CommonUtils.getGBstr(CommonUtils.hexStringToBytes(eventName));
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public int getValTh() {
            return valTh;
        }

        public void setValTh(int valTh) {
            this.valTh = valTh;
        }

        public int getThType() {
            return thType;
        }

        public void setThType(int thType) {
            this.thType = thType;
        }

        public int getEnvType() {
            return envType;
        }

        public void setEnvType(int envType) {
            this.envType = envType;
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

        public int getRev() {
            return rev;
        }

        public void setRev(int rev) {
            this.rev = rev;
        }

        public List<RunDevItemBean> getRun_dev_item() {
            return run_dev_item;
        }

        public void setRun_dev_item(List<RunDevItemBean> run_dev_item) {
            this.run_dev_item = run_dev_item;
        }

        public static class RunDevItemBean implements Serializable {
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
            /**
             * devUnitID : 39ffd805484d303416600643
             * datType : 27
             * subType1 : 0
             * subType2 : 1
             * timerEvent_rows : [{"uidSrc":"000000000000000000000000","eventName":"b4a5b7a2c6f7300000000000","valTh":22,"thType":0,"envType":2,"devCnt":1,"eventId":0,"valid":1,"rev":0,"run_dev_item":[{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]},{"uidSrc":"000000000000000000000000","eventName":"b4a5b7a2c6f7310000000000","valTh":0,"thType":0,"envType":0,"devCnt":0,"eventId":1,"valid":0,"rev":0,"run_dev_item":[]},{"uidSrc":"000000000000000000000000","eventName":"b4a5b7a2c6f7320000000000","valTh":0,"thType":0,"envType":0,"devCnt":0,"eventId":2,"valid":0,"rev":0,"run_dev_item":[]},{"uidSrc":"000000000000000000000000","eventName":"b4a5b7a2c6f7330000000032","valTh":20,"thType":1,"envType":2,"devCnt":2,"eventId":3,"valid":1,"rev":1,"run_dev_item":[{"canCpuID":"36ffd7054842373507781843","devID":7,"devType":3,"lmVal":0,"rev2":1,"rev3":0,"bOnOff":1,"param1":0,"param2":0},{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":1,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]}]
             * itemCnt : 4
             */

            private String devUnitID;
            private int datType;
            private int subType1;
            private int subType2;
            private int itemCnt;
            private List<TimerEventRowsBean> timerEvent_rows;

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

            public static class TimerEventRowsBean implements Serializable{
                /**
                 * uidSrc : 000000000000000000000000
                 * eventName : b4a5b7a2c6f7300000000000
                 * valTh : 22
                 * thType : 0
                 * envType : 2
                 * devCnt : 1
                 * eventId : 0
                 * valid : 1
                 * rev : 0
                 * run_dev_item : [{"canCpuID":"36ffd7054842373507781843","devID":8,"devType":3,"lmVal":0,"rev2":0,"rev3":0,"bOnOff":1,"param1":0,"param2":0}]
                 */

                private String uidSrc;
                private String eventName;
                private int valTh;
                private int thType;
                private int envType;
                private int devCnt;
                private int eventId;
                private int valid;
                private int rev;
                private List<RunDevItemBean> run_dev_item;

                public String getUidSrc() {
                    return uidSrc;
                }

                public void setUidSrc(String uidSrc) {
                    this.uidSrc = uidSrc;
                }

                public String getEventName() {
                    return eventName;
                }

                public void setEventName(String eventName) {
                    this.eventName = eventName;
                }

                public int getValTh() {
                    return valTh;
                }

                public void setValTh(int valTh) {
                    this.valTh = valTh;
                }

                public int getThType() {
                    return thType;
                }

                public void setThType(int thType) {
                    this.thType = thType;
                }

                public int getEnvType() {
                    return envType;
                }

                public void setEnvType(int envType) {
                    this.envType = envType;
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

                public int getRev() {
                    return rev;
                }

                public void setRev(int rev) {
                    this.rev = rev;
                }

                public List<RunDevItemBean> getRun_dev_item() {
                    return run_dev_item;
                }

                public void setRun_dev_item(List<RunDevItemBean> run_dev_item) {
                    this.run_dev_item = run_dev_item;
                }
            }
        }
    }
}
