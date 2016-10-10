package cn.etsoft.smarthome.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.etsoft.smarthome.Fragment.AirConditionFragment;
import cn.etsoft.smarthome.Fragment.CurtainFragment;
import cn.etsoft.smarthome.Fragment.LamplightFragment;
import cn.etsoft.smarthome.Fragment.STBFragment;
import cn.etsoft.smarthome.Fragment.SceneFragment;
import cn.etsoft.smarthome.Fragment.SettingFragment;
import cn.etsoft.smarthome.Fragment.TvFragment;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    private RadioGroup equipmentRadioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment settingFragment, airConditionFragment, tvFragment, sTBFragment, lamplightFragment,
            curtainFragment, sceneFragment;
    private Button home, equipmentClose, equipmentOpen, lamplightControl, curtainControl;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化标题栏
        initTitleBar();
        //初始化控件
        initView();
        //初始化数据
        initData();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        fragmentManager = getSupportFragmentManager();
        equipmentRadioGroup = (RadioGroup) findViewById(R.id.rg_equipment);
        ((RadioButton) equipmentRadioGroup.findViewById(R.id.rb_equipment_airCondition)).setChecked(true);
        transaction = fragmentManager.beginTransaction();
        home = (Button) findViewById(R.id.home_home);
        equipmentClose = (Button) findViewById(R.id.home_equipmentClose);
        equipmentOpen = (Button) findViewById(R.id.home_equipmentOpen);
        lamplightControl = (Button) findViewById(R.id.home_lamplight);
        curtainControl = (Button) findViewById(R.id.home_curtain);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        settingFragment = new SettingFragment();

        airConditionFragment = new AirConditionFragment();
        tvFragment = new TvFragment();
        sTBFragment = new STBFragment();
        lamplightFragment = new LamplightFragment();
        curtainFragment = new CurtainFragment();
        sceneFragment = new SceneFragment();

        transaction.replace(R.id.home, airConditionFragment).commit();
        equipmentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.rb_equipment_airCondition:
                        transaction.replace(R.id.home, airConditionFragment);
                        mTitle.setText("空调控制");
                        break;
                    case R.id.rb_equipment_tv:
                        transaction.replace(R.id.home, tvFragment);
                        mTitle.setText("电视控制");
                        break;
                    case R.id.rb_equipment_STB:
                        transaction.replace(R.id.home, sTBFragment);
                        mTitle.setText("机顶盒控制");
                        break;
                    case R.id.rb_equipment_lamplight:
                        transaction.replace(R.id.home, lamplightFragment);
                        mTitle.setText("灯光控制");
                        break;
                    case R.id.rb_equipment_curtain:
                        transaction.replace(R.id.home, curtainFragment);
                        mTitle.setText("窗帘控制");
                        break;
                    case R.id.rb_equipment_scene:
                        transaction.replace(R.id.home, sceneFragment);
                        mTitle.setText("情景控制");
                        break;
                    case R.id.rb_home_setting:
                        transaction.replace(R.id.home, settingFragment);
                        mTitle.setText("系统设置");
                        break;
                }
                transaction.commit();
            }
        });
    }

    private long TimeExit = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (System.currentTimeMillis() - TimeExit < 1500) {
            Dtat_Cache.writeFile(MyApplication.getWareData());
            MyApplication.mInstance.getActivity().finish();
            System.exit(0);
        } else {
            Toast.makeText(HomeActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            TimeExit = System.currentTimeMillis();
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_home:
                break;
            case R.id.home_equipmentClose:
                break;
            case R.id.home_equipmentOpen:
                break;
            case R.id.home_lamplight:
                break;
            case R.id.home_curtain:
                break;

        }
    }
}
