package cn.etsoft.smarthome.UiHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.WeatherView.MovingPictureView;

/**
 * Author：FBL  Time： 2017/6/21.
 */

public class HomeWeatherAnim {

    private RelativeLayout today_yubao; //背景
    public static ForecastHandler forecastHandler; //加载数据，更新界面
    //切换图片定时器、handler、图片标号
    public static Timer weather_timer;
    private Day_Lei_Handler day_lei_handler;
    private Night_Qing_Handler night_qing_handler;
    private Day_Rain_Handler Day_Rain_Handler;
    private Day_Snow_Handler Day_Snow_Handler;
    private Day_RainSnow_Handler Day_RainSnow_Handler;
    private Day_Wu_Handler Day_Wu_Handler;
    //平移的图片
    private MovingPictureView
            w1_move1, w1_move2, w1_move3, w1_move4, w1_move5,
            w2_move1, w2_move2, w2_move3, w2_move4, w2_move5,
            w3_move1, w3_move2, w3_move3, w3_move4, w3_move5,
            w4_move1, w4_move2, w4_move3, w4_move4, w4_move5,
            w5_move1, w5_move2, w5_move3, w5_move4, w5_move5,
            w6_move1, w6_move2, w6_move3, w6_move4, w6_move5,
            w7_move1, w7_move2, w7_move3, w7_move4, w7_move5;
    private ImageView m1, m2, m3, m4, m5, m6, m7, m8, m9, m10;
    public static int imgIndex;
    //平移的图片所在布局
    private RelativeLayout weather_move1, weather_move2, weather_move3, weather_move4, weather_move5, weather_move6, weather_move7, weather_move8, weather_move9, weather_move10;
    //切换的图片所在布局
    private RelativeLayout weather_qing, weather_day_duoyun, weather_day_yin, weather_night_yin,
            weather_wu, weather_mai, weather_sha;

    //示意
    private int nowindex = 0; //第一个天气默认序号是10

    private Activity mActivity;


