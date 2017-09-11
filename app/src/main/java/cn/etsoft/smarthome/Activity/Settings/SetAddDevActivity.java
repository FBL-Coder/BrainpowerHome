package cn.etsoft.smarthome.Activity.Settings;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.SetAddDev_Adapter;
import cn.etsoft.smarthome.Domain.Condition_Event_Bean;
import cn.etsoft.smarthome.Domain.GroupSet_Data;
import cn.etsoft.smarthome.Domain.SetSafetyResult;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.Domain.WareDev;
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
    private TextView mSetAddDev_Back_Btn, mSetAddDev_Save_Btn, mNullTv;
    private GridView mSetAddDev_Info;
    private ImageView mSceneSet_IsSelectDev;
    private int DevType = -1;
    private String RoomName = "";
    private boolean OuterCircleClick = false;
    private boolean IsShowSelect;
    private List<WareDev> mRoomDevs;
    private int mListPosition;
    private String IntentFlag;
    private SetAddDev_Adapter mAdapter;

    @Override
    public void initView() {
        setLayout(R.layout.activity_setadddev);
        mCirclelayout = getViewById(R.id.SetAddDev_CircleMenu);

        mSetAddDev_Back_Btn = getViewById(R.id.SetAddDev_Back_Btn);
        mSetAddDev_Save_Btn = getViewById(R.id.SetAddDev_Save_Btn);
        mSetAddDev_Info = getViewById(R.id.SetAddDev_Info);
        mSceneSet_IsSelectDev = getViewById(R.id.SceneSet_IsSelectDev);
        mNullTv = getViewById(R.id.null_tv);
        mSetAddDev_Info.setEmptyView(mNullTv);

        mSetAddDev_Back_Btn.setOnClickListener(this);
        mSceneSet_IsSelectDev.setOnClickListener(this);
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
        } else if ("Safety".equals(IntentFlag)) {
            List<WareDev> addDevs = new ArrayList<>();
            for (int i = 0; i < WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows().get(mListPosition).getRun_dev_item().size(); i++) {
                SetSafetyResult.SecInfoRowsBean.RunDevItemBean bean = WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows()
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

        } else if ("Group".equals(IntentFlag)) {
            List<WareDev> addDevs = new ArrayList<>();
            for (int i = 0; i < WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows().get(mListPosition).getRun_dev_item().size(); i++) {
                GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean bean = WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows()
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
                RoomName = Data_InnerCircleList.get(position).getTitle();
                SetAddDevHelper.setRoomName(RoomName);
                mRoomDevs = SetAddDevHelper.getRoomDev(RoomName);

                if (OuterCircleClick) {
                    mNullTv.setText("没有数据");
                    List<WareDev> RecyclerViewDev = new ArrayList<>();
                    for (int i = 0; i < mRoomDevs.size(); i++) {
                        if (mRoomDevs.get(i).getType() == DevType)
                            RecyclerViewDev.add(mRoomDevs.get(i));
                    }
                    if (mAdapter == null)
                        mAdapter = new SetAddDev_Adapter(SetAddDevHelper.getAddDevs(), SetAddDevActivity.this, RecyclerViewDev, IsShowSelect);
                    else
                        mAdapter.notifyDataSetChanged(RecyclerViewDev, SetAddDevHelper.getAddDevs(), IsShowSelect);
                    mSetAddDev_Info.setAdapter(mAdapter);
                }
            }
        });
        mCirclelayout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                DevType = position % 10;
                mNullTv.setText("没有数据");
                OuterCircleClick = true;
                if ("".equals(RoomName)) {
                    return;
                }

                List<WareDev> RecyclerViewDev = new ArrayList<>();
                for (int i = 0; i < mRoomDevs.size(); i++) {
                    if (mRoomDevs.get(i).getType() == DevType)
                        RecyclerViewDev.add(mRoomDevs.get(i));
                }
                if (mAdapter == null)
                    mAdapter = new SetAddDev_Adapter(SetAddDevHelper.getAddDevs(), SetAddDevActivity.this, RecyclerViewDev, IsShowSelect);
                else
                    mAdapter.notifyDataSetChanged(RecyclerViewDev, SetAddDevHelper.getAddDevs(), IsShowSelect);
                mSetAddDev_Info.setAdapter(mAdapter);
            }
        });

        if (DevType == -1 || "".equals(RoomName)){
            mNullTv.setText("请先选择房间和设备类型");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SetAddDev_Back_Btn:
                SetAddDevHelper.setAddDevs(new ArrayList<WareDev>());
                finish();
                break;
            case R.id.SceneSet_IsSelectDev:
                IsShowSelect = !IsShowSelect;
                if (IsShowSelect)
                    mSceneSet_IsSelectDev.setImageResource(R.drawable.show_on);
                else mSceneSet_IsSelectDev.setImageResource(R.drawable.show_off);
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
                    if (AddData.size() > 4) {
                        ToastUtil.showText("最多添加4个设备");
                        return;
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
                    if (AddData.size() > 4) {
                        ToastUtil.showText("最多添加4个设备");
                        return;
                    }
                    WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows().get(mListPosition).setRun_dev_item(AddData);
                } else if ("Safety".equals(IntentFlag)) {
                    List<SetSafetyResult.SecInfoRowsBean.RunDevItemBean> AddData = new ArrayList<>();
                    for (int i = 0; i < SetAddDevHelper.getAddDevs().size(); i++) {
                        SetSafetyResult.SecInfoRowsBean.RunDevItemBean bean = new SetSafetyResult.SecInfoRowsBean.RunDevItemBean();
                        bean.setCanCpuID(SetAddDevHelper.getAddDevs().get(i).getCanCpuId());
                        bean.setBOnOff(SetAddDevHelper.getAddDevs().get(i).getbOnOff());
                        bean.setDevID(SetAddDevHelper.getAddDevs().get(i).getDevId());
                        bean.setDevType(SetAddDevHelper.getAddDevs().get(i).getType());
                        AddData.add(bean);
                    }
                    if (AddData.size() > 4) {
                        ToastUtil.showText("最多添加4个设备");
                        return;
                    }
                    WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows().get(mListPosition).setRun_dev_item(AddData);

                } else if ("Group".equals(IntentFlag)) {
                    List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> AddData = new ArrayList<>();
                    for (int i = 0; i < SetAddDevHelper.getAddDevs().size(); i++) {
                        GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean bean = new GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean();
                        bean.setCanCpuID(SetAddDevHelper.getAddDevs().get(i).getCanCpuId());
                        bean.setBOnOff(SetAddDevHelper.getAddDevs().get(i).getbOnOff());
                        bean.setDevID(SetAddDevHelper.getAddDevs().get(i).getDevId());
                        bean.setDevType(SetAddDevHelper.getAddDevs().get(i).getType());
                        AddData.add(bean);
                    }
                    if (AddData.size() > 4) {
                        ToastUtil.showText("最多添加4个设备");
                        return;
                    }
                    WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows().get(mListPosition).setRun_dev_item(AddData);
                }
                finish();
                break;
        }
    }


}
