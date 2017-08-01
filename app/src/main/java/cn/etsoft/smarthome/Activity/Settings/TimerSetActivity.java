package cn.etsoft.smarthome.Activity.Settings;

import android.annotation.SuppressLint;
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

import static cn.etsoft.smarthome.UiHelper.TimerSetHelper.reverseString;

/**
 * Author：FBL  Time： 2017/6/22.
 * 定时器页面
 */

public class TimerSetActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private TextView mTimerWeeks, mTimerStartTime, mTimerEndTime, mTimerSaveBtn, mTimer_AddDev, mNull_tv;
    private EditText mTimerName;
    private ImageView mShiNeng, mWeekAgain, mQuanWang;
    private GridView mTimerGirdView;
    private Timer_DevAdapter mAdapter;
    private boolean IsNoData = true;
    private boolean IsCanClick = false;
    private boolean IsOpenShiNeng = false, IsOpenWeekAgain = false, IsOPenQuanWang = false;
    private Timer_Data.TimerEventRowsBean mBean;
    private int mTimerPosition = -1;
    private int CirclePosition = -1;


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
        mNull_tv = getViewById(R.id.null_tv);
        mTimerGirdView.setEmptyView(mNull_tv);

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
                MyApplication.mApplication.dismissLoadDialog();
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
        if (mTimerPosition == -1) {
            mNull_tv.setText("请先选择定时器");
        }
    }

    private void initTimer() {
        Data_OuterCircleList = TimerSetHelper.initSceneCircleOUterData(IsCanClick, CirclePosition);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                IsCanClick = true;
                mNull_tv.setText("没有设备，可以添加设备");
                CirclePosition = position;
                mTimerPosition = position % WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows().size();
                mBean = WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows()
                        .get(mTimerPosition);
                mAdapter = new Timer_DevAdapter(mBean.getRun_dev_item(), TimerSetActivity.this);
                mTimerGirdView.setAdapter(mAdapter);

                mTimerName.setText("");
                mTimerName.setHint(mBean.getTimerName());

                if (mBean.getRun_dev_item() == null
                        || mBean.getRun_dev_item().size() == 0) {
                    mShiNeng.setImageResource(R.drawable.checkbox1_unselect);
                    mWeekAgain.setImageResource(R.drawable.checkbox1_unselect);
                    mQuanWang.setImageResource(R.drawable.checkbox1_unselect);
                    mTimerStartTime.setText("点击选择时间");
                    mTimerEndTime.setText("点击选择时间");
                    mTimerWeeks.setText("点击选择星期");
                } else {
                    if (mBean.getValid() == 1) {
                        IsOpenShiNeng = true;
                        mShiNeng.setImageResource(R.drawable.checkbox1_selected);
                    } else {
                        IsOpenShiNeng = false;
                        mShiNeng.setImageResource(R.drawable.checkbox1_unselect);
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
                        mWeekAgain.setImageResource(R.drawable.checkbox1_selected);
                        IsOpenWeekAgain = true;
                    } else {
                        mWeekAgain.setImageResource(R.drawable.checkbox1_unselect);
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

        mTimerGirdView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mBean.getRun_dev_item().remove(position);
                mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
                return true;
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
                    timername = mBean.getTimerName();
                } else if (timername.length() > 6) {
                    ToastUtil.showText("名称太长");
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
                        IsOpenWeekAgain, IsOpenShiNeng, weekss, mBean,
                        mBean.getRun_dev_item());
                break;
            case R.id.Timer_ShiNeng: //使能开关
                IsOpenShiNeng = !IsOpenShiNeng;
                if (IsOpenShiNeng) mShiNeng.setImageResource(R.drawable.checkbox1_selected);
                else mShiNeng.setImageResource(R.drawable.checkbox1_unselect);
                break;
            case R.id.Timer_Weeks: //星期时间
                TimerSetHelper.initWeekDialog(this, mTimerWeeks, new boolean[7]);
                break;
            case R.id.Timer_AddDev: //添加设备
                Intent intent = new Intent(TimerSetActivity.this, SetAddDevActivity.class);
                intent.putExtra("name", "Timer");
                intent.putExtra("position", mTimerPosition);
                startActivityForResult(intent, 0);

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
                if (IsOpenWeekAgain) mWeekAgain.setImageResource(R.drawable.checkbox1_selected);
                else mWeekAgain.setImageResource(R.drawable.checkbox1_unselect);
                break;
            case R.id.Timer_QuanWang://是否开开全网
                IsOPenQuanWang = !IsOPenQuanWang;
                if (IsOPenQuanWang) mQuanWang.setImageResource(R.drawable.checkbox1_selected);
                else mQuanWang.setImageResource(R.drawable.checkbox1_unselect);
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
