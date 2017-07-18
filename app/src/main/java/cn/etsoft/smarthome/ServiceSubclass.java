package cn.etsoft.smarthome;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import cn.etsoft.smarthome.domain.Safety_Data;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;

/**
 * 安防警报服务
 */
public class ServiceSubclass extends Service {

    private ServiceSubclass sever;
//    private String message = "";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        System.out.println("---> Service onCreate()");
        //防区警报通知
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//        builder.setContentTitle("警报");
//        builder.setContentText("有防区发生警报，请及时处理");
//        builder.setSmallIcon(R.drawable.et);
//        //收到通知一般有三种用户提示方式：声音，震动，呼吸灯
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        //所有设置必须在builder.build之前
//        //commit，确认刚才的设置，并且生成一个Notification对象
//        //创建一个pendingIntent对象用于点击notification之后跳转
//        PendingIntent intent1 = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), SafetyActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(intent1);
//        //设置点击之后notification消失
//        builder.setAutoCancel(true);
//        Notification build = builder.build();
//        //创建并在通知栏弹出一个消息
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        nm.notify(1, build);//1为notification一个标签，可以通过这个标签进行取消等操作
        System.out.println("---> Service onCreate()");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("---> Service onStart()");
        System.out.println("---> Service onStart()");
    }

    /**
     * 回调接口
     * 警报一触发，主页安防页面刷新
     */
    private static OnGetSafetyDataListener onGetSafetyDataListener;

    public static void setOnGetSafetyDataListener(OnGetSafetyDataListener ongetSafetyDataListener) {
        onGetSafetyDataListener = ongetSafetyDataListener;
    }

    public interface OnGetSafetyDataListener {
        void getSafetyData();
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
        int year = 0, month = 0, day_of_month = 0, hour = 0, minute = 0, second = 0;
        try {
            Calendar cal = Calendar.getInstance();
            //当前年
            year = cal.get(Calendar.YEAR);
            //当前月
            month = (cal.get(Calendar.MONTH)) + 1;
            //当前月的第几天：即当前日
            day_of_month = cal.get(Calendar.DAY_OF_MONTH);
            //当前时：HOUR_OF_DAY-24小时制；HOUR-12小时制
            hour = cal.get(Calendar.HOUR_OF_DAY);
            //当前分
            minute = cal.get(Calendar.MINUTE);
            //当前秒
            second = cal.get(Calendar.SECOND);
            //获取触发警报的位置index
            index = intent.getStringExtra("index");

        } catch (Exception e) {
            System.out.println("Exception" + e);
            index = "";
        }
        String message = "";
        //警报位置集合
        List<Integer> index_list = new ArrayList<>();
        for (int i = 0; i < index.length(); i++) {
            if (index.charAt(index.length() - i - 1) == '1')
                index_list.add(index.length() - i - 1);
        }
        try {
            for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
                if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecType() == safety_style) {
                    boolean IsContain = false;
                    for (int j = 0; j < index_list.size(); j++) {
                        if (i == index_list.get(j)) {
                            IsContain = true;
                        }
                    }
                    if (IsContain)
                        message += i + 1 + "、";
                }
            }
        } catch (Exception e) {
            return super.onStartCommand(intent, flags, startId);
        }
        if (!"".equals(message)) {
            message = message.substring(0, message.lastIndexOf("、"));
            sever = this;
//            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(this);
//            builder.setTitle("报警提示：");
//            builder.setMessage("防区 " + message + " 报警！！！");
            //将被触发的防区以及时间写入文件
            String id = "";
            List<String> ID = new ArrayList<>();
            Safety_Data data = Dtat_Cache.readFile_safety(false);

            if (data == null)
                data = new Safety_Data();
            List<Safety_Data.Data_Data> safety_datalist = data.getDatas();
            if (message.length() == 1) {
                id = message;
                safety_datalist.add(setSafety(year, month, day_of_month, hour, minute, second, id, data));
            } else {
                StringTokenizer st = new StringTokenizer(message, "、");
                while (st.hasMoreTokens()) {
                    String a = st.nextToken();
                    ID.add(a);
                }
                for (int j = 0; j < ID.size(); j++) {
                    safety_datalist.add(setSafety(year, month, day_of_month, hour, minute, second, ID.get(j), data));
                }
            }
            data.setDatas(safety_datalist);
            Dtat_Cache.writeFile_safety(data);
            //警报一触发，主页安防页面刷新
            if (onGetSafetyDataListener != null)
                onGetSafetyDataListener.getSafetyData();
//            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    try {
//                        MyApplication.mInstance.getSp().stop(jingbao);
//                    } catch (Exception e) {
//                        System.out.println("Exception" + e);
//                    }
//                    dialog.dismiss();
//                    sever.onDestroy();
//                }
//            });
//            dialog = builder.create();
            //要弹出全局dialog必须设置
//            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            if (!dialog.isShowing()) {
//                dialog.show();
//                jingbao = MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic1(), 1, 1, 0, 0, 1);
//            }
            //弹出警报信息，5秒钟不操作，dialog自动消失
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (dialog.isShowing()) {
//                        try {
//                            MyApplication.mInstance.getSp().stop(jingbao);
//                        } catch (Exception e) {
//                        }
//                        dialog.dismiss();
//                        sever.stopSelf();
//                    }
//                }
//            }, 5000);
        }
        System.out.println("---> Service onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    //设置报警消息
    private Safety_Data.Data_Data setSafety(int year, int month, int day_of_month, int hour, int minute, int second, String id, Safety_Data data) {
        Safety_Data.Data_Data data_data = data.new Data_Data();
        data_data.setYear(year);
        data_data.setMonth(month);
        data_data.setDay(day_of_month);
        data_data.setH(hour);
        data_data.setM(minute);
        data_data.setS(second);
        data_data.setId(Integer.parseInt(id));
        return data_data;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("---> Service onDestroy()");
    }
}
