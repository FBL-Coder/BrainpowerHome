package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.NetWorkListener.AppNetworkMgr;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.etsoft.smarthome.Activity.Settings.ConfigPassActivity;
import cn.etsoft.smarthome.Activity.Settings.NewWorkSetActivity;
import cn.etsoft.smarthome.Domain.City;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.Weather_All_Bean;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.HomeWeatherAnim;
import cn.etsoft.smarthome.UiHelper.Home_Weather;
import cn.etsoft.smarthome.UiHelper.LogoutHelper;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Utils.PermissionsUtli;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.LinearLayout.BamLinearLayout;
import cn.etsoft.smarthome.View.MarqueeTextView;

/**
 * Author：FBL  Time： 2017/6/20.
 * 主页页面
 */

public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private int LOCATION_SUCCEED = 100;
    private int LOCATION_FAILING = 111;
    private int LOCATION_NONETWORK = 1001;
    private int WRATHER_SUCCEED = 2000;
    private int WRATHER_FAILING = 2200;
    private int DB_INITOK = 200;

    private TextView mHomeLoactionText, mTitleName, mDialogName,
            mDialogCancle, mDialogOk, mTitle, mDialoghelp;
    private LinearLayout mHomeLoaction;
    private ImageView mHomeRefBtn, mHomeLogoutBtn;
    private TextView mHomeWeatherTemp, mHomeWeatherType, mHomeWeatherShidu,
            mHomeWeatherFengli, mHomeWeatherZhiliang, mNetWork_Ok, weather_no;
    private MarqueeTextView mHomeWeatherTishi;
    private RelativeLayout mHome_weather, mHome_weather_relativelayout;
    private Home_Weather weather_helper;
    private HomeHandler mHandler = new HomeHandler(HomeActivity.this);
    private HomeWeatherAnim mHomeWeatherAnim;
    private BamLinearLayout mHome_YunVideo, mHome_Safety, mHome_JiaDian, mHome_State, mHome_Scene,
            mHome_Timer, mHome_Health, mHome_Setting;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //添加Activity在ActivityList中
        PermissionsUtli.verifyStoragePermissions(HomeActivity.this);
        MyApplication.addActivity(this);
        MyApplication.mApplication.setmHomeActivity(this);
        initView();
        initData();
        //网络改变监听
        BaseActivity.setGetNetChangeListener(new BaseActivity.getNetChangeListener() {
            @Override
            public void NetChange() {
                GlobalVars.setIsLAN(true);
                getIp();
            }
        });
    }


    private void initView() {
        mHome_weather_relativelayout = (RelativeLayout) findViewById(R.id.home_weather_relativelayout);
        mHomeLoactionText = (TextView) findViewById(R.id.home_loaction_text);
        mHomeLoaction = (LinearLayout) findViewById(R.id.home_loaction);
        mHomeRefBtn = (ImageView) findViewById(R.id.home_ref_btn);
        mHomeLogoutBtn = (ImageView) findViewById(R.id.home_logout_btn);
        mHomeWeatherTemp = (TextView) findViewById(R.id.home_weather_temp);
        mHomeWeatherType = (TextView) findViewById(R.id.home_weather_type);
        mHomeWeatherShidu = (TextView) findViewById(R.id.home_weather_shidu);
        mHomeWeatherFengli = (TextView) findViewById(R.id.home_weather_fengli);
        mHomeWeatherZhiliang = (TextView) findViewById(R.id.home_weather_zhiliang);
        mNetWork_Ok = (TextView) findViewById(R.id.NetWork_Ok);
        weather_no = (TextView) findViewById(R.id.weather_no);
        mHomeWeatherTishi = (MarqueeTextView) findViewById(R.id.home_weather_tishi);
        mHome_weather = (RelativeLayout) findViewById(R.id.home_weather);
        mHomeRefBtn.setOnClickListener(this);
        mHomeLogoutBtn.setOnClickListener(this);
        mHomeLoaction.setOnClickListener(this);
        mHomeWeatherAnim = new HomeWeatherAnim();
        mHomeWeatherAnim.initAnimWeather(HomeActivity.this, R.layout.activity_home);

        mHome_YunVideo = (BamLinearLayout) findViewById(R.id.Home_YunVideo);
        mHome_YunVideo.setOnClickListener(this);
        mHome_Safety = (BamLinearLayout) findViewById(R.id.Home_Safety);
        mHome_Safety.setOnClickListener(this);
        mHome_JiaDian = (BamLinearLayout) findViewById(R.id.Home_JiaDian);
        mHome_JiaDian.setOnClickListener(this);
//        mHome_State = (BamLinearLayout) findViewById(R.id.Home_State);
//        mHome_State.setOnClickListener(this);
        mHome_Scene = (BamLinearLayout) findViewById(R.id.Home_Scene);
        mHome_Scene.setOnClickListener(this);
//        mHome_Timer = (BamLinearLayout) findViewById(R.id.Home_Timer);
//        mHome_Timer.setOnClickListener(this);
//        mHome_Health = (BamLinearLayout) findViewById(R.id.Home_Health);
//        mHome_Health.setOnClickListener(this);
        mHome_Setting = (BamLinearLayout) findViewById(R.id.Home_Setting);
        mHome_Setting.setOnClickListener(this);
        mNetWork_Ok.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!"".equals(AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, ""))) {
            List<RcuInfo> list = MyApplication.mApplication.getRcuInfoList();
            for (int i = 0; i < list.size(); i++) {
                if (AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, "")
                        .equals(list.get(i).getDevUnitID())) {
                    mNetWork_Ok.setText("使用中的模块\n" + list.get(i).getCanCpuName());
                }
            }
        }
    }

    /**
     * 初始化转盘数据
     */
    private void initData() {
        weather_helper = new Home_Weather(mHandler, getApplicationContext());
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 3)
                    MyApplication.mApplication.dismissLoadDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_ref_btn:
                SendDataUtil.getNetWorkInfo();
                break;
            case R.id.home_logout_btn:
                LogoutHelper.logout(HomeActivity.this);
                break;
            case R.id.Home_YunVideo:
                startActivity(new Intent(HomeActivity.this, cn.semtec.community2.WelcomeActivity.class));
                break;
            case R.id.Home_Safety:
                startActivity(new Intent(HomeActivity.this, SafetyHomeActivity.class));
                break;
            case R.id.Home_JiaDian:
                startActivity(new Intent(HomeActivity.this, ControlActivity.class));
                break;
