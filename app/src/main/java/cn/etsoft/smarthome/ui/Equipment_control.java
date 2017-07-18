package cn.etsoft.smarthome.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.fragment_equipment.EditDevFragment;
import cn.etsoft.smarthome.fragment_equipment.EditModuleFragment;

/**
 * Created by fbl on 16-11-17.
 * 设备控制
 */
public class Equipment_control extends FragmentActivity{
    private RadioGroup group;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment editDevFragment, editModuleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_control);
        //初始化控件
        initView();
        //初始化数据
        initData();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        fragmentManager = getSupportFragmentManager();
        group = (RadioGroup) findViewById(R.id.rg_group);
        ((RadioButton) group.findViewById(R.id.edit_dev)).setChecked(true);
        transaction = fragmentManager.beginTransaction();
    }
    /**
     * 初始化数据
     */
    private void initData() {
        editDevFragment = new EditDevFragment(Equipment_control.this);
        editModuleFragment = new EditModuleFragment(Equipment_control.this);

        transaction.replace(R.id.group, editDevFragment).commit();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.edit_dev:
                        transaction.replace(R.id.group, editDevFragment);
                        break;
                    case R.id.edit_module:
                        transaction.replace(R.id.group, editModuleFragment);
                        break;
                }
                transaction.commit();
            }
        });
    }
}
