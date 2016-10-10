package cn.etsoft.smarthome.pullmi.entity;

import java.io.Serializable;

public class WareValve implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private WareDev dev;
    private byte bOnOff;
    private byte timRun;
    private byte powChnOpen;
    private byte powChnClose;

    public WareDev getDev() {
        return dev;
    }

    public void setDev(WareDev dev) {
        this.dev = dev;
    }

    public byte getbOnOff() {
        return bOnOff;
    }

    public void setbOnOff(byte bOnOff) {
        this.bOnOff = bOnOff;
    }

    public byte getTimRun() {
        return timRun;
    }

    public void setTimRun(byte timRun) {
        this.timRun = timRun;
    }

    public byte getPowChnOpen() {
        return powChnOpen;
    }

    public void setPowChnOpen(byte powChnOpen) {
        this.powChnOpen = powChnOpen;
    }

    public byte getPowChnClose() {
        return powChnClose;
    }

    public void setPowChnClose(byte powChnClose) {
        this.powChnClose = powChnClose;
    }
}
