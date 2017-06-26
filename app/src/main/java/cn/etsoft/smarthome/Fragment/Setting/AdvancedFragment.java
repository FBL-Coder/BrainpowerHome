package cn.etsoft.smarthome.Fragment.Setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import cn.etsoft.smarthome.Activity.AdvancedSetting.ConditionSetActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.ControlActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.DevInfoActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.SafetySetActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.SceneSetActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.TimerSetActivity;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

import static android.support.v4.view.ViewCompat.animate;

/**
 * Author：FBL  Time： 2017/6/22.
 */

public class AdvancedFragment extends BaseFragment implements View.OnClickListener {


    private LinearLayout mControldev;
    private LinearLayout mDevinfo;
    private LinearLayout mScene;
    private LinearLayout mSafety;
    private LinearLayout mTimer;
    private LinearLayout mCondition;

    @Override
    public void initData(Bundle arguments) {

    }

    @Override
    protected void initView() {
        mControldev = findViewById(R.id.setting_advanced_controldev);
        mDevinfo = findViewById(R.id.setting_advanced_devinfo);
        mScene = findViewById(R.id.setting_advanced_scene);
        mSafety = findViewById(R.id.setting_advanced_safety);
        mTimer = findViewById(R.id.setting_advanced_timer);
        mCondition = findViewById(R.id.setting_advanced_condition);
    }

    @Override
    protected void setListener() {
        mCondition.setOnClickListener(this);
        mTimer.setOnClickListener(this);
        mSafety.setOnClickListener(this);
        mScene.setOnClickListener(this);
        mDevinfo.setOnClickListener(this);
        mControldev.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_advanced_controldev:
                startActivity(new Intent(mActivity, ControlActivity.class));
                break;
            case R.id.setting_advanced_devinfo:
                startActivity(new Intent(mActivity, DevInfoActivity.class));
                break;
            case R.id.setting_advanced_scene:
                SendDataUtil.getSceneInfo();
                startActivity(new Intent(mActivity, SceneSetActivity.class));
                break;
            case R.id.setting_advanced_safety:
                startActivity(new Intent(mActivity, SafetySetActivity.class));
                break;
            case R.id.setting_advanced_timer:
                startActivity(new Intent(mActivity, TimerSetActivity.class));
                break;
            case R.id.setting_advanced_condition:
                startActivity(new Intent(mActivity, ConditionSetActivity.class));
                break;

        }
    }


    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_set_advanced;
    }
}
