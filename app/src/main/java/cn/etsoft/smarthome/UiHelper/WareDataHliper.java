package cn.etsoft.smarthome.UiHelper;

import java.util.ArrayList;
import java.util.List;

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
}
