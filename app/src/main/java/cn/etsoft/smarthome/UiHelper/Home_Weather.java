package cn.etsoft.smarthome.UiHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.abc.mybaseactivity.NetWorkListener.NetUtil;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.etsoft.smarthome.Domain.City;
import cn.etsoft.smarthome.Domain.Weather_All_Bean;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/14.
 */

public class Home_Weather {

    private int LOCATION_SUCCEED = 100;
    private int LOCATION_FAILING = 111;
    private int LOCATION_NONETWORK = 1001;
    private int WRATHER_SUCCEED = 2000;
    private int TIME_UPDATA = 10;
    private int DB_INITOK = 200;

    public static final String PM2D5_BASE_URL = "http://jisutianqi.market.alicloudapi.com/weather/query?citycode=";
    private static final String 天气key = "APPCODE 500a3b58be714c519f83e8aa9a23810e";
    private TextView date, week;
    private ImageView weather;
    private City mCurCity;
    private int[] wrathers = new int[]{R.drawable.qing, R.drawable.duoyun, R.drawable.yin, R.drawable.leizhen,
            R.drawable.yujiaxue, R.drawable.yu, R.drawable.dayu, R.drawable.xue, R.drawable.daxue,
            R.drawable.huangsha, R.drawable.wu, R.drawable.mai, R.drawable.feng, R.drawable.jufeng,
            R.drawable.weizhi};
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private Context context;
    private Handler mHandler;

    public Home_Weather(Handler handler, Context context, TextView date, TextView week, ImageView weather) {
        this.context = context;
        this.date = date;
        this.week = week;
        this.weather = weather;
        mHandler = handler;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("TIMER", "进入一次定时器");
                if (MyApplication.mApplication.getmCityDB() != null) {
                    Message message = mHandler.obtainMessage();
                    message.what = DB_INITOK;
                    mHandler.sendMessage(message);
                    Log.i("TIMER", "执行查询天气");
                    timer.cancel();
                }
            }
        }, 1000, 1000);

    }

    public void initWeatherData() {
        initLocation();
        initData();
    }

    /**
     * 天气回调图标
     */
    public void initWrather(Weather_All_Bean results) {

        if (MyApplication.mApplication.getmWrather_results() == null)
            return;
        int code = Integer.parseInt(results.getResult().getImg());

        if (code == 0)
            weather.setImageResource(wrathers[0]);
        else if (code == 1 || code == 2)
            weather.setImageResource(wrathers[1]);
        else if (code == 3)
            weather.setImageResource(wrathers[2]);
        else if (code == 4 || code == 5)
            weather.setImageResource(wrathers[3]);
        else if (code == 6)
            weather.setImageResource(wrathers[4]);
        else if (code == 7 || code == 8)
            weather.setImageResource(wrathers[5]);
        else if (8 < code && code < 13)
            weather.setImageResource(wrathers[6]);
        else if (12 < code && code < 16)
            weather.setImageResource(wrathers[7]);
        else if (15 < code && code < 18)
            weather.setImageResource(wrathers[8]);
        else if (code == 18)
            weather.setImageResource(wrathers[10]);
        else if (code == 19)
            weather.setImageResource(wrathers[5]);
        else if (code == 20)
            weather.setImageResource(wrathers[9]);
        else if (20 < code && code < 23)
            weather.setImageResource(wrathers[5]);
        else if (22 < code && code < 26)
            weather.setImageResource(wrathers[6]);
        else if (25 < code && code < 28)
            weather.setImageResource(wrathers[7]);
        else if (code == 28)
            weather.setImageResource(wrathers[8]);
        else if (28 < code && code < 32)
            weather.setImageResource(wrathers[9]);
        else if (31 < code && code < 59)
            weather.setImageResource(wrathers[11]);
        else if (code == 301)
            weather.setImageResource(wrathers[5]);
        else if (code == 302)
            weather.setImageResource(wrathers[7]);
        else
            weather.setImageResource(wrathers[14]);
    }

    /**
     * 时间
     */
    @SuppressLint("WrongConstant")
    private void initData() {
        final Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        date.setText(c.get(Calendar.YEAR) + "." + month + "." + c.get(Calendar.DAY_OF_MONTH));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String time;
                for (; ; ) {
                    try {
                        Calendar c = Calendar.getInstance();
                        Thread.sleep(500);
                        if (c.get(Calendar.MINUTE) < 10) {
                            time = (c.get(Calendar.HOUR_OF_DAY) + ":" + 0 + c.get(Calendar.MINUTE));
                        } else {
                            time = (c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = TIME_UPDATA;
                        msg.obj = time;
                        mHandler.sendMessage(msg);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
            week.setText("星期一");
        else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
            week.setText("星期二");
        else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
            week.setText("星期三");
        else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
            week.setText("星期四");
        else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
            week.setText("星期五");
        else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            week.setText("星期六");
        else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            week.setText("星期日");
    }

    /**
     * 定位
     */
    @SuppressLint("WrongConstant")
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        getLocationClientOption();
        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        mCurCity = MyApplication.mApplication.getmCityDB().getCity(aMapLocation.getCity());
                        Message message = mHandler.obtainMessage();
                        message.obj = mCurCity;
                        message.what = LOCATION_SUCCEED;
                        mHandler.sendMessage(message);
//                        Log.i("LOCATION", mCurCity.getCity() + "-----------" + mCurCity.getNumber());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getSimpleWeatherInfo(mCurCity);
                            }
                        }).start();
                    } else {
                        ToastUtil.showText("定位失败");
                        mCurCity = MyApplication.mApplication.getmCityDB().getCity("北京市");
//                        Log.i("LOCATION", mCurCity.getCity() + "-----------" + mCurCity.getNumber());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getSimpleWeatherInfo(mCurCity);
                            }
                        }).start();
