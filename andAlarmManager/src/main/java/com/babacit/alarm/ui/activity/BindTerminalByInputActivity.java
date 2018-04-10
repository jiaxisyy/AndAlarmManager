package com.babacit.alarm.ui.activity;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.ApplyBindAlarmServer;
import com.babacit.alarm.server.ConfirmBindAlarmServer;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class BindTerminalByInputActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout mLlInput2;
	private Button mBtConfirmInput;
	private EditText mEtDeviceCode, mEtVerifyCode;
	private SharedConfig config;
	private static final int APPLY_BIND_SUCCESS = 0;
	private static final int APPLY_BIND_FAIL = 1;
	private static final int CONFIRM_BIND_SUCCESS = 2;
	private static final int CONFIRM_BIND_FAIL = 3;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case APPLY_BIND_SUCCESS:
				mLlInput2.setVisibility(View.VISIBLE);
				dismissProgress();
				break;
			case APPLY_BIND_FAIL:
				ToastUtil.showToast(BindTerminalByInputActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			case CONFIRM_BIND_SUCCESS:
				if (config.getAlarmCode().equals("")) {
					config.setAlarmCode(mEtDeviceCode.getText().toString());
				}
				Intent intent = new Intent("bind_success");
				sendBroadcast(intent);
				setResult(1);
				dismissProgress();
				finish();
				break;
			case CONFIRM_BIND_FAIL:
				ToastUtil.showToast(BindTerminalByInputActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			default:
				break;
			}
		}
	};

	private RequestCallBack applyBindAlarmCallback = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(APPLY_BIND_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(APPLY_BIND_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack confirmBindAlarmCallback = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(CONFIRM_BIND_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(CONFIRM_BIND_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_terminal_by_input);
		config = new SharedConfig(this);
		bindField();
		initData();
	}

	private void initData() {
		if (getIntent().getStringExtra("device") != null) {
			mEtDeviceCode.setText(getIntent().getStringExtra("device"));
			// mLlInput2.setVisibility(View.VISIBLE);
		}
	}

	private void bindField() {
		findViewById(R.id.btn_bind_termination_by_input_back)
				.setOnClickListener(this);
		mBtConfirmInput = (Button) findViewById(R.id.btn_bind_confirm);
		mBtConfirmInput.setOnClickListener(this);
		mLlInput2 = (LinearLayout) findViewById(R.id.ll_input_2);
		mEtDeviceCode = (EditText) findViewById(R.id.et_device_code);
		mEtVerifyCode = (EditText) findViewById(R.id.et_verify_code);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bind_confirm:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			if (mLlInput2.getVisibility() == View.GONE) {
				if (TextUtils.isEmpty(mEtDeviceCode.getText())) {
					ToastUtil.showToast(BindTerminalByInputActivity.this,
							"请输入闹钟序列号！");
					return;
				}
				new ApplyBindAlarmServer().start(config.getUserId(),
						mEtDeviceCode.getText().toString(),
						applyBindAlarmCallback);
				showProgressDialog("", false);
			} else {
				if (TextUtils.isEmpty(mEtDeviceCode.getText())) {
					ToastUtil.showToast(BindTerminalByInputActivity.this,
							"请输入闹钟序列号！");
					return;
				}
				if (TextUtils.isEmpty(mEtVerifyCode.getText())) {
					ToastUtil.showToast(BindTerminalByInputActivity.this,
							"请输入验证码！");
					return;
				}
				new ConfirmBindAlarmServer()
						.start(config.getUserId(), mEtDeviceCode.getText()
								.toString(),
								mEtVerifyCode.getText().toString(),
								confirmBindAlarmCallback);
				showProgressDialog("", false);
			}
			break;
		case R.id.btn_bind_termination_by_input_back:
			finish();
			break;
		default:
			break;
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
