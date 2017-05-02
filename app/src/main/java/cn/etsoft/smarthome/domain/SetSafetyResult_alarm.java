package cn.etsoft.smarthome.domain;

import java.io.Serializable;

/**
 * Created by Say GoBay on 2017/4/21.
 * 防区警报信息的bean类
 */
public class SetSafetyResult_alarm implements Serializable {


    /**
     * devUnitID : 39ffd505484d303408650743
     * datType : 32
     * subType1 : 2
     * subType2 : 0
     * secStatus : 0
     * secDat : 4
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int secStatus;
    private int secDat;

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

    public int getSecStatus() {
        return secStatus;
    }

    public void setSecStatus(int secStatus) {
        this.secStatus = secStatus;
    }

    public int getSecDat() {
        return secDat;
    }

    public void setSecDat(int secDat) {
        this.secDat = secDat;
    }
}
