package cn.etsoft.smarthome.domain;

import java.util.List;

/**
 * Created by fbl on 16-11-9.
 */
public class UpBoardKeyData {


    /**
     * chn_opitem_rows : [{"key_cpuCanID":"50ff6c067184515640421267","keyDownValid":0,"keyUpValid":49,"keyUpCmd":[3,0,0,0,3,1],"keyDownCmd":[0,0,0,0,0,0]}]
     * devUnitID : 37ffdb05424e323416702443
     * datType : 14
     * subType1 : 1
     * subType2 : 1
     * chn_opitem : 1
     */

    private String devUnitID;
    private int datType;
    private int subType1;
    private int subType2;
    private int devType;
    private int devID;
    private String out_cpuCanID;
    private int chn_opitem;

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

    public String getOut_cpuCanID() {
        return out_cpuCanID;
    }

    public void setOut_cpuCanID(String out_cpuCanID) {
        this.out_cpuCanID = out_cpuCanID;
    }

    /**
     * key_cpuCanID : 50ff6c067184515640421267
     * keyDownValid : 0
     * keyUpValid : 49
     * keyUpCmd : [3,0,0,0,3,1]
     * keyDownCmd : [0,0,0,0,0,0]
     */



    private List<ChnOpitemRowsBean> chn_opitem_rows;

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

    public int getChn_opitem() {
        return chn_opitem;
    }

    public void setChn_opitem(int chn_opitem) {
        this.chn_opitem = chn_opitem;
    }

    public List<ChnOpitemRowsBean> getChn_opitem_rows() {
        return chn_opitem_rows;
    }

    public void setChn_opitem_rows(List<ChnOpitemRowsBean> chn_opitem_rows) {
        this.chn_opitem_rows = chn_opitem_rows;
    }

    public class ChnOpitemRowsBean {
        private String key_cpuCanID;
        private int keyDownValid;
        private int keyUpValid;
        private byte[] keyUpCmd;
        private byte[] keyDownCmd;

        public String getKey_cpuCanID() {
            return key_cpuCanID;
        }

        public void setKey_cpuCanID(String key_cpuCanID) {
            this.key_cpuCanID = key_cpuCanID;
        }

        public int getKeyDownValid() {
            return keyDownValid;
        }

        public void setKeyDownValid(int keyDownValid) {
            this.keyDownValid = keyDownValid;
        }

        public int getKeyUpValid() {
            return keyUpValid;
        }

        public void setKeyUpValid(int keyUpValid) {
            this.keyUpValid = keyUpValid;
        }

        public byte[] getKeyUpCmd() {
            return keyUpCmd;
        }

        public void setKeyUpCmd(byte[] keyUpCmd) {
            this.keyUpCmd = keyUpCmd;
        }

        public byte[] getKeyDownCmd() {
            return keyDownCmd;
        }

        public void setKeyDownCmd(byte[] keyDownCmd) {
            this.keyDownCmd = keyDownCmd;
        }
    }
}
