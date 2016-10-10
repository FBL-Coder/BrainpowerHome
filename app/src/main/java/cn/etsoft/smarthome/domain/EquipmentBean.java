package cn.etsoft.smarthome.domain;

/**
 * Created by Say GoBay on 2016/8/24.
 */
public class EquipmentBean {

    private String titleId;
    private int imageId;

    public EquipmentBean() {
        super();
    }

    public EquipmentBean(String titleId, int imageId) {
        super();
        this.titleId = titleId;
        this.imageId = imageId;
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
