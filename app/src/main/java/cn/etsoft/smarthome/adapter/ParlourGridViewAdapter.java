package cn.etsoft.smarthome.adapter;

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
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareSceneDevItem;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class ParlourGridViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareDev> listViewItems;
    private int eventId;

    public ParlourGridViewAdapter(Context context, List<WareDev> listViewItems, int eventId) {
        this.listViewItems = listViewItems;
        mInflater = LayoutInflater.from(context);
        this.eventId = eventId;
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
            viewHolder.light_gv_img = (ImageView) convertView.findViewById(R.id.light_gv_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(listViewItems.get(position).getDevName());

        List<WareSceneDevItem> items = null;
        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++){
            if(eventId == MyApplication.getWareData().getSceneEvents().get(i).getEventld()){
                items = MyApplication.getWareData().getSceneEvents().get(i).getItemAry();
                break;
            }
        }
        if (items == null) {
            for (int i = 0; i < listViewItems.size(); i++) {
                //设备默认图标  或者是默认为关闭状态
                if (listViewItems.get(position).getType() == UdpProPkt.E_WARE_TYPE.e_ware_light.getValue())
                    viewHolder.light_gv_img.setImageResource(R.drawable.lightoff);
                if (listViewItems.get(position).getType() == UdpProPkt.E_WARE_TYPE.e_ware_airCond.getValue())
                    viewHolder.light_gv_img.setImageResource(R.drawable.off3);
                if (listViewItems.get(position).getType() == UdpProPkt.E_WARE_TYPE.e_ware_curtain.getValue())
                    viewHolder.light_gv_img.setImageResource(R.drawable.clg);
            }
        } else {
            for (int i = 0; i < items.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (listViewItems.get(position).getType() == UdpProPkt.E_WARE_TYPE.e_ware_light.getValue()) {
                    if (items.get(i).getDevID() == listViewItems.get(position).getDevId()
                            && items.get(i).getbOnOff() == 1) {
                        viewHolder.light_gv_img.setImageResource(R.drawable.lighton);
                        break;
                    } else
                        viewHolder.light_gv_img.setImageResource(R.drawable.lightoff);
                }
                if (listViewItems.get(position).getType() == UdpProPkt.E_WARE_TYPE.e_ware_airCond.getValue()) {
                    if (items.get(i).getDevID() == listViewItems.get(position).getDevId()
                            && items.get(i).getbOnOff() == 1) {
                        viewHolder.light_gv_img.setImageResource(R.drawable.on3);
                        break;
                    } else
                        viewHolder.light_gv_img.setImageResource(R.drawable.off3);
                }
                if (listViewItems.get(position).getType() == UdpProPkt.E_WARE_TYPE.e_ware_curtain.getValue()) {
                    if (items.get(i).getDevID() == listViewItems.get(position).getDevId()
                            && items.get(i).getbOnOff() == 1) {
                        viewHolder.light_gv_img.setImageResource(R.drawable.clk);
                        break;
                    } else
                        viewHolder.light_gv_img.setImageResource(R.drawable.clg);
                }
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        ImageView light_gv_img;
    }
}
