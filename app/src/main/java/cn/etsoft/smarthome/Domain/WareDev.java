package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WareDev implements Serializable {

    private String canCpuId = "";
    private String devName;
    private String roomName;
    private int devType;
    private int devId;
    private int devCtrlType; // 取值范围E_DEV_TYPE
    private boolean isSelect;
    private int bOnOff;
    private String powChn;

    public String getCanCpuId() {
        return canCpuId;
    }

    public void setCanCpuId(String canCpuId) {
        this.canCpuId = canCpuId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getType() {
        return devType;
    }

    public void setType(int type) {
        this.devType = type;
    }

    public int getDevId() {
        return devId;
    }

    public void setDevId(int devId) {
        this.devId = devId;
    }

    public int getDevCtrlType() {
        return devCtrlType;
    }

    public void setDevCtrlType(int devCtrlType) {
        this.devCtrlType = devCtrlType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getbOnOff() {
        return bOnOff;
    }

    public void setbOnOff(int bOnOff) {
        this.bOnOff = bOnOff;
    }

    public String getPowChn() {
        return powChn;
    }

    public void setPowChn(int powChn) {
        this.powChn = powChn + "";
    }
}
