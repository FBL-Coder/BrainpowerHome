package cn.etsoft.smarthome.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：FBL  Time： 2017/6/16.
 */

public class Http_Result {

    /**
     * code : 0
     * msg :
     * data : [{"canCpuName":"匿名联网模块1","devUnitID":"39ffd505484d303408650743","devPass":"39ffd505"}]
     * uid : d062b85bdc4f6f8f1347a98270efd19b52f23a3e
     * expires_in : 704800
     */

    private int code;
    private String msg;
    private String uid;
    private int expires_in;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public List<DataBean> getData() {
        if (data == null)
            data = new ArrayList<>();
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * canCpuName : 匿名联网模块1
         * devUnitID : 39ffd505484d303408650743
         * devPass : 39ffd505
         */

        private String canCpuName;
        private String devUnitID;
        private String devPass;
        private boolean isOnline;

        public String getCanCpuName() {
            return canCpuName;
        }

        public void setCanCpuName(String canCpuName) {
            this.canCpuName = canCpuName;
        }

        public String getDevUnitID() {
            return devUnitID;
        }

        public void setDevUnitID(String devUnitID) {
            this.devUnitID = devUnitID;
        }

        public String getDevPass() {
            return devPass;
        }

        public void setDevPass(String devPass) {
            this.devPass = devPass;
        }

        public boolean isOnline() {
            return isOnline;
        }

        public void setOnline(boolean online) {
            isOnline = online;
        }
    }
}
