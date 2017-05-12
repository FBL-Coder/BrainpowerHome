package cn.etsoft.smarthome.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 网络工具类
 */
public class NetUtil {
	public static final int NETWORK_NONE = 0;
	public static final int NETWORK_WIFI = 1;
	public static final int NETWORK_MOBILE = 2;

	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Wifi
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORK_WIFI;
		}

		try {
			// 3G
			state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState();
			if (state == State.CONNECTED || state == State.CONNECTING) {
				return NETWORK_MOBILE;
			}
		}catch (Exception e){
			return NETWORK_NONE;
		}
		return NETWORK_NONE;
	}
}
