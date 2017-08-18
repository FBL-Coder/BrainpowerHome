package cn.semtec.community2.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.RemoteViews;

import org.linphone.squirrel.squirrelCallImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.activity.BaseActivity;
import cn.semtec.community2.activity.CallingActivity;
import cn.semtec.community2.activity.IncomingActivity;
import cn.semtec.community2.activity.LoginActivity;
import cn.semtec.community2.activity.MyBaseActivity;
import cn.semtec.community2.model.LoginHelper;
import cn.semtec.community2.model.VoiceWaveHelper;
import cn.semtec.community2.util.SharedPreferenceUtil;
import cn.semtec.community2.util.ToastUtil;

/**
 * Created by mac on 15/9/22.
 */
public class SIPService extends Service {
    private squirrelCallImpl squirrelCall = null;
    public static VoiceWaveHelper voiceWaveHelper;
    Timer timer;
    Timer squirreltimer;
    TimerTask timerTask;
    TimerTask squirreltask;
    // we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    final Handler squirrelhandler = new Handler();
    Notification noti;
    NotificationManager notificationManager;
    Builder builder;
    private SharedPreferenceUtil preference;

    public SIPService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //  Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Handler loginHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };
    private RemoteViews mRemoteViews;

    // 本地有登陆 记录时，自动登陆
    private void autoLogin() {
        SharedPreferenceUtil preference = MyApplication.getSharedPreferenceUtil();
        if (preference.contains("cellphone") && preference.contains("password")) {
            String cellphone = preference.getString("cellphone");
            String password = preference.getString("password");
            LoginHelper loginHelper = new LoginHelper(loginHandler);
            loginHelper.loginServer(cellphone, password);
        }
    }

