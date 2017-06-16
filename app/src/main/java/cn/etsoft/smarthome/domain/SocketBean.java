package cn.etsoft.smarthome.domain;

import java.io.Serializable;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class SocketBean implements Serializable {

    private int applianceId;
    private int timeId;
    private int onId;
    private String titleId;
    private String moudleId;

    public int getOnId() {
        return onId;
    }

    public void setOnId(int onId) {
        this.onId = onId;
    }



    public String getMoudleId() {
        return moudleId;
    }

    public void setMoudleId(String moudleId) {
        this.moudleId = moudleId;
    }

    public int getApplianceId() {
        return applianceId;
    }

    public void setApplianceId(int applianceId) {
        this.applianceId = applianceId;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }



    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public SocketBean() {
        super();
    }

    public SocketBean(int applianceId, String titleId, int timeId, String moudleId, int onId ) {
        this.moudleId = moudleId;
        this.onId = onId;
        this.titleId = titleId;
        this.timeId = timeId;
        this.applianceId = applianceId;
    }

}
