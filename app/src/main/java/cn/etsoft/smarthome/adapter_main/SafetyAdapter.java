package cn.etsoft.smarthome.adapter_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.domain.Safety_Data;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class SafetyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Safety_Data.Data_Data> data_data;
    private Context context;

    public SafetyAdapter( List<Safety_Data.Data_Data> data_data, Context context, LayoutInflater inflater) {
        this.context = context;
        this.data_data = data_data;
        mInflater = inflater;
    }

    public void notifyDataSetChanged(List<Safety_Data.Data_Data> data_data) {
        this.data_data = data_data;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != data_data) {
            return data_data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return data_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item_safety, null);
            viewHolder = new ViewHolder();
            viewHolder.safetyMessage = (TextView) convertView.findViewById(R.id.safetyMessage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.safetyMessage.setText("防区" + (data_data.get(position).getId()-1) + ",于" + data_data.get(position).getYear() + "/" + data_data.get(position).getMonth() + "/" + data_data.get(position).getDay() + "/," + data_data.get(position).getH() + ":" + data_data.get(position).getM() + ":" + data_data.get(position).getS() + "被触发");

        return convertView;
    }

    private class ViewHolder {
        TextView safetyMessage;
    }
}
