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

import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——灯光 适配器
 */

public class SetAddDev_Air_Adapter extends BaseAdapter {

    private List<WareDev> mDevs;
    private Context mContext;
    private List<WareAirCondDev> mAirs;
    private boolean mIsShowSelect;

    public SetAddDev_Air_Adapter(List<WareDev> devs, Context context, List<WareAirCondDev> airs, boolean isShowSelect) {
        mIsShowSelect = isShowSelect;
        mAirs = airs;
        mContext = context;
        SelectDev(devs);
    }

    public void SelectDev(List<WareDev> devs) {
        mDevs = devs;
        if (mDevs == null) {
            mDevs = new ArrayList<>();
        }
        //给所有设备和情景关联的赋值
        for (int j = 0; j < mAirs.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < mDevs.size(); i++) {
                if (mDevs.get(i).getDevId() == mAirs.get(j).getDev().getDevId()
                        && mDevs.get(i).getCanCpuId().equals(mAirs.get(j).getDev().getCanCpuId())
                        && mDevs.get(i).getType() == mAirs.get(j).getDev().getType()) {
                    mAirs.get(j).getDev().setSelect(true);
                    isContain = true;
                }
            }
            if (!isContain) {
                mAirs.get(j).getDev().setSelect(false);
            }
        }
        if (mIsShowSelect) {
            for (int i = 0; i < mAirs.size(); ) {
                if (!mAirs.get(i).getDev().isSelect()) {
                    mAirs.remove(i);
                } else
                    i++;
            }
        }
    }


    public void notifyDataSetChanged(List<WareAirCondDev> mAirs, List<WareDev> devs, boolean mIsShowSelect) {
        this.mAirs = mAirs;
        this.mIsShowSelect = mIsShowSelect;
        SelectDev(devs);
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mAirs.size();
    }

    @Override
    public Object getItem(int position) {
        return mAirs.get(position);
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
        viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
        viewHoler.mSelect.setImageResource(R.drawable.ic_launcher);
        viewHoler.mSeekBar.setRight(4);
        if (mDevs != null) {
            for (int i = 0; i < mDevs.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mDevs.get(i).getDevId() == mAirs.get(position).getDev().getDevId()
                        && mDevs.get(i).getCanCpuId().equals(mAirs.get(position).getDev().getCanCpuId())
                        && mDevs.get(i).getType() == mAirs.get(position).getDev().getType()) {
                    mAirs.get(position).getDev().setSelect(true);
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
                if (mAirs.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mDevs.size(); i++) {
                        if (mAirs.get(position).getDev().getDevId() == mDevs.get(i).getDevId()
                                && mAirs.get(position).getDev().getType() == mDevs.get(i).getType()
                                && mAirs.get(position).getDev().getCanCpuId().equals(mDevs.get(i).getCanCpuId())) {
                            mDevs.remove(i);
                            finalViewHoler.mSelect.setImageResource(R.drawable.ic_launcher);
                            mAirs.get(position).getDev().setSelect(false);
                        }
                    }
                } else {
                    mAirs.get(position).getDev().setSelect(true);
                    WareDev dev = new WareDev();
                    dev.setDevId((byte) mAirs.get(position).getDev().getDevId());
                    dev.setbOnOff(mAirs.get(position).getbOnOff());
                    dev.setType((byte) mAirs.get(position).getDev().getType());
                    dev.setCanCpuId(mAirs.get(position).getDev().getCanCpuId());
                    mDevs.add(dev);
                    finalViewHoler.mSelect.setImageResource(R.drawable.ic_launcher_round);
                }
                Log.i(TAG, "onClick: ****" + mDevs.size());
            }
        });

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAirs.get(position).getbOnOff() == 0) {
                    mAirs.get(position).setbOnOff((byte) 1);
                    finalViewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
                } else {
                    mAirs.get(position).setbOnOff((byte) 0);
                    finalViewHoler.mIV.setImageResource(R.drawable.ic_launcher);
                }

                if (mAirs.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mDevs.size(); i++) {
                        if (mAirs.get(position).getDev().getDevId() == mDevs.get(i).getDevId()
                                && mAirs.get(position).getDev().getType() == mDevs.get(i).getType()
                                && mAirs.get(position).getDev().getCanCpuId().equals(mDevs.get(i).getCanCpuId())) {
                            mDevs.get(i).setbOnOff(mAirs.get(position).getbOnOff());
                        }
                        Log.i(TAG, "onClick: ----" + mDevs.get(i).getbOnOff());
                    }
                }
            }
        });
        viewHoler.mName.setText(mAirs.get(position).getDev().getDevName());
        return convertView;
    }

    class ViewHoler {
        ImageView mSelect, mIV;
        TextView mName;
        RangeSeekBar mSeekBar;
    }
}
