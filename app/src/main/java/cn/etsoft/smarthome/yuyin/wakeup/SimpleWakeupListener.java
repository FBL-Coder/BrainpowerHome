package cn.etsoft.smarthome.yuyin.wakeup;


import cn.etsoft.smarthome.MyApplication;

/**
 * Created by fujiayi on 2017/6/21.
 */

public class SimpleWakeupListener implements IWakeupListener {


    public SimpleWakeupListener() {

    }

    private static final String TAG = "SimpleWakeupListener";

    @Override
    public void onSuccess(String word, WakeUpResult result) {
//        Logger.info(TAG, "唤醒成功，唤醒词：" + word);
        MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic_wozai(), 1, 1, 0, 0, 1);
    }

    @Override
    public void onStop() {
//        Logger.info(TAG, "唤醒词识别结束：");
    }

    @Override
    public void onError(int errorCode, String errorMessge, WakeUpResult result) {
//        Logger.info(TAG, "唤醒错误：" + errorCode + ";错误消息：" + errorMessge + "; 原始返回" + result.getOrigalJson());
    }

    @Override
    public void onASrAudio(byte[] data, int offset, int length) {
//        Logger.error(TAG, "audio data： " + data.length);
    }

}
