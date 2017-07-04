package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaygoo.widget.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——灯光 适配器
 */

public class SetAddDev_Light_Adapter extends BaseAdapter {

    private List<WareDev> mDevs;
    private Context mContext;
    private List<WareLight> mLights;
    private boolean mIsShowSelect;

    public SetAddDev_Light_Adapter(List<WareDev> devs, Context context, List<WareLight> lights, boolean isShowSelect) {
        mIsShowSelect = isShowSelect;
        mLights = lights;
        mContext = context;
        mDevs = devs;
        SelectDev();
    }

    public void SelectDev() {
        if (mDevs == null) {
            mDevs = new ArrayList<>();
        }
        //给所有设备和情景关联的赋值
        for (int j = 0; j < mLights.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < mDevs.size(); i++) {
                if (mDevs.get(i).getDevId() == mLights.get(j).getDev().getDevId()
                        && mDevs.get(i).getCanCpuId().equals(mLights.get(j).getDev().getCanCpuId())
                        && mDevs.get(i).getType() == mLights.get(j).getDev().getType()) {
                    mLights.get(j).getDev().setSelect(true);
                    isContain = true;
                }
            }
            if (!isContain) {
                mLights.get(j).getDev().setSelect(false);
            }
        }
        if (mIsShowSelect) {
            for (int i = 0; i < mLights.size(); ) {
                if (!mLights.get(i).getDev().isSelect()) {
                    mLights.remove(i);
                } else
                    i++;
            }
        }
    }


    public void notifyDataSetChanged(List<WareLight> mLights, List<WareDev> devs, boolean mIsShowSelect) {
        this.mLights = mLights;
        this.mIsShowSelect = mIsShowSelect;
        mDevs = devs;
        SelectDev();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sceneset_gridview_light_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.SceneSet_GridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_IV);
            viewHoler.mSelect = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_Select);
            viewHoler.mSeekBar = (RangeSeekBar) convertView.findViewById(R.id.SceneSet_GridView_Item_Slide);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();
        if (mLights.get(position).getbOnOff() == 0)
            viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
        else viewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
        viewHoler.mSelect.setImageResource(R.drawable.ic_launcher);
        viewHoler.mSeekBar.setRight(4);

        if (mDevs != null) {
            for (int i = 0; i < mDevs.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mDevs.get(i).getDevId() == mLights.get(position).getDev().getDevId()
                        && mDevs.get(i).getCanCpuId().equals(mLights.get(position).getDev().getCanCpuId())
                        && mDevs.get(i).getType() == mLights.get(position).getDev().getType()) {
                    mLights.get(position).getDev().setSelect(true);
                    viewHoler.mSelect.setImageResource(R.drawable.ic_launcher_round);
                    if (mDevs.get(i).getbOnOff() == 0) {
                        viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
                    } else if (mDevs.get(i).getbOnOff() == 1) {
                        viewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
                    }
                }
            }
        }
        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLights.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mDevs.size(); i++) {
                        if (mLights.get(position).getDev().getDevId() == mDevs.get(i).getDevId()
                                && mLights.get(position).getDev().getType() == mDevs.get(i).getType()
                                && mLights.get(position).getDev().getCanCpuId().equals(mDevs.get(i).getCanCpuId())) {
                            mDevs.remove(i);
                            finalViewHoler.mSelect.setImageResource(R.drawable.ic_launcher);
                            mLights.get(position).getDev().setSelect(false);
                        }
                    }
                } else {
                    mLights.get(position).getDev().setSelect(true);
                    WareDev dev = new WareDev();
                    dev.setDevId((byte) mLights.get(position).getDev().getDevId());
                    dev.setbOnOff(mLights.get(position).getbOnOff());
                    dev.setType((byte) mLights.get(position).getDev().getType());
                    dev.setCanCpuId(mLights.get(position).getDev().getCanCpuId());
                    mDevs.add(dev);
                    finalViewHoler.mSelect.setImageResource(R.drawable.ic_launcher_round);
                }
                Log.i(TAG, "onClick: ****" + mDevs.size());
            }
        });

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLights.get(position).getbOnOff() == 0) {
                    mLights.get(position).setbOnOff((byte) 1);
                    finalViewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
                } else {
                    mLights.get(position).setbOnOff((byte) 0);
                    finalViewHoler.mIV.setImageResource(R.drawable.ic_launcher);
                }
            }
        });
        viewHoler.mName.setText(mLights.get(position).getDev().getDevName());
        return convertView;
    }

    class ViewHoler {
        ImageView mSelect, mIV;
        TextView mName;
        RangeSeekBar mSeekBar;
    }
}
