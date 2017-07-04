package cn.etsoft.smarthome.Activity.AdvancedSetting;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.Condition_Event_Bean;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Fragment.SetAddDev.AirSetAddDevFragment;
import cn.etsoft.smarthome.Fragment.SetAddDev.CurtarnSetAddDevFragment;
import cn.etsoft.smarthome.Fragment.SetAddDev.LightSetAddDevFragment;
import cn.etsoft.smarthome.Fragment.SetAddDev.TVSetAddDevFragment;
import cn.etsoft.smarthome.Fragment.SetAddDev.TvUpSetAddDevFragment;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SetAddDevHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/30.
 * 设置界面中的添加设备界面
 */

public class SetAddDevActivity extends BaseActivity implements View.OnClickListener {
    private CircleMenuLayout mCirclelayout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private TextView mSetAddDev_Back_Btn, mSetAddDev_Save_Btn;
    private Fragment mLightFragment, mAirFragment, mTVFragment, mTvUpFragment, mCurFragment;
    private Dialog mLoadDialog;
    private int DevType = 0;
    private String RoomName = "";
    private boolean IsNoData = true;
    private int mListPosition;
    private String IntentFlag;

    @Override
    public void initView() {
        setLayout(R.layout.activity_setadddev);
        mCirclelayout = getViewById(R.id.SetAddDev_CircleMenu);

        mSetAddDev_Back_Btn = getViewById(R.id.SetAddDev_Back_Btn);
        mSetAddDev_Save_Btn = getViewById(R.id.SetAddDev_Save_Btn);

        mSetAddDev_Back_Btn.setOnClickListener(this);
        mSetAddDev_Save_Btn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (MyApplication.getWareData().getRooms().size() == 0) {
            ToastUtil.showText("没有房间数据");
            return;
        }
        IntentFlag = getIntent().getStringExtra("name");
        mListPosition = getIntent().getIntExtra("position", 0);
        if ("Timer".equals(IntentFlag)) {
            List<WareDev> addDevs = new ArrayList<>();
            for (int i = 0; i < WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows().get(mListPosition).getRun_dev_item().size(); i++) {
                Timer_Data.TimerEventRowsBean.RunDevItemBean bean = WareDataHliper.initCopyWareData().getCopyTimers()
                        .getTimerEvent_rows().get(mListPosition).getRun_dev_item().get(i);
                WareDev dev = new WareDev();
                dev.setSelect(true);
                dev.setDevId((byte) bean.getDevID());
                dev.setbOnOff((byte) bean.getBOnOff());
                dev.setCanCpuId(bean.getCanCpuID());
                dev.setType((byte) bean.getDevType());
                addDevs.add(dev);
            }
            SetAddDevHelper.setAddDevs(addDevs);
        } else if ("Condition".equals(IntentFlag)) {
            List<WareDev> addDevs = new ArrayList<>();
            for (int i = 0; i < WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows().get(mListPosition).getRun_dev_item().size(); i++) {
                Condition_Event_Bean.EnvEventRowsBean.RunDevItemBean bean = WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows()
                        .get(mListPosition).getRun_dev_item().get(i);
                WareDev dev = new WareDev();
                dev.setSelect(true);
                dev.setDevId((byte) bean.getDevID());
                dev.setbOnOff((byte) bean.getBOnOff());
                dev.setCanCpuId(bean.getCanCpuID());
                dev.setType((byte) bean.getDevType());
                addDevs.add(dev);
            }
            SetAddDevHelper.setAddDevs(addDevs);
        }

        Data_OuterCircleList = SetAddDevHelper.initSceneCircleOUterData();
        Data_InnerCircleList = SetAddDevHelper.initSceneCircleInnerData();
        mCirclelayout.Init(200, 100);
        mCirclelayout.setInnerCircleMenuData(Data_InnerCircleList);
        mCirclelayout.setOuterCircleMenuData(Data_OuterCircleList);

        mCirclelayout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                if (mSetAddDevCircleClickListener != null)
                    mSetAddDevCircleClickListener.SetAddDevClickPosition(DevType, Data_InnerCircleList.get(position).getTitle());
                RoomName = Data_InnerCircleList.get(position).getTitle();
                SetAddDevHelper.setRoomName(RoomName);
            }
        });
        mCirclelayout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                OuterCircleClick(SetAddDevActivity.this, position, RoomName);
                if (mSetAddDevCircleClickListener != null)
                    mSetAddDevCircleClickListener.SetAddDevClickPosition(position, RoomName);
                DevType = position;

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SetAddDev_Back_Btn:
                SetAddDevHelper.setAddDevs(new ArrayList<WareDev>());
                finish();
                break;
            case R.id.SetAddDev_Save_Btn:
                if ("Timer".equals(IntentFlag)) {
                    List<Timer_Data.TimerEventRowsBean.RunDevItemBean> AddData = new ArrayList<>();
                    for (int i = 0; i < SetAddDevHelper.getAddDevs().size(); i++) {
                        Timer_Data.TimerEventRowsBean.RunDevItemBean bean = new Timer_Data.TimerEventRowsBean.RunDevItemBean();
                        bean.setCanCpuID(SetAddDevHelper.getAddDevs().get(i).getCanCpuId());
                        bean.setBOnOff(SetAddDevHelper.getAddDevs().get(i).getbOnOff());
                        bean.setDevID(SetAddDevHelper.getAddDevs().get(i).getDevId());
                        bean.setDevType(SetAddDevHelper.getAddDevs().get(i).getType());
                        AddData.add(bean);
                    }
                    WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows().get(mListPosition).setRun_dev_item(AddData);
                } else if ("Condition".equals(IntentFlag)) {
                    List<Condition_Event_Bean.EnvEventRowsBean.RunDevItemBean> AddData = new ArrayList<>();
                    for (int i = 0; i < SetAddDevHelper.getAddDevs().size(); i++) {
                        Condition_Event_Bean.EnvEventRowsBean.RunDevItemBean bean = new Condition_Event_Bean.EnvEventRowsBean.RunDevItemBean();
                        bean.setCanCpuID(SetAddDevHelper.getAddDevs().get(i).getCanCpuId());
                        bean.setBOnOff(SetAddDevHelper.getAddDevs().get(i).getbOnOff());
                        bean.setDevID(SetAddDevHelper.getAddDevs().get(i).getDevId());
                        bean.setDevType(SetAddDevHelper.getAddDevs().get(i).getType());
                        AddData.add(bean);
                    }
                    WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows().get(mListPosition).setRun_dev_item(AddData);
                }
                finish();
                break;
        }
    }

    /**
     * 外圆菜单 点击事件
     */
    public void OuterCircleClick(FragmentActivity activity, int position, String RoomName) {
        if ("".equals(RoomName)) {
            ToastUtil.showText("请先选择房间");
            return;
        }
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (mTVFragment != null)
            transaction.hide(mTVFragment);
        if (mTvUpFragment != null)
            transaction.hide(mTvUpFragment);
        if (mCurFragment != null)
            transaction.hide(mCurFragment);
        if (mAirFragment != null)
            transaction.hide(mAirFragment);
        if (mLightFragment != null)
            transaction.hide(mLightFragment);
        Bundle bundle = new Bundle();
        bundle.putString("RoomName", RoomName);
        switch (position) {
            case 0:
            case 8:
                if (mAirFragment == null) {
                    mAirFragment = new AirSetAddDevFragment();
                    mAirFragment.setArguments(bundle);
                    transaction.add(R.id.SetAddDev_Info, mAirFragment);
                } else transaction.show(mAirFragment);
                break;
            case 1:
            case 9:
                if (mTVFragment == null) {
                    mTVFragment = new TVSetAddDevFragment();
                    mTVFragment.setArguments(bundle);
                    transaction.add(R.id.SetAddDev_Info, mTVFragment);
                } else transaction.show(mTVFragment);
                break;
            case 2:
            case 10:
                if (mTvUpFragment == null) {
                    mTvUpFragment = new TvUpSetAddDevFragment();
                    mTvUpFragment.setArguments(bundle);
                    transaction.add(R.id.SetAddDev_Info, mTvUpFragment);
                } else transaction.show(mTvUpFragment);
                break;
            case 3:
            case 11:
                if (mLightFragment == null) {
                    mLightFragment = new LightSetAddDevFragment();
                    mLightFragment.setArguments(bundle);
                    transaction.add(R.id.SetAddDev_Info, mLightFragment);
                } else transaction.show(mLightFragment);
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
            case 14:
                if (mCurFragment == null) {
                    mCurFragment = new CurtarnSetAddDevFragment();
                    mCurFragment.setArguments(bundle);
                    transaction.add(R.id.SetAddDev_Info, mCurFragment);
                } else transaction.show(mCurFragment);
                break;
        }
        transaction.commit();
    }

    //点击情景，房间，设备。触发回调，刷新界面
    public static SetAddDevCircleClickListener mSetAddDevCircleClickListener;

    public static void setmSetAddDevCircleClickListener(SetAddDevCircleClickListener mSetAddDevCircleClickListener) {
        SetAddDevActivity.mSetAddDevCircleClickListener = mSetAddDevCircleClickListener;
    }

    public interface SetAddDevCircleClickListener {
        void SetAddDevClickPosition(int DevType, String RoomName);
    }
}
