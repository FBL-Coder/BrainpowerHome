package cn.semtec.community2.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

import org.linphone.mediastream.video.AndroidVideoWindowImpl;
import org.linphone.mediastream.video.capture.hwconf.AndroidCameraConfiguration;
import org.linphone.squirrel.squirrelCallImpl;

import java.io.File;
import java.util.Date;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.database.DBhelper;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

@SuppressLint("HandlerLeak")
public class CallingActivity extends MyBaseActivity implements OnClickListener {

    private View btn_handFree;
    private TextView btn_hangup;
    private TextView btn_pic;
    private TextView btn_open;
    private SurfaceView remoteview;
    private SurfaceView localview;
    MediaPlayer mMediaPlayer;
    private TextView tv_handFree;
    private View video;
    private View btn_silence;
    private TextView iv_silence;
    private TextView tv_silence;
    private TextView iv_handFree;
    private TextView tv_comefrom;
    private boolean ismute;
    private boolean isConnect;
    public static CallingActivity instance;
    AudioManager audioManager;
    private squirrelCallImpl myCallImpl;
    private AndroidVideoWindowImpl mVideoWindow;
    int currentCallID;
    String username;
    String nickname;
    String devicename;
    // TextView status = null;

    // ICE status
    final Handler ICEHandler = new Handler();

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:// 呼叫状态
                    HandlerCallState(msg.getData());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Handler videoHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    remoteview.setVisibility(View.VISIBLE);
                    audioManager.setSpeakerphoneOn(true);
                    btn_pic.setEnabled(true);
                    btn_open.setEnabled(true);
                    tv_handFree.setText(getString(R.string.calling_handFree));
                    iv_handFree.setBackgroundResource(R.drawable.calling_handfree);
                    break;
                default:
                    break;
            }
        }
    };

    void HandlerCallState(Bundle _bundle) {
        int state = _bundle.getInt("state");
        currentCallID = _bundle.getInt("callid");
        username = _bundle.getString("username");
        nickname = _bundle.getString("nickname");
        if (state == squirrelCallImpl.squirrelCallStreamsRunning) {
            LogUtils.i(nickname != null ? nickname + "接通" : username + " talking...");
        } else if (state == squirrelCallImpl.squirrelCallEnd) {
            if (!isFinishing()) {
                finish();
            }
        } else if (state == squirrelCallImpl.squirrelCallError) {
            ToastUtil.l(this, getString(R.string.calling_callerror));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        ismute = false;
        instance = this;
        myCallImpl = (squirrelCallImpl) getApplication();
        myCallImpl.setCallHandler(mHandler);

        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle _bundle = this.getIntent().getExtras();
        if (_bundle != null) {
            // int state = _bundle.getInt("state");
            currentCallID = _bundle.getInt("callid");
            username = _bundle.getString("username");
            nickname = _bundle.getString("nickname");
            devicename = _bundle.getString("devicename");
        }
        setView();
        setListener();

        if (currentCallID == 0) {
            // 查看监控时 未传id 默认id=0 设置MIC无声音 ；
            myCallImpl.squirrelSetMicMuted(1);
            ismute = true;
            iv_silence.setBackgroundResource(R.drawable.calling_micoff);
            tv_silence.setText(getString(R.string.calling_mute));
        }
        // Switch to front camera
        int videoDeviceId = myCallImpl.squirrelGetCamera();
        int length = AndroidCameraConfiguration.retrieveCameras().length;
        if (length > 0) {
            videoDeviceId = (videoDeviceId + 1) % length;
        }

        myCallImpl.squirrelSetCamera(videoDeviceId);

        mVideoWindow = new AndroidVideoWindowImpl(remoteview, localview,
                new AndroidVideoWindowImpl.VideoWindowListener() {

                    @Override
                    public void onVideoRenderingSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView surface) {
                        if (myCallImpl != null) {
                            myCallImpl.squirrelSetRemoteVideoWindow(vw);
                            myCallImpl.squirrelSetCameraRotation(
                                    rotationToAngle(getWindowManager().getDefaultDisplay().getRotation()));
                            remoteview = surface;
                        }
                    }

                    @Override
                    public void onVideoRenderingSurfaceDestroyed(AndroidVideoWindowImpl vw) {

                    }

                    @Override
                    public void onVideoPreviewSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView surface) {
                        if (myCallImpl != null) {
                            // localview = surface;
                            // myCallImpl.squirrelSetLocalVideoWindow(localview);
                        }
                    }

                    @Override
                    public void onVideoPreviewSurfaceDestroyed(AndroidVideoWindowImpl vw) {
                        if (myCallImpl != null) {
                            myCallImpl.squirrelSetLocalVideoWindow(null);
                        }
                    }
                }) {
            // 重写 为了延迟视频的出现 在这里调用显示
            @Override
            public void requestRender() {
                super.requestRender();
                if (!isConnect) {
                    videoHandler.sendEmptyMessage(1);
                    isConnect = true;
                }
            }
        };
        if(BaseActivity.instance != null){
            BaseActivity.instance.openScanBlue(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (remoteview != null) {
            ((GLSurfaceView) remoteview).onResume();
        }

        if (mVideoWindow != null) {
            synchronized (mVideoWindow) {
                if (myCallImpl != null)
                    myCallImpl.squirrelSetRemoteVideoWindow(mVideoWindow);
            }
        }
        if (BaseActivity.instance != null){
            BaseActivity.instance.stopBleService();
        }
    }

    @Override
    public void onPause() {
        if (mVideoWindow != null) {
            synchronized (mVideoWindow) {
                if (myCallImpl != null)
                    myCallImpl.squirrelSetRemoteVideoWindow(null);
            }
        }
        if (remoteview != null) {
            ((GLSurfaceView) remoteview).onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        instance = null;
        audioManager.setSpeakerphoneOn(false);
        if (mVideoWindow != null)
            mVideoWindow.release();
        myCallImpl.setCallHandler(null);
        // mic恢复声音 1静音 0不静音
        myCallImpl.squirrelSetMicMuted(0);
        if (BaseActivity.instance != null){
            BaseActivity.instance.startBleService();
        }

        super.onDestroy();
    }

    private void setView() {
        // 视频控件
        video = (View) findViewById(R.id.video);
        remoteview = (SurfaceView) findViewById(R.id.remotevideo);
        btn_handFree = (View) findViewById(R.id.btn_handFree);
        tv_handFree = (TextView) findViewById(R.id.tv_handFree);
        iv_handFree = (TextView) findViewById(R.id.iv_handFree);
        btn_hangup = (TextView) findViewById(R.id.btn_hangup);
        btn_pic = (TextView) findViewById(R.id.btn_pic);
        btn_open = (TextView) findViewById(R.id.btn_open);
        btn_silence = (View) findViewById(R.id.btn_silence);
        iv_silence = (TextView) findViewById(R.id.iv_silence);
        tv_silence = (TextView) findViewById(R.id.tv_silence);
        tv_comefrom = (TextView) findViewById(R.id.tv_comefrom);
        if (devicename != null)
            tv_comefrom.setText(devicename);

        setVideoView();
    }

    private void setVideoView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        LayoutParams lp;
        lp = video.getLayoutParams();
        lp.width = width;
        lp.height = width * 3 / 4;
        video.setLayoutParams(lp);
    }

    private void setListener() {
        btn_handFree.setOnClickListener(this);
        btn_silence.setOnClickListener(this);
        btn_hangup.setOnClickListener(this);
        btn_pic.setOnClickListener(this);
        btn_open.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open:
                // 开门
//                String s = SipCompress.compress(MyApplication.houseProperty.sipnum, MyApplication.cellphone);
                ((squirrelCallImpl) getApplication()).squirrelSendMessage(username, MyApplication.houseProperty.sipaddr,
                        squirrelCallImpl.serverport, squirrelCallImpl.OPENDOOR, 100);

                break;
            case R.id.btn_pic:
                // 保存 视频连接记录
                saveChatRecord();
                break;
            case R.id.btn_hangup:
                // 为防sdk原挂断方法 通信不良 ， 再次发送msg挂断
                ((squirrelCallImpl) getApplication()).squirrelSendMessage(username, MyApplication.houseProperty.sipaddr,
                        squirrelCallImpl.serverport, squirrelCallImpl.HANGUP, 300);
                // 挂断 断开连接
                onTerminate();
                break;
            case R.id.btn_handFree:
                // 切换 免提
                if (audioManager.isSpeakerphoneOn()) {
                    audioManager.setSpeakerphoneOn(false);
                    tv_handFree.setText(getString(R.string.calling_handSet));
                    iv_handFree.setBackgroundResource(R.drawable.calling_handfree_1);
                } else {
                    audioManager.setSpeakerphoneOn(true);
                    tv_handFree.setText(getString(R.string.calling_handFree));
                    iv_handFree.setBackgroundResource(R.drawable.calling_handfree);
                }
                break;
            case R.id.btn_silence:
                // 切换 静音
                if (ismute) {
                    myCallImpl.squirrelSetMicMuted(0);
                    tv_silence.setText(getString(R.string.calling_voice));
                    iv_silence.setBackgroundResource(R.drawable.calling_micon);
                    ismute = false;
                } else {
                    myCallImpl.squirrelSetMicMuted(1);
                    tv_silence.setText(getString(R.string.calling_mute));
                    iv_silence.setBackgroundResource(R.drawable.calling_micoff);
                    ismute = true;
                }
                break;
            default:
                break;
        }

    }

    private void saveChatRecord() {
        File dirFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "recordpic/" + MyApplication.cellphone);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String chatDir = dirFile.toString();
        String fileName = "/" + String.valueOf(new Date().getTime()) + ".jpg";
        String path = chatDir + fileName;

        myCallImpl.squirrelVideoSnapshot(path);

        String date = String.valueOf(new Date().getTime());
        LogUtils.i("date=" + date + "   ,path=" + path);

        ContentValues values = new ContentValues();
        values.put(DBhelper.RECORD_DATE, date);
        values.put(DBhelper.RECORD_PICTURE, path);
        values.put(DBhelper.RECORD_ACCOUNT, MyApplication.cellphone);
        if (devicename != null) {
            values.put(DBhelper.RECORD_DEVICE, devicename);
        } else {
            values.put(DBhelper.RECORD_DEVICE, getString(R.string.calling_machine));
        }

        SQLiteDatabase db = MyApplication.getDB();
        db.insert(DBhelper.VIDEO_RECORD, null, values);
        ToastUtil.s(this, getString(R.string.calling_saved));
        db.close();
    }

    static int rotationToAngle(int r) {
        switch (r) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        onTerminate();
        finish();
    }

    public void onTerminate() {
        try {
            if (currentCallID == -1 || currentCallID == 0) {
                currentCallID = myCallImpl.getCurrentCallId();
            } else {
                myCallImpl.squirrelTerminate(currentCallID);
                finish();
            }
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }
}
