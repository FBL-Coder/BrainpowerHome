package cn.semtec.community2.entity;

import java.util.List;

/**
 * Author：FBL  Time： 2017/12/5.
 */

public class RecordEntity {


    /**
     * returnCode : 0
     * msg :
     * args : http://yitong-ss1.weedoor.com:8040/sipManager/
     * object : [{"photoUrl":"files/20176/upload/openDoorLog/a2384b2756924709a0245c18bce9b00c.jpg","time":"2017-02-07 16:15:24","userName":"17089111219","openType":1,"lockName":"易同2号门口机"},{"photoUrl":"files/20176/upload/openDoorLog/38cf20b63b6148808bf71be627368284.jpg","time":"2017-02-08 14:55:59","userName":"17089111219","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/154e591ed02c4146b17fea787c416a8b.jpg","time":"2017-02-08 17:33:52","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/ab39ddbf3e8e481286f771ef08dc92c4.jpg","time":"2017-02-09 08:22:36","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/dd828f9ac88944d09206542e3aaef06d.jpg","time":"2017-02-09 11:25:37","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/9faa04886dfd4c8fadcc88c3bd564472.jpg","time":"2017-02-09 13:13:28","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/b775943eb25349188ca9e3d2c804cc2d.jpg","time":"2017-02-09 13:16:38","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/bf87d1b735c14ba788fc063f253a2d6b.jpg","time":"2017-02-09 14:16:50","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/700f648b82a64a60bb33fbdaec1639d2.jpg","time":"2017-02-11 09:43:31","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/d3028de34c534493a9e7972e7ab53d54.jpg","time":"2017-02-11 09:43:43","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/c3336526ef0c46589c3a6cea1878f4c8.jpg","time":"2017-02-11 09:44:48","userName":"0200000000000000","openType":1,"lockName":"易同2号"},{"photoUrl":"files/20176/upload/openDoorLog/71a63de282e8445fa7c555192d905167.jpg","time":"2017-02-11 10:06:40","userName":"0200000000000000","openType":1,"lockName":"易同2号"}]
     */

    private int returnCode;
    private String msg;
    private String args;
    private List<ObjectBean> object;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public List<ObjectBean> getObject() {
        return object;
    }

    public void setObject(List<ObjectBean> object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * photoUrl : files/20176/upload/openDoorLog/a2384b2756924709a0245c18bce9b00c.jpg
         * time : 2017-02-07 16:15:24
         * userName : 17089111219
         * openType : 1
         * lockName : 易同2号门口机
         */

        private String photoUrl;
        private String time;
        private String userName;
        private int openType;
        private String lockName;

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getOpenType() {
            return openType;
        }

        public void setOpenType(int openType) {
            this.openType = openType;
        }

        public String getLockName() {
            return lockName;
        }

        public void setLockName(String lockName) {
            this.lockName = lockName;
        }
    }
}
