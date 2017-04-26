package cn.etsoft.smarthome;

import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.widget.CustomDialog_comment;

public class ServiceSubclass extends Service {

    private ServiceSubclass sever;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        System.out.println("---> Service onCreate()");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("---> Service onStart()");
    }
    Dialog dialog;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String index = intent.getStringExtra("index");
        List<Integer> index_list = new ArrayList<>();
        for (int i = 0; i < index.length(); i++) {
            if (index.charAt(index.length() - i - 1) == '1')
                index_list.add(i);
        }
        String message = "";
        for (int i = 0; i < index_list.size(); i++) {
            message += index_list.get(i) + "、";
        }
        message = message.substring(0, message.lastIndexOf("、"));
        sever = this;
        System.out.println("---> Service onStartCommand()");
        CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(this);
        builder.setTitle("报警提示：");
        builder.setMessage("防区 " + message + " 报警！！！");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sever.stopSelf();
            }
        });
//        Builder builder = new Builder(getApplicationContext());
//        builder.setTitle("报警提示：");
//        builder.setMessage("防区 " + message + " 报警！！！");
//        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                sever.stopSelf();
//            }
//        });
        dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        if (!dialog.isShowing())
            dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("---> Service onDestroy()");
    }


}
