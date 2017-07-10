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

import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——窗帘 适配器
 */

public class SceneSet_Curtain_Adapter extends BaseAdapter {

    private List<WareSceneDevItem> mSceneDev;
    private Context mContext;
    private List<WareCurtain> mCurtains;
    private boolean mIsShowSelect;

    public SceneSet_Curtain_Adapter(int sceneposition, Context context, List<WareCurtain> Curtains, boolean isShowSelect) {
        mIsShowSelect = isShowSelect;
        mCurtains = Curtains;
        mContext = context;
        SelectDev(sceneposition);
    }

    public void SelectDev(int sceneposition) {
        mSceneDev = WareDataHliper.initCopyWareData().getCopyScenes().get(sceneposition).getItemAry();
        if (mSceneDev == null) {
            mSceneDev = new ArrayList<>();
            for (int i = 0; i < WareDataHliper.initCopyWareData().getCopyScenes().size(); i++) {
                if (MyApplication.getWareData().getSceneEvents().get(sceneposition).getEventId()
                        == WareDataHliper.initCopyWareData().getCopyScenes().get(i).getEventId()) {
                    WareDataHliper.initCopyWareData().getCopyScenes().get(i).setItemAry(mSceneDev);
                    break;
                }
            }
        }
        //给所有设备和情景关联的赋值
        for (int j = 0; j < mCurtains.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < mSceneDev.size(); i++) {
                if (mSceneDev.get(i).getDevID() == mCurtains.get(j).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mCurtains.get(j).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mCurtains.get(j).getDev().getType()) {
                    mCurtains.get(j).getDev().setSelect(true);
                    isContain = true;
                }
            }
            if (!isContain) {
                mCurtains.get(j).getDev().setSelect(false);
            }
        }
        if (mIsShowSelect) {
            for (int i = 0; i < mCurtains.size(); ) {
                if (!mCurtains.get(i).getDev().isSelect()) {
                    mCurtains.remove(i);
                } else
                    i++;
            }
        }
    }


    public void notifyDataSetChanged(List<WareCurtain> mCurtains, int sceneposition, boolean mIsShowSelect) {
        this.mCurtains = mCurtains;
        this.mIsShowSelect = mIsShowSelect;
        SelectDev(sceneposition);
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
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_sceneset_light_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.SceneSet_GridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_IV);
            viewHoler.mSelect = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_Select);
            viewHoler.mSeekBar = (RangeSeekBar) convertView.findViewById(R.id.SceneSet_GridView_Item_Slide);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();
        if (mCurtains.get(position).getbOnOff() == 0)
            viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
        else viewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
        viewHoler.mSelect.setImageResource(R.drawable.ic_launcher);
        viewHoler.mSeekBar.setVisibility(View.GONE);
        if (mSceneDev != null) {
            for (int i = 0; i < mSceneDev.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mSceneDev.get(i).getDevID() == mCurtains.get(position).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mCurtains.get(position).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mCurtains.get(position).getDev().getType()) {
                    mCurtains.get(position).getDev().setSelect(true);
                    viewHoler.mSelect.setImageResource(R.drawable.ic_launcher_round);
                    if (mSceneDev.get(i).getbOnOff() == 0) {
                        viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
                    } else if (mSceneDev.get(i).getbOnOff() == 1) {
                        viewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
                    }
                }
            }
        }
        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurtains.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mCurtains.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mCurtains.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mCurtains.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            mSceneDev.remove(i);
                            finalViewHoler.mSelect.setImageResource(R.drawable.ic_launcher);
                            mCurtains.get(position).getDev().setSelect(false);
                        }
                    }
                } else {
                    mCurtains.get(position).getDev().setSelect(true);
                    WareSceneDevItem item = new WareSceneDevItem();
                    item.setDevID((byte) mCurtains.get(position).getDev().getDevId());
                    item.setbOnOff(mCurtains.get(position).getbOnOff());
                    item.setDevType((byte) mCurtains.get(position).getDev().getType());
                    item.setCanCpuID(mCurtains.get(position).getDev().getCanCpuId());
                    mSceneDev.add(item);
                    finalViewHoler.mSelect.setImageResource(R.drawable.ic_launcher_round);
                }
                Log.i(TAG, "onClick: ****" + mSceneDev.size());
            }
        });

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurtains.get(position).getbOnOff() == 0) {
                    mCurtains.get(position).setbOnOff((byte) 1);
                    finalViewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
                } else {
                    mCurtains.get(position).setbOnOff((byte) 0);
                    finalViewHoler.mIV.setImageResource(R.drawable.ic_launcher);
                }

                if (mCurtains.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mCurtains.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mCurtains.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mCurtains.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            mSceneDev.get(i).setbOnOff(mCurtains.get(position).getbOnOff());
                        }
                        Log.i(TAG, "onClick: ----" + mSceneDev.get(i).getbOnOff());
                    }
                }
            }
        });
        viewHoler.mName.setText(mCurtains.get(position).getDev().getDevName());
        return convertView;
    }

    class ViewHoler {
        ImageView mSelect, mIV;
        TextView mName;
        RangeSeekBar mSeekBar;
    }
}
