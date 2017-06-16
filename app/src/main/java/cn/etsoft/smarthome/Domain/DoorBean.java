package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class DoorBean implements Serializable {

    private int applianceId;

    private int onId;
    private String titleId;


    public int getOnId() {
        return onId;
    }

    public void setOnId(int onId) {
        this.onId = onId;
    }





    public int getApplianceId() {
        return applianceId;
    }

    public void setApplianceId(int applianceId) {
        this.applianceId = applianceId;
    }





    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public DoorBean() {
        super();
    }

    public DoorBean(int applianceId, String titleId,  int onId ) {

        this.onId = onId;
        this.titleId = titleId;

        this.applianceId = applianceId;
    }

}
