package cn.etsoft.smarthome.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.fragment.Setting_AccountNumberFragment;
import cn.etsoft.smarthome.fragment.Setting_MainFrameFragment;
import cn.etsoft.smarthome.fragment.Setting_OtherSetFragment;
import cn.etsoft.smarthome.fragment.Setting_PasswordFragment;
//import cn.etsoft.smarthome.fragment.Setting_RoomFragment;
import cn.etsoft.smarthome.fragment.Setting_SafetyFragment;
import cn.etsoft.smarthome.fragment.Setting_SystemFragment;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.R;


/**
 * Created by Say GoBay on 2016/11/28.
 * 设置页面
 */
public class SettingActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView back;
    private TextView login_out;
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment setting_MainFrameFragment, setting_AccountNumberFragment, setting_PasswordFragment,
            setting_SafetyFragment, setting_SystemFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etsoft_setting);
        //初始化控件
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        login_out = (TextView) findViewById(R.id.login_out);
        fragmentManager = getSupportFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ((RadioButton) findViewById(R.id.mainFrame)).setChecked(true);
        transaction = fragmentManager.beginTransaction();

        back.setOnClickListener(this);
        login_out.setOnClickListener(this);
        //初始化RadioGroup
        initRadioGroup();
    }


    /**
     * 初始化RadioGroup
     */
    private void initRadioGroup() {
        setting_MainFrameFragment = new Setting_MainFrameFragment();
        transaction.replace(R.id.fragment, setting_MainFrameFragment).commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.mainFrame:
                        transaction.replace(R.id.fragment, setting_MainFrameFragment);
                        break;
                    case R.id.accountNumber:
                        setting_AccountNumberFragment = new Setting_AccountNumberFragment();
                        transaction.replace(R.id.fragment, setting_AccountNumberFragment);
                        break;
                    case R.id.password:
                        setting_PasswordFragment = new Setting_PasswordFragment();
                        transaction.replace(R.id.fragment, setting_PasswordFragment);
                        break;
//                    case R.id.room:
//                        setting_RoomFragment = new Setting_RoomFragment();
//                        transaction.replace(R.id.fragment, setting_RoomFragment);
//                        break;
                    case R.id.safety:
                        setting_SafetyFragment = new Setting_SafetyFragment();
                        transaction.replace(R.id.fragment, setting_SafetyFragment);
                        break;
                    case R.id.system:
                        setting_SystemFragment = new Setting_SystemFragment();
                        transaction.replace(R.id.fragment, setting_SystemFragment);
                        break;
                    case R.id.other_set:
                        setting_SystemFragment = new Setting_OtherSetFragment();
                        transaction.replace(R.id.fragment, setting_SystemFragment);
                        break;
                }
                transaction.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login_out:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("您确定要退出登录？");
                builder.setTitle("提示");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getSharedPreferences("profile",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("list", "");
                        editor.putString("user", "");
                        editor.commit();
                        String a = sharedPreferences.getString("list", "");
                        String b = sharedPreferences.getString("user", "");
                        GlobalVars.setDevid("");
                        GlobalVars.setDevpass("");

                        MyApplication.getmHomeActivity().finish();
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                builder.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        String jsondata = sharedPreferences.getString("list", "");
        if ("".equals(jsondata))
            System.exit(0);
        else
            super.onBackPressed();
    }
}
