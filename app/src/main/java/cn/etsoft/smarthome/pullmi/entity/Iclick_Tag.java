package cn.etsoft.smarthome.pullmi.entity;

/**
 * Created by hwp on 16-11-2.
 */
public class Iclick_Tag {
    int position;

    int Type;

    String [] text;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }


}
