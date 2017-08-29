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
import cn.etsoft.smarthome.R;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——灯光 适配器
 */

public class SetAddDev_Adapter extends BaseAdapter {

    private List<WareDev> mSaveDevs;
    private Context mContext;
    private List<WareDev> mAllDevs;
    private boolean mIsShowSelect;

    public SetAddDev_Adapter(List<WareDev> savedevs, Context context, List<WareDev> alldevs, boolean isShowSelect) {
        mIsShowSelect = isShowSelect;
        mAllDevs = alldevs;
        mContext = context;
        mSaveDevs = savedevs;
        SelectDev();
    }

    public void SelectDev() {
        if (mSaveDevs == null) {
            mSaveDevs = new ArrayList<>();
        }
        //给所有设备和情景关联的赋值
        for (int j = 0; j < mAllDevs.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < mSaveDevs.size(); i++) {
                if (mSaveDevs.get(i).getDevId() == mAllDevs.get(j).getDevId()
                        && mSaveDevs.get(i).getCanCpuId().equals(mAllDevs.get(j).getCanCpuId())
                        && mSaveDevs.get(i).getType() == mAllDevs.get(j).getType()) {
                    mAllDevs.get(j).setSelect(true);
                    isContain = true;
                }
            }
            if (!isContain) {
                mAllDevs.get(j).setSelect(false);
            }
        }
        if (mIsShowSelect) {
            for (int i = 0; i < mAllDevs.size(); ) {
                if (!mAllDevs.get(i).isSelect()) {
                    mAllDevs.remove(i);
                } else
                    i++;
            }
        }
    }


    public void notifyDataSetChanged(List<WareDev> mAllDevs, List<WareDev> devs, boolean mIsShowSelect) {
        this.mAllDevs = mAllDevs;
        this.mIsShowSelect = mIsShowSelect;
        mSaveDevs = devs;
        SelectDev();
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mAllDevs.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllDevs.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_sceneset_light_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.SceneSet_GridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_IV);
            viewHoler.mSelect = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_Select);
            viewHoler.mSeekBar = (RangeSeekBar) convertView.findViewById(R.id.SceneSet_GridView_Item_Slide);
            viewHoler.mSeekBar.setVisibility(View.GONE);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();
        if (mAllDevs.get(position).getType() == 0) {
            if (mAllDevs.get(position).getbOnOff() == 0)
                viewHoler.mIV.setImageResource(R.drawable.kt_dev_item_close);
            else viewHoler.mIV.setImageResource(R.drawable.kt_dev_item_open);
        } else if (mAllDevs.get(position).getType() == 3) {
            if (mAllDevs.get(position).getbOnOff() == 0)
                viewHoler.mIV.setImageResource(R.drawable.dg_dev_item_close);
            else viewHoler.mIV.setImageResource(R.drawable.dg_dev_item_open);
        } else if (mAllDevs.get(position).getType() == 4) {
            if (mAllDevs.get(position).getbOnOff() == 0)
                viewHoler.mIV.setImageResource(R.drawable.cl_dev_item_close);
            else viewHoler.mIV.setImageResource(R.drawable.cl_dev_item_open);
        } else if (mAllDevs.get(position).getType() == 7) {
            if (mAllDevs.get(position).getbOnOff() == 0)
                viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
            else viewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
        }
        viewHoler.mSelect.setImageResource(R.drawable.select_no);

        if (mSaveDevs != null) {
            for (int i = 0; i < mSaveDevs.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mSaveDevs.get(i).getDevId() == mAllDevs.get(position).getDevId()
                        && mSaveDevs.get(i).getCanCpuId().equals(mAllDevs.get(position).getCanCpuId())
                        && mSaveDevs.get(i).getType() == mAllDevs.get(position).getType()) {
                    mAllDevs.get(position).setSelect(true);
                    viewHoler.mSelect.setImageResource(R.drawable.select_ok);
                    if (mAllDevs.get(position).getType() == 0) {
                        if (mAllDevs.get(position).getbOnOff() == 0)
                            viewHoler.mIV.setImageResource(R.drawable.kt_dev_item_close);
                        else viewHoler.mIV.setImageResource(R.drawable.kt_dev_item_open);
                    } else if (mAllDevs.get(position).getType() == 3) {
                        if (mAllDevs.get(position).getbOnOff() == 0)
                            viewHoler.mIV.setImageResource(R.drawable.dg_dev_item_close);
                        else viewHoler.mIV.setImageResource(R.drawable.dg_dev_item_open);
                    } else if (mAllDevs.get(position).getType() == 4) {
                        if (mAllDevs.get(position).getbOnOff() == 0)
                            viewHoler.mIV.setImageResource(R.drawable.cl_dev_item_close);
                        else viewHoler.mIV.setImageResource(R.drawable.cl_dev_item_open);
                    } else if (mAllDevs.get(position).getType() == 7) {
                        if (mAllDevs.get(position).getbOnOff() == 0)
                            viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
                        else viewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
                    }
                }
            }
        }
        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAllDevs.get(position).isSelect()) {
                    for (int i = 0; i < mSaveDevs.size(); i++) {
                        if (mAllDevs.get(position).getDevId() == mSaveDevs.get(i).getDevId()
                                && mAllDevs.get(position).getType() == mSaveDevs.get(i).getType()
                                && mAllDevs.get(position).getCanCpuId().equals(mSaveDevs.get(i).getCanCpuId())) {
                            mSaveDevs.remove(i);
                            finalViewHoler.mSelect.setImageResource(R.drawable. select_no);
                            mAllDevs.get(position).setSelect(false);
                        }
                    }
                } else {
                    mAllDevs.get(position).setSelect(true);
                    WareDev dev = new WareDev();
                    dev.setDevId((byte) mAllDevs.get(position).getDevId());
                    dev.setbOnOff(mAllDevs.get(position).getbOnOff());
                    dev.setType((byte) mAllDevs.get(position).getType());
                    dev.setCanCpuId(mAllDevs.get(position).getCanCpuId());
                    mSaveDevs.add(dev);
                    finalViewHoler.mSelect.setImageResource(R.drawable.select_ok);
                }
                Log.i(TAG, "onClick: ****" + mSaveDevs.size());
            }
        });

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAllDevs.get(position).getbOnOff() == 0) {
                    mAllDevs.get(position).setbOnOff((byte) 1);
                } else {
                    mAllDevs.get(position).setbOnOff((byte) 0);
                }
                notifyDataSetChanged();
            }
        });
        viewHoler.mName.setText(mAllDevs.get(position).getDevName());
        return convertView;
    }

    class ViewHoler {
        ImageView mSelect, mIV;
        TextView mName;
        RangeSeekBar mSeekBar;
    }
}
