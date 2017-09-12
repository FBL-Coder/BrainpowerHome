package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareFloorHeat;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制页面——灯光 适配器
 */

public class Control_FloorHeat_Adapter extends BaseAdapter {

    private Context mContext;
    private List<WareFloorHeat> mFloorHeat;
    private int floorHeatTemp = 0;

    public Control_FloorHeat_Adapter(Context context, List<WareFloorHeat> lights) {
        mFloorHeat = lights;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareFloorHeat> mFloorHeat) {
        this.mFloorHeat = mFloorHeat;
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mFloorHeat.size();
    }

    @Override
    public Object getItem(int position) {
        return mFloorHeat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHoler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_control_floorheat_item, null);
            viewHoler = new ViewHolder(convertView);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHolder) convertView.getTag();
        if (mFloorHeat.get(position).getbOnOff() == 0)
            viewHoler.mControlGridViewItemIV.setImageResource(R.drawable.floorheat_close);
        else viewHoler.mControlGridViewItemIV.setImageResource(R.drawable.floorheat_open);
        viewHoler.mControlGridViewItemTemp.setText("当前温度 :" + mFloorHeat.get(position).getTempget() + "℃");
        viewHoler.mControlGridViewItemName.setText(mFloorHeat.get(position).getDev().getDevName());
        viewHoler.mFloorheatTempSet.setText(mFloorHeat.get(position).getTempget() + "℃");
        floorHeatTemp = mFloorHeat.get(position).getTempget();

        viewHoler.mControlGridViewItemIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);

                if (mFloorHeat.get(position).getbOnOff() == 1)
                    SendDataUtil.controlDev(mFloorHeat.get(position).getDev(), UdpProPkt.E_FLOOR_HEAT_CMD.e_floorHeat_close.getValue());
                else
                    SendDataUtil.controlDev(mFloorHeat.get(position).getDev(), UdpProPkt.E_FLOOR_HEAT_CMD.e_floorHeat_open.getValue());
            }
        });
        final ViewHolder finalViewHoler = viewHoler;
        viewHoler.mFloorheatTempAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++floorHeatTemp;
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
//                SendDataUtil.controlDev(mFloorHeat.get(position).getDev(), UdpProPkt.E_FLOOR_HEAT_CMD..getValue());
                finalViewHoler.mFloorheatTempSet
                        .setText(floorHeatTemp + "℃");
            }
        });
        viewHoler.mFloorheatTempDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                --floorHeatTemp;
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                finalViewHoler.mFloorheatTempSet
                        .setText(floorHeatTemp + "℃");
            }
        });

        return convertView;
    }

    static class ViewHolder {
        View view;
        TextView mControlGridViewItemName;
        ImageView mControlGridViewItemIV;
        TextView mControlGridViewItemTemp;
        ImageView mFloorheatTempAdd;
        TextView mFloorheatTempSet;
        ImageView mFloorheatTempDown;

        ViewHolder(View view) {
            this.view = view;
            this.mControlGridViewItemName = (TextView) view.findViewById(R.id.Control_GridView_Item_Name);
            this.mControlGridViewItemIV = (ImageView) view.findViewById(R.id.Control_GridView_Item_IV);
            this.mControlGridViewItemTemp = (TextView) view.findViewById(R.id.Control_GridView_Item_Temp);
            this.mFloorheatTempAdd = (ImageView) view.findViewById(R.id.floorheat_temp_add);
            this.mFloorheatTempSet = (TextView) view.findViewById(R.id.floorheat_temp_set);
            this.mFloorheatTempDown = (ImageView) view.findViewById(R.id.floorheat_temp_down);
        }
    }
}
