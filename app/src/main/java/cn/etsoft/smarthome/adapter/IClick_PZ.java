package cn.etsoft.smarthome.adapter;

import android.view.View;

import cn.etsoft.smarthome.pullmi.entity.Iclick_Tag;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public abstract class IClick_PZ implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Iclick_Tag tag = (Iclick_Tag) v.getTag();
        listViewItemClick(tag.getPosition(), v);
    }

    public abstract void listViewItemClick(int position, View v);

}
