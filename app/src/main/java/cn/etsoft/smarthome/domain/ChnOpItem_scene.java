package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Say GoBay on 2017/4/21.
 * 查询联网模块防区信息bean类
 */
public class ChnOpItem_scene implements Serializable {


    /**
     * devUnitID : 39ffd505484d303408650743
     * datType : 58
     * subType1 : 1
     * subType2 : 0
     * key2scene_item : [{"canCpuID":"48ff6c065087485725170287","keyIndex":1,"eventId":2},{"canCpuID":"48ff6c065087485725170287","keyIndex":2,"eventId":3},{"canCpuID":"48ff6c065087485725170287","keyIndex":3,"eventId":0}]
     * itemCnt : 3
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int itemCnt;
    /**
     * canCpuID : 48ff6c065087485725170287
     * keyIndex : 1
     * eventId : 2
     */

    private List<Key2sceneItemBean> key2scene_item;

    public String getDevUnitID() {
        return devUnitID;
    }

    public void setDevUnitID(String devUnitID) {
        this.devUnitID = devUnitID;
    }

    public int getDatType() {
        return datType;
    }

    public void setDatType(int datType) {
        this.datType = datType;
    }

    public int getSubType1() {
        return subType1;
    }

    public void setSubType1(int subType1) {
        this.subType1 = subType1;
    }

    public int getSubType2() {
        return subType2;
    }

    public void setSubType2(int subType2) {
        this.subType2 = subType2;
    }

    public int getItemCnt() {
        return itemCnt;
    }

    public void setItemCnt(int itemCnt) {
        this.itemCnt = itemCnt;
    }

    public List<Key2sceneItemBean> getKey2scene_item() {
        if (key2scene_item == null)
            return new ArrayList<>();
        return key2scene_item;
    }

    public void setKey2scene_item(List<Key2sceneItemBean> key2scene_item) {
        this.key2scene_item = key2scene_item;
    }

    public static class Key2sceneItemBean implements Serializable{
        private String canCpuID;
        private int keyIndex;
        private int eventId;

        public String getCanCpuID() {
            return canCpuID;
        }

        public void setCanCpuID(String canCpuID) {
            this.canCpuID = canCpuID;
        }

        public int getKeyIndex() {
            return keyIndex;
        }

        public void setKeyIndex(int keyIndex) {
            this.keyIndex = keyIndex;
        }

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }
    }
}
