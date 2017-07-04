package cn.etsoft.smarthome.Activity.AdvancedSetting;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;

import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/22.
 * 安防设置界面
 */

public class SafetySetActivity extends BaseActivity{
    @Override
    public void initView() {

        SendDataUtil.getSecurityInfo();
    }

    @Override
    public void initData() {

    }
}
