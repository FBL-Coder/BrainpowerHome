package cn.etsoft.smarthome.Activity;

import android.content.Intent;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.R;

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

        String json_RcuInfo = (String) AppSharePreferenceMgr.get("RcuInfo", "");

        startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
        finish();

//        Gson gson = new Gson();
//        if (!json_RcuInfo.equals("")) {
//            mRcuInfos = gson.fromJson(json_RcuInfo, new TypeToken<List<RcuInfo>>() {
//            }.getType());
//        }
//
//        if (mRcuInfos.size() == 0){
//
//
//
//        }else if (mRcuInfos.size() == 1){
//
//        }else {
//
//        }
    }
}