    @SuppressLint("ResourceAsColor")
    public void initAnimWeather(Activity activity, int activityResourcesID) {
        mActivity = activity;
        Window window = activity.getWindow();
//        View view = LayoutInflater.from(activity).inflate(activityResourcesID,null,false);
        MovingPictureView.isRuning = true;
        today_yubao = (RelativeLayout) window.findViewById(R.id.today_yubao);

        weather_qing = (RelativeLayout) window.findViewById(R.id.weather_qing);
        weather_day_duoyun = (RelativeLayout) window.findViewById(R.id.weather_day_duoyun);
        weather_day_yin = (RelativeLayout) window.findViewById(R.id.weather_day_yin);
        weather_night_yin = (RelativeLayout) window.findViewById(R.id.weather_night_yin);
        weather_wu = (RelativeLayout) window.findViewById(R.id.weather_wu);
        weather_mai = (RelativeLayout) window.findViewById(R.id.weather_mai);
        weather_sha = (RelativeLayout) window.findViewById(R.id.weather_sha);

        //将所有要用到的平移的图片加载好，根据当前天气，选择显示哪些图片，
        //我在这里只为weather_qing何weather_day_yin这两个天气添加了平移图片，你可以为其他的天气加上你需要的图片，然后好用.
        w1_move1 = new MovingPictureView(mActivity, R.drawable.yjjc_h_a3, -300, 10, 40);
        w1_move2 = new MovingPictureView(mActivity, R.drawable.yjjc_h_a3, 250, 10, 40);
        w1_move3 = new MovingPictureView(mActivity, R.drawable.yjjc_h_a4, 480, 40, 40);
        weather_qing.removeAllViews();
        weather_qing.addView(w1_move1);
        weather_qing.addView(w1_move2);
        weather_qing.addView(w1_move3);

        w3_move1 = new MovingPictureView(mActivity, R.drawable.yjjc_h_d2, -250, 0, 30);
        w3_move2 = new MovingPictureView(mActivity, R.drawable.yjjc_h_d3, 180, 60, 40);
        weather_day_yin.addView(w3_move1);
        weather_day_yin.addView(w3_move2);

        weather_move1 = (RelativeLayout) window.findViewById(R.id.weather_move1);
        weather_move2 = (RelativeLayout) window.findViewById(R.id.weather_move2);
        weather_move3 = (RelativeLayout) window.findViewById(R.id.weather_move3);
        weather_move4 = (RelativeLayout) window.findViewById(R.id.weather_move4);
        weather_move5 = (RelativeLayout) window.findViewById(R.id.weather_move5);
        weather_move6 = (RelativeLayout) window.findViewById(R.id.weather_move6);
        weather_move7 = (RelativeLayout) window.findViewById(R.id.weather_move7);
        weather_move8 = (RelativeLayout) window.findViewById(R.id.weather_move8);
        weather_move9 = (RelativeLayout) window.findViewById(R.id.weather_move9);
        weather_move10 = (RelativeLayout) window.findViewById(R.id.weather_move10);
        m1 = (ImageView) window.findViewById(R.id.m1);
        m2 = (ImageView) window.findViewById(R.id.m2);
        m3 = (ImageView) window.findViewById(R.id.m3);
        m4 = (ImageView) window.findViewById(R.id.m4);
        m5 = (ImageView) window.findViewById(R.id.m5);
        m6 = (ImageView) window.findViewById(R.id.m6);
        m7 = (ImageView) window.findViewById(R.id.m7);
        m8 = (ImageView) window.findViewById(R.id.m8);
        m9 = (ImageView) window.findViewById(R.id.m9);
        m10 = (ImageView) window.findViewById(R.id.m10);
        day_lei_handler = new Day_Lei_Handler(mActivity);
        night_qing_handler = new Night_Qing_Handler(mActivity);
        Day_Rain_Handler = new Day_Rain_Handler(mActivity);
        Day_Snow_Handler = new Day_Snow_Handler(mActivity);
        Day_RainSnow_Handler = new Day_RainSnow_Handler(mActivity);
        Day_Wu_Handler = new Day_Wu_Handler(mActivity);
        forecastHandler = new ForecastHandler();

        //发送消息，显示默认的第一个天气示例
        Message msg = new Message();
        Bundle b = new Bundle();
        msg.what = nowindex;
        msg.setData(b);
        forecastHandler.sendMessage(msg);

    }

    //加载预报数据
    public class ForecastHandler extends Handler {
        //接受数据
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                   晴天
                    day_qing();
                    return;
                case 1:
//                    多云
                    day_duoyun();
                    return;
                case 2:
//                    阴天
                    day_yin();
                    return;
                case 18:
                case 32:
                case 49:
                case 57:
                case 58:
//                    夜·阴
//                    night_yin();
//                    return;

//                    大雾
                    day_wu();
                    return;
                case 53:
                case 54:
                case 55:
                case 56:
//                    雾霾
                    day_mai();
                    return;
                case 20:
                case 29:
                case 30:
                case 31:
//                    风沙
                    day_sha();
                    return;
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 26:
                case 27:
                case 28:
                case 302:
//                    下雪
                    day_snow();
                    return;
                case 5:
                case 6:
//                    雨·雪
                    day_rainsnow();
                    return;
                case 4:
//                    雷电
                    day_lei();
                    return;
//                case 20:
//                                  夜·晴
//                    night_qing();
//                    return;
                case 3:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 19:
                case 21:
                case 22:
                case 23:
                case 24:
                case 301:
                    //下雨
                    day_rain();
                    return;
                default:
                    break;
            }
        }
    }

    //10白天_晴
    public void day_qing() {
        wordBlack();
        showweather("day_qing");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_a1);
        if (!w1_move1.isstarted) {
            new Thread(w1_move1).start();//每一个移动的图片都是一个线程
            new Thread(w1_move2).start();
            new Thread(w1_move3).start();
        }
    }

    //11白天_多云
    public void day_duoyun() {
        wordBlack();
        showweather("day_duoyun");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_c1);
        if (!w1_move1.isstarted) {
            new Thread(w1_move1).start();//这里的天气用了上一个天气的图片，也可以根据自己需要用想要的图片
            new Thread(w1_move2).start();
            new Thread(w1_move3).start();
        }
