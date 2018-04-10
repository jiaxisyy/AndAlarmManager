package com.babacit.alarm.ui.activity;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.db.dao.UserDao;
import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.PwdSettingServer;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.PasswordUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ChangePasswordActivity extends BaseActivity implements
		OnClickListener {
	private EditText mEtOriginPwd, mEtNewPwd, mEtConfirmNewPwd;
	private String originPwd, newPwd, confirmNewPwd;
	private UserInfo user;
	private SharedConfig config;

	private RequestCallBack pwdSettingCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(0);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(1);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dismissProgress();
				UserDao.getInstance().update(user);
				finish();
				break;
			case 1:
				dismissProgress();
				ToastUtil.showToast(getApplicationContext(),
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		config = new SharedConfig(this);
		bindField();
		user = getIntent().getParcelableExtra("user");
	}

	private void bindField() {
		findViewById(R.id.btn_confirm).setOnClickListener(this);
		findViewById(R.id.btn_change_password_back).setOnClickListener(this);
		mEtOriginPwd = (EditText) findViewById(R.id.et_origin_pwd);
		mEtNewPwd = (EditText) findViewById(R.id.et_new_pwd);
		mEtConfirmNewPwd = (EditText) findViewById(R.id.et_confirm_new_pwd);
	}

	@Override
	public void onClick(View v) {
		originPwd = mEtOriginPwd.getText().toString().trim();
		newPwd = mEtNewPwd.getText().toString().trim();
		confirmNewPwd = mEtConfirmNewPwd.getText().toString().trim();
		switch (v.getId()) {
		case R.id.btn_change_password_back:
			finish();
			break;
		case R.id.btn_confirm:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			if (originPwd.equals("") || newPwd.equals("")
					|| confirmNewPwd.equals("")) {
				ToastUtil.showToast(ChangePasswordActivity.this, getResources()
						.getString(R.string.txt_password_cannot_be_empty));
				return;
			}
			if (newPwd.length() < 8 || confirmNewPwd.length() < 8
					|| newPwd.length() > 12 || confirmNewPwd.length() > 12) {
				ToastUtil.showToast(
						ChangePasswordActivity.this,
						getResources().getString(
								R.string.txt_password_length_short_than_eight));
				return;
			}
			if (!newPwd.equals(confirmNewPwd)) {
				ToastUtil.showToast(ChangePasswordActivity.this, getResources()
						.getString(R.string.txt_password_not_equal));
				return;
			}

			if (!PasswordUtils.isValid(newPwd)) {
				ToastUtil.showToast(ChangePasswordActivity.this, getResources()
						.getString(R.string.txt_password_not_valid));
				return;
			}
			showProgressDialog("", false);
			new PwdSettingServer().start(user.getPhoneNo(), "", newPwd,
					originPwd, 2, pwdSettingCallBack);
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