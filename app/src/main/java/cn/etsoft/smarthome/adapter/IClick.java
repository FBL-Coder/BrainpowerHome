package cn.etsoft.smarthome.adapter;

import android.view.View;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public abstract class IClick implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        listViewItemClick((Integer) v.getTag(), v);
    }

    public abstract void listViewItemClick(int position, View v);

}
