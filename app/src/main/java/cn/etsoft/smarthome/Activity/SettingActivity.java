package cn.etsoft.smarthome.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.etsoft.smarthome.Activity.Settings.ConditionSetActivity;
import cn.etsoft.smarthome.Activity.Settings.DevInfoActivity;
import cn.etsoft.smarthome.Activity.Settings.Dev_KeysSetActivity;
import cn.etsoft.smarthome.Activity.Settings.GroupSetActivity;
import cn.etsoft.smarthome.Activity.Settings.Key_DevsSetActivity;
import cn.etsoft.smarthome.Activity.Settings.NewWorkSetActivity;
import cn.etsoft.smarthome.Activity.Settings.SafetySetActivity;
import cn.etsoft.smarthome.Activity.Settings.SceneSetActivity;
import cn.etsoft.smarthome.Activity.Settings.Scene_KeysActivity;
import cn.etsoft.smarthome.Activity.Settings.TimerSetActivity;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.Http_Result;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.LogoutHelper;
import cn.etsoft.smarthome.Utils.Data_Cache;
import cn.etsoft.smarthome.Utils.NewHttpPort;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.LinearLayout.BamLinearLayout;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;

/**
 * Author：FBL  Time： 2017/6/15.
 * 设置界面
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private BamLinearLayout mSettingNetwork, mSettingGroup,
            mSettingDevinfo, mSettingScene, mSettingSafety,
            mSettingTimer, mSettingCondition, mSettingDevKeys,
            mSettingKeyDevs, mSettingKeyScenes;

    @Override
    public void initView() {

        setTitleViewVisible(true, R.color.color_4489CA);
        setTitleImageBtn(true, R.drawable.back_image_select, true, R.drawable.logout_icon);
        setLayout(R.layout.activity_setting);
        mSettingNetwork = getViewById(R.id.setting_network);
        mSettingDevinfo = getViewById(R.id.setting_devinfo);
        mSettingScene = getViewById(R.id.setting_scene);
        mSettingSafety = getViewById(R.id.setting_safety);
        mSettingTimer = getViewById(R.id.setting_timer);
        mSettingCondition = getViewById(R.id.setting_condition);
        mSettingGroup = getViewById(R.id.setting_group);
        mSettingDevKeys = getViewById(R.id.setting_dev_keys);
        mSettingKeyDevs = getViewById(R.id.setting_key_devs);
        mSettingKeyScenes = getViewById(R.id.setting_key_scenes);

        mSettingNetwork.setOnClickListener(this);
        mSettingDevinfo.setOnClickListener(this);
        mSettingScene.setOnClickListener(this);
        mSettingSafety.setOnClickListener(this);
        mSettingTimer.setOnClickListener(this);
        mSettingCondition.setOnClickListener(this);
        mSettingGroup.setOnClickListener(this);
        mSettingDevKeys.setOnClickListener(this);
        mSettingKeyDevs.setOnClickListener(this);
        mSettingKeyScenes.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutHelper.lohout(SettingActivity.this);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_network://联网模块
                startActivity(new Intent(SettingActivity.this, NewWorkSetActivity.class));
                break;
            case R.id.setting_dev_keys://设备配按键
                startActivity(new Intent(SettingActivity.this, Dev_KeysSetActivity.class));
                break;
            case R.id.setting_key_devs://按键配设备
                startActivity(new Intent(SettingActivity.this, Key_DevsSetActivity.class));
                break;
            case R.id.setting_key_scenes://按键配情景
                SendDataUtil.getScene_KeysData();
                startActivity(new Intent(SettingActivity.this, Scene_KeysActivity.class));
                break;
            case R.id.setting_devinfo://设备详情
                startActivity(new Intent(SettingActivity.this, DevInfoActivity.class));
                break;
            case R.id.setting_scene://情景设置
                if (MyApplication.getWareData().getSceneEvents().size() == 0)
                    SendDataUtil.getSceneInfo();
                startActivity(new Intent(SettingActivity.this, SceneSetActivity.class));
                break;
            case R.id.setting_safety://安防设置
                if (MyApplication.getWareData().getSceneEvents().size() == 0)
                    SendDataUtil.getSceneInfo();
                SendDataUtil.getSafetyInfo();
                startActivity(new Intent(SettingActivity.this, SafetySetActivity.class));
                break;
            case R.id.setting_timer://定时设置
                SendDataUtil.getTimerInfo();
                startActivity(new Intent(SettingActivity.this, TimerSetActivity.class));
                break;
            case R.id.setting_condition://环境事件
                SendDataUtil.getConditionInfo();
                startActivity(new Intent(SettingActivity.this, ConditionSetActivity.class));
                break;
            case R.id.setting_group://组合设置
                if (MyApplication.getWareData().getResult_safety().getSec_info_rows().size() == 0)
                    SendDataUtil.getSafetyInfo();
                SendDataUtil.getGroupSetInfo();
                startActivity(new Intent(SettingActivity.this, GroupSetActivity.class));
                break;
        }

    }
}
