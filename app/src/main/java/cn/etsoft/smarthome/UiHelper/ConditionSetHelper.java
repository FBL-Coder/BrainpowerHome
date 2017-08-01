package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.Condition_Event_Bean;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;


/**
 * Author：FBL  Time： 2017/6/29.
 * 环境触发器  界面帮助类
 */

public class ConditionSetHelper {

    public static List<CircleDataEvent> initSceneCircleOUterData(boolean isClick, int position) {

        List<CircleDataEvent> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setTitle(WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows()
                    .get(i % WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows()
                            .size()).getEventName());
            event.setImage(R.drawable.chufaqi_icon);
            if (isClick && i == position)
                event.setSelect(true);
            list.add(event);
        }
        return list;
    }

    public static void SaveCondition(final Activity activity, final EditText conditionName, final EditText chuFaZhi, final boolean ShiNeng
            , final TextView event_way, final TextView event_type, final List<String> Event_Way, final List<String> Event_type,
                                     final int Condition_position, final List<Condition_Event_Bean.EnvEventRowsBean.RunDevItemBean> common_dev) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示 :");
        builder.setMessage("您要保存这些设置吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                try {
                    Condition_Event_Bean time_data = new Condition_Event_Bean();
                    List<Condition_Event_Bean.EnvEventRowsBean> envEvent_rows = new ArrayList<>();
                    Condition_Event_Bean.EnvEventRowsBean bean = new Condition_Event_Bean.EnvEventRowsBean();
                    //  "devCnt": 1,
                    bean.setDevCnt(common_dev.size());
                    //"eventId":	0,
                    bean.setEventId(MyApplication.getWareData().getCondition_event_bean().getenvEvent_rows().get(Condition_position).getEventId());
                    // "run_dev_item":
                    bean.setRun_dev_item(common_dev);

                    if ("".equals(conditionName.getText().toString())) {
                        bean.setEventName(CommonUtils.bytesToHexString(MyApplication.getWareData().getCondition_event_bean().getenvEvent_rows().get(Condition_position).getEventName().getBytes("GB2312")));
                    } else {
                        try {
                            //触发器名称
                            bean.setEventName(CommonUtils.bytesToHexString(conditionName.getText().toString().getBytes("GB2312")));
                        } catch (UnsupportedEncodingException e) {
                            MyApplication.mApplication.dismissLoadDialog();
                            ToastUtil.showText("触发器名称不合适");
                            return;
                        }
                    }
                    if (conditionName.getText().toString().length() > 24) {
                        MyApplication.mApplication.dismissLoadDialog();
                        ToastUtil.showText("触发器名称不能过长");
                        return;
                    }


                    // "valTh":
                    if ("输入触发值".equals(chuFaZhi.getText().toString()) || "".equals(chuFaZhi.getText().toString())) {
                        MyApplication.mApplication.dismissLoadDialog();
                        ToastUtil.showText("触发器阀值不能为空");
                        return;
                    }
                    bean.setValTh(Integer.parseInt(chuFaZhi.getText().toString()));

                    //触发器是否启用   "valid":
                    if (ShiNeng)
                        bean.setValid(1);
                    else
                        bean.setValid(0);

                    //触发器触发方式  "thType":
                    if ("选择触发方式".equals(event_way.getText().toString())) {
                        MyApplication.mApplication.dismissLoadDialog();
                        ToastUtil.showText("请选择触发方式");
                        return;
                    }
                    for (int i = 0; i < Event_Way.size(); i++) {
                        if (Event_Way.get(i).equals(event_way.getText().toString())) {
                            bean.setThType(i);
                        }
                    }

                    //触发器触发类别  "envType"

                    if ("选择触发类别".equals(event_type.getText().toString())) {
                        MyApplication.mApplication.dismissLoadDialog();
                        ToastUtil.showText("请选择触发类别");
                        return;
                    }
                    for (int i = 0; i < Event_type.size(); i++) {
                        if (Event_type.get(i).equals(event_type.getText().toString())) {
                            bean.setEnvType(i);
                        }
                    }

                    // "uidSrc":
                    bean.setUidSrc(MyApplication.getWareData().getCondition_event_bean().getenvEvent_rows().get(Condition_position).getUidSrc());

                    envEvent_rows.add(bean);
                    time_data.setDatType(29);
                    time_data.setDevUnitID(GlobalVars.getDevid());
                    time_data.setItemCnt(1);
                    time_data.setSubType1(0);
                    time_data.setSubType2(0);
                    time_data.setenvEvent_rows(envEvent_rows);
                    Gson gson = new Gson();
                    Log.i("保存触发器数据", gson.toJson(time_data));
                    MyApplication.mApplication.showLoadDialog(activity);
                    MyApplication.mApplication.getUdpServer().send(gson.toJson(time_data));
                } catch (Exception e) {
                    MyApplication.mApplication.dismissLoadDialog();
                    Log.e("保存触发器数据", "保存数据异常" + e);
                    ToastUtil.showText("保存数据异常,请检查数据是否合适");
                }
            }
        });
        builder.create().show();
    }
}

