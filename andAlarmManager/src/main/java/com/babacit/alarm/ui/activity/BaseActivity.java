package com.babacit.alarm.ui.activity;

import com.babacit.alarm.ui.dialog.CustomProgressDialog;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class BaseActivity extends Activity {
	private CustomProgressDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		PushAgent.getInstance(this).onAppStart();
	}
	
	public void showProgressDialog(String msg, boolean canCancel) {
		if (loading != null) {
			loading.cancel();
		} else {
			loading = new CustomProgressDialog(this, msg);
			loading.setCancelable(canCancel);
			loading.show();
		}
	}

	public void dismissProgress() {
		if (loading != null) {
			loading.dismiss();
			loading = null;
		}
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		return false;
//	}
}
