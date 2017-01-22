package cn.semtec.community2.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import org.linphone.squirrel.squirrelCallImpl;

import java.util.Calendar;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.fragment.VideoFragment;
import cn.semtec.community2.util.SharedPreferenceUtil;
import cn.semtec.community2.util.Util;

public class IncomingActivity extends MyBaseActivity implements OnClickListener {
    public static Activity instance;

    private TextView btn_answer;
    private TextView btn_hangup;
    private TextView tv_comefrom;
    private MediaPlayer mMediaPlayer;
    private Bundle _bundle;
    private squirrelCallImpl myCallImpl;
    private int currentCallID;
//    private AudioManager audioManager;
    private Vibrator vibrator;
    private SharedPreferenceUtil preference;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_comefrom.setText(msg.getData().getString("name"));
                    break;
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myCallImpl = (squirrelCallImpl) getApplication();
        instance = this;
        preference = MyApplication.getSharedPreferenceUtil();
        setView();
        setListener();
        _bundle = getIntent().getExtras();
        currentCallID = _bundle.getInt("callid");
//        if (!preference.getBoolean(MyApplication.cellphone + "isDND") && !inDNDTime()) {
            Util.OnPlayRing(getApplication());
            OnVibrator();
//        }
    }

    private void setView() {
        btn_answer = (TextView) findViewById(R.id.btn_answer);
        btn_hangup = (TextView) findViewById(R.id.btn_hangup);
        tv_comefrom = (TextView) findViewById(R.id.tv_comefrom);
        checkName();
    }

    private void setListener() {
        btn_answer.setOnClickListener(this);
        btn_hangup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_answer:
                Intent intent = new Intent(this, CallingActivity.class);
                intent.putExtras(_bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                myCallImpl.squirrelAnswer(currentCallID, 1);
                finish();
                break;
            case R.id.btn_hangup:
                if (currentCallID == -1 || currentCallID == 0) {
                    currentCallID = myCallImpl.getCurrentCallId();
                } else {
                    myCallImpl.squirrelTerminate(currentCallID);
                    finish();
                }
                break;

            default:
                break;
        }
    }

   /* private void OnPlayRing() {
        try {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(getApplication(), alert);
            audioManager.setSpeakerphoneOn(true);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }

    private void OnStopRing() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(false);
            mMediaPlayer.stop();
        }
    }*/

    private void OnVibrator() {
        vibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
        /*参数1：表示停100ms, 震500ms, 停200ms，震1000ms
        *参数2：-1 表示不重复，非-1表示从指定的下标开始重复震动！
		*如第二个参数是0, 则一圈一圈的循环震动下去了；
		*如果是2，这第一遍震动后，从"200”这个参数开始再循环震动！
		*/
        vibrator.vibrate(new long[]{100, 500, 200, 1000}, 0);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (vibrator != null) {
            vibrator.cancel();
        }
        Util.OnStopRing();
        Util.audioManager.setSpeakerphoneOn(false);
        instance = null;
        super.onDestroy();

    }
    private void checkName() {
        new Thread() {
            public void run() {
                String username = null;
                if(_bundle != null && _bundle.containsKey("username")){
                     username = _bundle.getString("username");
                }
                String name;
                if (username == null || VideoFragment.mlist == null)
                    return;
                if (username == null){
                    username = "未知";
                }
                for (int i = 0; i < VideoFragment.mlist.size(); i++) {
                    String sip = VideoFragment.mlist.get(i).get("obj_sipnum");
                    if (sip.equals(username)) {
                        name = VideoFragment.mlist.get(i).get("obj_name");
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        msg.setData(bundle);
                        msg.what = 1;
                        handler.sendMessage(msg);
                        _bundle.putString("devicename", name);
                        return;
                    }
                }
            }
        }.start();
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
            int nowMunute = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
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
