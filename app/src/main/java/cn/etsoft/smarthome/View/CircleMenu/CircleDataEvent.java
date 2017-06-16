package cn.etsoft.smarthome.View.CircleMenu;

/**
 * Author：FBL  Time： 2017/6/7.
 * 转盘菜单实体。包含图片，标题。以及是否选中
 */

public class CircleDataEvent {

    private int image;
    private String title;
    boolean isSelect;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
