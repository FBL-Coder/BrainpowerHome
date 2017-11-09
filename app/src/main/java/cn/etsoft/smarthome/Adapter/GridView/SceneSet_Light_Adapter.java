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

public class SceneSet_Light_Adapter extends BaseAdapter {

    private List<WareSceneDevItem> mSceneDev;
    private Context mContext;
    private List<WareLight> mLights;
    private boolean mIsShowSelect;

    public SceneSet_Light_Adapter(int sceneposition, Context context, List<WareLight> lights, boolean isShowSelect) {
        mIsShowSelect = isShowSelect;
        mLights = lights;
        mContext = context;
        SelectDev(sceneposition);
    }

    public void SelectDev(int sceneposition) {
        if (WareDataHliper.initCopyWareData().getCopyScenes().size() <= sceneposition)
            return;
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
        for (int j = 0; j < mLights.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < mSceneDev.size(); i++) {
                if (mSceneDev.get(i).getDevID() == mLights.get(j).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mLights.get(j).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mLights.get(j).getDev().getType()) {
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

    public void notifyDataSetChanged(List<WareLight> mLights, int sceneposition, boolean mIsShowSelect) {
        this.mLights = mLights;
        this.mIsShowSelect = mIsShowSelect;
        SelectDev(sceneposition);
        super.notifyDataSetChanged();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_sceneset_light_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.SceneSet_GridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_IV);
            viewHoler.mSelect = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_Select);
            viewHoler.mSeekBar = (RangeSeekBar) convertView.findViewById(R.id.SceneSet_GridView_Item_Slide);
            viewHoler.mSeekBar.setVisibility(View.GONE);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        mLights.get(position).setbOnOff((byte) 0);
        viewHoler.mIV.setImageResource(R.drawable.dg_dev_item_close);
        viewHoler.mSelect.setImageResource(R.drawable.select_no);
        viewHoler.mSeekBar.setRight(4);

        if (mSceneDev != null) {
            for (int i = 0; i < mSceneDev.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mSceneDev.get(i).getDevID() == mLights.get(position).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mLights.get(position).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mLights.get(position).getDev().getType()) {
                    mLights.get(position).getDev().setSelect(true);
                    viewHoler.mSelect.setImageResource(R.drawable.select_ok);
                    if (mSceneDev.get(i).getbOnOff() == 0) {
                        mLights.get(position).setbOnOff((byte) 0);
                        viewHoler.mIV.setImageResource(R.drawable.dg_dev_item_close);
                    } else if (mSceneDev.get(i).getbOnOff() == 1) {
                        mLights.get(position).setbOnOff((byte) 1);
                        viewHoler.mIV.setImageResource(R.drawable.dg_dev_item_open);
                    }
                }
            }
        }


        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLights.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mLights.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mLights.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mLights.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            mSceneDev.remove(i);
                            finalViewHoler.mSelect.setImageResource(R.drawable.select_no);
                            mLights.get(position).getDev().setSelect(false);
                        }
                    }
                } else {
                    mLights.get(position).getDev().setSelect(true);
                    WareSceneDevItem item = new WareSceneDevItem();
                    item.setDevID((byte) mLights.get(position).getDev().getDevId());
                    item.setbOnOff(mLights.get(position).getbOnOff());
                    item.setDevType((byte) mLights.get(position).getDev().getType());
                    item.setCanCpuID(mLights.get(position).getDev().getCanCpuId());
                    mSceneDev.add(item);
                    finalViewHoler.mSelect.setImageResource(R.drawable.select_ok);
                }
                Log.i(TAG, "onClick: ****" + mSceneDev.size());
            }
        });

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLights.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mLights.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mLights.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mLights.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            if (mLights.get(position).getbOnOff() == 0) {
                                mSceneDev.get(i).setbOnOff((byte) 1);
                            } else {
                                mSceneDev.get(i).setbOnOff((byte) 0);
                            }

                        }
                    }
                    notifyDataSetChanged(mLights);
                } else {
                    ToastUtil.showText("未选中，不可操作");
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
