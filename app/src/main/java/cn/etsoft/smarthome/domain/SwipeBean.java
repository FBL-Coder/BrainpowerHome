package cn.etsoft.smarthome.domain;

/**
 * Created by Say GoBay on 2016/8/24.
 */
public class SwipeBean {
    private String titleId;

    public SwipeBean() {
        super();
    }

    public SwipeBean(String titleId) {
        super();
        this.titleId = titleId;

    }

    public String getTitle() {
        return titleId;
    }

    public void setTitle(String title) {
        this.titleId = title;
    }

}
