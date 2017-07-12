package cn.etsoft.smarthome.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/13.
 * 欢迎界面
 */

public class WelcomeActivity extends BaseActivity {

    private List<RcuInfo> mRcuInfos;
    private WelcomeHandler welcomeHandler = new WelcomeHandler(this);

    @Override
    public void initView() {
        setLayout(R.layout.activity_welcome);
    }

    @Override
    public void initData() {
        setStatusColor(0xffffffff);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                welcomeHandler.sendMessage(welcomeHandler.obtainMessage());
            }
        }).start();
    }



    static class WelcomeHandler extends Handler {
        WeakReference<WelcomeActivity> weakReference;

        WelcomeHandler(WelcomeActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference != null) {

                String json_RcuInfolist = (String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, "");

                String json_RcuinfoID = (String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, "");

                String UserID = (String) AppSharePreferenceMgr.get(GlobalVars.USERID_SHAREPREFERENCE, "");

                if ("".equals(UserID)) {
                    weakReference.get().startActivity(new Intent(weakReference.get(), LoginActivity.class));
                    weakReference.get().finish();
                }
                if (!"".equals(UserID) && "".equals(json_RcuinfoID)) {
                    weakReference.get().startActivity(new Intent(weakReference.get(), SettingActivity.class));
                    weakReference.get().finish();
                }
                if (!"".equals(UserID) && !"".equals(json_RcuinfoID)) {
                    SendDataUtil.getNetWorkInfo();
                    weakReference.get().startActivity(new Intent(weakReference.get(), HomeActivity.class));
                    weakReference.get().finish();
                }
            }
        }
    }
}
