package cn.etsoft.smarthome.domain;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class CurtainBean {

    private int applianceId;
    private int timeId;
    private int onId;
    private int leftId;

    public int getRightId() {
        return rightId;
    }

    public void setRightId(int rightId) {
        this.rightId = rightId;
    }

    public int getLeftId() {
        return leftId;
    }

    public void setLeftId(int leftId) {
        this.leftId = leftId;
    }

    private int rightId;
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

    public CurtainBean() {
        super();
    }

    public CurtainBean(int applianceId, String titleId, int timeId, String moudleId, int leftId,int onId ,int rightId) {
        this.moudleId = moudleId;
        this.onId = onId;
        this.titleId = titleId;
        this.timeId = timeId;
        this.applianceId = applianceId;
        this.leftId = leftId;
        this.rightId = rightId;
    }

}
