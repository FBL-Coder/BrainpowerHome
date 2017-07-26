package cn.etsoft.smarthome.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.fragment_group3.InPutFragment;
import cn.etsoft.smarthome.fragment_group3.KeySceneFragment;
import cn.etsoft.smarthome.fragment_group3.OutPutFragment;

/**
 * Created by Say GoBay on 2016/9/5.
 * 高级设置-控制设置页面
 */
public class GroupActivity extends FragmentActivity {
    private RadioGroup radioGroup;
    public Fragment inputFragment,outPutFragment,keySceneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        radioGroup = (RadioGroup) findViewById(R.id.rg_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (inputFragment != null) {
                    ft.hide(inputFragment);
                }
                if (outPutFragment != null) {
                    ft.hide(outPutFragment);
                }
                if (keySceneFragment != null) {
                    ft.hide(keySceneFragment);
                }
                switch (checkedId) {
                    case R.id.group_input:
                        if (inputFragment == null) {
                            inputFragment = new InPutFragment(GroupActivity.this);
                            ft.add(R.id.group, inputFragment);
                        } else {
                            ft.show(inputFragment);
                        }
                        break;
                    case R.id.group_output:
                        if (outPutFragment == null) {
                            outPutFragment = new OutPutFragment(GroupActivity.this);
                            ft.add(R.id.group, outPutFragment);
                        } else {
                            ft.show(outPutFragment);
                        }
                        break;
                    case R.id.group_infrared:
                        if (keySceneFragment == null) {
                            keySceneFragment = new KeySceneFragment(GroupActivity.this);
                            ft.add(R.id.group, keySceneFragment);
                        } else {
                            ft.show(keySceneFragment);
                        }
                        break;
                    default:
                        break;
                }
                ft.commit();
            }
        });
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            inputFragment = new InPutFragment(GroupActivity.this);
            fragmentManager.beginTransaction()
                    .replace(R.id.group, inputFragment).commit();
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton mTab = (RadioButton) radioGroup.getChildAt(i);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag((String) mTab.getTag());
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment != null) {
                if (!mTab.isChecked()) {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }
}
