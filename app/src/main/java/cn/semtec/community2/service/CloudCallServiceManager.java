package cn.semtec.community2.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mac on 15/9/28.
 */
public class CloudCallServiceManager extends BroadcastReceiver {
    public static final String TAG = "SemtecCloudCall";
    @Override
    public void onReceive(Context context, Intent intent) {
    	//来电 广播接收器
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent service = new Intent(context,SIPService.class);
            context.startService(service);
        }
//        else if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
//            //Intent service = new Intent(context,SIPService.class);
//            //context.startService(service);
//        }
    }

}
