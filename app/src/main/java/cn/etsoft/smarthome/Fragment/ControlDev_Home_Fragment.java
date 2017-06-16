package cn.etsoft.smarthome.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.GridView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import cn.etsoft.smarthome.Activity.HomeActivity;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/15.
 * HomeActivity页面控制设备Fragment
 */

@SuppressLint("ValidFragment")
public class ControlDev_Home_Fragment extends BaseFragment {
    private GridView mGradView;
    private int mRoonPosition;
    private String mControlType;

    @Override
    public void initData(Bundle arguments) {
        if (mControlType == null)
            mControlType = "灯光";
        ToastUtil.showText("控制类型："+mControlType+"  房间位置："+ mRoonPosition);
    }

    @Override
    protected void initView() {
        mGradView = findViewById(R.id.fragment_home_controldev_gridview);
    }

    @Override
    protected void setListener() {

        HomeActivity.setOnPageSelectedListener(new HomeActivity.OnPageSelectedListener() {
            @Override
            public void getPageSelected(int RoomPosition) {
                mRoonPosition = RoomPosition;
                initFragment();
            }
        });
        HomeActivity.setOnRadioButtonSelectedListener(new HomeActivity.OnRadioButtonSelectedListener() {
            @Override
            public void getRadioButtonSelected(String ControlType) {
                mControlType = ControlType;
                initFragment();
            }
        });
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_home_controldev;
    }
}
