package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.etsoft.smarthome.Domain.City;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.Weather_All_Bean;
import cn.etsoft.smarthome.Fragment.ControlDev_Home_Fragment;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Home_Weather;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.PageView.BezierViewPager;
import cn.etsoft.smarthome.View.PageView.CardPagerAdapter;

/**
 * Author：FBL  Time： 2017/6/14.
 */
public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private int LOCATION_SUCCEED = 100;
    private int LOCATION_FAILING = 111;
    private int LOCATION_NONETWORK = 1001;
    private int WRATHER_SUCCEED = 2000;
    private int DB_INITOK = 200;
    private int TIME_UPDATA = 10;
    //left
    private TextView date, week, time, weather_shidu, weather_pm,
            weather_temp2, weather_zhiliang, text_temp, weather_info,
            loaction_text, setting, home_tv_ref, video, user;
    private ImageView weather_iv, edit_loaction;

    private Home_Weather weather_helper;
    private HomeHandler mHandler = new HomeHandler(this);

    //right
    private PageIndicatorView mPageIndicatorView;
    private BezierViewPager mBezierViewPager;
    private CardPagerAdapter mPageAdapter;
    private RadioGroup mRadioGroup;
    private RelativeLayout mRelativelayout_home;
    private ControlDev_Home_Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initLeftView();
        initRightView();
        initData();
    }

    /**
     * 初始化右边视图
     */
    private void initRightView() {
        mBezierViewPager = (BezierViewPager) findViewById(R.id.view_page_home);
        mPageIndicatorView = (PageIndicatorView) findViewById(R.id.pageindicatorview_home);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_home);
        mRelativelayout_home = (RelativeLayout) findViewById(R.id.relativelayout_home);
        DataChange();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (onRadioButtonSelectedListener != null)
                    switch (checkedId) {
                        case R.id.light:
                            onRadioButtonSelectedListener.getRadioButtonSelected("灯光");
                            break;
                        case R.id.scene:
                            onRadioButtonSelectedListener.getRadioButtonSelected("情景");
                            break;
                        case R.id.curtain:
                            onRadioButtonSelectedListener.getRadioButtonSelected("窗帘");
                            break;
                        case R.id.appliance:
                            onRadioButtonSelectedListener.getRadioButtonSelected("家电");
                            break;
                        case R.id.spile:
                            onRadioButtonSelectedListener.getRadioButtonSelected("插座");
                            break;
                        case R.id.doorlock:
                            onRadioButtonSelectedListener.getRadioButtonSelected("门锁");
                            break;
                        case R.id.safety:
                            onRadioButtonSelectedListener.getRadioButtonSelected("安防");
                            break;
                        case R.id.monitoring:
                            onRadioButtonSelectedListener.getRadioButtonSelected("监控");
                            break;
                    }
            }
        });
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mFragment = new ControlDev_Home_Fragment();
        transaction.replace(R.id.relativelayout_home, mFragment);
        transaction.commit();
    }

    /**
     * 数据变化后需要页面数据刷新
     */
    public void DataChange() {
        if (mPageAdapter == null)
            mPageAdapter = new CardPagerAdapter(this);
        if (MyApplication.getWareData().getRooms().size()<1)
            return;
        int mWidth = getWindowManager().getDefaultDisplay().getWidth();
        int maxFactor = mWidth / 40;
        mPageAdapter.setMaxElevationFactor(maxFactor);
        mPageAdapter.setData(MyApplication.getWareData().getRooms(),createPageList());
        mBezierViewPager.showTransformer(0.2f);
        mBezierViewPager.setAdapter(mPageAdapter);
        mBezierViewPager.setClipToPadding(false);
        mBezierViewPager.setPadding(100,-30,100,-30);
        mPageIndicatorView.setVisibility(View.VISIBLE);
        mPageIndicatorView.setViewPager(mBezierViewPager);
        mPageIndicatorView.setAnimationType(AnimationType.FILL);
        mBezierViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (onPageSelectedListener != null)
                    onPageSelectedListener.getPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @NonNull
    private List<Object> createPageList() {
       List<Object> imgList = new ArrayList<>();
        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490984457722&di=6d7d3e20e07fc833fc606089d01132e6&imgtype=0&src=http%3A%2F%2Fimgst.izhangheng.com%2F2016%2F08%2Fnight-beauty-girl-3.jpg");
        imgList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2946550071,381041431&fm=11&gp=0.jpg");
        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490984320392&di=8290126f83c2a2c0d45be41e3f88a6d0&imgtype=0&src=http%3A%2F%2Ffile.mumayi.com%2Fforum%2F201307%2F19%2F152440r9ov9ololkzdcz7d.jpg");
        return imgList;
    }

    /**
     * 初始化左边区域视图
     */
    private void initLeftView() {
        loaction_text = (TextView) findViewById(R.id.loaction_text);
        edit_loaction = (ImageView) findViewById(R.id.edit_loaction);
        date = (TextView) findViewById(R.id.date);
        week = (TextView) findViewById(R.id.week);
        time = (TextView) findViewById(R.id.time);
        text_temp = (TextView) findViewById(R.id.text_temp);
        weather_info = (TextView) findViewById(R.id.weather_info);
        weather_iv = (ImageView) findViewById(R.id.weather);
        weather_shidu = (TextView) findViewById(R.id.weather_shidu);
        weather_temp2 = (TextView) findViewById(R.id.weather_temp2);
        weather_pm = (TextView) findViewById(R.id.weather_pm);
        weather_zhiliang = (TextView) findViewById(R.id.weather_zhiliang);
        home_tv_ref = (TextView) findViewById(R.id.home_tv_ref);
        setting = (TextView) findViewById(R.id.setting);
        video = (TextView) findViewById(R.id.video);
        user = (TextView) findViewById(R.id.user);
        user.setOnClickListener(this);
        video.setOnClickListener(this);
        edit_loaction.setOnClickListener(this);
        setting.setOnClickListener(this);
        home_tv_ref.setOnClickListener(this);
    }


    public void initData() {
        weather_helper = new Home_Weather(mHandler, this, date, week, weather_iv);

        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getDevsInfo.getValue()) {
                    ToastUtil.showText(MyApplication.getWareData().getDevs().size() + "");
                    DataChange();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user:
                break;
            case R.id.setting:
                MyApplication.getWareData();
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;
            case R.id.home_tv_ref:
                GlobalVars.setDevid("39ffd505484d303408650743");
                MyApplication.mApplication.getUdpServer().send(SendDataUtil.GETNETWORKINFO);
                break;
            case R.id.edit_loaction:
                break;
            case R.id.video:
                break;
        }
    }
    /**
     * 防止Handler导致的内存溢出
     */
    static class HomeHandler extends Handler {
        WeakReference<HomeActivity> weakReference;

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
                    weakReference.get().loaction_text.setText(mCity.getCity());

                }
                if (msg.what == weakReference.get().LOCATION_FAILING) {//定位失败
                    weakReference.get().loaction_text.setText("北京市");
                }
                if (msg.what == weakReference.get().LOCATION_NONETWORK) {//没有网络

                }
                if (msg.what == weakReference.get().TIME_UPDATA) {//刷新时间
                    if (msg.obj != null || "".equals(msg.obj))
                        weakReference.get().time.setText((String) msg.obj);
                    else {
                        Calendar c = Calendar.getInstance();
                        if (c.get(Calendar.MINUTE) < 10) {
                            weakReference.get().time.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + 0 + c.get(Calendar.MINUTE));
                        } else
                            weakReference.get().time.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                    }
                }
                if (msg.what == weakReference.get().DB_INITOK) {//获取CityDB数据完成
                    //开始定位，获取时间，获取天气
                    weakReference.get().weather_helper.initWeatherData();
                }
                if (msg.what == weakReference.get().WRATHER_SUCCEED) {//天气数据成功返回
                    Weather_All_Bean results = (Weather_All_Bean) msg.obj;
                    weakReference.get().weather_helper.initWrather(results);
                    String text = results.getResult().getWeather();
                    String temp = results.getResult().getTemp();
                    String shidu = "";
                    if (results.getResult().getHumidity().contains("%"))
                        shidu = results.getResult().getHumidity();
                    else shidu = results.getResult().getHumidity() + "%";
                    String qujian = results.getResult().getTemplow() + " ~ " + results.getResult().getTemphigh() + "℃";
                    String pm = results.getResult().getAqi().getPm2_5();
                    String zhiliang = results.getResult().getAqi().getQuality();
                    weakReference.get().weather_shidu.setText(shidu);
                    weakReference.get().weather_pm.setText(pm);
                    weakReference.get().weather_temp2.setText(qujian);
                    weakReference.get().weather_zhiliang.setText(zhiliang);
                    weakReference.get().text_temp.setText(temp + " ℃");
                    weakReference.get().weather_info.setText(text);
                }
            }
        }
    }


    public static OnPageSelectedListener onPageSelectedListener;

    public static void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        HomeActivity.onPageSelectedListener = onPageSelectedListener;
    }
    /**
     * 房间变动通知Fragment更新数据 监听
     */
    public interface OnPageSelectedListener {
        void getPageSelected(int RoomPosition);
    }
    public static OnRadioButtonSelectedListener onRadioButtonSelectedListener;

    public static void setOnRadioButtonSelectedListener(OnRadioButtonSelectedListener onRadioButtonSelectedListener) {
        HomeActivity.onRadioButtonSelectedListener = onRadioButtonSelectedListener;
    }

    /**
     * 设备类型变化通知Fragment更新数据 监听
     */
    public interface OnRadioButtonSelectedListener {
        void getRadioButtonSelected(String ControlType);
    }
}
