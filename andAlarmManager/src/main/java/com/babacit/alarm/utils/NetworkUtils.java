package com.babacit.alarm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	public static boolean isNetWorkOk(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
		if (networkInfo != null && networkInfo.length > 0) {
			for (int i = 0; i < networkInfo.length; i++) {
				if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}
}
