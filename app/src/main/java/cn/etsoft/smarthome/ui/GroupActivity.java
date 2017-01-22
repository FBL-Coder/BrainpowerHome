package cn.etsoft.smarthome.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.fragment.InPutFragment;
import cn.etsoft.smarthome.fragment.InfraredFragment;
import cn.etsoft.smarthome.fragment.OutPutFragment;

/**
 * Created by Say GoBay on 2016/8/23.
 */
public class GroupActivity extends FragmentActivity {

    private RadioGroup group;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment outPutFragment, inPutFragment, infraredFragment;
    private TextView mTitle;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
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
        mTitle.setText(getIntent().getStringExtra("title"));
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
