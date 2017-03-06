package cn.semtec.community2.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.linphone.squirrel.squirrelCallImpl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.adapter.DeviceListAdapter;
import cn.semtec.community2.adapter.ViewPagerAdapter;
import cn.semtec.community2.entity.BluetoochEntity;
import cn.semtec.community2.fragment.VideoFragment;
import cn.semtec.community2.model.LoginHelper;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.service.BluetoothLeService;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;
import cn.semtec.community2.util.ToastUtil;
import cn.semtec.community2.util.Util;
import cn.semtec.community2.view.BufferBarAnimation;
import cn.semtec.community2.zxing.activity.CaptureActivity;


public class BaseActivity extends MyBaseActivity implements OnClickListener {
    public static BaseActivity instance;
    private squirrelCallImpl squirrelCall;
    private SharedPreferenceUtil preference;
    private DrawerLayout mdrawerlayout;
    private ImageView title_head;
    private View title_scan;
    private View btn_opendoor;
    private View viewPager_layout;
    private View tab_grid, tab_1, tab_2, tab_3, tab_4, tab_5, tab_6;
    private ViewPager viewPager;
    private View base_sliding;
    private TextView sliding_name;
    private TextView sliding_community;
    private TextView sliding_house;
    private View sliding_code;
    private View sliding_information, sliding_memberCheck, sliding_setting, sliding_help;
    private TextView sliding_memberManage;
    private long exitTime; // 第一次返回键的时间
    public boolean isLoop; // 控制 viewpager 轮询 界面是否显示在最前台
    private ArrayList<ImageView> list; // viewpager 数据
    private ViewPagerAdapter viewPagerAdapter; // viewpager 适配器
    private LinearLayout tv_dot; // viewpager 下标点

    //摇一摇功能
    private SensorManager sensorManager;
    private Vibrator vibrator;

    public static final int SENSOR_SHAKE = 10;
    private static final int END_SHAKE = 11;
    private static final int CLEANANI = 12;
    private static final int DISCONNECTBLE = 13;
    private static final int STARTBLE = 14;
    private static final int STOPBLE = 15;
    private static final int CLOSEBLE = 16;
    private boolean isShake = false;
    private boolean isRegister = false;
    public boolean isDisplayDialog = false;//是否显示列表
    private int  tryNumber = 0;//重连次数
    //	private boolean isLeave = false;//判断人是否离开
//	private int openNumber = 0;//连续开门次数
//	private int notFindNumber = 0;//找不到设备次数
    private boolean isActive = true;
    private Resources re;

    public ArrayList<String> deviceList = new ArrayList<String>();
    public ArrayList<String> deviceNames = new ArrayList<String>();
    //---------Bluetooth start-----------//
    private byte[] sendBuf ;//要发送的开门数据
    private int sendDataLen = 0;
    private int sendIndex = 0;

