package cn.etsoft.smarthome;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.ui.SafetyActivity;
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
        //防区警报通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle("警报");
        builder.setContentText("有防区发生警报，请及时处理");
        builder.setSmallIcon(R.drawable.et);
        //收到通知一般有三种用户提示方式：声音，震动，呼吸灯
        builder.setDefaults(Notification.DEFAULT_ALL);
        //所有设置必须在builder.build之前
        //commit，确认刚才的设置，并且生成一个Notification对象
        //创建一个pendingIntent对象用于点击notification之后跳转
        PendingIntent intent1 = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), SafetyActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent1);
        //设置点击之后notification消失
        builder.setAutoCancel(true);
        Notification build = builder.build();
        //创建并在通知栏弹出一个消息
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, build);//1为notification一个标签，可以通过这个标签进行取消等操作
        System.out.println("---> Service onCreate()");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("---> Service onStart()");
        super.onStart(intent, startId);

    }

    Dialog dialog;
    int jingbao = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("---> Service onStartCommand()");
        SharedPreferences sharedPreferences1 = getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        int safety_style = sharedPreferences1.getInt("safety_style", 255);
        if (sharedPreferences1.getBoolean("IsDisarming", false)) {
            return super.onStartCommand(intent, flags, startId);
        }
        String index;
        try {
            //获取触发警报的位置index
            index = intent.getStringExtra("index");
        } catch (Exception e) {
            System.out.println("Exception"+e);
            index = "";
        }
        //警报位置集合
        List<Integer> index_list = new ArrayList<>();
        for (int i = 0; i < index.length(); i++) {
            if (index.charAt(index.length() - i - 1) == '1')
                index_list.add(index.length() - i - 1);
        }
        String message = "";
        try {
            for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
                if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecType() == safety_style) {
                    boolean IsContan = false;
                    for (int j = 0; j < index_list.size(); j++) {
                        if (i == index_list.get(j)) {
                            IsContan = true;
                        }
                    }
                    if (IsContan)
                        message += i + 1 + "、";
                }
            }
        } catch (Exception e) {
            System.out.println("Exception" + e);
            return super.onStartCommand(intent, flags, startId);
        }
        if (!"".equals(message)) {
            message = message.substring(0, message.lastIndexOf("、"));
            sever = this;
            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(this);
            builder.setTitle("报警提示：");
            builder.setMessage("防区 " + message + " 报警！！！");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        MyApplication.mInstance.getSp().stop(jingbao);
                    } catch (Exception e) {
                        System.out.println("Exception" + e);
                    }
                    dialog.dismiss();
                    sever.onDestroy();
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
        System.out.println("---> Service onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("---> Service onDestroy()");
    }
}
