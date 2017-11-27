package cn.etsoft.smarthome.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.NetWorkListener.AppNetworkMgr;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.etsoft.smarthome.Activity.Settings.ConfigPassActivity;
import cn.etsoft.smarthome.Activity.Settings.NewWorkSetActivity;
import cn.etsoft.smarthome.Adapter.ListView.HomeRoomTempAdapter;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.LogoutHelper;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Utils.PermissionsUtli;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.LinearLayout.BamLinearLayout;

/**
 * Author：FBL  Time： 2017/11/24.
 * 主页之方案二（简洁式主页）
 */

public class HomeActivity_Play2 extends Activity implements View.OnClickListener {

    /**
     * 20：00
     */
    private TextView mHomeTime;

    private TextView mTitleName, mDialogName,
            mDialogCancle, mDialogOk, mTitle, mDialoghelp;
    /**
     * 2017年11月24日
     */
    private TextView mHomeData;
    /**
     * 星期二
     */
    private TextView mHomeWeek, home_play2_net_ok;
    private ListView mHomePlay2Roomtemp;
    private ImageView mHomePlay2Ref;
    private ImageView mHomePlay2Logout;
    private BamLinearLayout mHomePlay2Netset;
    private ImageView mElevatorUp;
    private ImageView mElevatorDown;
    private BamLinearLayout mHomePlay2Video;
    private BamLinearLayout mHomePlay2User;
    private BamLinearLayout mHomePlay2Safety;
    private BamLinearLayout mHomePlay2Appliances;
    private BamLinearLayout mHomePlay2Scene;
    private BamLinearLayout mHomePlay2Setting;
    private Time time;
    private boolean isLogout;
    private HomeRoomTempAdapter mHomeRoomTempAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_play2_activity);
        PermissionsUtli.verifyStoragePermissions(HomeActivity_Play2.this);
        MyApplication.addActivity(this);
        MyApplication.mApplication.setmHomeActivity(this);
        initView();

        initData();

        //网络改变监听
        BaseActivity.setGetNetChangeListener(new BaseActivity.getNetChangeListener() {
            @Override
            public void NetChange() {
                getIp();
                GlobalVars.setIsLAN(true);
                SendDataUtil.getNetWorkInfo();
            }
        });
    }


    @Override
    protected void onResume() {
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 3 || datType == 8)
                    MyApplication.mApplication.dismissLoadDialog();
                if (datType == 68) {
                    initRoomTemp();
                }
            }
        });
        initRoomTemp();
        super.onResume();
        if (!"".equals(AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, ""))) {
            List<RcuInfo> list = MyApplication.mApplication.getRcuInfoList();
            for (int i = 0; i < list.size(); i++) {
                if (AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, "")
                        .equals(list.get(i).getDevUnitID())) {
                    if ("null".equals(list.get(i).getCanCpuName()) || "".equals(list.get(i).getCanCpuName())) {
                        home_play2_net_ok.setText(list.get(i).getName());
                    } else {
                        home_play2_net_ok.setText(list.get(i).getCanCpuName());
                    }
                    if ("".equals(home_play2_net_ok.getText().toString())) {
                        home_play2_net_ok.setText("点击设置模块");
                    }
                }
            }
        } else {
            home_play2_net_ok.setText("点击设置模块");
        }
    }

    private void initData() {
        time = new Time();
        time.setToNow();
        int min = time.minute;
        String mins = "";
        if (min < 10)
            mins = "0" + min;
        else mins = min + "";
        mHomeTime.setText(time.hour + ":" + mins);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mHomeTime.setText(msg.obj + "");
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        Thread.sleep(5000);
                        Message message = handler.obtainMessage();
                        time.setToNow();
                        int min = time.minute;
                        String mins = "";
                        if (min < 10)
                            mins = "0" + min;
                        else mins = min + "";
                        message.obj = time.hour + "：" + mins;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();


        mHomeData.setText(time.year + "年" + (time.month + 1) + "月" + time.monthDay + "日");

        if (time.weekDay == 0)
            mHomeWeek.setText("星期日");
        else if (time.weekDay == 1)
            mHomeWeek.setText("星期一");
        else if (time.weekDay == 2)
            mHomeWeek.setText("星期二");
        else if (time.weekDay == 3)
            mHomeWeek.setText("星期三");
        else if (time.weekDay == 4)
            mHomeWeek.setText("星期四");
        else if (time.weekDay == 5)
            mHomeWeek.setText("星期五");
        else if (time.weekDay == 6)
            mHomeWeek.setText("星期六");


    }

    private void initView() {
        mHomeTime = (TextView) findViewById(R.id.home_time);
        mHomeData = (TextView) findViewById(R.id.home_data);
        mHomeWeek = (TextView) findViewById(R.id.home_week);
        home_play2_net_ok = (TextView) findViewById(R.id.home_play2_net_ok);
        mHomePlay2Roomtemp = (ListView) findViewById(R.id.home_play2_roomtemp);
        mHomePlay2Ref = (ImageView) findViewById(R.id.home_play2_ref);
        mHomePlay2Ref.setOnClickListener(this);
        mHomePlay2Logout = (ImageView) findViewById(R.id.home_play2_logout);
        mHomePlay2Logout.setOnClickListener(this);
        mHomePlay2Netset = (BamLinearLayout) findViewById(R.id.home_play2_netset);
        mHomePlay2Netset.setOnClickListener(this);
        mElevatorUp = (ImageView) findViewById(R.id.elevator_up);
        mElevatorUp.setOnClickListener(this);
        mElevatorDown = (ImageView) findViewById(R.id.elevator_down);
        mElevatorDown.setOnClickListener(this);
        mHomePlay2Video = (BamLinearLayout) findViewById(R.id.home_play2_video);
        mHomePlay2Video.setOnClickListener(this);
        mHomePlay2User = (BamLinearLayout) findViewById(R.id.home_play2_user);
        mHomePlay2User.setOnClickListener(this);
        mHomePlay2Safety = (BamLinearLayout) findViewById(R.id.home_play2_safety);
        mHomePlay2Safety.setOnClickListener(this);
        mHomePlay2Appliances = (BamLinearLayout) findViewById(R.id.home_play2_appliances);
        mHomePlay2Appliances.setOnClickListener(this);
        mHomePlay2Scene = (BamLinearLayout) findViewById(R.id.home_play2_scene);
        mHomePlay2Scene.setOnClickListener(this);
        mHomePlay2Setting = (BamLinearLayout) findViewById(R.id.home_play2_setting);
        mHomePlay2Setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.home_play2_ref:
                GlobalVars.IsclearCache = 0;
                SendDataUtil.getNetWorkInfo();
                MyApplication.mApplication.showLoadDialog(this);
                break;
            case R.id.home_play2_logout:
                LogoutHelper.logout(HomeActivity_Play2.this);
                isLogout = true;
                break;
            case R.id.home_play2_netset:
                startActivity(new Intent(HomeActivity_Play2.this, NewWorkSetActivity.class));
                break;
            case R.id.elevator_up:
                ToastUtil.showText("找不到以关联电梯");
                break;
            case R.id.elevator_down:
                ToastUtil.showText("找不到以关联电梯");
                break;
            case R.id.home_play2_video:
                if (MyApplication.mApplication.isVisitor()) {
                    ToastUtil.showText("这里您不可以操作哦~");
                    return;
                }
                startActivity(new Intent(HomeActivity_Play2.this, cn.semtec.community2.WelcomeActivity.class));
                break;
            case R.id.home_play2_user:
                if (MyApplication.mApplication.isVisitor()) {
                    ToastUtil.showText("这里您不可以操作哦~");
                    return;
                }
                startActivity(new Intent(HomeActivity_Play2.this, UserInterface.class));
                break;
            case R.id.home_play2_safety:
                startActivity(new Intent(HomeActivity_Play2.this, SafetyHomeActivity.class));
                break;
            case R.id.home_play2_appliances:
                startActivity(new Intent(HomeActivity_Play2.this, ControlActivity.class));
                break;
            case R.id.home_play2_scene:
                if ("".equals(GlobalVars.getDevid())) {
                    ToastUtil.showText("没有联网模块");
                    return;
                }
                startActivity(new Intent(HomeActivity_Play2.this, ControlSceneActivity.class));
                break;
            case R.id.home_play2_setting:
                if (MyApplication.mApplication.isVisitor()) {
                    ToastUtil.showText("这里您不可以操作哦~");
                    return;
                }
                if ("".equals(GlobalVars.getDevid())) {
                    ToastUtil.showText("没有联网模块");
                    return;
                }
                if (Condition()) return;
                Intent intent = new Intent(HomeActivity_Play2.this, SettingActivity.class);
                if (MyApplication.mApplication.isInputPass) {
                    startActivity(intent);
                } else InputPass(intent);
                break;
        }
    }

    public void getIp() {
        int NETWORK = AppNetworkMgr.getNetworkState(MyApplication.mContext);
        if (NETWORK >= 10) {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            if (!ip.equals(GlobalVars.WIFI_IP)) {
                GlobalVars.WIFI_IP = ip;
            }
        }
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    /**
     * 判断是否处于局域网
     *
     * @return
     */
    private boolean Condition() {
        if (!GlobalVars.isIsLAN() || !MyApplication.mApplication.isStartTimeOk) {
            ToastUtil.showText("请先切换到该局域网下的联网模快");
            return true;
        }
        return false;
    }

    /**
     * 配置密码认证
     *
     * @param intent
     */
    public void InputPass(final Intent intent) {

        final Dialog dialog = new Dialog(HomeActivity_Play2.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        dialog.setContentView(R.layout.dialog_addscene);
        dialog.show();
        mDialogName = (EditText) dialog.findViewById(R.id.dialog_addScene_name);
        mDialogCancle = (TextView) dialog.findViewById(R.id.dialog_addScene_cancle);
        mDialogOk = (TextView) dialog.findViewById(R.id.dialog_addScene_ok);
        mTitleName = (TextView) dialog.findViewById(R.id.title_name);
        mTitle = (TextView) dialog.findViewById(R.id.title);
        mDialoghelp = (TextView) dialog.findViewById(R.id.dialog_help);
        mDialoghelp.setVisibility(View.VISIBLE);
        mDialogName.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mTitle.setText("配置密码");
        mDialogName.setHint("请输入当前配置密码");
        mDialogName.setTextSize(14);
        mTitleName.setText("模块密码 :");
        mDialogOk.setText("确定");
        mDialogCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mDialogName.getText().toString().equals(
                        AppSharePreferenceMgr.get(GlobalVars.CONFIG_PASS_SHAREPREFERENCE, ""))) {
                    MyApplication.mApplication.isInputPass = true;
                    startActivity(intent);
                } else {
                    ToastUtil.showText("对不起，密码输入不匹配哦");
                }
            }
        });
        mDialoghelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity_Play2.this, ConfigPassActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.finishActivity(this);
        MyApplication.mApplication.isInputPass = false;
        if (!isLogout && !MyApplication.mApplication.isVisitor()) {
            System.exit(0);
        }
    }

    /**
     * 初始化主页房间温度湿度数据
     */
    public void initRoomTemp() {
        if (mHomeRoomTempAdapter == null) {
            mHomeRoomTempAdapter = new HomeRoomTempAdapter(this);
            mHomePlay2Roomtemp.setAdapter(mHomeRoomTempAdapter);
        } else mHomeRoomTempAdapter.notifyDataSetChanged();
    }
}
