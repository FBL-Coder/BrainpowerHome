package cn.semtec.community2.util;

import android.view.Gravity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.etsoft.smarthome.R;


/**
 * Created by Sai on 15/8/9.
 */
public class PickerViewAnimateUtil {
	private static final int INVALID = -1;
	/**
	 * Get default animation resource when not defined by the user
	 *
	 * @param gravity       the gravity of the dialog
	 * @param isInAnimation determine if is in or out animation. true when is is
	 * @return the id of the animation resource
	 */
	public static int getAnimationResource(int gravity, boolean isInAnimation) {
		switch (gravity) {
		case Gravity.BOTTOM:
			return isInAnimation ? R.anim.slide_in_bottom : R.anim.slide_out_bottom;
		}
		return INVALID;
	}
	public static String getLongTime(int dayType) {
		Calendar c = Calendar.getInstance(); // 当时的日期和时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int day; // 需要更改的天数
		day = c.get(Calendar.DAY_OF_MONTH) + dayType;
		c.set(Calendar.DAY_OF_MONTH, day);
//		System.out.println(df.format(c.getTime()));
		return df.format(c.getTime());
	}
	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}
	public static String getTime2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		return format.format(date);
	}
}
