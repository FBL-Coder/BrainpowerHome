package cn.etsoft.smarthome.adapter;

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
import cn.etsoft.smarthome.domain.SocketBean;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class SocketAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<SocketBean> listViewItems;

    public SocketAdapter(int[] appliance, String[] title, int[] time, String[] moudle, int[] on, Context context) {
        super();
        listViewItems = new ArrayList<SocketBean>();
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < appliance.length; i++) {
            SocketBean item = new SocketBean(appliance[i],title[i],time[i],moudle[i],on[i]);
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
            convertView = mInflater.inflate(R.layout.gridview_item_socket, null);
            viewHolder = new ViewHolder();
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.time = (ImageView) convertView.findViewById(R.id.time);
            viewHolder.on = (ImageView) convertView.findViewById(R.id.on);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.moudle = (TextView) convertView.findViewById(R.id.moudle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appliance.setImageResource(listViewItems.get(position).getApplianceId());
        viewHolder.time.setImageResource(listViewItems.get(position).getTimeId());
        viewHolder.on.setImageResource(listViewItems.get(position).getOnId());
        viewHolder.title.setText(listViewItems.get(position).getTitleId());
        viewHolder.moudle.setText(listViewItems.get(position).getMoudleId());
        return convertView;
    }

    private class ViewHolder {
        ImageView appliance,time,on;
        TextView title,moudle;
    }
}
