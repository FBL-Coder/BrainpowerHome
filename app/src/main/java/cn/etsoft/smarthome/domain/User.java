package cn.etsoft.smarthome.domain;

import java.io.Serializable;

/**
 * Created by Say GoBay on 2016/12/22.
 */
public class User implements Serializable {
    String id;
    String pass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
