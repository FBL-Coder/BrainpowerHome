package cn.etsoft.smarthome.UiHelper;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.Condition_Event_Bean;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;

/**
 * Author：FBL  Time： 2017/6/28.
 */

public class WareDataHliper {


    public static WareDataHliper wareDataHliper;

    private List<WareSceneEvent> mSceneEvents;
    private Timer_Data mTimer_Data;
    private List<Timer_Data.TimerEventRowsBean> mTimerEvevts;
    private Condition_Event_Bean mConditionEventBean;
    private List<Condition_Event_Bean.EnvEventRowsBean> mConditionListBean;


    public  static WareDataHliper initCopyWareData(){
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

    public void startCopyConditionData(){
        mConditionEventBean = new Condition_Event_Bean();
        mConditionListBean = new ArrayList<>();
        mConditionListBean.addAll(MyApplication.getWareData().getCondition_event_bean().getenvEvent_rows());
        mConditionEventBean.setenvEvent_rows(mConditionListBean);
    }



    public List<WareSceneEvent> getCopyScenes(){
        if (mSceneEvents == null)
            startCopySceneData();
        return mSceneEvents;
    }
    public Timer_Data getCopyTimers(){
        if (mTimer_Data == null)
            startCopyTimerData();
        return mTimer_Data;
    }

    public Condition_Event_Bean getConditionEvent(){
        if (mConditionEventBean == null)
            startCopyConditionData();
        return mConditionEventBean;
    }
}
