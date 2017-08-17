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
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制页面——窗帘 适配器
 */

public class Control_Curtain_Adapter extends BaseAdapter {

    private Context mContext;
    private List<WareCurtain> mCurtains;

    public Control_Curtain_Adapter(Context context, List<WareCurtain> Curtains) {
        mCurtains = Curtains;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareCurtain> mCurtains) {
        this.mCurtains = mCurtains;
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mCurtains.size();
    }

    @Override
    public Object getItem(int position) {
        return mCurtains.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHoler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_control_curtain_item, null);
            viewHoler = new ViewHolder(convertView);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHolder) convertView.getTag();

        mCurtains.get(position).setbOnOff(0);
        viewHoler.mIV.setImageResource(R.drawable.chuanglian_heng);
        viewHoler.mName.setText(mCurtains.get(position).getDev().getDevName());
        viewHoler.mOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                SendDataUtil.controlDev(mCurtains.get(position).getDev(),UdpProPkt.E_CURT_CMD.e_curt_offOn.getValue());
            }
        });
        viewHoler.mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                SendDataUtil.controlDev(mCurtains.get(position).getDev(),UdpProPkt.E_CURT_CMD.e_curt_stop.getValue());
            }
        });
        viewHoler.mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                SendDataUtil.controlDev(mCurtains.get(position).getDev(),UdpProPkt.E_CURT_CMD.e_curt_offOff.getValue());
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView mName;
        public ImageView mIV;
        public ImageView mOpen;
        public ImageView mStop;
        public ImageView mClose;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mName = (TextView) rootView.findViewById(R.id.Control_GridView_Item_Name);
            this.mIV = (ImageView) rootView.findViewById(R.id.Control_GridView_Item_IV);
            this.mOpen = (ImageView) rootView.findViewById(R.id.open);
            this.mStop = (ImageView) rootView.findViewById(R.id.stop);
            this.mClose = (ImageView) rootView.findViewById(R.id.close);
        }

    }
}
