package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.RcuInfo;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;


/**
 * 作者：FBL  时间： 2016/8/31.
 * 欢迎页面
 */
public class WelcomeActivity extends Activity {

    private List<RcuInfo> mRcuInfos;
    private String TAG = "WelCome";
    private Handler mDataHandler;
    private int OUTTIME_DOWNLOAD = 1111;
    private int OUTTIME_INITUID = 1000;
    private int count = 1;
    private int LOGIN_OK = 1;
    @SuppressWarnings("unused")
    private String versionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish();
//            return;
//        }
        setContentView(R.layout.activity_welcome);
        try {
            //获取屏幕数据
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            // 屏幕宽度（像素）
            cn.semtec.community2.MyApplication.display_width = metric.widthPixels;
            // 屏幕高度（像素）
            cn.semtec.community2.MyApplication.display_height = metric.heightPixels;
            cn.semtec.community2.MyApplication.density = metric.density;
        } catch (Exception e1) {
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在这个页面把welcomeActivity set进去，在其他页面销毁
        MyApplication.mInstance.setActivity(this);
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
            }
        });
        //登录状态
        int login = getIntent().getIntExtra("login", 0);
        //启动服务
        MyApplication.mInstance.startSer();
        SharedPreferences sharedPreferences = getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        String jsonData = sharedPreferences.getString("list", "");
        Gson gson = new Gson();
        if (!jsonData.equals("")) {
            mRcuInfos = gson.fromJson(jsonData, new TypeToken<List<RcuInfo>>() {
            }.getType());
        }
//        {
//            "devUnitID":	"39ffd505484d303408650743",
//                "datType":	0,
//                "subType1":	0,
//                "subType2":	1,
//                "rcu_rows":	[{
//            "canCpuID":	"39ffd505484d303408650743",
//                    "devUnitPass":	"08650743",
//                    "canCpuName":	"西安本地",
//                    "name":	"",
//                    "IpAddr":	"",
//                    "SubMask":	"",
//                    "Gateway":	"",
//                    "centerServ":	"",
//                    "roomNum":	"",
//                    "macAddr":	"",
//                    "SoftVersion":	"",
//                    "HwVersion":	"",
//                    "bDhcp":	0
//        }]
//        }

//        {
//            "devUnitID":	"39ffd905484d303429620443",
//                "datType":	0,
//                "subType1":	0,
//                "subType2":	1,
//                "rcu_rows":	[{
//            "canCpuID":	"39ffd905484d303429620443",
//                    "devUnitPass":	"29620443",
//                    "canCpuName":	"上海",
//                    "name":	"d5b9ccfcff00000000000000",
//                    "IpAddr":	"131.107.1.2",
//                    "SubMask":	"255.255.0.0",
//                    "Gateway":	"131.107.2.155",
//                    "centerServ":	"123.206.104.89",
//                    "roomNum":	"0000",
//                    "macAddr":	"00502a040002",
//                    "SoftVersion":	"",
//                    "HwVersion":	"",
//                    "bDhcp":	0
//        }]
//        }
        //只有一个联网模块信息
        if (mRcuInfos != null && mRcuInfos.size() == 1) {
            GlobalVars.setDevid(mRcuInfos.get(mRcuInfos.size() - 1).getDevUnitID());
            GlobalVars.setDevpass(mRcuInfos.get(mRcuInfos.size() - 1).getDevUnitPass());
            mDataHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == OUTTIME_INITUID) {
                        LogUtils.LOGE("", GlobalVars.getDstip() + "获取数据");
                        MyApplication.setRcuDevIDtoLocal();
                        /**
                         * 局域网内 300秒发送一次数据请求；
                         */
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    for (; ; ) {
//                                        Log.i("TIME", "局域网内数据请求-----------------");
                                        Thread.sleep(300 * 1000);
                                        if (MyApplication.getWareData().getRcuInfos() == null
                                                || MyApplication.getWareData().getRcuInfos().size() == 0) {
                                            GlobalVars.setDstip(MyApplication.LOCAL_IP);
                                        }
                                        MyApplication.setRcuDevIDtoLocal();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }).start();

