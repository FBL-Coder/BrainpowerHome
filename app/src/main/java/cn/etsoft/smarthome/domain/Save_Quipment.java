package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fbl on 16-11-7.
 * 高级设置-输入模块-保存设备的bean类
 */
public class Save_Quipment implements Serializable {

    String devUnitID;
    int datType;
    int subType1;
    int subType2;
    String key_cpuCanID;
    int key_index;
    int key_opitem;

    List<key_Opitem_Rows> key_opitem_rows;

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

    public String getKey_cpuCanID() {
        return key_cpuCanID;
    }

    public void setKey_cpuCanID(String key_cpuCanID) {
        this.key_cpuCanID = key_cpuCanID;
    }

    public int getKey_index() {
        return key_index;
    }

    public void setKey_index(int key_index) {
        this.key_index = key_index;
    }

    public int getKey_opitem() {
        return key_opitem;
    }

    public void setKey_opitem(int key_opitem) {
        this.key_opitem = key_opitem;
    }


    public List<key_Opitem_Rows> getKey_opitem_rows() {
        return key_opitem_rows;
    }

    public void setKey_opitem_rows(List<key_Opitem_Rows> key_opitem_rows) {
        this.key_opitem_rows = key_opitem_rows;
    }

    public class key_Opitem_Rows implements Serializable{
        String out_cpuCanID;
        int devType;
        int devID;
        int keyOpCmd;
        int KeyOp;

        public String getOut_cpuCanID() {
            return out_cpuCanID;
        }

        public void setOut_cpuCanID(String out_cpuCanID) {
            this.out_cpuCanID = out_cpuCanID;
        }

        public int getDevType() {
            return devType;
        }

        public void setDevType(int devType) {
            this.devType = devType;
        }

        public int getDevID() {
            return devID;
        }

        public void setDevID(int devID) {
            this.devID = devID;
        }

        public int getKeyOpCmd() {
            return keyOpCmd;
        }

        public void setKeyOpCmd(int keyOpCmd) {
            this.keyOpCmd = keyOpCmd;
        }

        public int getKeyOp() {
            return KeyOp;
        }

        public void setKeyOp(int keyOp) {
            KeyOp = keyOp;
        }

    }
}
