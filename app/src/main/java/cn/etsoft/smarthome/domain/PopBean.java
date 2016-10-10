package cn.etsoft.smarthome.domain;

/**
 * Created by Say GoBay on 2016/8/26.
 */
public class PopBean {
    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    private String textId;

    public PopBean(String textId) {
        this.textId = textId;
    }

}
