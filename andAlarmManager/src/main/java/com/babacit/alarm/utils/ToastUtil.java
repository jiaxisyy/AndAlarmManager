package com.babacit.alarm.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void showToast(Context context, String msg) {
		if (msg == null || msg.isEmpty()) {
			return;
		}
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
