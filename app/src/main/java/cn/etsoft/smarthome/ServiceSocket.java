package cn.etsoft.smarthome;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.view.WindowManager;

import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * 安防警报服务
 */
public class ServiceSocket extends Service {


    private Service service;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        System.out.println("---> Service onCreate()");
        service = this;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("---> Service onStart()");
    }

    Dialog dialog;
    int jingbao = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(this);
            builder.setTitle("提示：");
            builder.setMessage("链接已断开，请重新打开软件");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    service.stopSelf();
                    System.exit(0);

                }
            });
            dialog = builder.create();
            //要弹出全局dialog必须设置
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            if (!dialog.isShowing()) {
                dialog.show();
            }
            //弹出警报信息，5秒钟不操作，dialog自动消失
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        service.stopSelf();
                        System.exit(0);
                    }
                }
            }, 10000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("---> Service onDestroy()");
    }
}
