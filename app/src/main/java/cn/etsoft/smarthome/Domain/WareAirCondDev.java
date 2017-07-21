package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareAirCondDev implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private WareDev dev;
    private int bOnOff;
    private int selMode;
    private int selTemp = 16;
    private int selSpd;
    private int selDirect;
    private int rev1;
    private int powChn;

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

    public int getSelMode() {
        return selMode;
    }

    public void setSelMode(int selMode) {
        this.selMode = selMode;
    }

    public int getSelTemp() {
        return selTemp;
    }

    public void setSelTemp(int selTemp) {
        this.selTemp = selTemp;
    }

    public int getSelSpd() {
        return selSpd;
    }

    public void setSelSpd(int selSpd) {
        this.selSpd = selSpd;
    }

    public int getSelDirect() {
        return selDirect;
    }

    public void setSelDirect(int selDirect) {
        this.selDirect = selDirect;
    }

    public int getRev1() {
        return rev1;
    }

    public void setRev1(int rev1) {
        this.rev1 = rev1;
    }

    public int getPowChn() {
        return powChn;
    }

    public void setPowChn(int powChn) {
        this.powChn = powChn;
        dev.setPowChn(powChn);
    }
}