//                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Message message = mHandler.obtainMessage();
                        message.obj = mCurCity;
                        message.what = LOCATION_FAILING;
                        mHandler.sendMessage(message);
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);
        if (NetUtil.getNetWorkState(context) != NetUtil.NETWORK_NONE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mLocationClient.startLocation();
                    Log.i("Location", "定位开始");
                }
            }).start();
        } else {
            Message message = mHandler.obtainMessage();
            message.obj = mCurCity;
            message.what = LOCATION_NONETWORK;
            mHandler.sendMessage(message);
        }
    }

    /**
     * 配置定位
     */
    private void getLocationClientOption() {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(30000);
        mLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 请求天气信息
     */
    private void getSimpleWeatherInfo(City mCurCity) {
        String url = PM2D5_BASE_URL + mCurCity.getNumber();
        // L.i("weather url: " + url);
//        Log.i("DATA", "请求网址  " + url);

        String weatherResult = connServerForResult(url);
//        Log.i("DATA", "返回结果" + weatherResult);
        Gson gson = new Gson();
        if (!"".equals(weatherResult)) {
            try {
                Weather_All_Bean results = gson.fromJson(weatherResult, Weather_All_Bean.class);
                MyApplication.mApplication.setmWrather_results(results);
                Message message = mHandler.obtainMessage();
                message.what = WRATHER_SUCCEED;
                message.obj = results;
                mHandler.sendMessage(message);
            } catch (Exception e) {
                System.out.println(e + "");
            }
        } else {
            ToastUtil.showText("获取天气信息失败");
        }
    }

    // 请求服务器，获取返回数据
    private String connServerForResult(String url) {
        HttpGet httpRequest = new HttpGet(url);
        //天气key
        httpRequest.setHeader("Authorization", 天气key);
        String strResult = "";
        if (NetUtil.getNetWorkState(context) != NetUtil.NETWORK_NONE) {
            try {
                // HttpClient对象
                HttpClient httpClient = new DefaultHttpClient();
                // 获得HttpResponse对象
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                    // 取得返回的数据
                    strResult = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception e) {
            }
        }
        return strResult; // 返回结果
    }
}
