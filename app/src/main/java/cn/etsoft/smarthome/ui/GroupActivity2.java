package cn.etsoft.smarthome.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.fragment_group2.InPutFragment;
import cn.etsoft.smarthome.fragment_group2.InfraredFragment;
import cn.etsoft.smarthome.fragment_group2.OutPutFragment;

/**
 * Created by Say GoBay on 2016/9/5.
 * 输出，输入以及红外编辑页面
 */
public class GroupActivity2 extends FragmentActivity {
    private RadioGroup group;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment outPutFragment, inPutFragment, infraredFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group2);
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
        ((RadioButton) group.findViewById(R.id.group_input)).setChecked(true);
        transaction = fragmentManager.beginTransaction();
    }
    /**
     * 初始化数据
     */
    private void initData() {
        outPutFragment = new OutPutFragment();
        inPutFragment = new InPutFragment();
        infraredFragment = new InfraredFragment();

        transaction.replace(R.id.group, inPutFragment).commit();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.group_output:
                        transaction.replace(R.id.group, outPutFragment);
                        break;
                    case R.id.group_input:
                        transaction.replace(R.id.group, inPutFragment);
                        break;
                    case R.id.group_infrared:
                        transaction.replace(R.id.group, infraredFragment);
                        break;
                }
                transaction.commit();
            }
        });
    }
}
