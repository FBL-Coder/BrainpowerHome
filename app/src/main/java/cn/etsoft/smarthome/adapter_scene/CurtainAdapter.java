package cn.etsoft.smarthome.adapter_scene;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareSceneDevItem;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class CurtainAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareCurtain> list_curtain;
    private Context context;
    private byte eventId;

    public CurtainAdapter(List<WareCurtain> list_curtain, Context context, LayoutInflater mInflater,byte eventId) {
        this.list_curtain = list_curtain;
        this.context = context;
        this.mInflater = mInflater;
        this.eventId = eventId;
    }
    public void notifyDataSetChanged(List<WareCurtain> list_curtain) {
        this.list_curtain = list_curtain;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != list_curtain) {
            return list_curtain.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list_curtain.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item_light, null);
            viewHolder = new ViewHolder();
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.time = (ImageView) convertView.findViewById(R.id.time);
            viewHolder.on = (ImageView) convertView.findViewById(R.id.on);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.pattern = (TextView) convertView.findViewById(R.id.pattern);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list_curtain.get(position).getDev().getDevName());

        List<WareSceneDevItem> items = null;
        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++){
            if(eventId == MyApplication.getWareData().getSceneEvents().get(i).getEventld()){
                items = MyApplication.getWareData().getSceneEvents().get(i).getItemAry();
                break;
            }
        }

        if (items == null) {
            for (int i = 0; i < list_curtain.size(); i++) {
                //设备默认图标  或者是默认为关闭状态
                viewHolder.appliance.setImageResource(R.drawable.quanguan_scene);
            }
        } else {
            for (int i = 0; i < items.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (items.get(i).getDevID() == list_curtain.get(position).getDev().getDevId()
                        && items.get(i).getUid().equals(list_curtain.get(position).getDev().getCanCpuId())
                        && items.get(i).getDevType() == 4
                        && items.get(i).getbOnOff() == 1) {
                    viewHolder.appliance.setImageResource(R.drawable.quankai_scene);
                } else
                    viewHolder.appliance.setImageResource(R.drawable.quanguan_scene);
            }
        }

        if (list_curtain.get(position).getbOnOff() == 0){
            viewHolder.appliance.setImageResource(R.drawable.quanguan_scene);
        }else {
            viewHolder.appliance.setImageResource(R.drawable.quankai_scene);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView appliance,time,on;
        TextView title,pattern;
    }
}
