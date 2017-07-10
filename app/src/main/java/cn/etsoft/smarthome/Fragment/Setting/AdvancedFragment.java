package cn.etsoft.smarthome.Fragment.Setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import cn.etsoft.smarthome.Activity.AdvancedSetting.ConditionSetActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.ControlActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.DevInfoActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.Dev_KeysSetActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.Key_DevsSetActivity;
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
    private LinearLayout mDev_Keys;
    private LinearLayout mKey_Devs;
    private LinearLayout mOther;

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
        mDev_Keys = findViewById(R.id.setting_advanced_dev_keys);
        mKey_Devs = findViewById(R.id.setting_advanced_key_devs);
        mOther = findViewById(R.id.setting_advanced_qita);
    }

    @Override
    protected void setListener() {
        mCondition.setOnClickListener(this);
        mTimer.setOnClickListener(this);
        mSafety.setOnClickListener(this);
        mScene.setOnClickListener(this);
        mDevinfo.setOnClickListener(this);
        mControldev.setOnClickListener(this);
        mDev_Keys.setOnClickListener(this);
        mKey_Devs.setOnClickListener(this);
        mOther.setOnClickListener(this);
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
                SendDataUtil.getSceneInfo();
                SendDataUtil.getSafetyInfo();
                startActivity(new Intent(mActivity, SafetySetActivity.class));
                break;
            case R.id.setting_advanced_timer:
                startActivity(new Intent(mActivity, TimerSetActivity.class));
                break;
            case R.id.setting_advanced_condition:
                SendDataUtil.getConditionInfo();
                startActivity(new Intent(mActivity, ConditionSetActivity.class));
                break;
            case R.id.setting_advanced_dev_keys:
                startActivity(new Intent(mActivity, Dev_KeysSetActivity.class));
                break;
            case R.id.setting_advanced_key_devs:
                startActivity(new Intent(mActivity, Key_DevsSetActivity.class));
                break;
            case R.id.setting_advanced_qita:
                ToastUtil.showText("哈哈");
                break;
        }
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_set_advanced;
    }
}