//                        /**
//                         * 局域网内 30秒发送一次数据请求；
//                         */
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    for (; ; ) {
////                                        Log.i("TIME", "局域网内数据请求-----------------");
//                                        Thread.sleep(30 * 1000);
//                                        if (MyApplication.getWareData().getRcuInfos() == null
//                                                || MyApplication.getWareData().getRcuInfos().size() == 0) {
//                                        }
//                                        MyApplication.getlife();
//                                    }
//                                } catch (Exception e) {
//                                }
//                            }
//                        }).start();
                    }
                    super.handleMessage(msg);
                }
            };
            WareData wareData = new WareData();
            MyApplication.setWareData(wareData);
            MyApplication.mInstance.setAllHandler(mDataHandler);
            WareData wareData_locat = (WareData) Dtat_Cache.readFile(GlobalVars.getDevid());
            if (wareData_locat != null)
                MyApplication.setWareData(wareData_locat);
            MyApplication.mInstance.setRcuInfo(mRcuInfos.get(0));
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        } else if (mRcuInfos != null && mRcuInfos.size() > 1) {
            SharedPreferences sharedPreferences1 = getSharedPreferences("profile",
                    Context.MODE_PRIVATE);
            String module_str = sharedPreferences1.getString("module_str", "");
            if (!"".equals(module_str)) {
                String DevID = module_str.substring(0, module_str.indexOf("-"));
                GlobalVars.setDevid(DevID);
                GlobalVars.setDevpass(module_str.substring(module_str.indexOf("-") + 1));
                //读缓存数据
//

                mDataHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == OUTTIME_INITUID) {
                            LogUtils.LOGE("", GlobalVars.getDstip() + "获取数据");
                            MyApplication.setRcuDevIDtoLocal();
                            /**
                             * 局域网内 300秒发送一次数据请求；
                             */
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        for (; ; ) {
//                                        Log.i("TIME", "局域网内数据请求-----------------");
                                            Thread.sleep(300 * 1000);
                                            if (MyApplication.getWareData().getRcuInfos() == null
                                                    || MyApplication.getWareData().getRcuInfos().size() == 0) {
                                                GlobalVars.setDstip(MyApplication.LOCAL_IP);
                                            }
                                            MyApplication.setRcuDevIDtoLocal();
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }).start();

//                        /**
//                         * 局域网内 30秒发送一次数据请求；
//                         */
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    for (; ; ) {
////                                        Log.i("TIME", "局域网内数据请求-----------------");
//                                        Thread.sleep(30 * 1000);
//                                        if (MyApplication.getWareData().getRcuInfos() == null
//                                                || MyApplication.getWareData().getRcuInfos().size() == 0) {
//                                        }
//                                        MyApplication.getlife();
//                                    }
//                                } catch (Exception e) {
//                                }
//                            }
//                        }).start();
                        }
                        super.handleMessage(msg);
                    }
                };
                for (int i = 0; i < mRcuInfos.size(); i++) {
                    if (DevID.equals(mRcuInfos.get(i).getDevUnitID())) {
                        MyApplication.mInstance.setRcuInfo(mRcuInfos.get(i));
                        break;
                    }
                }
                WareData wareData = new WareData();
                MyApplication.setWareData(wareData);
                MyApplication.mInstance.setAllHandler(mDataHandler);
                WareData wareData_locat = (WareData) Dtat_Cache.readFile(GlobalVars.getDevid());
                if (wareData_locat != null) {
                    MyApplication.setWareData(wareData_locat);
                    MyApplication.mInstance.setReadData(true);
                    if (cn.semtec.community2.MyApplication.getWareData().getDevs().size() > 0) {
                        for (int i = 0; i < cn.semtec.community2.MyApplication.getWareData().getDevs().size(); i++) {
                            Log.e("Exception", cn.semtec.community2.MyApplication.getWareData().getDevs().get(i).getDevName());
                        }
                    }
                }
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                finish();
            } else {
                startActivity(new Intent(WelcomeActivity.this, SettingActivity.class));
                finish();
            }
        } else {
            if (login == LOGIN_OK) {
                startActivity(new Intent(WelcomeActivity.this, SettingActivity.class));
                finish();
            } else {
                startActivity(new Intent(WelcomeActivity.this, cn.semtec.community2.activity.LoginActivity.class));
//                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                finish();
            }
        }
    }

}
