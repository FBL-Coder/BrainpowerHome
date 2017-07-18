package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.domain.PopBean;

/**
 * Created by Say GoBay on 2016/8/30.
 */
public class PopupWindowAdapter3 extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PopBean> listViewItems;
    private Context context;
    private Map<String, Boolean> map = new HashMap<>();// 存放已被选中的CheckBox

    public PopupWindowAdapter3(List<String> text, Context context) {
        super();
        listViewItems = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        this.context = context;
        if (text == null || "".equals(text)) {
            text = new ArrayList<>();
        }
        for (int i = 0; i < text.size(); i++) {
            PopBean item = new PopBean(text.get(i));
            listViewItems.add(item);
        }

    }

    @Override
    public int getCount() {
        if (null != listViewItems) {
            return listViewItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return listViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.popupwindow_listview_item2, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.popupWindow_equipment_tv);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.popupWindow_equipment_cb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(listViewItems.get(position).getTextId());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    map.put(String.valueOf(listViewItems.get(position)), true);
                } else {
                    map.remove(listViewItems.get(position));
                }
            }
        });

        if (map != null && map.containsKey(listViewItems.get(position))) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }
        return convertView;
    }


    public class ViewHolder {
        public TextView text;
        public CheckBox checkBox;
    }
}
