package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.RcuInfo;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;

/**
 * 作者：FBL  时间： 2016/8/31.
 */
public class WelcomeActivity extends Activity {

    private List<RcuInfo> mRcuInfos;
    private String TAG = "WelCome";
    private Handler mHandler, mDataHandler;
    private int OUTTIME_DOWNLOAD = 1111;
    private int OUTTIME_INITUID = 1000;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.mInstance.setActivity(this);
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData() {
            }
        });
        SharedPreferences sharedPreferences = this.getSharedPreferences("FirstRun", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        mRcuInfos = getGwList();
        if (mRcuInfos != null && mRcuInfos.size() > 0) {

            GlobalVars.setDevid(mRcuInfos.get(0).getDevUnitID());
            GlobalVars.setDevpass(mRcuInfos.get(0).getDevUnitPass());

            mDataHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == OUTTIME_INITUID) {
                        MyApplication.setRcuDevIDtoLocal();
                    }
                    if (msg.what == OUTTIME_DOWNLOAD) {
                        MyApplication.getRcuInfo();
                    }
                    super.handleMessage(msg);
                }
            };

            MyApplication.mInstance.setAllHandler(mDataHandler);

            if (isFirstRun) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (count == 1) {
                            count++;
                            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                            finish();
                        }
                    }
                };
                MyApplication.mInstance.setHandler(mHandler);
                editor.putBoolean("isFirstRun", false);
                editor.commit();
                GlobalVars.setDevid(mRcuInfos.get(0).getDevUnitID());
                GlobalVars.setDevpass(mRcuInfos.get(0).getDevUnitPass());

                MyApplication.getRcuInfo();
            } else {
                mHandler = new Handler();
                MyApplication.mInstance.setHandler(mHandler);
                MyApplication.setWareData((WareData) Dtat_Cache.readFile());
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));

                GlobalVars.setDevid(mRcuInfos.get(0).getDevUnitID());
                GlobalVars.setDevpass(mRcuInfos.get(0).getDevUnitPass());

                MyApplication.getRcuInfo();
            }
        } else {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }
    }

    private List<RcuInfo> getGwList() {
        SharedPreferences sharedPreferences = getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        String jsondata = sharedPreferences.getString("list", "null");
        Gson gson = new Gson();
        if (!jsondata.equals("null")) {
            List<RcuInfo> list = gson.fromJson(jsondata,
                    new TypeToken<List<RcuInfo>>() {
                    }.getType());
            for (int i = 0; i < list.size(); i++) {
                RcuInfo p = list.get(i);
                System.out.println(p.getName());
            }
            return list;
        } else {
            return null;
        }
    }
}
