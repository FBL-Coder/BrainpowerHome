package cn.etsoft.smarthome.Domain;

import java.io.Serializable;

public class WareFloorHeat implements Serializable {


//    {"canCpuID":	"36ffd7054842373507781843",
//                "devName":	"b5d8c5af31",
//                "roomName":	"b2e2cad4b7bfbce4",
//                "devType":	9,
//                "devID":	0,
//                "c":	0,
//                "tempGet":	0,
//                "tempSet":	0,
//                "powChn":	0,
//                "autoRun":	0
//    }

    public WareDev dev;
    public int type;//
    public int rev1;
    public int rev2;
    public int rev3;
    public int bOnOff;
    public int tempget;
    public int tempset;
    public int powChn;
    public int autoRun;

    public WareDev getDev() {
        return dev;
    }

    public void setDev(WareDev dev) {
        this.dev = dev;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRev1() {
        return rev1;
    }

    public void setRev1(int rev1) {
        this.rev1 = rev1;
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

    public int getbOnOff() {
        return bOnOff;
    }

    public void setbOnOff(int bOnOff) {
        this.bOnOff = bOnOff;
    }

    public int getTempget() {
        return tempget;
    }

    public void setTempget(int tempget) {
        this.tempget = tempget;
    }

    public int getTempset() {
        return tempset;
    }

    public void setTempset(int tempset) {
        this.tempset = tempset;
    }

    public int getPowChn() {
        return powChn;
    }

    public void setPowChn(int powChn) {
        this.powChn = powChn;
        dev.setPowChn(powChn);
    }

    public int getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(int autoRun) {
        this.autoRun = autoRun;
    }
}
