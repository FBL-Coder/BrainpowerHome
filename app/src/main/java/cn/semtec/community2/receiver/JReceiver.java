package cn.semtec.community2.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.semtec.community2.MyApplication;
import cn.semtec.community2.activity.BaseActivity;
import cn.semtec.community2.activity.LoginActivity;
import cn.semtec.community2.database.DBhelper;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;
import cn.jpush.android.api.JPushInterface;
import cn.semtec.community2.util.Util;

public class JReceiver extends BroadcastReceiver {
    private static final String TAG = "JReceiver";
    private int type;
    private SharedPreferenceUtil preference;

    @Override
    public void onReceive(Context context, Intent intent) {

        preference = MyApplication.getSharedPreferenceUtil();
        Bundle bundle = intent.getExtras();
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (extras == null) {
            return;
        }
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Intent i;
            if (!MyApplication.logined) {
                i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else {

                i = new Intent(context, BaseActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	        	i.setAction(BaseActivity.mAction);
                context.startActivity(i);
            }
            Util.OnStopRing();
            Util.audioManager.setSpeakerphoneOn(false);
            return;
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e("rrrrrrr", "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);
//
        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            if (!MyApplication.logined){
                Util.OnPlayRing(context);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Util.OnStopRing();
                        Util.audioManager.setSpeakerphoneOn(false);
                    }
                }).start();
            }

        }

        Log.e("dddddd", "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + extras);

        JSONObject jo;

        try {
            jo = new JSONObject(extras);

            //to为null时  没有特定接受对象  默认收取      特定对象与登录手机相等时  收取
            if ((!jo.isNull("to")) && (!jo.optString("to").equals(preference.getString("cellphone")))) {
                return;
            }

            if (null != jo.optString("msg_type") && !"".equals(jo.optString("msg_type"))){
                type = Integer.parseInt(jo.optString("msg_type"));
            }
            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
            String content = jo.isNull("msg_content") ? alert : (jo.optString("msg_content"));

            ContentValues values = new ContentValues();
            values.put(DBhelper.MESSAGE_ACCOUNT, preference.getString("cellphone"));
            values.put(DBhelper.MESSAGE_DATE, new Date().getTime());
            values.put(DBhelper.MESSAGE_ISREAD, 0);
            values.put(DBhelper.MESSAGE_TYPE, type);
            values.put(DBhelper.MESSAGE_CONTENT, content);
            values.put(DBhelper.MESSAGE_FROM, jo.optString("from"));
            values.put(DBhelper.MESSAGE_URL, jo.isNull("msg_url") ? "" : jo.optString("msg_url"));


            SQLiteDatabase db = MyApplication.getDB();
            db.insert(DBhelper.MESSAGE, null, values);
            db.close();


//			if(BaseActivity.instance != null){
//				BaseActivity.instance.setCount();
//			}
            //TODO
//			if(MessageFragment.instance != null){
//			    MessageFragment.instance.initData();
//			    MessageFragment.instance.adapter.notifyDataSetChanged();
//			}

        } catch (JSONException e) {
            CatchUtil.catchM(e);
        } finally {
            switch (type) {
                //审核通过
                case 1:
//				squirrelCallImpl instance = (squirrelCallImpl)context.getApplicationContext();
//				instance.squirrelAccountLogin(squirrelCallImpl.serverip,squirrelCallImpl.serverport,
//					1, null,MyApplication.sipnum, MyApplication.sippassword, null, 1);
//				MyApplication.approved = true;
                    break;
                //用户申请
                case 3:
                    preference.putBoolean("newRequest", true);
//				if(MyActivity.instance != null){
//					MyActivity.instance.tv_hadRequest.setVisibility(View.VISIBLE);
//				}
                    break;
                case 2:
                    preference.putBoolean("newRequest", true);
                    break;

                default:
                    break;
            }
        }

    }


//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//            //send the Registration Id to your server...
//                        
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);
//        
//        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//        	
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
//            
//        	//打开自定义的Activity
//        	Intent i = new Intent(context, BaseActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
//        	
//        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//        	
//        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
//        } else {
//        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//        }
//	}

    // 打印所有的 intent extra 数据
//	private static String printBundle(Bundle bundle) {
//		StringBuilder sb = new StringBuilder();
//		for (String key : bundle.keySet()) {
//			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
//				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
//				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
//					Log.i(TAG, "This message has no Extra data");
//					continue;
//				}
//
//				try {
//					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//					Iterator<String> it =  json.keys();
//
//					while (it.hasNext()) {
//						String myKey = it.next().toString();
//						sb.append("\nkey:" + key + ", value: [" +
//								myKey + " - " +json.optString(myKey) + "]");
//					}
//				} catch (JSONException e) {
//					Log.e(TAG, "Get message extra JSON error!");
//				}
//
//			} else {
//				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//			}
//		}
//		return sb.toString();
//	}

    //send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
//	}
}
