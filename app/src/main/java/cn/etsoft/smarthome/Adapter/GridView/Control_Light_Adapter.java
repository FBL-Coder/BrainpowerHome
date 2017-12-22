package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jaygoo.widget.RangeSeekBar;

import java.util.List;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制页面——灯光 适配器
 */

public class Control_Light_Adapter extends BaseAdapter {

    private Context mContext;
    private List<WareLight> mLights;

    public Control_Light_Adapter(Context context, List<WareLight> lights) {
        mLights = lights;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareLight> mLights) {
        this.mLights = mLights;
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mLights.size();
    }

    @Override
    public Object getItem(int position) {
        return mLights.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_control_light_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.Control_GridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.Control_GridView_Item_IV);
            viewHoler.seekBar = (RangeSeekBar) convertView.findViewById(R.id.Control_GridView_Item_Slide);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        if (mLights.size() == 0 || position > mLights.size() - 1)
            return convertView;

        if (mLights.get(position).getbTuneEn() == 1) {
            viewHoler.seekBar.setVisibility(View.VISIBLE);
            viewHoler.seekBar.setValue(mLights.get(position).getLmVal());
        } else viewHoler.seekBar.setVisibility(View.INVISIBLE);


        if (mLights.get(position).getbOnOff() == 0)
            viewHoler.mIV.setImageResource(R.drawable.light_close);
        else viewHoler.mIV.setImageResource(R.drawable.light_open);

        viewHoler.mName.setText(mLights.get(position).getDev().getDevName());


        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                if (mLights.get(position).getbOnOff() == 0)
                    SendDataUtil.controlDev(mLights.get(position).getDev(), 0);
                else SendDataUtil.controlDev(mLights.get(position).getDev(), 1);
            }
        });
        viewHoler.seekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            int min = 0;

            @Override
            public void onRangeChanged(RangeSeekBar rangeSeekBar, float v, float v1, boolean b) {
                min = (int) v;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar rangeSeekBar, boolean b) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar rangeSeekBar, boolean b) {
                if (min < mLights.get(position).getLmVal())
                    SendDataUtil.controlLight(mLights.get(position), UdpProPkt.E_LGT_CMD.e_lgt_dark.getValue(), min);
                else if (min > mLights.get(position).getLmVal())
                    SendDataUtil.controlLight(mLights.get(position), UdpProPkt.E_LGT_CMD.e_lgt_bright.getValue(), min);
            }
        });
        return convertView;
    }

    class ViewHoler {
        ImageView mIV;
        TextView mName;
        RangeSeekBar seekBar;
    }
}
