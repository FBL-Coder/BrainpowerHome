package cn.etsoft.smarthome.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Say GoBay on 2017/4/21.
 */
public class Out_List_printcmd implements Serializable {
    private String unitid;
    private List<PrintCmd> printCmds;

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public List<PrintCmd> getPrintCmds() {
        return printCmds;
    }

    public void setPrintCmds(List<PrintCmd> printCmds) {
        this.printCmds = printCmds;
    }
}
