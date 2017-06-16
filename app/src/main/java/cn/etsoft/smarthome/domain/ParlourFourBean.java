package cn.etsoft.smarthome.domain;

import java.io.Serializable;

/**
 * Created by Say GoBay on 2016/8/24.
 */
public class ParlourFourBean implements Serializable {

    private String titleId;
    private String textId;
    private String deployId;

    public ParlourFourBean() {
        super();
    }

    public ParlourFourBean(String titleId, String textId, String deployId) {
        super();
        this.titleId = titleId;
        this.textId = textId;
        this.deployId = deployId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public String getDeployId() {
        return deployId;
    }

    public void setDeployId(String deployId) {
        this.deployId = deployId;
    }
}
