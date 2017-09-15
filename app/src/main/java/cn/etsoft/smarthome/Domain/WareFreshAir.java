package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareFreshAir implements Serializable {
    /**
     * "canCpuID":	"36ffd7054842373507781843",
     * "devName":	"d0c2b7e731",
     * "roomName":	"bfcdccfc",
     * "devType":	7,
     * "devID":	0,
     * "bOnOff":	1,
     * "spdSel":	4,
     * "onOffChn":	255,
     * "spdLowChn":	5,
     * "spdMidChn":	6,
     * "spdHighChn":	7,
     * "autoRun":	0,
     * "valPm10":	0,
     * "valPm25":	0
     * },
     */
    private static final long serialVersionUID = 1L;
    private WareDev dev;
    private int bOnOff;
    private int spdSel;
    private int onOffChn;
    private int spdLowChn;
    private int spdMidChn;
    private int spdHighChn;
    private int autoRun;
    private int rev;
    private int valPm10;
    private int valPm25;

    public WareDev getDev() {
        return dev;
    }

    public void setDev(WareDev dev) {
        this.dev = dev;
    }

    public int getbOnOff() {
        return bOnOff;
    }

    public void setbOnOff(int bOnOff) {
        this.bOnOff = bOnOff;
        dev.setbOnOff(bOnOff);
    }

    public int getSpdSel() {
        if (spdSel > 4)
            return 4;
        if (spdSel < 2)
            return 2;
        return spdSel;
    }

    public void setSpdSel(int spdSel) {
        this.spdSel = spdSel;
    }

    public int getPowChn() {
        return onOffChn;
    }

    public void setPowChn(int powChn) {
        this.onOffChn = powChn;
        dev.setPowChn(powChn);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getOnOffChn() {
        return onOffChn;
    }

    public void setOnOffChn(int onOffChn) {
        this.onOffChn = onOffChn;
    }

    public int getSpdLowChn() {
        return spdLowChn;
    }

    public void setSpdLowChn(int spdLowChn) {
        this.spdLowChn = spdLowChn;
    }

    public int getSpdMidChn() {
        return spdMidChn;
    }

    public void setSpdMidChn(int spdMidChn) {
        this.spdMidChn = spdMidChn;
    }

    public int getSpdHighChn() {
        return spdHighChn;
    }

    public void setSpdHighChn(int spdHighChn) {
        this.spdHighChn = spdHighChn;
    }

    public int getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(int autoRun) {
        this.autoRun = autoRun;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }

    public int getValPm10() {
        return valPm10;
    }

    public void setValPm10(int valPm10) {
        this.valPm10 = valPm10;
    }

    public int getValPm25() {
        return valPm25;
    }

    public void setValPm25(int valPm25) {
        this.valPm25 = valPm25;
    }
}