    private BluetoothLeService mBluetoothLeService;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 2 seconds.
    private static final long SCAN_PERIOD = 2000;
    private String mDeviceName;
    private String mDeviceAddress;
    private DeviceListAdapter mDeviceListAdapter;
    private ImageView bufferBar;
    private boolean openSuccess = false;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    break;
                case 1:
                    startOpenAnimation();
                    break;
                case 2:
                    stopOpenAnimation();
                    break;
                case 3:
                    loginSip();
                    break;
            }
        }
    };

    private void loginSip(){
        if (MyApplication.houseProperty == null) {
            return;
        }
        squirrelCallImpl instan = (squirrelCallImpl) this.getApplication();
        instan.squirrelAccountLogin(MyApplication.houseProperty.sipaddr, squirrelCallImpl.serverport, 1, null,
                MyApplication.houseProperty.sipnum, MyApplication.houseProperty.sippassword, null, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        instance = this;
        preference = MyApplication.getSharedPreferenceUtil();
        squirrelCall = (squirrelCallImpl) getApplication();
        squirrelCall.squirrelSetGlobalVideo(0, 1); // 不发送本地视频，接收远程视频
        if (savedInstanceState != null) {
            loadState(savedInstanceState);
        }
        getDisplay();
        setView();
        setListener();
        re = getResources();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mHandler = new Handler(Looper.getMainLooper());

        initDevices();
        //开启蓝牙service
        startBleService();
        //5分钟登录一次sip账号
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(3);
            }
        }, 1000,5*60*1000);
    }

    protected void startBleService() {
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void initDevices() {
        for (int i = 0;i < VideoFragment.mlist.size();i++){
            String obj_name =  VideoFragment.mlist.get(i).get("obj_name");
            String obj_sipnum = VideoFragment.mlist.get(i).get("obj_sipnum");
            obj_sipnum = obj_sipnum.substring(0, 8)+obj_sipnum.substring(10, 16)+obj_sipnum.substring(obj_sipnum.length()-1);
            deviceList.add(obj_sipnum);
            deviceNames.add(obj_name);
        }
    }

    private void loadState(Bundle bundle) {
        MyApplication.cellphone = bundle.getString("cellphone");
        String password = preference.getString("password");
        LoginHelper loginHelper = new LoginHelper(null);
        loginHelper.loginServer(MyApplication.cellphone, password);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (preference.getBoolean(MyApplication.cellphone + "isAuto")) {
            openScanBlue(false);
//        }
//        if (Util.isWorked(instance)) {
//        stopBleService();
//        }
        isActive = true;
        isLoop = true;
        if (MyApplication.houseProperty != null) {
            StringBuffer name = new StringBuffer();
            if (MyApplication.houseProperty.userName == null || MyApplication.houseProperty.userName.equals("null")
                    || MyApplication.houseProperty.userName.length() < 1) {
                name.append(MyApplication.cellphone);
            } else {
                name.append(MyApplication.houseProperty.userName);
            }
            switch (MyApplication.houseProperty.userType) {
                case 1:
                    name.append("(业主)");
                    break;
                case 2:
                    name.append("(户主)");
                    break;
                case 3:
                    break;
            }
            sliding_name.setText(name);
            sliding_community.setText(MyApplication.houseProperty.communityName+" "+MyApplication.houseProperty.blockName);
            sliding_house.setText(MyApplication.houseProperty.buildName + MyApplication.houseProperty.roomName);
            if (MyApplication.houseProperty.userType != 3) {
                sliding_memberManage.setText("成员管理");
                sliding_code.setVisibility(View.VISIBLE);
            } else {
                sliding_memberManage.setText("成员查看");
                sliding_code.setVisibility(View.INVISIBLE);
            }
        }
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        isRegister = true;
        startActivity(new Intent(new Intent(this, VideoActivity.class)));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isLoop = false;
        if (preference.getBoolean(MyApplication.cellphone + "isAuto")) {
            openScanBlue(true);
        }
//        openScanBlue(true);
        isActive = false;
        Log.e("onPause", "onPause    "+!Util.isWorked(instance));
        if (sensorManager != null) {// 取消摇一摇监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
//        if(!Util.isWorked(instance)){
//            startBleService();
//        }
    }

    private void getDisplay() {
        try {
            //获取屏幕数据
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            MyApplication.display_width = metric.widthPixels; // 屏幕宽度（像素）
            MyApplication.display_height = metric.heightPixels;
            MyApplication.density = metric.density;
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }

    private void setView() {
        mdrawerlayout = (DrawerLayout) findViewById(R.id.mdrawerlayout);
        base_sliding = findViewById(R.id.base_sliding);

        title_head = (ImageView) findViewById(R.id.title_head);
        title_scan = findViewById(R.id.title_scan);
        tv_dot = (LinearLayout) findViewById(R.id.tv_dot);
        btn_opendoor = findViewById(R.id.btn_opendoor);
        bufferBar = (ImageView) findViewById(R.id.bufferBar);

        tab_grid = findViewById(R.id.tab_grid);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab_grid.getLayoutParams();
        layoutParams.height = MyApplication.display_width * 2 / 3;
        tab_grid.setLayoutParams(layoutParams);

        tab_1 = findViewById(R.id.tab_1);
        tab_2 = findViewById(R.id.tab_2);
        tab_3 = findViewById(R.id.tab_3);
        tab_4 = findViewById(R.id.tab_4);
        tab_5 = findViewById(R.id.tab_5);
        tab_6 = findViewById(R.id.tab_6);
        setViewPage();

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

    }

    private void setViewPage() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager_layout = findViewById(R.id.viewPager_layout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager_layout.getLayoutParams();
        layoutParams.height = MyApplication.display_width * 9 / 16;
        viewPager_layout.setLayoutParams(layoutParams);
        initList();
    }

    private void initList() {
        list = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(list);

        list.clear();
        ImageView view1 = new ImageView(this);
        ImageView view2 = new ImageView(this);
        ImageView view3 = new ImageView(this);
        view1.setScaleType(ScaleType.FIT_XY);
        view2.setScaleType(ScaleType.FIT_XY);
        view3.setScaleType(ScaleType.FIT_XY);
        view1.setImageResource(R.drawable.fragment_a_vp1);
        view2.setImageResource(R.drawable.fragment_a_vp2);
        view3.setImageResource(R.drawable.fragment_a_vp3);
        list.add(view1);
        list.add(view2);
        list.add(view3);
        viewPager.setAdapter(viewPagerAdapter);
        init_dot();

        Thread pagerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        CatchUtil.catchM(e);
                    }
                    if (isLoop)
                        handler.sendEmptyMessage(0);
                }
            }
        });
        pagerThread.start();
    }

    private void setListener() {
        title_head.setOnClickListener(this);
        title_scan.setOnClickListener(this);
        btn_opendoor.setOnClickListener(this);
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_3.setOnClickListener(this);
        tab_4.setOnClickListener(this);
        tab_5.setOnClickListener(this);
        tab_6.setOnClickListener(this);

        sliding_code.setOnClickListener(this);
        sliding_memberManage.setOnClickListener(this);
        sliding_information.setOnClickListener(this);
        sliding_memberCheck.setOnClickListener(this);
        sliding_setting.setOnClickListener(this);
        sliding_help.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                init_dot();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_head:
                mdrawerlayout.openDrawer(base_sliding);
                break;
            case R.id.title_scan:
                if (!MyApplication.logined || MyApplication.cellphone == null) {
                    ToastUtil.s(this, "您当前没有登录！");
                    return;
                }
                Intent intentScan = new Intent(this, CaptureActivity.class);
                startActivityForResult(intentScan, 0);
                break;
            case R.id.btn_opendoor:
//                if (SIPService.voiceWaveHelper != null)
//                    SIPService.voiceWaveHelper.play();
                handler1.sendEmptyMessage(SENSOR_SHAKE);
                break;
            case R.id.tab_1:
                Intent intent1 = new Intent(this, MessageListActivity.class);
                startActivity(intent1);
                break;
            case R.id.tab_3:
                Intent intent3 = new Intent(this, VideoActivity.class);
                startActivity(intent3);
                break;
            case R.id.tab_4:
                Intent intent4 = new Intent(this, TenementActivity.class);
                startActivity(intent4);
                break;
            case R.id.tab_2:
                Intent intent5 = new Intent(this, AuthorizationActivity.class);
                startActivity(intent5);
                break;
            case R.id.tab_5:
            case R.id.tab_6:
                ToastUtil.s(this, "建设中，敬请期待！");
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

    /**
     * 开门的动画效果
     */
    private void startOpenAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.base_act_opendoor);
        btn_opendoor.startAnimation(animation);
    }

    private void stopOpenAnimation() {
        btn_opendoor.clearAnimation();
    }

    public void init_dot() {
        tv_dot.removeAllViews();
        int n = viewPager.getCurrentItem();
        for (int i = 0; i < list.size(); i++) {
            ImageView view = new ImageView(this);
            if (i == n % list.size()) {
                view.setImageResource(R.drawable.fragment_a_dot2);
            } else {
                view.setImageResource(R.drawable.fragment_a_dot1);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                lp.setMargins(20, 0, 0, 0);
            }
            view.setLayoutParams(lp);
            tv_dot.addView(view);
        }
    }


    //扫描二维码 申请绑定
    public void bindHouseByQRcode(String string) {
        RequestParams params = new RequestParams();
        params.addHeader("Content-type", "application/json; charset=utf-8");
        params.setHeader("Accept", "application/json");
        StringEntity entity = null;
        try {
            entity = new StringEntity(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.setBodyEntity(entity);
        String url = Constants.BINDHOUSEBYQRCODE;
        MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(
                            ResponseInfo<String> responseInfo) {
                        cancelProgress();
                        String mResult = responseInfo.result.toString();
                        try {
                            JSONObject jo = new JSONObject(mResult);
                            if (jo.getInt("returnCode") == 0) {
                                ToastUtil.s(BaseActivity.this, "绑定申请成功");
                                LogUtils.i("绑定申请成功");
                            } else {
                                ToastUtil.s(BaseActivity.this, jo.getString("msg"));
                                LogUtils.i(jo.getString("msg"));
                            }
                        } catch (Exception e) {
                            CatchUtil.catchM(e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error,
                                          String msg) {
                        cancelProgress();
                        ToastUtil.s(BaseActivity.this, getString(R.string.net_abnormal));
                        LogUtils.i(getString(R.string.net_abnormal) + msg);
                    }
                });
        httpUtil.send();
        showProgress();
    }

//    @Override
//    public void onBackPressed() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            ToastUtil.s(getApplicationContext(), getString(R.string.base_exit));
//            exitTime = System.currentTimeMillis();
//        } else {
//            moveTaskToBack(true);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBleService();
        if(isRegister){
            unregisterReceiver(mGattUpdateReceiver);
            isRegister = false;
        }
        instance = null;
    }

    protected void stopBleService() {
        if (Util.isWorked(instance)){
            unbindService(mServiceConnection);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //二维码扫描界面返回
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            ToastUtil.s(this, scanResult);
            bindHouseByQRcode(scanResult);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("cellphone", MyApplication.cellphone);
        String password = preference.getString("password");
        super.onSaveInstanceState(outState);
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            //	Log.i(TAG, "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if ((Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) && !isShake) {
                isShake = true;
                Thread thread = new Thread(){
                    public void run() {
                        if(!Util.isWorked(instance)){
                            startBleService();
                        }
                        vibrator.vibrate(200);
                        Message msg = new Message();
                        msg.what = SENSOR_SHAKE;
                        handler1.sendMessage(msg);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Message msg1 = new Message();
                        msg1.what = END_SHAKE;
                        handler1.sendMessage(msg1);
                    };
                };
                thread.start();

            }
        }

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
            // TODO 自动生成的方法存根

        }
    };

    /**
     * 摇一摇动作执行
     *
     */
    public Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE://摇一摇后连接蓝牙
                    isDisplayDialog = true;
                    openSuccess = false;
                    tryNumber = 0;
                    bufferBar.setAnimation(BufferBarAnimation.getAnimation());
                    bufferBar.setVisibility(View.VISIBLE);
                    startScanBlue();
                    break;
                case END_SHAKE://摇一摇结束，才能进行下一次摇一摇
                    isShake = false;
                    break;
                case CLEANANI://取消搜索蓝牙加载框
                    bufferBar.clearAnimation();
                    bufferBar.setVisibility(View.INVISIBLE);
                    break;
                case DISCONNECTBLE://取消搜索蓝牙加载框
                    mBluetoothLeService.disconnect();;
                    break;
                case STARTBLE://开启蓝牙服务
                    startBleService();
                    openScanBlue(true);
                    break;
                case STOPBLE://取消蓝牙服务
                    mBluetoothLeService.disconnect();
                    mBluetoothLeService.close();
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    openScanBlue(false);
                    stopBleService();
                    Log.e("STOPBLE---------------", "STOPBLE");
                    handler1.sendEmptyMessageDelayed(STARTBLE, 1000);
                    break;
                case CLOSEBLE://取消蓝牙服务
                    mBluetoothLeService.close();
                    handler1.sendEmptyMessage(CLEANANI);
                    break;
            }
        }
    };

    /**
     * 是否开启后台扫描蓝牙
     * @param boo
     */
    public void openScanBlue(boolean boo) {
        if(mBluetoothLeService != null){
            mBluetoothLeService.scanBlue(boo);
        }
    }

    /**
     * 开始搜索蓝牙
     */
    public void startScanBlue() {
        initialize();
        checkAndSetBluetoothEnabled();
    }

    /**
     * 初始化蓝牙
     */
    private void initialize(){
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    /**
     * 判断蓝牙是否可用
     */
    private void checkAndSetBluetoothEnabled(){
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mDeviceListAdapter = new DeviceListAdapter(this);
        if(!mScanning){
            scanLeDevice(true);
        }
    }
    /**
     * 获取要发送的字节数组
     */
    private void getSendBuf(){
        sendIndex = 0;
        if (MyApplication.houseProperty.sipnum.length() > 20){
            byte[] tmp = MyApplication.houseProperty.sipnum.substring(6,MyApplication.houseProperty.sipnum.length()).getBytes();
//        byte[] tmp = MyApplication.cellphone.getBytes();
            sendDataLen = 20;
            sendBuf = null;
            sendBuf = new byte[sendDataLen];
            sendBuf[0] = (byte) 0xFC;
            sendBuf[1] = 0x03;
            sendBuf[19] = (byte) 0xAA;
            for (int i = 0; i < tmp.length; i++) {
                sendBuf[i+3] = tmp[i];
            }
            sendBuf[18] = BluetoochEntity.getInspectionSum(tmp);
        }

    }
    /**
     * 发送开门指令
     */
    private void sendOpenDoorData() {
        if (sendDataLen>20) {
            final byte[] buf = new byte[20];
            for (int i=0;i<20;i++)
            {
                buf[i] = sendBuf[sendIndex+i];
            }
            sendIndex+=20;
            mBluetoothLeService.writeData(buf);
            sendDataLen -= 20;
        }
        else {
            final byte[] buf = new byte[sendDataLen];
            for (int i=0;i<sendDataLen;i++)
            {
                buf[i] = sendBuf[sendIndex+i];
            }
            mBluetoothLeService.writeData(buf);
            sendDataLen = 0;
            sendIndex = 0;
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
//			mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            Log.e("action", action + "");
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                if (!openSuccess && isDisplayDialog && tryNumber < 3){
                    if (null != mDeviceAddress && !"".equals(mDeviceAddress)){
                        mBluetoothLeService.connect(mDeviceAddress);

                    }
                    bufferBar.setAnimation(BufferBarAnimation.getAnimation());
                    bufferBar.setVisibility(View.VISIBLE);
                    tryNumber++;
                    Log.e("tryNumber", tryNumber + "");
                    handler1.removeMessages(CLOSEBLE);
                    if (tryNumber == 3){
                        tryNumber = 0;
                        openSuccess = true;
                        handler1.sendEmptyMessageDelayed(CLOSEBLE,3000);
                    }
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //特征值找到才代表连接成功
                if(isActive){
                    if(isDisplayDialog){
                        getSendBuf();
                        sendOpenDoorData();
                        handler1.sendEmptyMessageDelayed(CLOSEBLE,4000);
                    }
                }else if(Util.isApplicationBroughtToBackground(context)){
                    getSendBuf();
                    sendOpenDoorData();
                    handler1.sendEmptyMessageDelayed(CLOSEBLE,3000);
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                String hex = Util.bytesToHex(data, data.length);
                if(hex.length() > 10){
                    hex = hex.substring(6, 10);
                    Log.e("hex", hex);
                    if("OK".equals(Util.hexStr2Str(hex))){
                        if(isDisplayDialog){
                            ToastUtil.s(BaseActivity.this, re.getString(R.string.success_open));
                            isDisplayDialog = false;
                            handler1.sendEmptyMessage(CLOSEBLE);
                        }
                    }
                }
                openSuccess = true;
                tryNumber = 0;
                handler1.removeMessages(DISCONNECTBLE);
                handler1.sendEmptyMessage(DISCONNECTBLE);
                cleanAnimatied(0);

            }else if (BluetoothLeService.ACTION_WRITE_SUCCESSFUL.equals(action)) {
                if (sendDataLen>0)
                {
                    Log.e("log","Write OK,Send again"+mDeviceName);
                    sendOpenDoorData();
                }
                else {
                    Log.e("log","Write Finish");
//                    openSuccess = true;
                    handler1.removeMessages(CLOSEBLE);
                    handler1.sendEmptyMessageDelayed(CLOSEBLE, 3000);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!openSuccess){
                                getSendBuf();
                                sendOpenDoorData();
                                handler1.removeMessages(CLOSEBLE);
                                handler1.sendEmptyMessageDelayed(CLOSEBLE,4000);
                            }
                        }
                    },1000);
                }
            }

        }
    };
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_SUCCESSFUL);
        return intentFilter;
    }

    /**
     * 扫描设备
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("-----------------", mDeviceListAdapter.getCount() + "");
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    int temp = -70;
                    for (int i = 0; i < mDeviceListAdapter.getRssiLists().size(); i++) {
                        if (mDeviceListAdapter.getRssiLists().get(i) > temp) {
                            temp = mDeviceListAdapter.getRssiLists().get(i);
                        }
                    }
                    int index = mDeviceListAdapter.getRssiLists().indexOf(temp);
                    //如果只找到一个设备，直接连接，多个显示列表进行选择
                    if (mDeviceListAdapter.getCount() > 1) {
                        if (isDisplayDialog) {
                            createDeviceListDialog();
                        } else if (-1 != index) {
                            mDeviceAddress = mDeviceListAdapter.getDevice(index).getAddress();
                            mDeviceName = mDeviceListAdapter.getDevice(index).getName();
                            mBluetoothLeService.connect(mDeviceAddress);
                            cleanAnimatied(3000);
                        }
                    } else if (mDeviceListAdapter.getCount() == 1) {
                        mDeviceAddress = mDeviceListAdapter.getDevice(0).getAddress();
                        mDeviceName = mDeviceListAdapter.getDevice(0).getName();
                        mBluetoothLeService.connect(mDeviceAddress);
                        cleanAnimatied(3000);
                    } else {
                        if (isDisplayDialog){
						ToastUtil.s(BaseActivity.this, re.getString(R.string.not_ble_device));
                        }
                        cleanAnimatied(0);
                    }
                }

            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    /**
     * 隐藏加载框
     */
    private void cleanAnimatied(long time) {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler1.sendEmptyMessage(CLEANANI);
            }
        }, time);
    }
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(deviceList.contains((device.getName()))){
                                mDeviceListAdapter.addDevice(device,rssi);
                                mDeviceListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            };
    AlertDialog dialog;
    /**
     * 创建一个新的设备列表对话框
     */
    public void createDeviceListDialog() {
        if (mDeviceListAdapter.getCount() > 0) {
            dialog =  new AlertDialog.Builder(this).setCancelable(true)
                    .setAdapter(mDeviceListAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BluetoothDevice device = mDeviceListAdapter.getDevice(which);
                            mDeviceAddress = device.getAddress();
                            mDeviceName = device.getName();
                            mBluetoothLeService.connect(mDeviceAddress);
                            bufferBar.setAnimation(BufferBarAnimation.getAnimation());
                            bufferBar.setVisibility(View.VISIBLE);
                            cleanAnimatied(3000);
                        }
                    }).show();
        }
        cleanAnimatied(5000);
    }
}
