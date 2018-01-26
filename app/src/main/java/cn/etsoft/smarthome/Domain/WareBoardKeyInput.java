package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareBoardKeyInput implements Serializable {
    /**
     *输入板实体
     */
    private static final long serialVersionUID = 8372972355485111907L;
    private String canCpuID; //12
    private String boardName; //8
    private int boardType;
    private int keyCnt;
    private int bResetKey;
    private int ledBkType;
    private String keyName_rows[]; //6-12
    /**
     * KeyAdapter_keyscene所需属性
     * 选中为1，不然为0，默认为键名数组长度的int数组全为0；
     */
    private int keyIsSelect[]; //6-12
    private int keyAllCtrlType[]; //6

    public String getCanCpuID() {
        return canCpuID;
    }
    public void setCanCpuID(String devUnitID) {
        this.canCpuID = devUnitID;
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

    public int getbResetKey() {
        return bResetKey;
    }

    public void setbResetKey(int bResetKey) {
        this.bResetKey = bResetKey;
    }

    public int getLedBkType() {
        return ledBkType;
    }

    public void setLedBkType(int ledBkType) {
        this.ledBkType = ledBkType;
    }

    public String[] getKeyName() {
        return keyName_rows;
    }

    public void setKeyName(String[] keyName) {
        this.keyName_rows = keyName;
    }

    //解决8个按键只有6个按键名的问题
    public int[] getKeyIsSelect() {
        if (keyIsSelect == null || keyIsSelect.length < 8)
            return keyIsSelect = new int[8];
        return keyIsSelect;
    }

    public int[] getKeyAllCtrlType() {
        return keyAllCtrlType;
    }

    public void setKeyAllCtrlType(int[] keyAllCtrlType) {
        this.keyAllCtrlType = keyAllCtrlType;
    }
}
