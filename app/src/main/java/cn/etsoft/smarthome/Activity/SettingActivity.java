package cn.etsoft.smarthome.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;

import cn.etsoft.smarthome.Fragment.Setting.AdvancedFragment;
import cn.etsoft.smarthome.Fragment.Setting.LongIpFragment;
import cn.etsoft.smarthome.Fragment.Setting.NetModuleFragment;
import cn.etsoft.smarthome.R;

import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

/**
 * Author：FBL  Time： 2017/6/15.
 * 设置界面
 */

public class SettingActivity extends BaseActivity {
    private RadioGroup mSettingRadiogroup;
    private Fragment mNewModule,mLongIP,mAdvanced;

    @Override
    public void initView() {

        setTitleViewVisible(true, R.color.color_4489CA);
        setTitleImageBtn(true, R.drawable.back_image_select, false, 0);
        setLayout(R.layout.activity_setting);
        mSettingRadiogroup = getViewById(R.id.setting_radiogroup);
        setOnClick();
    }
    private void setOnClick() {
        mSettingRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.anim_home_control_open,R.anim.anim_home_weather_close);
                transaction.addToBackStack(null);
                if (mNewModule!= null){
                    transaction.hide(mNewModule);
                }
                if (mLongIP!= null){
                    transaction.hide(mLongIP);
                }
                if (mAdvanced!= null){
                    transaction.hide(mAdvanced);
                }
                switch (checkedId) {
                    case R.id.setting_netmodule_rbt:
                        if (mNewModule == null) {
                            mNewModule = new NetModuleFragment();
                            transaction.add(R.id.setting_linearlayout_right,mNewModule);
                        } else transaction.show(mNewModule);

                        break;
                    case R.id.setting_longip_rbt:
                        if (mLongIP == null) {
                            mLongIP = new LongIpFragment();
                            transaction.add(R.id.setting_linearlayout_right,mLongIP);
                        }
                        else transaction.show(mLongIP);
                        break;
                    case R.id.setting_advanced_rbt:
                        if (mAdvanced == null) {
                            mAdvanced = new AdvancedFragment();
                            transaction.add(R.id.setting_linearlayout_right,mAdvanced);
                        } else transaction.show(mAdvanced);
                        break;
                }
                transaction.commit();
            }
        });
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }
}
