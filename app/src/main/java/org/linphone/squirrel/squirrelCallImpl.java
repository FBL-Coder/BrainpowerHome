package org.linphone.squirrel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.linphone.mediastream.MediastreamerAndroidContext;

import java.util.Timer;

import cn.semtec.community2.MyApplication;
import cn.semtec.community2.activity.CallingActivity;
import cn.semtec.community2.service.SIPService;
import cn.semtec.community2.util.ToastUtil;

public class squirrelCallImpl extends MyApplication {
    public static final int squirrelPlayerEof = 27;
    public static final int squirrelCallIdle = 0;
    /**
     * <Initial call state
     */
    public static final int squirrelCallIncomingReceived = 1;
    /**
     * <This is a new incoming call
     */
    public static final int squirrelCallOutgoingInit = 2;
    /**
     * <An outgoing call is started
     */
    public static final int squirrelCallOutgoingProgress = 3;
    /**
     * <An outgoing call is in progress
     */
    public static final int squirrelCallOutgoingRinging = 4;
    /**
     * <An outgoing call is ringing at remote end
     */
    public static final int squirrelCallOutgoingEarlyMedia = 5;
    /**
     * <An outgoing call is proposed early media
     */
    public static final int squirrelCallConnected = 6;
    /**
     * <Connected, the call is answered
     */
    public static final int squirrelCallStreamsRunning = 7;
    /**
     * <The media streams are established and running
     */
    public static final int squirrelCallPausing = 8;
    /**
     * <The call is pausing at the initiative of local end
     */
    public static final int squirrelCallPaused = 9;
    /**
     * < The call is paused, remote end has accepted the pause
     */
    public static final int squirrelCallResuming = 10;
    /**
     * <The call is being resumed by local end
     */
    public static final int squirrelCallRefered = 11;
    /**
     * <The call is being transfered to another party, resulting in a new
     * outgoing call to follow immediately
     */
    public static final int squirrelCallError = 12;
    /**
     * <The call encountered an error
     */
    public static final int squirrelCallEnd = 13;
    /**
     * <The call ended normally
     */
    public static final int squirrelCallPausedByRemote = 14;
    /**
     * <The call is paused by remote end
     */
    public static final int squirrelCallUpdatedByRemote = 15;
    /**
     * <The call's parameters change is requested by remote end, used for
     * example when video is added by remote
     */
    public static final int squirrelCallIncomingEarlyMedia = 16;
    /**
     * <We are proposing early media to an incoming call
     */
    public static final int squirrelCallUpdating = 17;
    /**
     * <A call update has been initiated by us
     */
    public static final int squirrelCallReleased = 18;
    /**
     * < The call object is no more retained by the core
     */

    public static final int squirrelRegistrationProgress = 19;
    /**
     * <Registration is in progress
     */
    public static final int squirrelRegistrationOk = 20;
    /**
     * < Registration is successful
     */
    public static final int squirrelRegistrationCleared = 21;
    /**
     * < Unregistration succeeded
     */
    public static final int squirrelRegistrationFailed = 22;
    /**
     * <Registration failed
     */
    public static final int squirrelHasUnlock = 23;
    /**
     * hava lock msg arrived
     */
    public static final int squirrelMessageArrived = 24;
    /**
     * new text message arrived
     */
    public static final int squirrelMessageDelivered = 25;
    /**
     * send text message succeeded
     */
    public static final int squirrelMessageNotDelivered = 26;
    /**
     * send text message failed
     */

    public static final int serverport = 9647;
    public static final String HANGUP = "qingguajiba :-)";
    public static final String OPENDOOR = "squirrelunlock";

    public long currentCallId = -1;
    private int currentCallState = -1;
    private int currentRegState = -1;
    private Timer timer = null;
    private Handler mHandler = null;
    Handler callHandler = null;
    // 是否登录到SIP
    public static int login_state = 0;
    public static boolean help2terminate;

    public void setHandler(Handler _handle) {
        mHandler = _handle;
    }

    public void setCallHandler(Handler _h) {
        callHandler = _h;
    }

    public Handler getCallHandler() {
        return callHandler;
    }

    public int isReceivedCall() {
        if (currentCallState == squirrelCallIncomingReceived) {
            return 1;
        }
        return 0;
    }

    public long getCurrentCallId() {
        return currentCallId;
    }

