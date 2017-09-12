package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareFloorHeat;
import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制页面—新风 适配器
 */

public class Control_FreshAir_Adapter extends BaseAdapter {

    private Context mContext;
    private List<WareFreshAir> mFreshAir;

    public Control_FreshAir_Adapter(Context context, List<WareFreshAir> freshAirs) {
        mFreshAir = freshAirs;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareFreshAir> freshAirs) {
        this.mFreshAir = freshAirs;
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mFreshAir.size();
    }

    @Override
    public Object getItem(int position) {
        return mFreshAir.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_control_freshair_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.Control_GridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.Control_GridView_Item_IV);
            viewHoler.mLow = (TextView) convertView.findViewById(R.id.Control_GridView_Item_low);
            viewHoler.mMin = (TextView) convertView.findViewById(R.id.Control_GridView_Item_min);
            viewHoler.mHig = (TextView) convertView.findViewById(R.id.Control_GridView_Item_hig);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();
        if (mFreshAir.get(position).getbOnOff() == 0)
            viewHoler.mIV.setImageResource(R.drawable.freshair_close);
        else viewHoler.mIV.setImageResource(R.drawable.freshair_open);
        if (mFreshAir.get(position).getSpdSel() == 2) {
            viewHoler.mLow.setBackground(mContext.getResources().getDrawable(R.color.blue));
            viewHoler.mMin.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
            viewHoler.mHig.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
        } else if (mFreshAir.get(position).getSpdSel() == 3) {
            viewHoler.mLow.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
            viewHoler.mMin.setBackground(mContext.getResources().getDrawable(R.color.blue));
            viewHoler.mHig.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
        } else if (mFreshAir.get(position).getSpdSel() == 4) {
            viewHoler.mLow.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
            viewHoler.mMin.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
            viewHoler.mHig.setBackground(mContext.getResources().getDrawable(R.color.blue));
        }


        viewHoler.mName.setText(mFreshAir.get(position).getDev().getDevName());

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                if (mFreshAir.get(position).getbOnOff() == 1) {
                    SendDataUtil.controlDev(mFreshAir.get(position).getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_close.getValue());
                } else {
                    SendDataUtil.controlDev(mFreshAir.get(position).getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_open.getValue());
                }
            }
        });
        viewHoler.mLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                SendDataUtil.controlDev(mFreshAir.get(position).getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_spd_low.getValue());
            }
        });
        viewHoler.mMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                SendDataUtil.controlDev(mFreshAir.get(position).getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_spd_mid.getValue());
            }
        });
        viewHoler.mHig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                SendDataUtil.controlDev(mFreshAir.get(position).getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_spd_high.getValue());
            }
        });


        return convertView;
    }

    class ViewHoler {
        ImageView mIV;
        TextView mName, mLow, mMin, mHig;
    }
}
