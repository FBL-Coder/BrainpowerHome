package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.GroupSet_Data;
import cn.etsoft.smarthome.Domain.SetSafetyResult;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.PopupWindow.MultiChoicePopWindow;


/**
 * Author：FBL  Time： 2017/6/29.
 * 组合设置界面  帮助类
 */

public class GroupSetHelper {

    public static MultiChoicePopWindow mMultiChoicePopWindow;

    public static List<CircleDataEvent> initSceneCircleOUterData(int position) {

        if (WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows()
                .size() == 0)
            return new ArrayList<>();
        List<CircleDataEvent> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            CircleDataEvent event = new CircleDataEvent();

            event.setTitle(WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows()
                    .get(i % WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows()
                            .size()).getTriggerName());
            event.setImage(R.drawable.group_icon);
            if (i == position)
                event.setSelect(true);
            list.add(event);
        }
        return list;
    }

    /**
     * 保存
     */
    public static void Save(Activity activity, final int group_position, final TextView safety_nums,
                            final EditText groupName, final boolean enabled, final boolean event_way,
                            final GroupSet_Data.SecsTriggerRowsBean Bean) {

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
                List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> common_dev = Bean.getRun_dev_item();
                try {
                    GroupSet_Data groupSet_data = new GroupSet_Data();
                    List<GroupSet_Data.SecsTriggerRowsBean> envEvent_rows = new ArrayList<>();
                    GroupSet_Data.SecsTriggerRowsBean bean = new GroupSet_Data.SecsTriggerRowsBean();
                    //  "devCnt": 1,
                    bean.setDevCnt(common_dev.size());
                    //"eventId":	0,
                    bean.setTriggerId(Bean.getTriggerId());
                    // "run_dev_item":
                    bean.setRun_dev_item(common_dev);

                    if ("".equals(groupName.getText().toString())) {
                        bean.setTriggerName(CommonUtils.bytesToHexString(
                                MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows()
                                        .get(group_position).getTriggerName().getBytes("GB2312")));
                    } else {
                        try {
                            //触发器名称
                            bean.setTriggerName(CommonUtils.bytesToHexString(
                                    groupName.getText().toString().getBytes("GB2312")));
                        } catch (UnsupportedEncodingException e) {
                            MyApplication.mApplication.dismissLoadDialog();
                            ToastUtil.showText("触发器名称不合适");
                            return;
                        }
                    }
                    if (groupName.getText().toString().length() > 24) {
                        MyApplication.mApplication.dismissLoadDialog();
                        ToastUtil.showText("触发器名称不能过长");
                        return;
                    }
                    //组合触发器是否启用 "valid":
                    if (enabled)
                        bean.setValid(1);
                    else
                        bean.setValid(0);
                    //是否上报服务器
                    if (event_way)
                        bean.setReportServ(1);
                    else bean.setReportServ(0);
                    String SafetyData = safety_nums.getText().toString();
                    if (SafetyData.equals("点击选择防区")) {
                        ToastUtil.showText("请选择防区");
                        MyApplication.mApplication.dismissLoadDialog();
                        return;
                    }
                    SafetyData = SafetyData.replaceAll(" ", "");
                    StringBuffer Safety_sb = new StringBuffer(SafetyData);
                    for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
                        if (Safety_sb.length() <= i)
                            Safety_sb.append("0");
                    }
                    for (int i = 0; i < Safety_sb.length(); i++) {
                        if (Safety_sb.charAt(i) != '0')
                            Safety_sb.setCharAt(i, '1');
                    }
                    bean.setTriggerSecs(Integer.parseInt(Safety_sb.reverse().toString(), 2));
                    envEvent_rows.add(bean);
                    groupSet_data.setDatType(66);
                    groupSet_data.setDevUnitID(GlobalVars.getDevid());
                    groupSet_data.setItemCnt(1);
                    groupSet_data.setSubType1(2);
                    groupSet_data.setSubType2(0);
                    groupSet_data.setSecs_trigger_rows(envEvent_rows);
                    Gson gson = new Gson();
                    Log.i("保存触发器数据", gson.toJson(groupSet_data));
                    MyApplication.mApplication.getUdpServer().send(gson.toJson(groupSet_data),66);
                } catch (Exception e) {
                    MyApplication.mApplication.dismissLoadDialog();
                    Log.e("保存触发器数据", "保存数据异常" + e);
                    ToastUtil.showText("保存数据异常,请检查数据是否合适");
                }
            }
        });
        builder.create().show();
    }

    /**
     * 得到字符串中的数字和
     *
     * @param str
     * @return
     */
    public static int str2num(String str) {
        str = str.replaceAll("\\D", "");
        List<Integer> number = new ArrayList<>();
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                number.add(Integer.valueOf(String.valueOf(str.charAt(i))));
            }
        }
        String s = "";
        byte[] week_byte = new byte[8];
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < number.size(); i++) {
                if (j == number.get(i) - 1) {
                    week_byte[j] = 1;
                }
            }
            s += week_byte[j];
        }
        s = reverseString(s);
        return Integer.valueOf(s, 2);
    }


    /**
     * 选择防区dialog
     *
     * @param view 显示控件
     */
    public static void initSafetysDialog(Context context, final TextView view, boolean[] isSelect) {


        List<String> Safetys = new ArrayList<>();
        List<SetSafetyResult.SecInfoRowsBean> safetys = MyApplication.getWareData().getResult_safety().getSec_info_rows();
        for (int i = 0; i < safetys.size(); i++) {
            Safetys.add(safetys.get(i).getSecName() + " ");
        }
        if (mMultiChoicePopWindow == null)
            mMultiChoicePopWindow = new MultiChoicePopWindow(context, view, Safetys, isSelect);
        else {
            mMultiChoicePopWindow.upDataFlag(isSelect);
            mMultiChoicePopWindow.upParentView(view);
        }
        mMultiChoicePopWindow.setTitle("选择防区");
        mMultiChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
                int size = selItems.length;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < size; i++) {
                    if (selItems[i]) {
                        stringBuffer.append(i + 1 + " ");
                    }
                }
                if (stringBuffer.toString().length() == 0)
                    view.setText("点击选择防区");
                view.setText(stringBuffer.toString());
            }
        });
        mMultiChoicePopWindow.show();
    }

    /**
     * 倒置字符串
     *
     * @param str
     * @return
     */
    public static String reverseString(String str) {
        char[] arr = str.toCharArray();
        int middle = arr.length >> 1;//EQ length/2
        int limit = arr.length - 1;
        for (int i = 0; i < middle; i++) {
            char tmp = arr[i];
            arr[i] = arr[limit - i];
            arr[limit - i] = tmp;
        }
        return new String(arr);
    }
}

