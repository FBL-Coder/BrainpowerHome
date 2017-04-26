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
import cn.etsoft.smarthome.domain.SetSafetyResult.SecInfoRowsBean.RunDevItemBean;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;

/**
 * Created by Say GoBay on 2016/9/1.
 * 安防模块-安防fragment的适配器
 */
public class GridViewAdapter_safety extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<RunDevItemBean> devList;

    public GridViewAdapter_safety(List<RunDevItemBean> dev, Context context) {
        super();
        devList = dev;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (null != devList) {
            return devList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return devList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item_user, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.equip_name);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.equip_type);
            viewHolder.state = (TextView) convertView.findViewById(R.id.equip_style);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int type_dev = devList.get(position).getDevType();
        if (type_dev == 0) {
            for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                if (devList.get(position).getCanCpuID().equals(AirCondDev.getDev().getCanCpuId()) && devList.get(position).getDevID()== AirCondDev.getDev().getDevId()) {
                    viewHolder.name.setText(AirCondDev.getDev().getDevName());
                    if (devList.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.kongtiao1);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.kongtiao2);
                        viewHolder.state.setText("打开");
                    }

                }
            }
        } else if (type_dev == 1) {
            for (int j = 0; j <  MyApplication.getWareData().getTvs().size(); j++) {
                WareTv tv = MyApplication.getWareData().getTvs().get(j);
                if (devList.get(position).getCanCpuID().equals(tv.getDev().getCanCpuId()) && devList.get(position).getDevID() == tv.getDev().getDevId()) {
                    viewHolder.name.setText(tv.getDev().getDevName());
                    if (devList.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.ds);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.ds);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        } else if (type_dev == 2) {
            for (int j = 0; j <  MyApplication.getWareData().getStbs().size(); j++) {
                WareSetBox box = MyApplication.getWareData().getStbs().get(j);
                if (devList.get(position).getCanCpuID().equals(box.getDev().getCanCpuId()) && devList.get(position).getDevID() == box.getDev().getDevId()) {
                    viewHolder.name.setText(box.getDev().getDevName());
                    if (devList.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.jidinghe);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.jidinghe);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        } else if (type_dev == 3) {
            for (int j = 0; j <  MyApplication.getWareData().getLights().size(); j++) {
                WareLight Light = MyApplication.getWareData().getLights().get(j);
                if (devList.get(position).getCanCpuID().equals(Light.getDev().getCanCpuId()) && devList.get(position).getDevID() == Light.getDev().getDevId()) {
                    viewHolder.name.setText(Light.getDev().getDevName());
                    if (devList.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.light);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.lightk);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        } else if (type_dev == 4) {
            for (int j = 0; j <  MyApplication.getWareData().getCurtains().size(); j++) {
                WareCurtain Curtain = MyApplication.getWareData().getCurtains().get(j);
                if (devList.get(position).getCanCpuID().equals(Curtain.getDev().getCanCpuId()) && devList.get(position).getDevID() == Curtain.getDev().getDevId()) {
                    viewHolder.name.setText(Curtain.getDev().getDevName());
                    if (devList.get(position).getBOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.quanguan);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.quankai);
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
