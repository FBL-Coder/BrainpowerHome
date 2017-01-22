package cn.etsoft.smarthome.domain;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class GridViewBean2 {

    private int imageId;
    private String titleId;
    public GridViewBean2() {
        super();
    }
    public GridViewBean2(int imageId , String titleId) {
        this.imageId = imageId;
        this.titleId = titleId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
