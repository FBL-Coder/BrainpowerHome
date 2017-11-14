package cn.etsoft.smarthome.Activity.UserInterFace;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/11/14.
 * 用户界面 空调
 */

public class Air_Control extends Activity implements View.OnClickListener {

    /**
     * 你家空调我的
     */
    private TextView mAirName;
    private ImageView mAirSwitch, air_control_close;
    /**
     * 温度+
     */
    private TextView mAirSetTempAdd;
    /**
     * 25
     */
    private TextView mAirTempNow;
    /**
     * 温度-
     */
    private TextView mAirSetTempMin;
    /**
     * 风速+
     */
    private TextView mAirSetSpeedAdd;
    /**
     * 高风
     */
    private TextView mAirSpeed;
    /**
     * 风速-
     */
    private TextView mAirSetSpeedMin;
    /**
     * 制冷
     */
    private TextView mAirSetModeCool;
    /**
     * 制热
     */
    private TextView mAirSetModeHot;
    /**
     * 除湿
     */
    private TextView mAirSetModeArefaction;
    /**
     * 扫风
     */
    private TextView mAirSetModeWind;

    private String mCancpuid;

    private int mDevid;

    private WareAirCondDev mAirCondDev;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_user_control);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 4) {
                    initData();
                }
            }
        });
        super.onResume();
    }

    private void initData() {

        mCancpuid = getIntent().getStringExtra("cancpuid");
        mDevid = getIntent().getIntExtra("devid", 0);
        for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
            if (mCancpuid.equals(MyApplication.getWareData().getAirConds().get(j).getDev().getCanCpuId())
                    && mDevid == MyApplication.getWareData().getAirConds().get(j).getDev().getDevId()) {
                mAirCondDev = MyApplication.getWareData().getAirConds().get(j);
            }
        }
        mAirName.setText(mAirCondDev.getDev().getDevName());
        mAirTempNow.setText(mAirCondDev.getSelTemp() + "℃");

        if (mAirCondDev.getbOnOff() == 0) mAirSwitch.setImageResource(R.drawable.show_off);
        else mAirSwitch.setImageResource(R.drawable.show_on);

        if (mAirCondDev.getSelSpd()
                == UdpProPkt.E_AIR_CMD.e_air_spdLow.getValue()) {
            mAirSpeed.setText("低风");
        } else if (mAirCondDev.getSelSpd()
                == UdpProPkt.E_AIR_CMD.e_air_spdMid.getValue()) {
            mAirSpeed.setText("中风");
        } else if (mAirCondDev.getSelSpd()
                == UdpProPkt.E_AIR_CMD.e_air_spdHigh.getValue()) {
            mAirSpeed.setText("高风");
        } else if (mAirCondDev.getSelSpd()
                == UdpProPkt.E_AIR_CMD.e_air_spdAuto.getValue()) {
            mAirSpeed.setText("自动");
        }

        if (mAirCondDev.getSelMode() == 1) {
            mAirSetModeCool.setTextColor(Color.WHITE);
            mAirSetModeHot.setTextColor(Color.GREEN);
            mAirSetModeArefaction.setTextColor(Color.WHITE);
            mAirSetModeWind.setTextColor(Color.WHITE);
        } else if (mAirCondDev.getSelMode() == 2) {
            mAirSetModeCool.setTextColor(Color.GREEN);
            mAirSetModeHot.setTextColor(Color.WHITE);
            mAirSetModeArefaction.setTextColor(Color.WHITE);
            mAirSetModeWind.setTextColor(Color.WHITE);
        } else if (mAirCondDev.getSelMode() == 3) {
            mAirSetModeCool.setTextColor(Color.WHITE);
            mAirSetModeHot.setTextColor(Color.WHITE);
            mAirSetModeArefaction.setTextColor(Color.GREEN);
            mAirSetModeWind.setTextColor(Color.WHITE);
        } else if (mAirCondDev.getSelMode() == 4) {
            mAirSetModeCool.setTextColor(Color.WHITE);
            mAirSetModeHot.setTextColor(Color.WHITE);
            mAirSetModeArefaction.setTextColor(Color.WHITE);
            mAirSetModeWind.setTextColor(Color.GREEN);
        }
    }

    private void initView() {
        mAirName = (TextView) findViewById(R.id.air_name);
        mAirSwitch = (ImageView) findViewById(R.id.air_switch);
        air_control_close = (ImageView) findViewById(R.id.air_control_close);
        mAirSwitch.setOnClickListener(this);
        air_control_close.setOnClickListener(this);
        mAirSetTempAdd = (TextView) findViewById(R.id.air_set_temp_add);
        mAirSetTempAdd.setOnClickListener(this);
        mAirTempNow = (TextView) findViewById(R.id.air_temp_now);
        mAirSetTempMin = (TextView) findViewById(R.id.air_set_temp_min);
        mAirSetTempMin.setOnClickListener(this);
        mAirSetSpeedAdd = (TextView) findViewById(R.id.air_set_speed_add);
        mAirSetSpeedAdd.setOnClickListener(this);
        mAirSpeed = (TextView) findViewById(R.id.air_speed);
        mAirSetSpeedMin = (TextView) findViewById(R.id.air_set_speed_min);
        mAirSetSpeedMin.setOnClickListener(this);
        mAirSetModeCool = (TextView) findViewById(R.id.air_set_mode_cool);
        mAirSetModeCool.setOnClickListener(this);
        mAirSetModeHot = (TextView) findViewById(R.id.air_set_mode_hot);
        mAirSetModeHot.setOnClickListener(this);
        mAirSetModeArefaction = (TextView) findViewById(R.id.air_set_mode_arefaction);
        mAirSetModeArefaction.setOnClickListener(this);
        mAirSetModeWind = (TextView) findViewById(R.id.air_set_mode_wind);
        mAirSetModeWind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int cmdValue = 0, modelValue = 0;
        MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
        switch (v.getId()) {
            case R.id.air_switch:
                if (mAirCondDev.getbOnOff() == 0) {
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue();//打开空调
                } else {
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOff.getValue();//关闭空调
                }
                int value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_set_temp_add:
                if (mAirCondDev.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                if (mAirCondDev.getSelTemp() == 30) {
                    ToastUtil.showText("不能再高了");
                    return;
                }
                cmdValue = mAirCondDev.getSelTemp() + 1;
                value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_set_temp_min:
                if (mAirCondDev.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                if (mAirCondDev.getSelTemp() == 16) {
                    ToastUtil.showText("不能再低了");
                    return;
                }
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                cmdValue = mAirCondDev.getSelTemp() - 1;
                value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_set_speed_add:
                if (mAirCondDev.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                if (mAirCondDev.getSelSpd() == 5) {
                    ToastUtil.showText("不能再高了");
                    return;
                }
                cmdValue = mAirCondDev.getSelSpd() + 1;
                value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_set_speed_min:
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                if (mAirCondDev.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                if (mAirCondDev.getSelSpd() == 2) {
                    ToastUtil.showText("不能再低了");
                    return;
                }
                cmdValue = mAirCondDev.getSelSpd() - 1;
                value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_set_mode_cool:
                if (mAirCondDev.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                modelValue = UdpProPkt.E_AIR_MODE.e_air_cool.getValue();
                value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_set_mode_hot:
                if (mAirCondDev.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                modelValue = UdpProPkt.E_AIR_MODE.e_air_hot.getValue();
                value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_set_mode_arefaction:
                if (mAirCondDev.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                modelValue = UdpProPkt.E_AIR_MODE.e_air_dry.getValue();
                value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_set_mode_wind:
                if (mAirCondDev.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                modelValue = UdpProPkt.E_AIR_MODE.e_air_wind.getValue();
                value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirCondDev.getDev(), value);
                break;
            case R.id.air_control_close:
                finish();
                break;
        }
    }
}
