package cn.etsoft.smarthome.pullmi.app;

import android.content.Context;

public class GlobalVars {

	private static Context context;
	private static int sn;
	private static String devid, devpass;
	private static String dstip;

	public static void init(Context c) {
		context = c;
	}

	public static Context getContext() {
		return context;
	}

	public static String getDevid() {
		return devid;
	}

	public static void setDevid(String devid) {
		GlobalVars.devid = devid;
	}

	public static String getDevpass() {
		return devpass;
	}

	public static void setDevpass(String devpass) {
		GlobalVars.devpass = devpass;
	}

}
