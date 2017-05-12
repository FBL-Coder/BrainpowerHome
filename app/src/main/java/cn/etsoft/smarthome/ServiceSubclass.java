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

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * 安防警报服务
 */
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
    int jingbao = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        int safety_style = sharedPreferences1.getInt("safety_style", 255);
        if (sharedPreferences1.getBoolean("IsDisarming", false)) {
            return super.onStartCommand(intent, flags, startId);
        }
        //获取触发警报的位置index
        String index = intent.getStringExtra("index");
        //警报位置集合
        List<Integer> index_list = new ArrayList<>();
        for (int i = 0; i < index.length(); i++) {
            if (index.charAt(index.length() - i - 1) == '1')
                index_list.add(index.length() - i - 1);
        }
        String message = "";
        for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
            if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecType() == safety_style) {
                boolean IsContan = false;
                for (int j = 0; j < index_list.size(); j++) {
                    if (i == index_list.get(j)) {
                        IsContan = true;
                    }
                }
                if (IsContan)
                    message += i+1 + "、";
            }
        }
        if (!"".equals(message)) {
            message = message.substring(0, message.lastIndexOf("、"));
            sever = this;
            System.out.println("---> Service onStartCommand()");
            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(this);
            builder.setTitle("报警提示：");
            builder.setMessage("防区 " + message + " 报警！！！");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        MyApplication.mInstance.getSp().stop(jingbao);
                    } catch (Exception e) {
                    }
                    dialog.dismiss();
                    sever.stopSelf();
                }
            });
            dialog = builder.create();
            //要弹出全局dialog必须设置
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            if (!dialog.isShowing()) {
                dialog.show();
                jingbao = MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic1(), 1, 1, 0, 0, 1);
            }
            //弹出警报信息，5秒钟不操作，dialog自动消失
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        try {
                            MyApplication.mInstance.getSp().stop(jingbao);
                        } catch (Exception e) {
                        }
                        dialog.dismiss();
                        sever.stopSelf();
                    }
                }
            }, 5000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("---> Service onDestroy()");
    }
}
