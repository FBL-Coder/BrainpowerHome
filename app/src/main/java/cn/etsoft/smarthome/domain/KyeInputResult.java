package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Say GoBay on 2017/6/26.
 */
public class KyeInputResult implements Serializable {

    /**
     * devUnitID : 39ffd505484d303408650743
     * datType : 9
     * subType1 : 1
     * subType2 : 1
     * keyinput_rows : [{"canCpuID":"48ff6c065087485725170287","boardName":"b0b4bcfcb0e5303238370000","boardType":1,"keyCnt":8,"bResetKey":0,"ledBkType":8,"keyName_rows":["bcfc30000000000000000000","bcfc31000000000000000000","bcfc32000000000000000000","bcfc33000000000000000000","bcfc34000000000000000000","bcfc35000000000000000000","b0b4bcfc3700000000000000","000000000000000000000000"],"keyAllCtrlType_rows":[0,0,0,0,0,0,0,0],"roomName":"ac0400000000000000000000"}]
     * keyinput : 1
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int keyinput;
    /**
     * canCpuID : 48ff6c065087485725170287
     * boardName : b0b4bcfcb0e5303238370000
     * boardType : 1
     * keyCnt : 8
     * bResetKey : 0
     * ledBkType : 8
     * keyName_rows : ["bcfc30000000000000000000","bcfc31000000000000000000","bcfc32000000000000000000","bcfc33000000000000000000","bcfc34000000000000000000","bcfc35000000000000000000","b0b4bcfc3700000000000000","000000000000000000000000"]
     * keyAllCtrlType_rows : [0,0,0,0,0,0,0,0]
     * roomName : ac0400000000000000000000
     */

    private List<KeyinputRowsBean> keyinput_rows;

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

    public int getKeyinput() {
        return keyinput;
    }

    public void setKeyinput(int keyinput) {
        this.keyinput = keyinput;
    }

    public List<KeyinputRowsBean> getKeyinput_rows() {
        return keyinput_rows;
    }

    public void setKeyinput_rows(List<KeyinputRowsBean> keyinput_rows) {
        this.keyinput_rows = keyinput_rows;
    }

    public static class KeyinputRowsBean implements Serializable{
        private String canCpuID;
        private String boardName;
        private int boardType;
        private int keyCnt;
        private int bResetKey;
        private int ledBkType;
        private String roomName;
        private List<String> keyName_rows;
        private List<Integer> keyAllCtrlType_rows;

        public String getCanCpuID() {
            return canCpuID;
        }

        public void setCanCpuID(String canCpuID) {
            this.canCpuID = canCpuID;
        }

        public String getBoardName() {
            return boardName;
        }

        public void setBoardName(String boardName) {
            this.boardName = boardName;
        }

        public int getBoardType() {
            return boardType;
        }

        public void setBoardType(int boardType) {
            this.boardType = boardType;
        }

        public int getKeyCnt() {
            return keyCnt;
        }

        public void setKeyCnt(int keyCnt) {
            this.keyCnt = keyCnt;
        }

        public int getBResetKey() {
            return bResetKey;
        }

        public void setBResetKey(int bResetKey) {
            this.bResetKey = bResetKey;
        }

        public int getLedBkType() {
            return ledBkType;
        }

        public void setLedBkType(int ledBkType) {
            this.ledBkType = ledBkType;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public List<String> getKeyName_rows() {
            return keyName_rows;
        }

        public void setKeyName_rows(List<String> keyName_rows) {
            this.keyName_rows = keyName_rows;
        }

        public List<Integer> getKeyAllCtrlType_rows() {
            return keyAllCtrlType_rows;
        }

        public void setKeyAllCtrlType_rows(List<Integer> keyAllCtrlType_rows) {
            this.keyAllCtrlType_rows = keyAllCtrlType_rows;
        }
    }
}
