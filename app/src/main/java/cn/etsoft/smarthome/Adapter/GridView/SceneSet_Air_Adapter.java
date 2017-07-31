package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.lantouzi.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——空调 适配器
 */

public class SceneSet_Air_Adapter extends BaseAdapter {

    private List<WareSceneDevItem> mSceneDev;
    private Context mContext;
    private List<WareAirCondDev> mAirs;
    private boolean mIsShowSelect;
    private List<String> temp_texts = new ArrayList<>();
    private List<String> spead_texts = new ArrayList<>();

    public SceneSet_Air_Adapter(int sceneposition, Context context, List<WareAirCondDev> airs, boolean isShowSelect) {
        mIsShowSelect = isShowSelect;
        mAirs = airs;
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
        for (int j = 0; j < mAirs.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < mSceneDev.size(); i++) {
                if (mSceneDev.get(i).getDevID() == mAirs.get(j).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mAirs.get(j).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mAirs.get(j).getDev().getType()) {
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

    public void notifyDataSetChanged(List<WareAirCondDev> mAirs, int sceneposition, boolean mIsShowSelect) {
        this.mAirs = mAirs;
        this.mIsShowSelect = mIsShowSelect;
        SelectDev(sceneposition);
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


    int position_flag;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHoler = null;
        position_flag = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_sceneset_airs_item, null);
            viewHoler = new ViewHolder(convertView);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHolder) convertView.getTag();

        mAirs.get(position).setbOnOff((byte) 0);
        viewHoler.mAirSwitch.setImageResource(R.drawable.ic_launcher);
        viewHoler.mAirSelect.setImageResource(R.drawable.ic_launcher);
        if (mSceneDev != null) {
            for (int i = 0; i < mSceneDev.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (mSceneDev.get(i).getDevID() == mAirs.get(position).getDev().getDevId()
                        && mSceneDev.get(i).getCanCpuID().equals(mAirs.get(position).getDev().getCanCpuId())
                        && mSceneDev.get(i).getDevType() == mAirs.get(position).getDev().getType()) {

                    mAirs.get(position).getDev().setSelect(true);
                    viewHoler.mAirSelect.setImageResource(R.drawable.ic_launcher_round);
                    if (mSceneDev.get(i).getbOnOff() == 0) {
                        mAirs.get(position).setbOnOff((byte) 0);
                        viewHoler.mAirSwitch.setImageResource(R.drawable.ic_launcher);
                    } else {
                        mAirs.get(position).setbOnOff((byte) 0);
                        viewHoler.mAirSwitch.setImageResource(R.drawable.ic_launcher_round);
                    }
                    viewHoler.mHorizontalSelectSpead.selectIndex(mAirs.get(position).getSelSpd());
                    viewHoler.mHorizontalSelectTemp.selectIndex(mAirs.get(position).getSelTemp());
                }
            }
        }


        final ViewHolder finalViewHoler = viewHoler;

        viewHoler.mAirName.setText(mAirs.get(position).getDev().getDevName());
        for (int i = 16; i < 31; i++) {
            temp_texts.add(i + "");
        }
        spead_texts.add("低档");
        spead_texts.add("中档");
        spead_texts.add("高档");
        if (mAirs.get(position).getSelTemp() < 16 || mAirs.get(position).getSelTemp() > 30)
            mAirs.get(position).setSelTemp((byte) 16);
        viewHoler.mHorizontalSelectTemp.selectIndex(mAirs.get(position).getSelTemp());
        viewHoler.mHorizontalSelectTemp.setItems(temp_texts);
        viewHoler.mHorizontalSelectTemp.setAdditionCenterMark("℃");
        viewHoler.mHorizontalSelectTemp.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int i) {
            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int i) {
                ToastUtil.showText(temp_texts.get(i));
                mAirs.get(position).setSelTemp(Byte.valueOf(temp_texts.get(i)));
            }
        });
        viewHoler.mHorizontalSelectSpead.setItems(spead_texts);
        viewHoler.mHorizontalSelectSpead.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int i) {
            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int i) {
                ToastUtil.showText(spead_texts.get(i));
                mAirs.get(position).setSelSpd((byte) i);
            }
        });


        SelectClick(position, viewHoler, finalViewHoler);

        SwitchClick(position, viewHoler, finalViewHoler);

        TempClick(position, viewHoler, finalViewHoler);

        SpeadClick(position, viewHoler, finalViewHoler);

        viewHoler.mAirMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.air_tocool://制冷
                        break;
                    case R.id.air_toheat://制热
                        break;
                    case R.id.air_xeransis://除湿
                        break;
                    case R.id.air_swing_flap://扫风
                        break;
                }
            }
        });

        return convertView;
    }

    private void SpeadClick(final int position, ViewHolder viewHoler, final ViewHolder finalViewHoler) {
        viewHoler.mAirTospeadSam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getSelSpd() < 0) {
                    ToastUtil.showText("不能再低了");
                    return;
                }
                mAirs.get(position).setSelSpd((byte) (mAirs.get(position).getSelSpd() - 1));
                //getSceneDev(position)   数据变化，改变情景数据中的设备信息。
                finalViewHoler.mHorizontalSelectSpead.selectIndex(mAirs.get(position).getSelSpd() - 1);
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis()
                        , SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, v.getX() + 100, v.getY(), 0);
                finalViewHoler.mHorizontalSelectSpead.onSingleTapUp(event);
            }
        });
        viewHoler.mAirTospeadBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getSelSpd() > 2) {
                    ToastUtil.showText("不能再高了");
                    return;
                }
                mAirs.get(position).setSelSpd((byte) (mAirs.get(position).getSelSpd() + 1));
                //getSceneDev(position)  数据变化，改变情景数据中的设备信息。
                finalViewHoler.mHorizontalSelectSpead.selectIndex(mAirs.get(position).getSelSpd() + 1);
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis()
                        , SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, v.getX() - 150, v.getY(), 0);
                finalViewHoler.mHorizontalSelectSpead.onSingleTapUp(event);
            }
        });
    }

    private void TempClick(final int position, ViewHolder viewHoler, final ViewHolder finalViewHoler) {
        viewHoler.mAirTempDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getSelTemp() < 16) {
                    ToastUtil.showText("不能再低了");
                    return;
                }
                mAirs.get(position).setSelTemp((byte) (mAirs.get(position).getSelTemp() - 1));
                //getSceneDev(position)   数据变化，改变情景数据中的设备信息。
                finalViewHoler.mHorizontalSelectTemp.selectIndex(mAirs.get(position).getSelTemp());
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis()
                        , SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, v.getX() + 100, v.getY(), 0);
                finalViewHoler.mHorizontalSelectTemp.onSingleTapUp(event);
            }
        });

        viewHoler.mAirTempAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getSelTemp() > 30) {
                    ToastUtil.showText("不能再高了");
                    return;
                }
                mAirs.get(position).setSelTemp((byte) (mAirs.get(position).getSelTemp() + 1));
                //getSceneDev(position)   数据变化，改变情景数据中的设备信息。
                finalViewHoler.mHorizontalSelectTemp.selectIndex(mAirs.get(position).getSelTemp());
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis()
                        , SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, v.getX() - 100, v.getY(), 0);
                finalViewHoler.mHorizontalSelectTemp.onSingleTapUp(event);
            }
        });
    }

    private void SwitchClick(final int position, ViewHolder viewHoler, final ViewHolder finalViewHoler) {
        viewHoler.mAirSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mAirs.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mAirs.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mAirs.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            if (mAirs.get(position).getbOnOff() == 0) {
                                mSceneDev.get(i).setbOnOff((byte) 1);
                            } else {
                                mSceneDev.get(i).setbOnOff((byte) 0);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }else {
                    ToastUtil.showText("未选中，不可操作");
                }
            }
        });
    }

    private void SelectClick(final int position, ViewHolder viewHoler, final ViewHolder finalViewHoler) {
        viewHoler.mAirSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getDev().isSelect()) {
                    for (int i = 0; i < mSceneDev.size(); i++) {
                        if (mAirs.get(position).getDev().getDevId() == mSceneDev.get(i).getDevID()
                                && mAirs.get(position).getDev().getType() == mSceneDev.get(i).getDevType()
                                && mAirs.get(position).getDev().getCanCpuId().equals(mSceneDev.get(i).getCanCpuID())) {
                            mSceneDev.remove(i);
                            finalViewHoler.mAirSelect.setImageResource(R.drawable.ic_launcher);
                            mAirs.get(position).getDev().setSelect(false);
                        }
                    }
                } else {
                    mAirs.get(position).getDev().setSelect(true);
                    WareSceneDevItem item = new WareSceneDevItem();
                    item.setDevID((byte) mAirs.get(position).getDev().getDevId());
                    item.setbOnOff(mAirs.get(position).getbOnOff());
                    item.setDevType((byte) mAirs.get(position).getDev().getType());
                    item.setCanCpuID(mAirs.get(position).getDev().getCanCpuId());
                    mSceneDev.add(item);
                    finalViewHoler.mAirSelect.setImageResource(R.drawable.ic_launcher_round);
                }
                Log.i(TAG, "onClick: ****" + mSceneDev.size());
            }
        });
    }

    public static class ViewHolder {
        public View rootView;
        public TextView mAirName;
        public TextView mAirNowTemp;
        public ImageView mAirNowSpead;
        public ImageView mAirTempDown;
        public ImageView mAirSelect;
        public ImageView mAirSwitch;
        public WheelView mHorizontalSelectTemp;
        public ImageView mAirTempAdd;
        public ImageView mAirTospeadSam;
        public WheelView mHorizontalSelectSpead;
        public ImageView mAirTospeadBig;
        public RadioButton mAirTocool;
        public RadioButton mAirToheat;
        public RadioButton mAirXeransis;
        public RadioButton mAirSwingFlap;
        private RadioGroup mAirMode;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mAirName = (TextView) rootView.findViewById(R.id.air_name);
            this.mAirNowTemp = (TextView) rootView.findViewById(R.id.air_now_temp);
            this.mAirNowSpead = (ImageView) rootView.findViewById(R.id.air_now_spead);
            this.mAirTempDown = (ImageView) rootView.findViewById(R.id.air_temp_down);
            this.mAirSwitch = (ImageView) rootView.findViewById(R.id.air_switch);
            this.mAirSelect = (ImageView) rootView.findViewById(R.id.air_select);
            this.mHorizontalSelectTemp = (WheelView) rootView.findViewById(R.id.HorizontalSelect_temp);
            this.mAirTempAdd = (ImageView) rootView.findViewById(R.id.air_temp_add);
            this.mAirTospeadSam = (ImageView) rootView.findViewById(R.id.air_tospead_sam);
            this.mHorizontalSelectSpead = (WheelView) rootView.findViewById(R.id.HorizontalSelect_spead);
            this.mAirTospeadBig = (ImageView) rootView.findViewById(R.id.air_tospead_big);
            this.mAirTocool = (RadioButton) rootView.findViewById(R.id.air_tocool);
            this.mAirToheat = (RadioButton) rootView.findViewById(R.id.air_toheat);
            this.mAirXeransis = (RadioButton) rootView.findViewById(R.id.air_xeransis);
            this.mAirSwingFlap = (RadioButton) rootView.findViewById(R.id.air_swing_flap);
            this.mAirMode = (RadioGroup) rootView.findViewById(R.id.air_mode);
        }

    }
}
