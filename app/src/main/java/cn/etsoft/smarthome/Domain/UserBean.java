package cn.etsoft.smarthome.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fbl on 16-11-23.
 */
public class UserBean implements Serializable {
    /**
     * devUnitID : 39ffd805484d303408580143
     * user_bean : [{"canCpuID":"36ffd7054842373507781843","isDev":true,"devType":9,"devID":1,"eventId":0},{"canCpuID":"36ffd7054842373507781843","isDev":true,"devType":3,"devID":2,"eventId":0},{"canCpuID":"","isDev":false,"eventId":9,"devType":0,"devID":0},{"canCpuID":"","isDev":false,"eventId":9,"devType":0,"devID":0}]
     */

    private String devUnitID;
    private List<UserBeanBean> user_bean;

    public String getDevUnitID() {
        return devUnitID;
    }

    public void setDevUnitID(String devUnitID) {
        this.devUnitID = devUnitID;
    }

    public List<UserBeanBean> getUser_bean() {
        if (user_bean == null)
            user_bean = new ArrayList<>();
        return user_bean;
    }

    public void setUser_bean(List<UserBeanBean> user_bean) {
        this.user_bean = user_bean;
    }

    public static class UserBeanBean {
        /**
         * canCpuID : 36ffd7054842373507781843
         * isDev : true
         * devType : 9
         * devID : 1
         * eventId : 0
         */

        private String canCpuID;
        private int isDev;
        private int devType;
        private int devID;
        private int eventId;

        public String getCanCpuID() {
            return canCpuID;
        }

        public void setCanCpuID(String canCpuID) {
            this.canCpuID = canCpuID;
        }

        public int isIsDev() {
            return isDev;
        }

        public void setIsDev(int isDev) {
            this.isDev = isDev;
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

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }
    }
}
