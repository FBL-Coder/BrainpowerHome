package cn.etsoft.smarthome.yuyin.control;

import android.content.Context;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import cn.etsoft.smarthome.yuyin.util.Logger;
import cn.etsoft.smarthome.yuyin.wakeup.IWakeupListener;
import cn.etsoft.smarthome.yuyin.wakeup.WakeupEventAdapter;

/**
 * Created by fujiayi on 2017/6/20.
 */

public class MyWakeup {


    private static boolean isInited = false;

    private EventManager wp;
    private EventListener eventListener;

    private static final String TAG = "MyWakeup";

    public MyWakeup(Context context, EventListener eventListener) {
        if (isInited) {
            Logger.error(TAG,"还未调用release()，请勿新建一个新类");
            throw new RuntimeException("还未调用release()，请勿新建一个新类");
        }
        isInited = true;
        this.eventListener = eventListener;
        wp = EventManagerFactory.create(context, "wp");
        wp.registerListener(eventListener);
    }

    public MyWakeup(Context context, IWakeupListener eventListener) {
        this(context,new WakeupEventAdapter(eventListener));
    }
    public void start() {
//        json  参数
//        {"accept-audio-data":false,"disable-punctuation":true,"accept-audio-volume":false,"pid":1936}
//        {"kws-file":"assets:\/\/\/WakeUp.bin","pid":1936}

        String json = "{\"kws-file\":\"assets:\\/\\/\\/WakeUp.bin\",\"pid\":1936}";
        Log.i(TAG + ".Debug", "wakeup params(反馈请带上此行日志):" + json);
        wp.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    public void stop() {
        Logger.info(TAG,"唤醒结束");
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
    }

    public void release(){
        stop();
        wp.unregisterListener(eventListener);
        wp = null;
        isInited  =false;
    }
}
