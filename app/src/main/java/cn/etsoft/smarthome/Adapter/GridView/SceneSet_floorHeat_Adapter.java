package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.jaygoo.widget.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.WareFloorHeat;
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

public class SceneSet_floorHeat_Adapter extends BaseAdapter {

    private List<WareSceneDevItem> mSceneDev;
    private Context mContext;
    private List<WareFloorHeat> mFloorHeat;
    private boolean mIsShowSelect;

    public SceneSet_floorHeat_Adapter(int sceneposition, Context context, List<WareFloorHeat> floorHeats, boolean isShowSelect) {
        mIsShowSelect = isShowSelect;
        mFloorHeat = floorHeats;
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
        for (int j = 0; j < mFloorHeat.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < mSceneDev.size(); i++) {
                if (mSceneDev.get(i).getDevID() == mFloorHeat.get(j).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mFloorHeat.get(j).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mFloorHeat.get(j).getDev().getType()) {
                    mFloorHeat.get(j).getDev().setSelect(true);
                    isContain = true;
                }
            }
            if (!isContain) {
                mFloorHeat.get(j).getDev().setSelect(false);
            }
        }
        if (mIsShowSelect) {
            for (int i = 0; i < mFloorHeat.size(); ) {
                if (!mFloorHeat.get(i).getDev().isSelect()) {
                    mFloorHeat.remove(i);
                } else
                    i++;
            }
        }
    }

    public void notifyDataSetChanged(List<WareFloorHeat> mFloorHeat, int sceneposition, boolean mIsShowSelect) {
        this.mFloorHeat = mFloorHeat;
        this.mIsShowSelect = mIsShowSelect;
        SelectDev(sceneposition);
        super.notifyDataSetChanged();
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

        mFloorHeat.get(position).setbOnOff((byte) 0);
        viewHoler.mIV.setImageResource(R.drawable.floorheat_close);
        viewHoler.mSelect.setImageResource(R.drawable.select_no);
        viewHoler.mSeekBar.setRight(4);

        if (mSceneDev != null) {
            for (int i = 0; i < mSceneDev.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mSceneDev.get(i).getDevID() == mFloorHeat.get(position).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mFloorHeat.get(position).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mFloorHeat.get(position).getDev().getType()) {
                    mFloorHeat.get(position).getDev().setSelect(true);
                    viewHoler.mSelect.setImageResource(R.drawable.select_ok);
                    if (mSceneDev.get(i).getbOnOff() == 0) {
                        mFloorHeat.get(position).setbOnOff((byte) 0);
                        viewHoler.mIV.setImageResource(R.drawable.floorheat_close);
                    } else if (mSceneDev.get(i).getbOnOff() == 1) {
                        mFloorHeat.get(position).setbOnOff((byte) 1);
                        viewHoler.mIV.setImageResource(R.drawable.floorheat_open);
                    }
                }
            }
        }
        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFloorHeat.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mFloorHeat.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mFloorHeat.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mFloorHeat.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            mSceneDev.remove(i);
                            finalViewHoler.mSelect.setImageResource(R.drawable.select_no);
                            mFloorHeat.get(position).getDev().setSelect(false);
                        }
                    }
                } else {
                    mFloorHeat.get(position).getDev().setSelect(true);
                    WareSceneDevItem item = new WareSceneDevItem();
                    item.setDevID((byte) mFloorHeat.get(position).getDev().getDevId());
                    item.setbOnOff(mFloorHeat.get(position).getbOnOff());
                    item.setDevType((byte) mFloorHeat.get(position).getDev().getType());
                    item.setCanCpuID(mFloorHeat.get(position).getDev().getCanCpuId());
                    mSceneDev.add(item);
                    finalViewHoler.mSelect.setImageResource(R.drawable.select_ok);
                }
                Log.i(TAG, "onClick: ****" + mSceneDev.size());
            }
        });

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFloorHeat.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mFloorHeat.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mFloorHeat.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mFloorHeat.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            if (mFloorHeat.get(position).getbOnOff() == 0) {
                                mSceneDev.get(i).setbOnOff((byte) 1);
                            } else {
                                mSceneDev.get(i).setbOnOff((byte) 0);
                            }

                        }
                    }
                    notifyDataSetChanged(mFloorHeat);
                }else {
                    ToastUtil.showText("未选中，不可操作");
                }

            }
        });
        viewHoler.mName.setText(mFloorHeat.get(position).getDev().getDevName());
        return convertView;
    }

    class ViewHoler {
        ImageView mSelect, mIV;
        TextView mName;
        RangeSeekBar mSeekBar;
    }
}
