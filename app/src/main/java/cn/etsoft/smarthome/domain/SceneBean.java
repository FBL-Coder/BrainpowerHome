package cn.etsoft.smarthome.domain;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class SceneBean {

    private int timeId;
    private String titleId;
    private String moudleId;




    public String getMoudleId() {
        return moudleId;
    }

    public void setMoudleId(String moudleId) {
        this.moudleId = moudleId;
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

    public SceneBean() {
        super();
    }

    public SceneBean( String titleId, int timeId, String moudleId ) {
        this.moudleId = moudleId;
        this.titleId = titleId;
        this.timeId = timeId;
    }

}
