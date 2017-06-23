package cn.etsoft.smarthome.Activity;

import android.content.Intent;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.NewHttpPort;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/13.
 */

public class WelcomeActivity extends BaseActivity {

    private List<RcuInfo> mRcuInfos;

    @Override
    public void initView() {
        setLayout(R.layout.activity_welcome);
    }

    @Override
    public void initData() {
        setStatusColor(0xffffffff);

        String json_RcuInfolist = (String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, "");

        String json_RcuinfoID = (String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, "");

        String UserID = (String) AppSharePreferenceMgr.get(GlobalVars.USERID_SHAREPREFERENCE, "");


//        startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
//        finish();

        if ("".equals(UserID)) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }
        if (!"".equals(UserID) && "".equals(json_RcuinfoID)) {
            startActivity(new Intent(WelcomeActivity.this, SettingActivity.class));
            finish();
        }
        if (!"".equals(UserID) && !"".equals(json_RcuinfoID)) {
            SendDataUtil.getNetWorkInfo();
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        }
    }
}
