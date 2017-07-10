package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.Condition_Event_Bean;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.SetSafetyResult;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.PopupWindow.MultiChoicePopWindow;


/**
 * Author：FBL  Time： 2017/6/29.
 * 安防设置界面  帮助类
 */

public class SafetySetHelper {


    public static List<CircleDataEvent> initSceneCircleOUterData(boolean isClick,int position) {

        if (WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows()
                .size() == 0)
            return new ArrayList<>();
        List<CircleDataEvent> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();

            event.setTitle(WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows()
                    .get(i % WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows()
                            .size()).getSecName());
            event.setImage(R.drawable.fangqu_icon);
            if (isClick && i == position)
                event.setSelect(true);
            list.add(event);
        }
        return list;
    }

    /**
     * 保存
     */
    public static void safetySet_Save(Activity activity, EditText safety_name, boolean ShiNeng
            , TextView safety_scene, TextView safety_state, List<String> safety_state_data,
                                      int Safety_position, List<SetSafetyResult.SecInfoRowsBean.RunDevItemBean> common_dev) {

        try {
            SetSafetyResult safetyResult = new SetSafetyResult();
            List<SetSafetyResult.SecInfoRowsBean> timerEvent_rows = new ArrayList<>();
            SetSafetyResult.SecInfoRowsBean bean = new SetSafetyResult.SecInfoRowsBean();
            if (common_dev.size() > 0)
                bean.setSecDev(1);
            else bean.setSecDev(0);
            bean.setItemCnt(common_dev.size());
            bean.setSecId(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecId());
            bean.setRun_dev_item(common_dev);
            if ("".equals(safety_name.getText().toString())) {
                bean.setSecName(CommonUtils.bytesToHexString(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecName().getBytes("GB2312")));
            } else {
                try {
                    //名称名称
                    bean.setSecName(CommonUtils.bytesToHexString(safety_name.getText().toString().getBytes("GB2312")));
                } catch (UnsupportedEncodingException e) {
                    ToastUtil.showText("定时器名称不合适");
                    return;
                }
            }
            if (ShiNeng)
                bean.setValid(1);
            else
                bean.setValid(0);
            bean.setSecCode(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecCode());
            //safety_state_data : 选择布防类型
            //safety_state : 状态
            for (int i = 0; i < safety_state_data.size(); i++) {
                if (safety_state_data.get(i).equals(safety_state.getText().toString())) {
                    if (i == 3)
                        bean.setSecType(255);
                    else
                        bean.setSecType(i);
                }
            }

            //关联情景
            for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                if (safety_scene.getText().toString().equals(MyApplication.getWareData().getSceneEvents().get(i).getSceneName()))
                    bean.setSceneId(i);
            }
            timerEvent_rows.add(bean);
            safetyResult.setDatType(32);
            safetyResult.setDevUnitID(GlobalVars.getDevid());
            safetyResult.setSubType1(5);
            safetyResult.setItemCnt(1);
            safetyResult.setSubType2(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecId());
            safetyResult.setSec_info_rows(timerEvent_rows);
            Gson gson = new Gson();
            Log.e("保存安防数据", gson.toJson(safetyResult));
            MyApplication.mApplication.getUdpServer().send(gson.toJson(safetyResult));
        } catch (Exception e) {
            MyApplication.mApplication.dismissLoadDialog();
            Log.e("保存安防数据", "保存数据异常" + e);
            ToastUtil.showText("保存数据异常,请检查数据是否合适");
        }
    }

    public static void safetySetDuiMa(Activity activity, EditText safety_name, boolean ShiNeng
            , TextView safety_scene, TextView safety_state, List<String> safety_state_data,
                                      int Safety_position, List<SetSafetyResult.SecInfoRowsBean.RunDevItemBean> common_dev) {
        try {
            SetSafetyResult safetyResult = new SetSafetyResult();
            List<SetSafetyResult.SecInfoRowsBean> timerEvent_rows = new ArrayList<>();
            SetSafetyResult.SecInfoRowsBean bean = new SetSafetyResult.SecInfoRowsBean();
            if (common_dev.size() > 0)
                bean.setSecDev(1);
            else bean.setSecDev(0);
            bean.setItemCnt(common_dev.size());
            bean.setSecId(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecId());
            bean.setRun_dev_item(common_dev);
            if ("".equals(safety_name.getText().toString())) {
                bean.setSecName(CommonUtils.bytesToHexString(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecName().getBytes("GB2312")));
            } else {
                try {
                    //名称名称
                    bean.setSecName(CommonUtils.bytesToHexString(safety_name.getText().toString().getBytes("GB2312")));
                } catch (UnsupportedEncodingException e) {
                    ToastUtil.showText("定时器名称不合适");
                    return;
                }
            }
            if (ShiNeng)
                bean.setValid(1);
            else
                bean.setValid(0);
            bean.setSecCode(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecCode());

            for (int i = 0; i < safety_state_data.size(); i++) {
                if (safety_state_data.get(i).equals(safety_state.getText().toString())) {
                    if (i == 3)
                        bean.setSecType(255);
                    else
                        bean.setSecType(i);
                }
            }

            //关联情景
            for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                if (safety_scene.getText().toString().equals(MyApplication.getWareData().getSceneEvents().get(i).getSceneName()))
                    bean.setSceneId(i);
            }
            timerEvent_rows.add(bean);
            safetyResult.setDatType(32);
            safetyResult.setDevUnitID(GlobalVars.getDevid());
            safetyResult.setSubType1(7);
            safetyResult.setItemCnt(1);
            safetyResult.setSubType2(0);
            safetyResult.setSec_info_rows(timerEvent_rows);
            Gson gson = new Gson();
            Log.e("对码数据", gson.toJson(safetyResult));
            MyApplication.mApplication.getUdpServer().send(gson.toJson(safetyResult));
        } catch (Exception e) {
            MyApplication.mApplication.dismissLoadDialog();
            Log.e("对码数据", "对码数据异常" + e);
            ToastUtil.showText("对码数据异常,请检查数据是否合适");
        }
    }

}

