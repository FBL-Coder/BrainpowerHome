package com.example.abc.mybaseactivity;

import android.graphics.Color;
import android.view.View;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;

/**
 * 作者：FBL  时间： 2017/5/3.
 */
public class TestClass extends BaseActivity {


    @Override
    public void initView() {
        setLayout(R.layout.activity_a);
        setStatusColor(R.color.colorAccent);
        setTitleViewVisible(true, R.color.colorAccent);
        setTitleText("测试测试测试测试测试", 20, Color.WHITE);
        setTitleImageBtn(true, R.drawable.back_image_select, false, 0);
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mRelativeLayout, new Fragment()).commit();
    }
}
