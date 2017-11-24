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

import cn.etsoft.smarthome.Domain.WareFreshAir;
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

public class SceneSet_FreshAir_Adapter extends BaseAdapter {

    private List<WareSceneDevItem> mSceneDev;
    private Context mContext;
    private List<WareFreshAir> mFreshAir;
    private boolean mIsShowSelect;

    public SceneSet_FreshAir_Adapter(int sceneposition, Context context, List<WareFreshAir> freshAirs, boolean isShowSelect) {
        mIsShowSelect = isShowSelect;
        mFreshAir = freshAirs;
        mContext = context;
        SelectDev(sceneposition);
    }

    public void SelectDev(int sceneposition) {
        if (WareDataHliper.initCopyWareData().getCopyScenes().size() != 0)
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
        for (int j = 0; j < mFreshAir.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < mSceneDev.size(); i++) {
                if (mSceneDev.get(i).getDevID() == mFreshAir.get(j).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mFreshAir.get(j).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mFreshAir.get(j).getDev().getType()) {
                    mFreshAir.get(j).getDev().setSelect(true);
                    isContain = true;
                }
            }
            if (!isContain) {
                mFreshAir.get(j).getDev().setSelect(false);
            }
        }
        if (mIsShowSelect) {
            for (int i = 0; i < mFreshAir.size(); ) {
                if (!mFreshAir.get(i).getDev().isSelect()) {
                    mFreshAir.remove(i);
                } else
                    i++;
            }
        }
    }

    public void notifyDataSetChanged(List<WareFreshAir> mFreshAir, int sceneposition, boolean mIsShowSelect) {
        this.mFreshAir = mFreshAir;
        this.mIsShowSelect = mIsShowSelect;
        SelectDev(sceneposition);
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<WareFreshAir> mFreshAir) {
        this.mFreshAir = mFreshAir;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_sceneset_light_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.SceneSet_GridView_Item_Name);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_IV);
            viewHoler.mSelect = (ImageView) convertView.findViewById(R.id.SceneSet_GridView_Item_Select);
            viewHoler.mSeekBar = (RangeSeekBar) convertView.findViewById(R.id.SceneSet_GridView_Item_Slide);
            viewHoler.mSeekBar.setVisibility(View.GONE);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        mFreshAir.get(position).setbOnOff((byte) 0);
        viewHoler.mIV.setImageResource(R.drawable.freshair_close);
        viewHoler.mSelect.setImageResource(R.drawable.select_no);
        viewHoler.mSeekBar.setRight(4);

        if (mSceneDev != null) {
            for (int i = 0; i < mSceneDev.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mSceneDev.get(i).getDevID() == mFreshAir.get(position).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mFreshAir.get(position).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mFreshAir.get(position).getDev().getType()) {
                    mFreshAir.get(position).getDev().setSelect(true);
                    viewHoler.mSelect.setImageResource(R.drawable.select_ok);
                    if (mSceneDev.get(i).getbOnOff() == 0) {
                        mFreshAir.get(position).setbOnOff((byte) 0);
                        viewHoler.mIV.setImageResource(R.drawable.freshair_close);
                    } else if (mSceneDev.get(i).getbOnOff() == 1) {
                        mFreshAir.get(position).setbOnOff((byte) 1);
                        viewHoler.mIV.setImageResource(R.drawable.freshair_open);
                    }
                }
            }
        }
        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFreshAir.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mFreshAir.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mFreshAir.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mFreshAir.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            mSceneDev.remove(i);
                            finalViewHoler.mSelect.setImageResource(R.drawable.select_no);
                            mFreshAir.get(position).getDev().setSelect(false);
                        }
                    }
                } else {
                    mFreshAir.get(position).getDev().setSelect(true);
                    WareSceneDevItem item = new WareSceneDevItem();
                    item.setDevID((byte) mFreshAir.get(position).getDev().getDevId());
                    item.setbOnOff(mFreshAir.get(position).getbOnOff());
                    item.setDevType((byte) mFreshAir.get(position).getDev().getType());
                    item.setCanCpuID(mFreshAir.get(position).getDev().getCanCpuId());
                    mSceneDev.add(item);
                    finalViewHoler.mSelect.setImageResource(R.drawable.select_ok);
                }
                Log.i(TAG, "onClick: ****" + mSceneDev.size());
            }
        });

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFreshAir.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mFreshAir.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mFreshAir.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mFreshAir.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            if (mFreshAir.get(position).getbOnOff() == 0) {
                                mSceneDev.get(i).setbOnOff((byte) 1);
                            } else {
                                mSceneDev.get(i).setbOnOff((byte) 0);
                            }

                        }
                    }
                    notifyDataSetChanged(mFreshAir);
                }else {
                    ToastUtil.showText("未选中，不可操作");
                }

            }
        });
        viewHoler.mName.setText(mFreshAir.get(position).getDev().getDevName());
        return convertView;
    }

    class ViewHoler {
        ImageView mSelect, mIV;
        TextView mName;
        RangeSeekBar mSeekBar;
    }
}
