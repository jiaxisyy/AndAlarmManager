package com.babacit.alarm.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.GetVerifyTypeServer;
import com.babacit.alarm.server.UpdatePhoneNoServer;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.PhoneNumCheckUtil;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChangePhoneNumActivity extends BaseActivity implements
		OnClickListener {
	private EditText mEtOriginNum, mEtNewNum, mEtCode, mEtPwd;
	private String originNum, newNum, code, pwd;
	private Button mBtFetchCode;
	private static final int MSG_FETCH_CODE_SUCCESS = 0;
	private static final int MSG_FETCH_CODE_FAIL = 1;
	private static final int MSG_UPDATE_PHONE_SUCCESS = 2;
	private static final int MSG_UPDATE_PHONE_FAIL = 3;
	private SharedConfig config;
	private static final int MSG_ENABLE_FETCH_BTN = 2003;
	private static final int MSG_REFRESH_BTN_TEXT = 2004;
	private Timer timer;
	private static int mCountDown;

	private RequestCallBack fetchCodeCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(MSG_FETCH_CODE_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_FETCH_CODE_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack updatePhoneNoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(MSG_UPDATE_PHONE_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_UPDATE_PHONE_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_PHONE_SUCCESS:
				Intent data = new Intent();
				data.putExtra("phone", newNum);
				setResult(0, data);
				finish();
				break;
			case MSG_UPDATE_PHONE_FAIL:
				ToastUtil.showToast(getApplicationContext(),
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			case MSG_FETCH_CODE_SUCCESS:

				break;
			case MSG_FETCH_CODE_FAIL:
				ToastUtil.showToast(getApplicationContext(),
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			case MSG_REFRESH_BTN_TEXT:
				if (msg.arg2 == 0) {
					mHandler.sendEmptyMessage(MSG_ENABLE_FETCH_BTN);
				}
				mBtFetchCode.setText(String.valueOf(msg.arg2)
						+ getResources().getString(
								R.string.txt_retry_after_seconds));
				break;
			case MSG_ENABLE_FETCH_BTN:
				mBtFetchCode.setText(getResources().getString(
						R.string.txt_fetch_verification_code));
				mBtFetchCode.setEnabled(true);
				stopTimer();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_phone_num);
		config = new SharedConfig(this);
		bindField();
	}

	private void bindField() {
		mEtOriginNum = (EditText) findViewById(R.id.et_change_phone_number_origin);
		mEtOriginNum.setText(getIntent().getStringExtra("origin"));
		mEtNewNum = (EditText) findViewById(R.id.et_change_phone_number_new);
		mEtCode = (EditText) findViewById(R.id.et_change_phone_num_verification_code);
		mEtPwd = (EditText) findViewById(R.id.et_change_phone_num_input_password);
		findViewById(R.id.btn_change_phone_num_back).setOnClickListener(this);
		findViewById(R.id.btn_confirm).setOnClickListener(this);
		mBtFetchCode = (Button) findViewById(R.id.btn_change_phone_num_fetch_verification_code);
		mBtFetchCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		originNum = mEtOriginNum.getText().toString().trim();
		newNum = mEtNewNum.getText().toString().trim();
		code = mEtCode.getText().toString().trim();
		pwd = mEtPwd.getText().toString().trim();
		switch (v.getId()) {
		case R.id.btn_change_phone_num_back:
			finish();
			break;
		case R.id.btn_confirm:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			if (originNum.equals("")) {
				ToastUtil.showToast(ChangePhoneNumActivity.this, getResources()
						.getString(R.string.txt_phone_number_cannot_be_empty));
				return;
			}
			if (!PhoneNumCheckUtil.isMobileNum(originNum)) {
				ToastUtil.showToast(getApplicationContext(), getResources()
						.getString(R.string.txt_phone_number_wrong_format));
				return;
			}
			if (newNum.equals("")) {
				ToastUtil.showToast(ChangePhoneNumActivity.this, getResources()
						.getString(R.string.txt_phone_number_cannot_be_empty));
				return;
			}
			if (!PhoneNumCheckUtil.isMobileNum(newNum)) {
				ToastUtil.showToast(getApplicationContext(), getResources()
						.getString(R.string.txt_phone_number_wrong_format));
				return;
			}
			if (code.equals("")) {
				ToastUtil
						.showToast(
								ChangePhoneNumActivity.this,
								getResources()
										.getString(
												R.string.txt_verification_code_cannot_be_empty));
				return;
			}
			if (pwd.equals("")) {
				ToastUtil.showToast(ChangePhoneNumActivity.this, getResources()
						.getString(R.string.txt_password_cannot_be_empty));
				return;
			}
			new UpdatePhoneNoServer().start(config.getUserId(), originNum, pwd,
					newNum, code, updatePhoneNoCallBack);
			break;
		case R.id.btn_change_phone_num_fetch_verification_code:
			if (newNum.equals("")) {
				ToastUtil.showToast(ChangePhoneNumActivity.this, getResources()
						.getString(R.string.txt_phone_number_cannot_be_empty));
				return;
			}
			if (!PhoneNumCheckUtil.isMobileNum(newNum)) {
				ToastUtil.showToast(getApplicationContext(), getResources()
						.getString(R.string.txt_phone_number_wrong_format));
				return;
			}
			mBtFetchCode.setEnabled(false);
			startTimer();
			new GetVerifyTypeServer().start(newNum, 2, fetchCodeCallBack);
			break;
		default:
			break;
		}
	}

	private void startTimer() {
		mCountDown = 30;
		if (timer == null) {
			timer = new Timer();
		}
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Message msg = mHandler.obtainMessage(MSG_REFRESH_BTN_TEXT);
				msg.arg2 = mCountDown--;
				msg.sendToTarget();
			}
		}, 0, 1000);
	}

	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
