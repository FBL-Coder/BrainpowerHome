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
    private int devCnt;
    private int eventId;
    private int rev2;
    private int rev3;


    private List<WareSceneDevItem> itemAry;

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public int getDevCnt() {
        return devCnt;
    }

    public void setDevCnt(int devCnt) {
        this.devCnt = devCnt;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventld) {
        this.eventId = eventld;
    }

    public int getRev2() {
        return rev2;
    }

    public void setRev2(int rev2) {
        this.rev2 = rev2;
    }

    public int getRev3() {
        return rev3;
    }

    public void setRev3(int rev3) {
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
