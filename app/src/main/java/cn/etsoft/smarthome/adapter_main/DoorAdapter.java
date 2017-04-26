package cn.etsoft.smarthome.adapter_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.domain.DoorBean;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class DoorAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<DoorBean> listViewItems;

    public DoorAdapter(int[] appliance, String[] title, int[] on, Context context) {
        super();
        listViewItems = new ArrayList<DoorBean>();
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < appliance.length; i++) {
            DoorBean item = new DoorBean(appliance[i],title[i],on[i]);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item_door, null);
            viewHolder = new ViewHolder();
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.on = (ImageView) convertView.findViewById(R.id.on);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appliance.setImageResource(listViewItems.get(position).getApplianceId());
        viewHolder.on.setImageResource(listViewItems.get(position).getOnId());
        viewHolder.title.setText(listViewItems.get(position).getTitleId());
        return convertView;
    }

    private class ViewHolder {
        ImageView appliance,time,on;
        TextView title,moudle;
    }
}
