package cn.etsoft.smarthome.Activity;

import android.content.Intent;
import android.view.View;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;

import cn.etsoft.smarthome.Activity.Settings.ConditionSetActivity;
import cn.etsoft.smarthome.Activity.Settings.ControlActivity;
import cn.etsoft.smarthome.Activity.Settings.DevInfoActivity;
import cn.etsoft.smarthome.Activity.Settings.Dev_KeysSetActivity;
import cn.etsoft.smarthome.Activity.Settings.NewWorkSetActivity;
import cn.etsoft.smarthome.Activity.Settings.SafetySetActivity;
import cn.etsoft.smarthome.Activity.Settings.SceneSetActivity;
import cn.etsoft.smarthome.Activity.Settings.TimerSetActivity;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.LinearLayout.BamLinearLayout;

/**
 * Author：FBL  Time： 2017/6/15.
 * 设置界面
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private BamLinearLayout mSettingNetwork, mSettingControl, mSettingDevinfo, mSettingScene, mSettingSafety, mSettingTimer, mSettingCondition, mSettingGroup;

    @Override
    public void initView() {

        setTitleViewVisible(true, R.color.color_4489CA);
        setTitleImageBtn(true, R.drawable.back_image_select, false, 0);
        setLayout(R.layout.activity_setting);
        mSettingNetwork = getViewById(R.id.setting_network);
        mSettingControl = getViewById(R.id.setting_control);
        mSettingDevinfo = getViewById(R.id.setting_devinfo);
        mSettingScene = getViewById(R.id.setting_scene);
        mSettingSafety = getViewById(R.id.setting_safety);
        mSettingTimer = getViewById(R.id.setting_timer);
        mSettingCondition = getViewById(R.id.setting_condition);
        mSettingGroup = getViewById(R.id.setting_group);

        mSettingNetwork.setOnClickListener(this);
        mSettingControl.setOnClickListener(this);
        mSettingDevinfo.setOnClickListener(this);
        mSettingScene.setOnClickListener(this);
        mSettingSafety.setOnClickListener(this);
        mSettingTimer.setOnClickListener(this);
        mSettingCondition.setOnClickListener(this);
        mSettingGroup.setOnClickListener(this);

    }

    @Override
    public void initData() {
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_network://联网模块
                startActivity(new Intent(SettingActivity.this,NewWorkSetActivity.class));
                break;
            case R.id.setting_control://控制设置
                startActivity(new Intent(SettingActivity.this, ControlActivity.class));
                break;
            case R.id.setting_devinfo://设备详情
                startActivity(new Intent(SettingActivity.this,DevInfoActivity.class));
                break;
            case R.id.setting_scene://情景设置
                SendDataUtil.getSceneInfo();
                startActivity(new Intent(SettingActivity.this,SceneSetActivity.class));
                break;
            case R.id.setting_safety://安防设置
                SendDataUtil.getSceneInfo();
                SendDataUtil.getSafetyInfo();
                startActivity(new Intent(SettingActivity.this,SafetySetActivity.class));
                break;
            case R.id.setting_timer://定时设置
                startActivity(new Intent(SettingActivity.this,TimerSetActivity.class));
                break;
            case R.id.setting_condition://环境事件
                SendDataUtil.getConditionInfo();
                startActivity(new Intent(SettingActivity.this,ConditionSetActivity.class));
                break;
            case R.id.setting_group://组合设置
                startActivity(new Intent(SettingActivity.this, Dev_KeysSetActivity.class));
                break;
        }

    }
}