//            case R.id.Home_State:
//                break;
            case R.id.Home_Scene:
                startActivity(new Intent(HomeActivity.this, ControlSceneActivity.class));
                break;
//            case R.id.Home_Timer:
//                SendDataUtil.getTimerInfo();
//                startActivity(new Intent(HomeActivity.this, TimerInfoActivity.class));
//                break;
//            case R.id.Home_Health:
//                break;
            case R.id.Home_Setting:
//                if (Condition()) return;
                InputPass(new Intent(HomeActivity.this, SettingActivity.class));
                break;
            case R.id.NetWork_Ok:
                startActivity(new Intent(HomeActivity.this, NewWorkSetActivity.class));
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
     * 防止Handler导致的内存溢出
     */
    static class HomeHandler extends Handler {
        WeakReference<HomeActivity> weakReference;

        int cunt = 0;

        public HomeHandler(HomeActivity activity) {
            weakReference = new WeakReference<HomeActivity>(activity);
        }

        @SuppressLint({"WrongConstant", "SetTextI18n"})
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference != null) {
                if (msg.what == weakReference.get().LOCATION_SUCCEED) {//定位成功
                    City mCity = (City) msg.obj;
                    weakReference.get().mHomeLoactionText.setText(mCity.getCity());
                }
                if (msg.what == weakReference.get().LOCATION_FAILING) {//定位失败
                    weakReference.get().mHomeLoactionText.setText("北京市");
                }
                if (msg.what == weakReference.get().LOCATION_NONETWORK) {//没有网络

                }
                if (msg.what == weakReference.get().WRATHER_FAILING) {//天气数据失败
                    ToastUtil.showText("获取天气数据失败");
                    weakReference.get().weather_no.setVisibility(View.VISIBLE);
                }
                if (msg.what == weakReference.get().DB_INITOK) {//获取CityDB数据完成
                    //开始定位，获取时间，获取天气
                    weakReference.get().weather_helper.initWeatherData();
                }
                if (msg.what == weakReference.get().WRATHER_SUCCEED) {//天气数据成功返回

                    weakReference.get().mHome_weather_relativelayout.setVisibility(View.VISIBLE);
                    try {
                        Weather_All_Bean results = (Weather_All_Bean) msg.obj;
                        Message weathermsg = new Message();
                        Bundle b = new Bundle();
                        //天气动画消息
                        weathermsg.what = Integer.parseInt(results.getResult().getImg());
//                    weathermsg.what = 0;
                        weathermsg.setData(b);
                        HomeWeatherAnim.forecastHandler.sendMessage(weathermsg);
                        String text = results.getResult().getWeather();
                        String temp = results.getResult().getTemp();
                        String shidu = "";
                        if (results.getResult().getHumidity().contains("%"))
                            shidu = results.getResult().getHumidity();
                        else shidu = results.getResult().getHumidity() + "%";
                        String pm = results.getResult().getAqi().getPm2_5();
                        String zhiliang = results.getResult().getAqi().getQuality();
                        String kongqi = results.getResult().getAqi().getAqiinfo().getAffect();
                        String kongtiao = results.getResult().getIndex().get(0).getDetail();
                        String yundong = results.getResult().getIndex().get(1).getDetail();
                        String fengli = results.getResult().getWindpower();
                        weakReference.get().mHomeWeatherShidu.setText("湿度：" + shidu);
                        weakReference.get().mHomeWeatherZhiliang.setText(pm + "     " + zhiliang);
                        weakReference.get().mHomeWeatherTemp.setText(temp + " ℃");
                        weakReference.get().mHomeWeatherType.setText(text);
                        weakReference.get().mHomeWeatherFengli.setText("风力：" + fengli);
                        weakReference.get().mHomeWeatherTishi.setText("空气：" + kongqi + "     空调：" + kongtiao + "      运动：" + yundong);
                    } catch (Exception e) {
                        if (cunt > 1)
                            return;
                        weakReference.get().weather_helper = new Home_Weather(weakReference.get().mHandler, MyApplication.getContext());
                        cunt++;
                    }
                }
            }
        }
    }

    /**
     * 判断是否处于局域网
     *
     * @return
     */
    private boolean Condition() {
        if (!GlobalVars.isIsLAN()) {
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
        final Dialog dialog = new Dialog(HomeActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
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
                    startActivity(intent);
                } else {
                    ToastUtil.showText("对不起，密码输入不匹配哦");
                }
            }
        });
        mDialoghelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ConfigPassActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.finishActivity(this);
    }
}