    public void regState(String domain, String reason, int state) // 由用户实现，被底层调用。
    {

        if (mHandler == null)
            return;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        msg.what = 1;
        bundle.putInt("state", state);
        bundle.putString("domain", domain);
        bundle.putString("reason", reason);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // if(state == squirrelRegistrationProgress) {
        // Log.d("SQUIRREL", "login " + domain + " progressing..."+
        // "reason:"+reason);
        //
        // }else if(state == squirrelRegistrationOk) {
        // Log.d("SQUIRREL", "login " + domain + " Succeeded"+
        // "reason:"+reason);
        //
        // }else if(state == squirrelRegistrationCleared) {
        // Log.d("SQUIRREL", "logout " + domain + " Succeeded"+
        // "reason:"+reason);
        //
        // }else if(state == squirrelRegistrationFailed) {
        // Log.d("SQUIRREL", "login " + domain + " Failed" + "reason:"+reason);
        // }
    }

    @Override
    public void onCreate() {
        try {
            super.onCreate();
            System.loadLibrary("gnustl_shared");
            System.loadLibrary("ffmpeg-squirrel-armeabi-v7a");
            System.loadLibrary("bctoolbox-armeabi-v7a");
            System.loadLibrary("ortp-armeabi-v7a");
            System.loadLibrary("openh264");
            System.loadLibrary("mediastreamer_base-armeabi-v7a");
            System.loadLibrary("mediastreamer_voip-armeabi-v7a");
            System.loadLibrary("squirrel-armeabi-v7a");
            System.loadLibrary("jcore100");

            MediastreamerAndroidContext.setContext(this);
            squirrelSetPowerManager(this.getSystemService(Context.POWER_SERVICE));
            squirrelSetKeepAliveperiod(60 * 1000);
            Log.d("SQUIRREL", "------------" + this.getApplicationInfo().nativeLibraryDir);
            squirrelInit(this, this.getApplicationInfo().nativeLibraryDir);
//            squirrelSetStunServer("stun.linphone.org");
            squirrelSetStunServer("stun.3cx.com");
            squirrelSetFirewall(1);
            //	squirrelSetKeepAliveperiod(30);
            squirrelSetAgent("android-weedoor");

            squirrelGetICEState(0);
            //squirrelGetICEState(1);
            squirrelSetVideoSizeByName("cif");
            squirrelSelectOnlyAudioCodec("iLBC", "opus");
            startService(new Intent(this, SIPService.class));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void callState(String username, String nickname, int state, long callid, int onlyaudio) {// 由用户实现，被底层调用。{

//        Log.i(TAG, "callState:我就试试看怎么走");
        if (mHandler == null) {
            return;
        }
        String _nickname = username;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        msg.what = 2;
        bundle.putInt("state", state);
        bundle.putLong("callid", callid);
        bundle.putString("username", username);
        if (nickname != null) {
            _nickname = nickname;
        }
        bundle.putString("nickname", _nickname);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        if (state == squirrelCallOutgoingInit) {
            // start other activity;
            currentCallId = callid;
        } else if (state == squirrelCallEnd) {
            if (currentCallId == callid)
                currentCallId = -1;
        }
        currentCallState = state;
    }

    public void messageState(String from, String text, int state, long userdata)// 由用户实现，被底层调用
    {
        if (state == squirrelMessageArrived) {
            if (text.equals(HANGUP) && CallingActivity.instance != null && !CallingActivity.instance.isFinishing()) {
                CallingActivity.instance.finish();
                return;
            }
        }
        if (userdata == 100) {
            if (state == squirrelMessageDelivered) {
                // Log.d("SQUIRREL","send text "+userdata+" ok \n");
                ToastUtil.s(this, "开门成功");
            } else if (state == squirrelMessageNotDelivered) {
                // Log.d("SQUIRREL","send text "+userdata+" failed \n");
                ToastUtil.s(this, "开门失败");
            } else if (state == squirrelMessageArrived) {
                Log.d("SQUIRREL", "from " + from + "recevied text: " + text + "\n");
            }

            // 手机接听切换
        } else if (userdata == 200) {
            if (state == squirrelMessageDelivered) {
                // ToastUtil.s(this, "切换成功");
            } else if (state == squirrelMessageNotDelivered) {
                // ToastUtil.s(this, "切换失败");
            }
            //挂断通话


        } else {
        }
    }

    public void testEchoValue(int state, int delay) {
        Log.d("SQUIRREL", "......" + state + "-------" + delay);
        if (mHandler == null)
            return;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        msg.what = 1;
        bundle.putInt("state", 1234);
        bundle.putString("domain", "dd");
        bundle.putString("reason", "11");
        bundle.putInt("delay", delay);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    public void playerEof() //由用户实现，被底层调用
    {
        if (mHandler == null)
            return;

        Message msg = new Message();
        Bundle bundle = new Bundle();
        msg.what = 3;
        bundle.putInt("state", squirrelPlayerEof);
        Log.d("SQUIRREL", "player eof--------------------------");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    public native void squirrelUninit();

    public native void squirrelIterate(); // 每隔20MS调用一次。

    public native void squirrelCall(String username, int av);

    public native void squirrelAnswer(int callid, int av);

    public native void squirrelTerminate(int callid);

    public native void squirrelUnlock(int callid);

    public native void squirrelUpdateCall(int callid);

    public native void squirrelVideoSnapshot(String path);

    public native void squirrelSwitchVideo(int callid, int oc);

    public native void squirrelAccountLogin(String proxy, int port, int protocol, String outbound, String username,
                                            String passwd, String nickname, int isdefault);

    public native void squirrelAccountExit(String proxy, int port, String username);

    public native void squirrelSetCleanProxyAndAccount();

    public native void squirrelSetStunServer(String stunserver);

    public native void squirrelSetFirewall(int firewall);

    public native void squirrelSetGlobalVideo(int local, int remote);

    public native void squirrelSetCaFilePath(String path);

    public native void squirrelSetRemoteVideoWindow(Object window);

    public native void squirrelSetLocalVideoWindow(Object window);

    public native void squirrelSetCameraRotation(int rotation);

    public native String squirrelGetInstanceUUID();

    public native void squirrelSetInstanceUUID(String uuid);

    public native void squirrelSetCamera(int id);

    public native int squirrelGetCamera();

    public native void squirrelSetLogEnabled(int enabled);

    public native void squirrelSetPowerManager(Object obj);

    // 1008
    public native void squirrelSetMicMuted(int isEnabled);

    public native void squirrelSendMessage(String toUserName, String proxy, int port, String content, int userdata);

    public native void squirrelDestroyChat(String toUserName, String proxy, int port);

    public native int squirrelGetAudioCodecSize();

    public native long squirrelGetAudioCodec(int idx);

    public native String squirrelGetAuidoCodecName(long pt);

    public native void squirrelResetAudioCodecList(long pt, int isend);

    public native void squirrelSetNetworkReachable(int isReachable);

    public native void squirrelRefreshRegisters();

    // 1009
    public native int squirrelGetICEState(int isVideo);

    public native float squirrelGetDownloadBandwidth(int isVideo);

    public native float squirrelGetUploadBandwidth(int isVideo);

    public native float squirrelGetRoundTripDelay(int isVideo);

    public native float squirrelGetLocalLossRate(int isVideo);

    public native float squirrelGetLocalLateRate(int isVideo);

    public native float squirrelGetCurrentQuality();

    //////
    public native void squirrelInit(Object obj, String pluginspath);

    public native void squirrelAnswer(long callid, int av);

    public native void squirrelTerminate(long callid);

    public native void squirrelUnlock(long callid);

    public native void squirrelUpdateCall(long callid);

    public native void squirrelSwitchVideo(long callid, int oc);

    public native void squirrelSetRecordFilePath(String filepath);

    public native void squirrelStartRecord();

    public native void squirrelStopRecord();

    public native long squirrelCreateLocalPlayer(Object window); //return playerPtr

    public native int squirrelDestroyLocalPlayer(long playerptr);

    public native int squirrelPlayerOpen(long playerPtr, String filename);

    public native int squirrelPlayerStart(long playerPtr);

    public native int squirrelPlayerClose(long playerPtr);

    public native void squirrelSetVideoSizeByName(String name);

    public native int squirrelStartEchoTest();

    public native int squirrelSetEchoValue(int delay);

    public native int squirrelSelectOnlyAudioCodec(String name, String name1);

    public native void squirrelSetPlaybackGainDB(float db);

    public native void squirrelSetlePath(String path);

    public native void squirrelSetRingbackFilePath(String path);

    public native void squirrelSetRingFilePath(String path);

    public native void squirrelSetEQ(String device, String value);

    public native void squirrelSetMicGainDB(float db);

    public native void squirrelSetAgent(String agent);

    public native void squirrelSetKeepAliveperiod(int sec);
}
