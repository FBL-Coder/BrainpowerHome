package cn.semtec.community2.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import cc.ldsd.common.util.SipCompress;
import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.activity.BaseActivity;
import cn.semtec.community2.util.ToastUtil;
import voice.encoder.DataEncoder;
import voice.encoder.VoicePlayer;
import voice.encoder.VoicePlayerListener;

/**
 * Created by Ladystyle005 on 2016/7/27.
 */
public class VoiceWaveHelper {
    private Context context;
    private VoicePlayer player;//声波播放类
    private AudioManager am;
    private int currentVol;//当前系统音量
    private MediaPlayer mediaplayer;

    public VoiceWaveHelper(Context context) {
        this.context = context;
        setVoicePlayer();

        mediaplayer = MediaPlayer.create(context, R.raw.xiu);
    }

    //初始化 声波
    private void setVoicePlayer() {
        int freqs[] = new int[19];
        int baseFreq = 16000;
        for (int i = 0; i < freqs.length; i++) {
            freqs[i] = baseFreq + i * 150;
        }
        player = new VoicePlayer();
        player.setFreqs(freqs);// 设置发送声波的频段

        player.setListener(new VoicePlayerListener() { // 声音的临时加大 和恢复；
            @Override
            public void onPlayStart(VoicePlayer arg0) {
                am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                currentVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
                mediaplayer.setLooping(true);
                mediaplayer.start();
                if (BaseActivity.instance != null && BaseActivity.instance.handler != null)
                    BaseActivity.instance.handler.sendEmptyMessage(1);
            }

            @Override
            public void onPlayEnd(VoicePlayer arg0) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol, 0);
                mediaplayer.setLooping(false);
                if (BaseActivity.instance != null && BaseActivity.instance.handler != null)
                    BaseActivity.instance.handler.sendEmptyMessage(2);
            }
        });
    }

    public void play() {
        if (MyApplication.houseProperty != null && MyApplication.cellphone != null) {
            if (player.isStopped()) {
                // TODO 声音 控制
                player.setPlayerType(VoicePlayer.PT_SoundPlayer);
                // String name =Environment.getExternalStorageDirectory().getAbsolutePath();
                // 生成文件
//                player.setWavPlayer(name + "/Download/player_out.wav");
//                player.mixAssetWav(context.getAssets(), "xiu.wav", 0.3f, 1000); // 提示音效，咻咻咻~
                String compressStr = SipCompress.compress(MyApplication.houseProperty.sipnum, MyApplication.cellphone);
                String vs = DataEncoder.encodeString(compressStr);
                player.play(vs, 1, 1000);// 发送声波
            }
        } else {
            ToastUtil.s(context, "您当前还没有房产，请先绑定房产！");
        }
    }
}
