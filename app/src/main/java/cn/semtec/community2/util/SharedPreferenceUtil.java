package cn.semtec.community2.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
	private Context mContext;

	public SharedPreferenceUtil(Context paramContext) {
		this.mContext = paramContext;
	}

	public SharedPreferences getSharedPreferences() {
		return this.mContext.getSharedPreferences("community",000000);
	}

	public boolean getBoolean(String paramString) {
		return getSharedPreferences().getBoolean(paramString, false);
	}
	public int getInt(String paramString) {
		// int value;
		// if (paramString.contains(BaseApplication.MY_EMOTIONSTATUS)) {
		// try {
		// value = getSharedPreferences().getInt(paramString, 0);
		// } catch (NumberFormatException ne) {
		// value = 0;
		// }
		// return value;
		// }

		return getSharedPreferences().getInt(paramString, 0);
	}

	public String getString(String paramString) {
		return getSharedPreferences().getString(paramString, "");
	}

	public Long getLong(String paramString) {
		return getSharedPreferences().getLong(paramString, 0);
	}

	public void putBoolean(String paramString, Boolean paramBoolean) {
		SharedPreferences.Editor localEditor = getSharedPreferences().edit();
		localEditor.putBoolean(paramString, paramBoolean.booleanValue());
		localEditor.commit();
	}

	public void putInt(String paramString, int paramInt) {
		SharedPreferences.Editor localEditor = getSharedPreferences().edit();
		localEditor.putInt(paramString, paramInt);
		localEditor.commit();
	}

	public void putString(String paramString1, String paramString2) {
		SharedPreferences.Editor localEditor = getSharedPreferences().edit();
		localEditor.putString(paramString1, paramString2);
		localEditor.commit();
	}

	public void putLong(String paramString1, Long paramLong2) {
		SharedPreferences.Editor localEditor = getSharedPreferences().edit();
		localEditor.putLong(paramString1, paramLong2);
		localEditor.commit();
	}

	public void remove(String paramString) {
		SharedPreferences.Editor localEditor = getSharedPreferences().edit();
		localEditor.remove(paramString);
		localEditor.commit();
	}
	public boolean contains(String key) {
		return getSharedPreferences().contains(key);
	}
}
