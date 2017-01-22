package cn.etsoft.smarthome.pullmi.entity;

import java.io.Serializable;

public class WareLock implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private WareDev dev;
    private byte bOnOff;
    private byte timRun;
    private byte bLockOut;
    private byte powChnOpen;
    private String pwd;

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

    public byte getbLockOut() {
        return bLockOut;
    }

    public void setbLockOut(byte bLockOut) {
        this.bLockOut = bLockOut;
    }

    public byte getPowChnOpen() {
        return powChnOpen;
    }

    public void setPowChnOpen(byte powChnOpen) {
        this.powChnOpen = powChnOpen;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
