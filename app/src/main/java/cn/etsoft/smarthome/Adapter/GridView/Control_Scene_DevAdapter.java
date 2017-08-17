package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/29.
 * 定时器 设置  设备适配器
 */

public class Control_Scene_DevAdapter extends BaseAdapter {

    private List<WareSceneDevItem> sceneDevs;
    private Context mContext;

    public Control_Scene_DevAdapter(List<WareSceneDevItem> list, Context context) {
        sceneDevs = list;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareSceneDevItem> list) {
        sceneDevs = list;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (sceneDevs == null)
            return 0;
        return sceneDevs.size();
    }

    @Override
    public Object getItem(int position) {
        if (sceneDevs == null)
            return null;
        return sceneDevs.get(position);
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

        int type_dev = sceneDevs.get(position).getDevType();
        if (type_dev == 0) {
            for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                if (sceneDevs.get(position).getDevID() == AirCondDev.getDev().getDevId() &&
                        sceneDevs.get(position).getCanCpuID().endsWith(AirCondDev.getDev().getCanCpuId())) {
                    viewHolder.name.setText(AirCondDev.getDev().getDevName());
                    if (AirCondDev.getbOnOff() == 0) {
                        viewHolder.type.setImageResource(R.drawable.kt_dev_item_close);
                        viewHolder.state.setText("关闭");
                    } else {
                        viewHolder.type.setImageResource(R.drawable.kt_dev_item_open);
                        viewHolder.state.setText("打开");
                    }
                }
            }
        }else if (type_dev == 3) {
            for (int j = 0; j < MyApplication.getWareData().getLights().size(); j++) {
                WareLight Light = MyApplication.getWareData().getLights().get(j);
                if (sceneDevs.get(position).getDevID() == Light.getDev().getDevId() &&
                        sceneDevs.get(position).getCanCpuID().endsWith(Light.getDev().getCanCpuId())) {
                    viewHolder.name.setText(Light.getDev().getDevName());
                    if (Light.getbOnOff() == 0) {
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
                if (sceneDevs.get(position).getDevID() == Curtain.getDev().getDevId() &&
                        sceneDevs.get(position).getCanCpuID().endsWith(Curtain.getDev().getCanCpuId())) {
                    viewHolder.name.setText(Curtain.getDev().getDevName());
                    if (Curtain.getbOnOff() == 0) {
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
