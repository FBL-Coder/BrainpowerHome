package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.icu.text.IDNA;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.TimerInfo_DevAdapter;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

import static cn.etsoft.smarthome.UiHelper.TimerSetHelper.reverseString;

/**
 * Author：FBL  Time： 2017/6/22.
 * 定时器详情页面（主页进入的定时）
 */

public class TimerInfoActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> mTimerInfoCircleMenu;
    private TextView mNull_tv, mTimerInfoStartTime, mTimerInfoWeek, mTimerInfoWeekAgain, mTimerInfoEndTime, TimerInfo_ShiNeng_tv;
    private GridView mTimerInfoGirdView;
    private ImageView TimerInfo_ShiNeng_iv;
    private LinearLayout mTimerInfoShiNeng;
    private RelativeLayout TimerInfo_Data;
    private TimerInfo_DevAdapter mAdapter;
    private boolean IsCanClick = false;
    private int mTimerPosition = -1;
    private int CirclePosition = -1;
    private Timer_Data.TimerEventRowsBean mBean;
    private boolean IsOpenShiNeng = false, IsOpenWeekAgain = false;


    @Override
    public void initView() {
        setLayout(R.layout.activity_timerinfo);
        layout = getViewById(R.id.TimerInfo_CircleMenu);
        mNull_tv = getViewById(R.id.TimerInfo_null_tv);
        mTimerInfoStartTime = getViewById(R.id.TimerInfo_StartTime);
        mTimerInfoEndTime = getViewById(R.id.TimerInfo_EndTime);
        mTimerInfoWeek = getViewById(R.id.TimerInfo_week);
        mTimerInfoWeekAgain = getViewById(R.id.TimerInfo_Week_Again);
        mTimerInfoShiNeng = getViewById(R.id.TimerInfo_ShiNeng);
        TimerInfo_ShiNeng_iv = getViewById(R.id.TimerInfo_ShiNeng_iv);
        TimerInfo_ShiNeng_tv = getViewById(R.id.TimerInfo_ShiNeng_tv);
        mTimerInfoGirdView = getViewById(R.id.TimerInfo_GirdView);
        TimerInfo_Data = getViewById(R.id.TimerInfo_Data);
        mTimerInfoGirdView.setEmptyView(mNull_tv);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();
                if (datType == 17) {
                    if (mTimerPosition == -1) return;
                    if (mAdapter == null)
                        mAdapter = new TimerInfo_DevAdapter(mBean.getRun_dev_item(), TimerInfoActivity.this);
                    else
                        mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
                    mTimerInfoGirdView.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public void initData() {
        initTimer();
        if (mTimerPosition == -1) {
            mNull_tv.setText("请先选择定时器");
            TimerInfo_Data.setVisibility(View.GONE);
        }
    }

    private void initTimer() {
        mTimerInfoCircleMenu = initSceneCircleOUterData(CirclePosition);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(mTimerInfoCircleMenu);

        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                IsCanClick = true;
                mNull_tv.setText("没有设备，通过情景设置添加设备");
                TimerInfo_Data.setVisibility(View.VISIBLE);
                CirclePosition = position;
                mTimerPosition = position % MyApplication.getWareData().getTimer_data().getTimerEvent_rows().size();

                mBean = WareDataHliper.initCopyWareData().getCopyTimers().getTimerEvent_rows()
                        .get(mTimerPosition);
                if (mAdapter == null)
                    mAdapter = new TimerInfo_DevAdapter(mBean.getRun_dev_item(), TimerInfoActivity.this);
                else
                    mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
                mTimerInfoGirdView.setAdapter(mAdapter);

                setData();

            }
        });

        mTimerInfoShiNeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsOpenShiNeng) {
                    Error
                    //TODO 定时器详情页面的启动按钮事件
                } else {

                }
            }
        });
    }

    private void setData() {
        if (mBean.getValid() == 1) {
            IsOpenShiNeng = true;
            TimerInfo_ShiNeng_iv.setImageResource(R.drawable.show_on);
            TimerInfo_ShiNeng_tv.setText("开启");
        } else {
            IsOpenShiNeng = false;
            TimerInfo_ShiNeng_iv.setImageResource(R.drawable.show_off);
            TimerInfo_ShiNeng_tv.setText("关闭");
        }

        List<Integer> data_start = mBean.getTimSta();
        String startTime = data_start.get(0) + " : " + data_start.get(1);
        mTimerInfoStartTime.setText(startTime);

        int weekSelect_10 = data_start.get(3);
        String weekSelect_2 = reverseString(Integer.toBinaryString(weekSelect_10));
        String weekSelect_2_data = "";
        for (int i = 0; i < weekSelect_2.toCharArray().length; i++) {
            if (weekSelect_2.toCharArray()[i] == '1')
                weekSelect_2_data += " " + (i + 1);
        }
        mTimerInfoWeek.setText(weekSelect_2_data + "");

        List<Integer> data_end = mBean.getTimEnd();
        String endtime = data_end.get(0) + " : " + data_end.get(1);
        mTimerInfoEndTime.setText(endtime);

        if (data_end.get(3) == 1) {
            mTimerInfoWeekAgain.setText("是");
            IsOpenWeekAgain = true;
        } else {
            mTimerInfoWeekAgain.setText("否");
            IsOpenWeekAgain = false;
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        if (!IsCanClick) {
            ToastUtil.showText("请先选择情景");
            return;
        }
        switch (v.getId()) {
            case R.id.Control_Scene_Run://执行情景
                break;
        }
    }

    public static List<CircleDataEvent> initSceneCircleOUterData(int position) {

        if (MyApplication.getWareData().getTimer_data().getTimerEvent_rows()
                .size() == 0)
            return new ArrayList<>();
        List<CircleDataEvent> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();

            event.setTitle(MyApplication.getWareData().getTimer_data().getTimerEvent_rows()
                    .get(i % MyApplication.getWareData().getTimer_data().getTimerEvent_rows()
                            .size()).getTimerName());
            event.setImage(R.drawable.timerinfo);
            if (i == position)
                event.setSelect(true);
            list.add(event);
        }
        return list;
    }

}
