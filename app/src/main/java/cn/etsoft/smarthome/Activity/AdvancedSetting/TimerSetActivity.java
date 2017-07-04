package cn.etsoft.smarthome.Activity.AdvancedSetting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.Calendar;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Timer_DevAdapter;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.TimerSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;
import cn.etsoft.smarthome.View.PopupWindow.MultiChoicePopWindow;

import static cn.etsoft.smarthome.UiHelper.TimerSetHelper.reverseString;

/**
 * Author：FBL  Time： 2017/6/22.
 * 定时器页面
 */

public class TimerSetActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private TextView mTimerWeeks, mTimerStartTime, mTimerEndTime, mTimerSaveBtn, mTimer_AddDev;
    private EditText mTimerName;
    private ImageView mShiNeng, mWeekAgain, mQuanWang;
    private GridView mTimerGirdView;
    private Timer_DevAdapter mAdapter;
    private boolean IsNoData = true;
    private boolean IsCanClick = false;
    private boolean IsOpenShiNeng = false, IsOpenWeekAgain = false, IsOPenQuanWang = false;
    private Timer_Data.TimerEventRowsBean mBean;
    private int mTimerPosition = 0;


    @Override
    public void initView() {
        setLayout(R.layout.activity_timer);
        layout = getViewById(R.id.Timer_CircleMenu);
        mTimerSaveBtn = getViewById(R.id.Timer_Save_Btn);
        mShiNeng = getViewById(R.id.Timer_ShiNeng);
        mTimerWeeks = getViewById(R.id.Timer_Weeks);
        mTimerStartTime = getViewById(R.id.Timer_TImer_StartTime);
        mTimerEndTime = getViewById(R.id.Timer_EndTime);
        mWeekAgain = getViewById(R.id.Timer_WeekAgain);
        mQuanWang = getViewById(R.id.Timer_QuanWang);
        mTimerName = getViewById(R.id.Timer_Name);
        mTimerGirdView = getViewById(R.id.Timer_GirdView);
        mTimer_AddDev = getViewById(R.id.Timer_AddDev);

        mShiNeng.setOnClickListener(this);
        mWeekAgain.setOnClickListener(this);
        mQuanWang.setOnClickListener(this);
        mTimerWeeks.setOnClickListener(this);
        mTimerStartTime.setOnClickListener(this);
        mTimerEndTime.setOnClickListener(this);
        mTimerSaveBtn.setOnClickListener(this);
        mTimer_AddDev.setOnClickListener(this);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 17) {
                    IsNoData = false;
                    WareDataHliper.initCopyWareData().startCopyTimerData();
                    initTimer();
                }
                if (datType == 19) {
                    ToastUtil.showText("添加成功");
                    WareDataHliper.initCopyWareData().startCopyTimerData();
                    initTimer();
                }
            }
        });

    }

    @Override
    public void initData() {
        if (WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows().size() == 0) {
            ToastUtil.showText("没有定时器数据");
            return;
        } else {
            initTimer();
        }
    }

    private void initTimer() {
        Data_OuterCircleList = TimerSetHelper.initSceneCircleOUterData();
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                IsCanClick = true;
                mTimerPosition = position % WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows().size();
                mBean = WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows()
                        .get(mTimerPosition);
                mAdapter = new Timer_DevAdapter(mBean.getRun_dev_item(), TimerSetActivity.this);
                mTimerGirdView.setAdapter(mAdapter);

                mTimerName.setText("");
                mTimerName.setHint(mBean.getTimerName());

                if (mBean.getRun_dev_item() == null
                        || mBean.getRun_dev_item().size() == 0) {
                    mShiNeng.setImageResource(R.drawable.ic_launcher);
                    mWeekAgain.setImageResource(R.drawable.ic_launcher);
                    mQuanWang.setImageResource(R.drawable.ic_launcher);
                    mTimerStartTime.setText("点击选择时间");
                    mTimerEndTime.setText("点击选择时间");
                    mTimerWeeks.setText("点击选择星期");
                } else {
                    if (mBean.getValid() == 1) {
                        IsOpenShiNeng = true;
                        mShiNeng.setImageResource(R.drawable.ic_launcher_round);
                    } else {
                        IsOpenShiNeng = false;
                        mShiNeng.setImageResource(R.drawable.ic_launcher);
                    }

                    List<Integer> data_start = mBean.getTimSta();
                    String startTime = data_start.get(0) + " : " + data_start.get(1);
                    mTimerStartTime.setText(startTime);

                    int weekSelect_10 = data_start.get(3);
                    String weekSelect_2 = reverseString(Integer.toBinaryString(weekSelect_10));
                    String weekSelect_2_data = "";
                    for (int i = 0; i < weekSelect_2.toCharArray().length; i++) {
                        if (weekSelect_2.toCharArray()[i] == '1')
                            weekSelect_2_data += " " + (i + 1);
                    }
                    mTimerWeeks.setText("星期集： " + weekSelect_2_data + "");

                    List<Integer> data_end = mBean.getTimEnd();
                    String endtime = data_end.get(0) + " : " + data_end.get(1);
                    mTimerEndTime.setText(endtime);

                    if (data_end.get(3) == 1) {
                        mWeekAgain.setImageResource(R.drawable.ic_launcher_round);
                        IsOpenWeekAgain = true;
                    } else {
                        mWeekAgain.setImageResource(R.drawable.ic_launcher);
                        IsOpenWeekAgain = false;
                    }
                }
            }
        });
        mTimerGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBean.getRun_dev_item().get(position).getBOnOff() == 0)
                    mBean.getRun_dev_item().get(position).setBOnOff(1);
                else mBean.getRun_dev_item().get(position).setBOnOff(0);
                mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
            }
        });

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        if (IsNoData && WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows().size() == 0) {
            ToastUtil.showText("获取数据异常，请稍后在试");
            return;
        }
        if (!IsCanClick) {
            ToastUtil.showText("请先选择定时器！");
            return;
        }
        switch (v.getId()) {
            case R.id.Timer_Save_Btn://保存

                String timername = mTimerName.getText().toString();
                if (timername.isEmpty()) {
                    ToastUtil.showText("请输入名称");
                    return;
                }
                String starttime = mTimerStartTime.getText().toString();
                if ("点击选择时间".equals(starttime)) {
                    ToastUtil.showText("请选择时间");
                    return;
                }
                String endtime = mTimerEndTime.getText().toString();
                if ("点击选择时间".equals(endtime)) {
                    ToastUtil.showText("请选择时间");
                    return;
                }
                String weekss = mTimerWeeks.getText().toString();
                if ("点击选择星期".equals(weekss) || "星期集：".equals(weekss)) {
                    ToastUtil.showText("请选择星期");
                    return;
                }
                TimerSetHelper.Timer_Save(this, timername, starttime, endtime,
                        IsOpenWeekAgain, IsOpenShiNeng, weekss,mBean,
                        mBean.getRun_dev_item());
                break;
            case R.id.Timer_ShiNeng: //使能开关
                IsOpenShiNeng = !IsOpenShiNeng;
                if (IsOpenShiNeng) mShiNeng.setImageResource(R.drawable.ic_launcher_round);
                else mShiNeng.setImageResource(R.drawable.ic_launcher);
                break;
            case R.id.Timer_Weeks: //星期时间
                TimerSetHelper.initWeekDialog(this, mTimerWeeks);
                break;
            case R.id.Timer_AddDev: //添加设备
                Intent intent = new Intent(TimerSetActivity.this, SetAddDevActivity.class);
                intent.putExtra("name", "Timer");
                intent.putExtra("position", mTimerPosition);
                startActivityForResult(intent,0);

                break;
            case R.id.Timer_TImer_StartTime://开始时间

                TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
                        mTimerStartTime.setText(i + " : " + i1);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");

                break;
            case R.id.Timer_EndTime:// 结束时间
                TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
                        mTimerEndTime.setText(i + " : " + i1);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
            case R.id.Timer_WeekAgain://是否周重复
                IsOpenWeekAgain = !IsOpenWeekAgain;
                if (IsOpenWeekAgain) mWeekAgain.setImageResource(R.drawable.ic_launcher_round);
                else mWeekAgain.setImageResource(R.drawable.ic_launcher);
                break;
            case R.id.Timer_QuanWang://是否开开全网
                IsOPenQuanWang = !IsOPenQuanWang;
                if (IsOPenQuanWang) mQuanWang.setImageResource(R.drawable.ic_launcher_round);
                else mQuanWang.setImageResource(R.drawable.ic_launcher);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.notifyDataSetChanged(WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows()
                .get(mTimerPosition).getRun_dev_item());
    }
}
