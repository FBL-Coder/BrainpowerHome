package cn.etsoft.smarthome.UiHelper;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.ChnOpItem_scene;
import cn.etsoft.smarthome.Domain.Condition_Event_Bean;
import cn.etsoft.smarthome.Domain.GroupSet_Data;
import cn.etsoft.smarthome.Domain.SetSafetyResult;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.Domain.WareEnvDevItem;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;

/**
 * Author：FBL  Time： 2017/6/28.
 * 设置界面  数据拷贝辅助类
 */

public class WareDataHliper {


    public static WareDataHliper wareDataHliper;

    private List<WareSceneEvent> mSceneEvents;
    private Timer_Data mTimer_Data;
    private List<Timer_Data.TimerEventRowsBean> mTimerEvevts;
    private Condition_Event_Bean mConditionEventBean;
    private List<Condition_Event_Bean.EnvEventRowsBean> mConditionListBean;
    private SetSafetyResult mSetSafetyResult;
    private List<SetSafetyResult.SecInfoRowsBean> mSecInfoRowsBean;
    private GroupSet_Data mGroupSet_Data;
    private List<GroupSet_Data.SecsTriggerRowsBean> mSecsTriggerRowsBean;
    private ChnOpItem_scene mChnOpItem_scene;
    private List<ChnOpItem_scene.Key2sceneItemBean> itemBeans;


    public static WareDataHliper initCopyWareData() {
        if (wareDataHliper == null)
            wareDataHliper = new WareDataHliper();
        return wareDataHliper;
    }

    public void startCopySceneData() {
        mSceneEvents = new ArrayList<>();
        mSceneEvents.addAll(MyApplication.getWareData().getSceneEvents());
    }

    public void startCopyTimerData() {
        mTimer_Data = new Timer_Data();
        mTimerEvevts = new ArrayList<>();
        mTimerEvevts.addAll(MyApplication.getWareData().getTimer_data().getTimerEvent_rows());
        mTimer_Data.setTimerEvent_rows(mTimerEvevts);
    }

    public void startCopyConditionData() {
        mConditionEventBean = new Condition_Event_Bean();
        mConditionListBean = new ArrayList<>();
        mConditionListBean.addAll(MyApplication.getWareData().getCondition_event_bean().getenvEvent_rows());
        mConditionEventBean.setenvEvent_rows(mConditionListBean);
    }

    public void startCopySafetySetData() {
        mSetSafetyResult = new SetSafetyResult();
        mSecInfoRowsBean = new ArrayList<>();
        mSecInfoRowsBean.addAll(MyApplication.getWareData().getResult_safety().getSec_info_rows());
        mSetSafetyResult.setSec_info_rows(mSecInfoRowsBean);
    }

    public void startCopyGroupSetData() {
        mGroupSet_Data = new GroupSet_Data();
        mSecsTriggerRowsBean = new ArrayList<>();
        mSecsTriggerRowsBean.addAll(MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows());
        mGroupSet_Data.setSecs_trigger_rows(mSecsTriggerRowsBean);
    }

    public void startCopyScene_KeysData() {
        mChnOpItem_scene = new ChnOpItem_scene();
        itemBeans = new ArrayList<>();
        itemBeans.addAll(MyApplication.getWareData().getChnOpItem_scene().getKey2scene_item());
        mChnOpItem_scene.setKey2scene_item(itemBeans);
    }

    public void startCopySceneControl() {
        mSceneEvents = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            WareSceneEvent mWareSceneEvent = new WareSceneEvent();
            List<WareSceneDevItem> mSceneDevItems = new ArrayList<>();
            if (j == 0) {
                mWareSceneEvent.setEventId(0);
                mWareSceneEvent.setSceneName("全开模式");
            } else {
                mWareSceneEvent.setEventId(1);
                mWareSceneEvent.setSceneName("全关模式");
            }
            for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                WareSceneDevItem item = new WareSceneDevItem();
                item.setDevID(MyApplication.getWareData().getDevs().get(i).getDevId());
                item.setDevType(MyApplication.getWareData().getDevs().get(i).getType());
                if (j == 0) item.setbOnOff(0);
                else item.setbOnOff(1);
                item.setCanCpuID(MyApplication.getWareData().getDevs().get(i).getCanCpuId());
                mSceneDevItems.add(item);
            }
            mWareSceneEvent.setItemAry(mSceneDevItems);
            mSceneEvents.add(mWareSceneEvent);
        }
        mSceneEvents.addAll(MyApplication.getWareData().getSceneEvents());
    }


    public List<WareSceneEvent> getCopyScenes() {
        startCopySceneData();
        return mSceneEvents;
    }

    public Timer_Data getCopyTimers() {
        startCopyTimerData();
        return mTimer_Data;
    }

    public Condition_Event_Bean getConditionEvent() {
        startCopyConditionData();
        return mConditionEventBean;
    }

    public SetSafetyResult getSetSafetyResult() {
        startCopySafetySetData();
        return mSetSafetyResult;

    }

    public GroupSet_Data getGroupSetResult() {
        if (mGroupSet_Data == null)
            startCopyGroupSetData();
        return mGroupSet_Data;

    }

    public ChnOpItem_scene getScenekeysResult() {
        if (mChnOpItem_scene == null)
            startCopyScene_KeysData();
        return mChnOpItem_scene;
    }

    public List<WareSceneEvent> getSceneControlData() {
        startCopySceneControl();
        return mSceneEvents;
    }
}
