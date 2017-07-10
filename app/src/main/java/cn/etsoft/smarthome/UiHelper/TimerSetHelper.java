package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.PopupWindow.MultiChoicePopWindow;


/**
 * Author：FBL  Time： 2017/6/29.
 * 定时器界面  帮助类
 */

public class TimerSetHelper {

    public static MultiChoicePopWindow mMultiChoicePopWindow;


    public static List<CircleDataEvent> initSceneCircleOUterData(boolean IsClick,int position) {

        if (MyApplication.getWareData().getTimer_data().getTimerEvent_rows()
                .size() == 0)
            return new ArrayList<>();
        List<CircleDataEvent> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();

            event.setTitle(MyApplication.getWareData().getTimer_data().getTimerEvent_rows()
                    .get(i % MyApplication.getWareData().getTimer_data().getTimerEvent_rows()
                            .size()).getTimerName());
            event.setImage(R.drawable.timer_icon);
            if (IsClick && i == position)
                event.setSelect(true);
            list.add(event);
        }
        return list;
    }

    /**
     * 保存
     *
     * @param TimerName  计时器名称
     * @param time_start 开始时间
     * @param time_end   结束时间
     * @param WeekAgain  周重复
     * @param ShiNeng    使能
     * @param Weeks      星期集
     * @param TimerEvent 计时器对象
     * @param common_dev 计时器对象中的设备
     */
    public static void Timer_Save(Activity activity, String TimerName,
                                  String time_start, String time_end, boolean WeekAgain,
                                  boolean ShiNeng, String Weeks,
                                  Timer_Data.TimerEventRowsBean TimerEvent,
                                  List<Timer_Data.TimerEventRowsBean.RunDevItemBean> common_dev) {
        try {
            Timer_Data time_data = new Timer_Data();
            List<Timer_Data.TimerEventRowsBean> timerEvent_rows = new ArrayList<>();
            Timer_Data.TimerEventRowsBean bean = new Timer_Data.TimerEventRowsBean();
            bean.setDevCnt(common_dev.size());
            bean.setEventId(TimerEvent.getEventId());
            bean.setRun_dev_item(common_dev);
            if ("".equals(TimerName)) {
                bean.setTimerName(CommonUtils.bytesToHexString(TimerEvent.getTimerName().getBytes("GB2312")));
            } else {
                try {
                    //触发器名称
                    bean.setTimerName(CommonUtils.bytesToHexString(TimerName.getBytes("GB2312")));
                } catch (UnsupportedEncodingException e) {
                    ToastUtil.showText("定时器名称不合适");
                    return;
                }
            }

            List<Integer> time_Data_start = new ArrayList<>();
            if ("点击选择时间".equals(time_start) || "点击选择时间".equals(time_end)) {
                ToastUtil.showText("请选择时间");
                return;
            }
            time_Data_start.add(Integer.parseInt(time_start.substring(0, time_start.indexOf(" : "))));
            time_Data_start.add(Integer.parseInt(time_start.substring(time_start.indexOf(" : ") + 3)));
            time_Data_start.add(0);
            if (str2num(Weeks) == 0) {
                ToastUtil.showText("请选择星期");
                return;
            }
            time_Data_start.add(str2num(Weeks));
            bean.setTimSta(time_Data_start);
            List<Integer> time_Data_end = new ArrayList<>();
            time_Data_end.add(Integer.parseInt(time_end.substring(0, time_end.indexOf(" : "))));
            time_Data_end.add(Integer.parseInt(time_end.substring(time_end.indexOf(" : ") + 3)));
            time_Data_end.add(0);

            if (WeekAgain)
                time_Data_end.add(1);
            else
                time_Data_end.add(0);
            bean.setTimEnd(time_Data_end);

            if (time_Data_start.get(0) > time_Data_end.get(0)) {
                ToastUtil.showText("开始时间不能比结束时间迟");
                return;
            }

            if (ShiNeng)
                bean.setValid(1);
            else
                bean.setValid(0);
            timerEvent_rows.add(bean);
            time_data.setDatType(19);
            time_data.setDevUnitID(GlobalVars.getDevid());
            time_data.setItemCnt(1);
            time_data.setSubType1(0);
            time_data.setSubType2(0);
            time_data.setTimerEvent_rows(timerEvent_rows);
            Gson gson = new Gson();
            Log.e("0000", gson.toJson(time_data));
            MyApplication.mApplication.showLoadDialog(activity);
            MyApplication.mApplication.getUdpServer().send(gson.toJson(time_data));
        } catch (Exception e) {
            MyApplication.mApplication.dismissLoadDialog();
            Log.e("保存定时器数据", "保存数据异常" + e);
            ToastUtil.showText("保存数据异常,请检查数据是否合适");
        }
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
     * 选择星期dialog
     *
     * @param view 显示控件
     */
    public static void initWeekDialog(Context context, final TextView view) {


        List<String> Time_week = new ArrayList<>();
        Time_week.add("星期一");
        Time_week.add("星期二");
        Time_week.add("星期三");
        Time_week.add("星期四");
        Time_week.add("星期五");
        Time_week.add("星期六");
        Time_week.add("星期日");
        if (mMultiChoicePopWindow == null)
            mMultiChoicePopWindow = new MultiChoicePopWindow(context, view, Time_week, new boolean[7]);
        mMultiChoicePopWindow.setTitle("请选择星期");
        mMultiChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
                int size = selItems.length;
                int data_to_network = 0;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < size; i++) {
                    if (selItems[i]) {
                        stringBuffer.append(i + 1 + " ");
                        data_to_network += Math.pow(2, i);
                    }
                }
                view.setText("星期集：" + stringBuffer.toString());
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

