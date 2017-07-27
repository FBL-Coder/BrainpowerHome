package cn.etsoft.smarthome.Domain;

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
     * key2scene_item : [
     * {"keyUId":"48ff6c065087485725170287","keyIndex":4,"eventId":3,"valid":1,"rev3":0},
     * {"keyUId":"48ff6c065087485725170287","keyIndex":5,"eventId":3,"valid":1,"rev3":0},
     * {"keyUId":"48ff6c065087485725170287","keyIndex":0,"eventId":3,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":1,"eventId":3,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":2,"eventId":3,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":3,"eventId":3,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":0,"eventId":0,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":1,"eventId":1,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":0,"eventId":2,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":2,"eventId":2,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":4,"eventId":2,"valid":1,"rev3":0},{"keyUId":"48ff6c065087485725170287","keyIndex":7,"eventId":2,"valid":1,"rev3":0}]
     * itemCnt : 12
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int itemCnt;
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
            key2scene_item = new ArrayList<>();
        return key2scene_item;
    }

    public void setKey2scene_item(List<Key2sceneItemBean> key2scene_item) {
        this.key2scene_item = key2scene_item;
    }

    public static class Key2sceneItemBean implements Serializable {
        /**
         * keyUId : 48ff6c065087485725170287
         * keyIndex : 4
         * eventId : 3
         * valid : 1
         * rev3 : 0
         */

        private String keyUId;
        private int keyIndex;
        private int eventId;
        private int valid;
        private int rev3;

        public String getKeyUId() {
            return keyUId;
        }

        public void setKeyUId(String keyUId) {
            this.keyUId = keyUId;
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

        public int getValid() {
            return valid;
        }

        public void setValid(int valid) {
            this.valid = valid;
        }

        public int getRev3() {
            return rev3;
        }

        public void setRev3(int rev3) {
            this.rev3 = rev3;
        }
    }
}
