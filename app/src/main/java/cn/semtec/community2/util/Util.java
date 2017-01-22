package cn.semtec.community2.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {

	/**   
	 * 十六进制转换字符串  
	 * @param hexStr str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串  
	 */      
	public static String hexStr2Str(String hexStr)    
	{      
	    String str = "0123456789ABCDEF";      
	    char[] hexs = hexStr.toCharArray();      
	    byte[] bytes = new byte[hexStr.length() / 2];      
	    int n;      
	  
	    for (int i = 0; i < bytes.length; i++)    
	    {      
	        n = str.indexOf(hexs[2 * i]) * 16;      
	        n += str.indexOf(hexs[2 * i + 1]);      
	        bytes[i] = (byte) (n & 0xff);      
	    }      
	    return new String(bytes);      
	}  
	/**
	 * 字节数组转为十六进制显示
	 */
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes, int size) {
		char[] hexChars = new char[size * 2];
		for (int j = 0; j < size; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	//本方法判断自己些的一个Service-->com.android.controlAddFunctions.PhoneService是否已经运行  
	public static boolean isWorked(Context context) {  
	  ActivityManager myManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
	  ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);  
	  for(int i = 0 ; i<runningService.size();i++)  
	  {  
	    if(runningService.get(i).service.getClassName().toString().equals("com.ladystyle.ecommunity.service.BluetoothLeService"))  
	  {  
	    return true;  
	   }  
	  }  
	  return false;  
	 }

	/**
	 *判断当前应用程序处于前台还是后台
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static MediaPlayer mMediaPlayer;
	public static AudioManager audioManager;
	public static void OnPlayRing(Context context) {
		try {
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(context, alert);
			audioManager.setSpeakerphoneOn(true);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {
			CatchUtil.catchM(e);
		}
	}

	public static void OnStopRing() {
		if (mMediaPlayer != null) {
			mMediaPlayer.setLooping(false);
			mMediaPlayer.stop();
		}
	}
	public static String getRandowNumber(){
		String pass = "";
		Random random=new Random();
		for (int i = 0; i <4; i++) {
			pass+=random.nextInt(10);
		}
		return pass;
	}
}