//		new Thread(w2_move1).start();
//		new Thread(w2_move2).start();
//		new Thread(w2_move3).start();
//		new Thread(w2_move4).start();
//		new Thread(w2_move5).start();
    }

    //12阴天
    public void day_yin() {
        wordWhite();
        showweather("day_yin");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_d1);
        if (!w3_move1.isstarted) {
            new Thread(w3_move1).start();
            new Thread(w3_move2).start();
        }
    }

    //13夜晚阴天
    public void night_yin() {
        wordWhite();
        showweather("night_yin");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_l1);
//		new Thread(w4_move1).start();
//		new Thread(w4_move2).start();
    }

    //14大雾
    public void day_wu() {
        wordBlack();
        showweather("day_wu");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_i1);
//		new Thread(w5_move1).start();
//		new Thread(w5_move2).start();
    }

    //15霾
    public void day_mai() {
        wordBlack();
        showweather("day_mai");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_j1);
    }

    //16风沙
    public void day_sha() {
        wordBlack();
        showweather("day_sha");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_k1);
    }

    //17雪
    public void day_snow() {
        wordBlack();
        showweather("other");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_g1);
        m1.setImageResource(R.drawable.yjjc_h_g2);
        m2.setImageResource(R.drawable.yjjc_h_g3);
        m3.setImageResource(R.drawable.yjjc_h_g4);
        m4.setImageResource(R.drawable.yjjc_h_g5);
        Day_Snow_Timer chage = new Day_Snow_Timer();
        Thread chageimg = new Thread(chage);
        chageimg.start();
    }

    //18雨夹雪
    public void day_rainsnow() {
        wordWhite();
        showweather("other");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_h1);
        m1.setImageResource(R.drawable.yjjc_h_h2);
        m2.setImageResource(R.drawable.yjjc_h_h3);
        m3.setImageResource(R.drawable.yjjc_h_h4);
        Day_RainSnow_Timer chage = new Day_RainSnow_Timer();
        Thread chageimg = new Thread(chage);
        chageimg.start();
    }

    //19雷雨
    public void day_lei() {
        wordWhite();
        showweather("other");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_f1);
        m1.setImageResource(R.drawable.yjjc_h_f2);
        m2.setImageResource(R.drawable.yjjc_h_f3);
        m3.setImageResource(R.drawable.yjjc_h_f4);
        m4.setImageResource(R.drawable.yjjc_h_f5);
        m5.setImageResource(R.drawable.yjjc_h_f6);
        m6.setImageResource(R.drawable.yjjc_h_f7);
        m7.setImageResource(R.drawable.yjjc_h_f8);
        Day_Lei_Timer chage = new Day_Lei_Timer();
        Thread chageimg = new Thread(chage);
        chageimg.start();
    }

    //20夜晚_晴
    public void night_qing() {
        wordWhite();
        showweather("other");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_b1);
        m1.setImageResource(R.drawable.yjjc_h_b2);
        m2.setImageResource(R.drawable.yjjc_h_b3);
        m3.setImageResource(R.drawable.yjjc_h_b4);
        m4.setImageResource(R.drawable.yjjc_h_b5);
        m5.setImageResource(R.drawable.yjjc_h_b6);
        Night_Qing_Timer chage = new Night_Qing_Timer();
        Thread chageimg = new Thread(chage);
        chageimg.start();
    }

    //21雨
    public void day_rain() {
        wordWhite();
        showweather("other");
        today_yubao.setBackgroundResource(R.drawable.yjjc_h_e1);
        m1.setImageResource(R.drawable.yjjc_h_e2);
        m2.setImageResource(R.drawable.yjjc_h_e3);
        m3.setImageResource(R.drawable.yjjc_h_e4);
        m4.setImageResource(R.drawable.yjjc_h_e5);
        Day_Rain_Timer chage = new Day_Rain_Timer();
        Thread chageimg = new Thread(chage);
        chageimg.start();
    }

    class Day_Rain_Timer implements Runnable {
        @Override
        public void run() {
            if (weather_timer != null) {
                weather_timer.cancel();
            }
            weather_timer = new Timer();
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    if (imgIndex > 3) {
                        imgIndex = 0;
                    }
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("index", String.valueOf(imgIndex));
                    msg.setData(b);
                    imgIndex += 1;
                    Day_Rain_Handler.sendMessage(msg);
                }
            };
            weather_timer.schedule(t, 0, 300);
        }
    }

    class Night_Qing_Timer implements Runnable {
        @Override
        public void run() {
            if (weather_timer != null) {
                weather_timer.cancel();
            }
            weather_timer = new Timer();
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    if (imgIndex > 4) {
                        imgIndex = 0;
                    }
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("index", String.valueOf(imgIndex));
                    msg.setData(b);
                    imgIndex += 1;
                    night_qing_handler.sendMessage(msg);
                }
            };
            weather_timer.schedule(t, 0, 1 * 500);
        }
    }

    class Day_Wu_Timer implements Runnable {
        @Override
        public void run() {
            if (weather_timer != null) {
                weather_timer.cancel();
            }
            weather_timer = new Timer();
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    if (imgIndex > 4) {
                        imgIndex = 0;
                    }
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("index", String.valueOf(imgIndex));
                    msg.setData(b);
                    imgIndex += 1;
                    Day_Wu_Handler.sendMessage(msg);
                }
            };
            weather_timer.schedule(t, 0, 1 * 500);
        }
    }

    class Day_Lei_Timer implements Runnable {
        @Override
        public void run() {
            if (weather_timer != null) {
                weather_timer.cancel();
            }
            weather_timer = new Timer();
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    if (imgIndex > 15) {
                        imgIndex = 0;
                    }
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("index", String.valueOf(imgIndex));
                    msg.setData(b);
                    imgIndex += 1;
                    day_lei_handler.sendMessage(msg);
                }
            };
            weather_timer.schedule(t, 0, 1 * 200);
        }
    }

    class Day_Snow_Timer implements Runnable {
        @Override
        public void run() {
            if (weather_timer != null) {
                weather_timer.cancel();
            }
            weather_timer = new Timer();
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    if (imgIndex > 3) {
                        imgIndex = 0;
                    }
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("index", String.valueOf(imgIndex));
                    msg.setData(b);
                    imgIndex += 1;
                    Day_Snow_Handler.sendMessage(msg);
                }
            };
            weather_timer.schedule(t, 0, 300);
        }
    }

    class Day_RainSnow_Timer implements Runnable {
        @Override
        public void run() {
            if (weather_timer != null) {
                weather_timer.cancel();
                System.gc();
            }
            weather_timer = new Timer();
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    if (imgIndex > 2) {
                        imgIndex = 0;
                    }
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("index", String.valueOf(imgIndex));
                    msg.setData(b);
                    imgIndex += 1;
                    Day_RainSnow_Handler.sendMessage(msg);
                }
            };
            weather_timer.schedule(t, 0, 300);
        }
    }

    class Day_Snow_Handler extends Handler {
        private Activity context;

        public Day_Snow_Handler() {
        }

        public Day_Snow_Handler(Activity context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgindex = 20;
            Bundle b = msg.getData();
            if (b.getString("index") != null) {
                msgindex = Integer.parseInt(b.getString("index"));
            }
            if (msgindex == 0) {
                weather_move4.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            } else if (msgindex == 1) {
                weather_move1.setVisibility(View.INVISIBLE);
                weather_move2.setVisibility(View.VISIBLE);
            } else if (msgindex == 2) {
                weather_move2.setVisibility(View.INVISIBLE);
                weather_move3.setVisibility(View.VISIBLE);
            } else if (msgindex == 3) {
                weather_move3.setVisibility(View.INVISIBLE);
                weather_move4.setVisibility(View.VISIBLE);
            } else {// if(msgindex == 4){
                weather_move4.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            }
        }
    }

    class Day_Wu_Handler extends Handler {
        private Activity context;

        public Day_Wu_Handler() {
        }

        public Day_Wu_Handler(Activity context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgindex = 20;
            Bundle b = msg.getData();
            if (b.getString("index") != null) {
                msgindex = Integer.parseInt(b.getString("index"));
            }
            if (msgindex == 0) {
                weather_move5.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            } else if (msgindex == 1) {
                weather_move1.setVisibility(View.INVISIBLE);
                weather_move2.setVisibility(View.VISIBLE);
            } else if (msgindex == 2) {
                weather_move2.setVisibility(View.INVISIBLE);
                weather_move3.setVisibility(View.VISIBLE);
            } else if (msgindex == 3) {
                weather_move3.setVisibility(View.INVISIBLE);
                weather_move4.setVisibility(View.VISIBLE);
            } else if (msgindex == 4) {
                weather_move4.setVisibility(View.INVISIBLE);
                weather_move5.setVisibility(View.VISIBLE);
            } else {

            }
        }
    }

    class Day_RainSnow_Handler extends Handler {
        private Activity context;

        public Day_RainSnow_Handler() {
        }

        public Day_RainSnow_Handler(Activity context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgindex = 20;
            Bundle b = msg.getData();
            if (b.getString("index") != null) {
                msgindex = Integer.parseInt(b.getString("index"));
            }
            if (msgindex == 0) {
                weather_move3.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            } else if (msgindex == 1) {
                weather_move1.setVisibility(View.INVISIBLE);
                weather_move2.setVisibility(View.VISIBLE);
            } else if (msgindex == 2) {
                weather_move2.setVisibility(View.INVISIBLE);
                weather_move3.setVisibility(View.VISIBLE);
            } else {

            }
        }
    }

    class Day_Rain_Handler extends Handler {
        private Activity context;

        public Day_Rain_Handler() {
        }

        public Day_Rain_Handler(Activity context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgindex = 20;
            Bundle b = msg.getData();
            if (b.getString("index") != null) {
                msgindex = Integer.parseInt(b.getString("index"));
            }
            if (msgindex == 0) {
                weather_move4.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            } else if (msgindex == 1) {
                weather_move1.setVisibility(View.INVISIBLE);
                weather_move2.setVisibility(View.VISIBLE);
            } else if (msgindex == 2) {
                weather_move2.setVisibility(View.INVISIBLE);
                weather_move3.setVisibility(View.VISIBLE);
            } else if (msgindex == 3) {
                weather_move3.setVisibility(View.INVISIBLE);
                weather_move4.setVisibility(View.VISIBLE);
            } else {// if(msgindex == 4){
                weather_move4.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            }
        }
    }

    class Night_Qing_Handler extends Handler {
        private Activity context;

        public Night_Qing_Handler() {
        }

        public Night_Qing_Handler(Activity context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgindex = 20;
            Bundle b = msg.getData();
            if (b.getString("index") != null) {
                msgindex = Integer.parseInt(b.getString("index"));
            }
            if (msgindex == 0) {
                weather_move5.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            } else if (msgindex == 1) {
                weather_move1.setVisibility(View.INVISIBLE);
                weather_move2.setVisibility(View.VISIBLE);
            } else if (msgindex == 2) {
                weather_move2.setVisibility(View.INVISIBLE);
                weather_move3.setVisibility(View.VISIBLE);
            } else if (msgindex == 3) {
                weather_move3.setVisibility(View.INVISIBLE);
                weather_move4.setVisibility(View.VISIBLE);
            } else if (msgindex == 4) {
                weather_move4.setVisibility(View.INVISIBLE);
                weather_move5.setVisibility(View.VISIBLE);
            } else {    //if(msgindex == 5){
                weather_move5.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            }
        }
    }

    class Day_Lei_Handler extends Handler {
        private Activity context;

        public Day_Lei_Handler() {
        }

        public Day_Lei_Handler(Activity context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 更新UI
            int msgindex = 20;
            Bundle b = msg.getData();
            if (b.getString("index") != null) {
                msgindex = Integer.parseInt(b.getString("index"));
            }
            if (msgindex == 0) {
                weather_move7.setVisibility(View.INVISIBLE);
                weather_move1.setVisibility(View.VISIBLE);
            } else if (msgindex == 1) {
                weather_move1.setVisibility(View.INVISIBLE);
                weather_move2.setVisibility(View.VISIBLE);
            } else if (msgindex == 2) {
                weather_move2.setVisibility(View.INVISIBLE);
                weather_move3.setVisibility(View.VISIBLE);
            } else if (msgindex == 3) {
                weather_move3.setVisibility(View.INVISIBLE);
                weather_move4.setVisibility(View.VISIBLE);
            } else if (msgindex == 4) {
                weather_move4.setVisibility(View.INVISIBLE);
                weather_move5.setVisibility(View.VISIBLE);
            } else if (msgindex == 5) {
                weather_move5.setVisibility(View.INVISIBLE);
                weather_move6.setVisibility(View.VISIBLE);
            } else if (msgindex == 6) {
                weather_move6.setVisibility(View.INVISIBLE);
                weather_move7.setVisibility(View.VISIBLE);
            } else {
                weather_move7.setVisibility(View.INVISIBLE);
            }
        }
    }

    //黑字
    public void wordBlack() {
        int color = mActivity.getResources().getColor(R.color.Myblack);
//        content.setTextColor(color);
    }

    //白字
    public void wordWhite() {
        int color = this.mActivity.getResources().getColor(R.color.MyWhite);
//        content.setTextColor(color);
    }

    //显示某一天气,显示帧天气传参数other。
    public void showweather(String weather) {
        initWeatherLayout();
        if (weather.equals("day_qing")) {
            weather_qing.setVisibility(View.VISIBLE);
        } else if (weather.equals("day_duoyun")) {
            weather_qing.setVisibility(View.VISIBLE);//没有为weather_day_duoyun添加图片，所以暂时用的是day_qing天气中的图片。你可以自己在create方法中加载，在这里就可以显示了
            weather_day_duoyun.setVisibility(View.VISIBLE);
        } else if (weather.equals("day_yin")) {
            weather_day_yin.setVisibility(View.VISIBLE);
        } else if (weather.equals("night_yin")) {
            weather_night_yin.setVisibility(View.VISIBLE);
        } else if (weather.equals("day_wu")) {
            weather_wu.setVisibility(View.VISIBLE);
        } else if (weather.equals("day_mai")) {
            weather_mai.setVisibility(View.VISIBLE);
        } else if (weather.equals("day_sha")) {
            weather_sha.setVisibility(View.VISIBLE);
        } else {

        }
    }

    //初始化天气布局
    public void initWeatherLayout() {
        if (weather_timer != null) {
            weather_timer.cancel();
        }
        weather_qing.setVisibility(View.INVISIBLE);
        weather_day_duoyun.setVisibility(View.INVISIBLE);
        weather_day_yin.setVisibility(View.INVISIBLE);
        weather_night_yin.setVisibility(View.INVISIBLE);
        weather_wu.setVisibility(View.INVISIBLE);
        weather_mai.setVisibility(View.INVISIBLE);
        weather_sha.setVisibility(View.INVISIBLE);
        weather_move1.setVisibility(View.INVISIBLE);
        weather_move2.setVisibility(View.INVISIBLE);
        weather_move3.setVisibility(View.INVISIBLE);
        weather_move4.setVisibility(View.INVISIBLE);
        weather_move5.setVisibility(View.INVISIBLE);
        weather_move6.setVisibility(View.INVISIBLE);
        weather_move7.setVisibility(View.INVISIBLE);
        weather_move8.setVisibility(View.INVISIBLE);
        weather_move9.setVisibility(View.INVISIBLE);
        weather_move10.setVisibility(View.INVISIBLE);
    }
}
