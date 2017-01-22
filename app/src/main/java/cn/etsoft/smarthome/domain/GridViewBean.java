package cn.etsoft.smarthome.domain;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class GridViewBean {

    private int imageId;
    private String titleId;

    public GridViewBean() {
        super();
    }
    public GridViewBean(int imageId, String titleId) {
        this.imageId = imageId;
        this.titleId = titleId;
    }
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }


}
