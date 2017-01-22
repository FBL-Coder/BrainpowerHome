package cn.semtec.community2.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class ToastUtil {
	private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };
	
	public static void l(Context context,String text) {
		 mHandler.removeCallbacks(r);
	        if (mToast != null){
	        	mToast.setText(text);
	        	mToast.setDuration(Toast.LENGTH_LONG);
	        }
	        else{
	        	mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
	        }
	        mToast.show();
	}
	public static void s(Context context,String text) {
		mHandler.removeCallbacks(r);
        if (mToast != null){
        	mToast.setText(text);
        	mToast.setDuration(Toast.LENGTH_SHORT);
        }
        else{
        	mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        mToast.show();
	}

}
