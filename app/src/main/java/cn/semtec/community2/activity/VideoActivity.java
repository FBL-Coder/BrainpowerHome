package cn.semtec.community2.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.fragment.PictureFragment;
import cn.semtec.community2.fragment.RecordFragment;
import cn.semtec.community2.fragment.VideoFragment;
import cn.semtec.community2.util.ToastUtil;

public class VideoActivity extends MyBaseActivity implements View.OnClickListener {

    private View tab_1, tab_2, tab_3;
    private ImageView tab_1_img, tab_2_img, tab_3_img, title_head;
    private View base_sliding;
    private TextView video_title, btn_back;
    private View checked;
    private DrawerLayout mdrawerlayout;
    private TextView sliding_name;
    private TextView sliding_community;
    private TextView sliding_house;
    private View sliding_code;
    private View sliding_information, sliding_memberCheck, sliding_setting, sliding_help;
    private TextView sliding_memberManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        setView();
        setListener();
        changeFragment(new VideoFragment());
    }

    private void setView() {
        btn_back = (TextView) findViewById(R.id.btn_back);
        mdrawerlayout = (DrawerLayout) findViewById(R.id.mdrawerlayout_video);
        video_title = (TextView) findViewById(R.id.video_title);
        base_sliding = findViewById(R.id.base_sliding);
        title_head = (ImageView) findViewById(R.id.title_head_video);
        tab_1 = findViewById(R.id.tab_1);
        tab_2 = findViewById(R.id.tab_2);
        tab_3 = findViewById(R.id.tab_3);
        checked = tab_1;

        tab_1_img = (ImageView) findViewById(R.id.tab_1_img);
        tab_2_img = (ImageView) findViewById(R.id.tab_2_img);
        tab_3_img = (ImageView) findViewById(R.id.tab_3_img);

        setSlidingView();
    }

    private void setSlidingView() {
        sliding_name = (TextView) findViewById(R.id.sliding_name);
        sliding_community = (TextView) findViewById(R.id.sliding_community);
        sliding_house = (TextView) findViewById(R.id.sliding_house);
        sliding_code = findViewById(R.id.sliding_code);

        sliding_memberManage = (TextView) findViewById(R.id.sliding_memberManage);
        sliding_information = findViewById(R.id.sliding_information);
        sliding_memberCheck = findViewById(R.id.sliding_memberCheck);
        sliding_setting = findViewById(R.id.sliding_setting);
        sliding_help = findViewById(R.id.sliding_help);

        sliding_code.setOnClickListener(this);
        sliding_memberManage.setOnClickListener(this);
        sliding_information.setOnClickListener(this);
        sliding_memberCheck.setOnClickListener(this);
        sliding_setting.setOnClickListener(this);
        sliding_help.setOnClickListener(this);

    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        title_head.setOnClickListener(this);
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (checked == v) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_head_video:
                mdrawerlayout.openDrawer(base_sliding);
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.tab_1:
                changeFragment(new VideoFragment());
                tab_1_img.setImageResource(R.drawable.video_tab1_on);
                tab_2_img.setImageResource(R.drawable.video_tab2_off);
                tab_3_img.setImageResource(R.drawable.video_tab3_off);
                video_title.setText(R.string.main_bottom_tab1);
                checked = v;
                break;
            case R.id.tab_2:
                changeFragment(new RecordFragment());
                tab_1_img.setImageResource(R.drawable.video_tab1_off);
                tab_2_img.setImageResource(R.drawable.video_tab2_on);
                tab_3_img.setImageResource(R.drawable.video_tab3_off);
                video_title.setText(R.string.main_bottom_tab2);
                checked = v;
                break;
            case R.id.tab_3:
                changeFragment(new PictureFragment());
                tab_1_img.setImageResource(R.drawable.video_tab1_off);
                tab_2_img.setImageResource(R.drawable.video_tab2_off);
                tab_3_img.setImageResource(R.drawable.video_tab3_on);
                video_title.setText(R.string.main_bottom_tab3);
                checked = v;
                break;
            case R.id.sliding_information:
                if (!MyApplication.logined || MyApplication.cellphone == null) {
                    ToastUtil.s(this, "您当前没有登录！");
                    return;
                }
                Intent sliding1 = new Intent(this, MyActivity.class);
                startActivity(sliding1);
                break;
            case R.id.sliding_code:
                Intent sliding2 = new Intent(this, CodeActivity.class);
                sliding2.putExtra("houseId", MyApplication.houseProperty.houseId);
                startActivity(sliding2);
                break;
            case R.id.sliding_memberManage:
                if (MyApplication.houseProperty != null) {
                    Intent sliding3 = new Intent(this, MemberManageActivity.class);
                    startActivity(sliding3);
                } else {
                    ToastUtil.s(this, "您当前还没有房产，请先绑定房产！");
                }
                break;
            case R.id.sliding_memberCheck:
                if (MyApplication.houseProperty == null || MyApplication.houseProperty.userType == 3) {
                    ToastUtil.s(this, "您当前没有足够的权限！");
                    return;
                }
                Intent sliding5 = new Intent(this, MemberCheckActivity.class);
                startActivity(sliding5);
                break;
            case R.id.sliding_setting:
                Intent sliding6 = new Intent(this, SettingActivity.class);
                startActivity(sliding6);
                break;
            case R.id.sliding_help:
                ToastUtil.s(this, "建设中，敬请期待！");
                break;
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_flayout, fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mdrawerlayout.isDrawerOpen(findViewById(R.id.base_sliding))) {
                mdrawerlayout.closeDrawers();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
