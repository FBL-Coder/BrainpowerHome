package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/9/1.
 * 高级设置-控制设置-按键情景—按键的适配器
 */
public class ModuleEditAdapter extends BaseAdapter {
    private Context context;
    private List<String> listData;
    int mSelect = 0;   //选中项

    public ModuleEditAdapter(Context context, List<String> listData) {
        this.listData = listData;
        this.context = context;
    }

    public void notifyDataSetChanged(List<String> listData) {
        this.listData = listData;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != listData) {
            return listData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    int pos;
    private boolean ischange = true;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.gridview_item_light5, null, false);
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.name = (EditText) convertView.findViewById(R.id.name);

            viewHolder.name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!ischange) {
                        listData.set(position, s + "");
                    }
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appliance.setImageResource(R.drawable.g);
        pos = position + 1;
        viewHolder.title.setText("按键" + pos);

        ischange = true;
        viewHolder.name.setText("");
        viewHolder.name.setHint(listData.get(position));
//        notifyDataSetChanged(listData);
        ischange = false;

        return convertView;
    }

    //刷新方法
    public void changeSelected(int position) {
        if (position != mSelect) {
            mSelect = position;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView appliance;
        TextView title;
        EditText name;
    }
}
