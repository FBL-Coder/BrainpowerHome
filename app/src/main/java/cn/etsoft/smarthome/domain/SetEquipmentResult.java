package cn.etsoft.smarthome.domain;

import java.io.Serializable;

/**
 * Created by fbl on 16-11-7.
 */
public class SetEquipmentResult implements Serializable {

    /**
     * devUnitID : 37ffdb05424e323416702443
     * datType : 12
     * subType1 : 1
     * subType2 : 0
     * canCpuID : 50ff6c067184515640421267
     * result : 1
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private String canCpuID;
    private int result;

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

    public String getCanCpuID() {
        return canCpuID;
    }

    public void setCanCpuID(String canCpuID) {
        this.canCpuID = canCpuID;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
