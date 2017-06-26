package cn.etsoft.smarthome.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WareSceneEvent implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 9164065895025538399L;
    private String sceneName;
    private byte devCnt;
    private byte eventId;
    private byte rev2;
    private byte rev3;


    private List<WareSceneDevItem> itemAry;

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public byte getDevCnt() {
        return devCnt;
    }

    public void setDevCnt(byte devCnt) {
        this.devCnt = devCnt;
    }

    public byte getEventId() {
        return eventId;
    }

    public void setEventId(byte eventld) {
        this.eventId = eventld;
    }

    public byte getRev2() {
        return rev2;
    }

    public void setRev2(byte rev2) {
        this.rev2 = rev2;
    }

    public byte getRev3() {
        return rev3;
    }

    public void setRev3(byte rev3) {
        this.rev3 = rev3;
    }

    public List<WareSceneDevItem> getItemAry() {
        if (itemAry == null)
            itemAry = new ArrayList<>();
        return itemAry;
    }

    public void setItemAry(List<WareSceneDevItem> itemAry) {
        this.itemAry = itemAry;
    }
}
