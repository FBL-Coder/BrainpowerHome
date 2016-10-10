package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareDev;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class ParlourGridViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareDev> listViewItems;

    public ParlourGridViewAdapter(Context context,List<WareDev> listViewItems) {
        this.listViewItems = listViewItems;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.parlour_gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.light_gv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(CommonUtils.getGBstr(listViewItems.get(position).getDevName()));
        return convertView;
    }

    private class ViewHolder {
        TextView title;
    }
}
