package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareData;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class Adapter_Lights extends BaseAdapter {
    private LayoutInflater mInflater;
    private WareData wareData;
    private Context context;

    public Adapter_Lights(WareData wareData, Context context) {
        this.wareData = wareData;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != wareData) {
            return wareData.getLights().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return wareData.getLights().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.light_gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (RelativeLayout) convertView.findViewById(R.id.light_gv_image);
            viewHolder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (wareData.getLights().get(position).getbOnOff() == 0) {
            viewHolder.image.setBackgroundResource(R.drawable.light3);//关闭
        } else {
            viewHolder.image.setBackgroundResource(R.drawable.light4);//打开
        }
        viewHolder.item_tv.setText(wareData.getLights().get(position).getDev().getDevName());

        return convertView;
    }

    private class ViewHolder {
        RelativeLayout image;
        TextView item_tv;
    }
}
