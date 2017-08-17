package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.GroupSet_Data;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.Domain.WareSetBox;
import cn.etsoft.smarthome.Domain.WareTv;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/29.
 * 定时器 设置  设备适配器
 */

public class GroupSet_DevAdapter extends BaseAdapter {

    private List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> timer_list;
    private Context mContext;

    public GroupSet_DevAdapter(List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> list, Context context) {
        timer_list = list;
        mContext = context;
    }

    public void notifyDataSetChanged(List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> list) {
        timer_list = list;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (timer_list == null)
            return 0;
        return timer_list.size();
    }

    @Override
    public Object getItem(int position) {
        if (timer_list == null)
            return null;
        return timer_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.girdview_devsitem, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.GirdView_devName);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.GirdView_devTypeIV);
            viewHolder.state = (TextView) convertView.findViewById(R.id.GirdView_devState);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int type_dev = timer_list.get(position).getDevType();
        if (type_dev == 0) {
            for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                if (timer_list.get(position).getDevID() == AirCondDev.getDev().getDevId() &&
                        timer_list.get(position).getCanCpuID().endsWith(AirCondDev.getDev().getCanCpuId())) {
                    viewHolder.name.setText(AirCondDev.getDev().getDevName());
                    if (timer_list.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.kt_dev_item_close);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.kt_dev_item_open);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        } else if (type_dev == 1) {
            for (int j = 0; j < MyApplication.getWareData().getTvs().size(); j++) {
                WareTv tv = MyApplication.getWareData().getTvs().get(j);
                if (timer_list.get(position).getDevID() == tv.getDev().getDevId() &&
                        timer_list.get(position).getCanCpuID().endsWith(tv.getDev().getCanCpuId())) {
                    viewHolder.name.setText(tv.getDev().getDevName());
                    if (timer_list.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.ic_launcher);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.ic_launcher_round);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        } else if (type_dev == 2) {
            for (int j = 0; j < MyApplication.getWareData().getStbs().size(); j++) {
                WareSetBox box = MyApplication.getWareData().getStbs().get(j);
                if (timer_list.get(position).getDevID() == box.getDev().getDevId() &&
                        timer_list.get(position).getCanCpuID().endsWith(box.getDev().getCanCpuId())) {
                    viewHolder.name.setText(box.getDev().getDevName());
                    if (timer_list.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.ic_launcher);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.ic_launcher_round);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        } else if (type_dev == 3) {
            for (int j = 0; j < MyApplication.getWareData().getLights().size(); j++) {
                WareLight Light = MyApplication.getWareData().getLights().get(j);
                if (timer_list.get(position).getDevID() == Light.getDev().getDevId() &&
                        timer_list.get(position).getCanCpuID().endsWith(Light.getDev().getCanCpuId())) {
                    viewHolder.name.setText(Light.getDev().getDevName());
                    if (timer_list.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.dg_dev_item_close);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.dg_dev_item_open);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        } else if (type_dev == 4) {
            for (int j = 0; j < MyApplication.getWareData().getCurtains().size(); j++) {
                WareCurtain Curtain = MyApplication.getWareData().getCurtains().get(j);
                if (timer_list.get(position).getDevID() == Curtain.getDev().getDevId() &&
                        timer_list.get(position).getCanCpuID().endsWith(Curtain.getDev().getCanCpuId())) {
                    viewHolder.name.setText(Curtain.getDev().getDevName());
                    if (timer_list.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.cl_dev_item_close);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.cl_dev_item_open);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView name, state;
        private ImageView type;
    }
}