    @Override
    public void onCreate() {
        // 20ms sip heartbeat
        squirrelCall = (squirrelCallImpl) getApplication();
        squirrelCall.setHandler(mMsgHandler);
        voiceWaveHelper = new VoiceWaveHelper(this);
        autoLogin();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        squirreltask = new TimerTask() {
            public void run() {
                squirrelhandler.post(new Runnable() {
                    public void run() {
                        squirrelCall.squirrelIterate();
                    }
                });
            }
        };
        // 定时 激活sip 保持连接
        squirreltimer = new Timer(true);
        squirreltimer.schedule(squirreltask, 10, 20);
        startTimer();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    public void startTimer() {
        // set a new Timer
        timer = new Timer();
        // 注册 通知栏监听器
        initButtonReceiver();

        // 初始化 timerTask任务
        initializeTimerTask();
        // 执行 timerTask任务
        timer.schedule(timerTask, 0, 1000);
    }

    @SuppressLint("SimpleDateFormat")
    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        // get the current timeStamp
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String strDate = simpleDateFormat.format(calendar.getTime());
                        String title = getApplication().getString(R.string.app_name);

                        if (squirrelCallImpl.login_state == 0) {
                            title += "未登录";
                        } else if (squirrelCallImpl.login_state == 1) {
                            title += "已登录";
                        } else {
                            title += "未连接";
                        }

                        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification);
                        // 设置控件的 text；
                        mRemoteViews.setTextViewText(R.id.textView1, title);
                        mRemoteViews.setTextViewText(R.id.textView2, strDate);
                        // 为控件添加 点击事件 ， 点击时触发 intent；
                        Intent buttonIntent = new Intent(ACTION_BUTTON);
                        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_1);
                        PendingIntent PI_button1 = PendingIntent.getBroadcast(SIPService.this, 1, buttonIntent, 0);
                        mRemoteViews.setOnClickPendingIntent(R.id.button1, PI_button1);
                        Class<? extends MyBaseActivity> into = IncomingActivity.instance != null
                                ? IncomingActivity.class
                                : ((MyApplication.logined == true) ? BaseActivity.class : LoginActivity.class);
                        Intent layoutIntent = new Intent(SIPService.this, into);
                        PendingIntent PI_layout = PendingIntent.getActivity(SIPService.this, 0, layoutIntent, 0);
                        mRemoteViews.setOnClickPendingIntent(R.id.layout, PI_layout);

                        Builder mBuilder = new Builder(SIPService.this);
                        mBuilder.setContent(mRemoteViews).setSmallIcon(R.drawable.yitong_icon);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(
                                NOTIFICATION_SERVICE);
                        // 发出通知
                        // 获取NotificationManager实例
                        Notification notify = mBuilder.build();
                        notify.flags |= Notification.FLAG_NO_CLEAR;
                        notificationManager.notify(0, notify);
                    }
                });
            }
        };
    }

    @SuppressLint("HandlerLeak")
    Handler mMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 注册状态
                    // 在这可进行想要的操作
                    HandlerRegState(msg.getData());
                case 2: //// 呼叫状态
                    HandlerCallState(msg.getData());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    void HandlerRegState(Bundle _bundle) {
        @SuppressWarnings("unused")
        String sState = null;
        int state = _bundle.getInt("state");
        String domain = _bundle.getString("domain");
        if (state == squirrelCallImpl.squirrelRegistrationProgress) {
            sState = "Login " + domain;
            // Toast.makeText(this, "云对讲正在登录...", Toast.LENGTH_SHORT).show();
        } else if (state == squirrelCallImpl.squirrelRegistrationOk) {
            sState = "Login " + domain + "Succeeded!";
            // ToastUtil.s(this, "云对讲登录成功");
            squirrelCallImpl.login_state = 1;
        } else if (state == squirrelCallImpl.squirrelRegistrationFailed) {
            sState = "Login " + domain + "Failed!";
            // ToastUtil.s(this, "云对讲登录失败");
            squirrelCallImpl.login_state = 0;
        } else if (state == squirrelCallImpl.squirrelRegistrationCleared) {
            sState = "Logout " + domain + "Succeeded!";
            // ToastUtil.s(this, "云对讲退出登录");
            squirrelCallImpl.login_state = 0;
        } else {
        }
    }

    void HandlerCallState(Bundle _bundle) {
        Bundle tmp = _bundle;
        int state = _bundle.getInt("state");
        // int callid = _bundle.getInt("callid");
        Log.e("SQUIRREL", "----------------current state " + state);
        if (state == squirrelCallImpl.squirrelCallIncomingReceived) {
            preference = MyApplication.getSharedPreferenceUtil();
            Log.e("!preference.getBoolean", !preference.getBoolean(MyApplication.cellphone + "isDND")+"  "+!inDNDTime());
            if (inDNDTime()) {
                if (!preference.getBoolean(MyApplication.cellphone + "isDND")){
                    Intent intent = new Intent(SIPService.this, IncomingActivity.class);
                    intent.putExtras(tmp);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }else{
                Intent intent = new Intent(SIPService.this, IncomingActivity.class);
                intent.putExtras(tmp);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


        } else if (state == squirrelCallImpl.squirrelCallEnd) {
            if (IncomingActivity.instance != null)
                IncomingActivity.instance.finish();
            if (CallingActivity.instance != null)
                CallingActivity.instance.finish();
        } else {
            Handler mh = squirrelCall.getCallHandler();
            if (mh != null) {
                Message msg = new Message();
                msg.setData(_bundle);
                msg.what = 2;
                mh.sendMessage(msg);
            }
        }
    }

    public final static String ACTION_BUTTON = "cc.ldsd.community2.notification.button";
    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    public final static int BUTTON_1 = 1;
    public ButtonBroadcastReceiver bReceiver;

    /**
     * 带按钮的通知栏点击广播接收
     */
    public void initButtonReceiver() {
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(bReceiver, intentFilter);
    }

    /**
     * 广播监听按钮点击时间
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                // 通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_1:// 发送声波给门禁机， 声波开门
                        try {
                            if (MyApplication.cellphone != null && MyApplication.logined) {
//                                voiceWaveHelper.play();
                            } else {
                                ToastUtil.s(context, "您当前还没有登录,请先登录！");
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    default:
                        break;
                }
            }
        }
    }

    //计算分钟数  ，  开始时间小于结束时间是当天时间段；
    //开始时间大于 结束时间是 隔天时间段；
    private boolean inDNDTime() {
        String fromTime = preference.getString(MyApplication.cellphone + "DNDFrom");
        String toTime = preference.getString(MyApplication.cellphone + "DNDTo");
        if (fromTime.length() > 4 && toTime.length() > 4) {
            String[] from = fromTime.split(":");
            String[] to = toTime.split(":");

            int fromMinute = Integer.parseInt(from[0]) * 60 + Integer.parseInt(from[1]);
            int toMinute = Integer.parseInt(to[0]) * 60 + Integer.parseInt(to[1]);
            if (fromMinute == toMinute) {
                return true;
            }
            Calendar c = Calendar.getInstance();
            @SuppressLint("WrongConstant") int nowMunute = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
            if (fromMinute < toMinute) {
                if (nowMunute > fromMinute && nowMunute < toMinute) {
                    return true;
                } else {
                    return false;
                }
            } else {
//                fromMinute > toMinute
                if (nowMunute > fromMinute || nowMunute < toMinute) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
