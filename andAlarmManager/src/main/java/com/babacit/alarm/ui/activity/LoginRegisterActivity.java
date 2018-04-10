package com.babacit.alarm.ui.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.db.dao.UserDao;
import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.GetVerifyTypeServer;
import com.babacit.alarm.server.LoginServer;
import com.babacit.alarm.server.PwdSettingServer;
import com.babacit.alarm.server.UpdateUserInfoServer;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.PasswordUtils;
import com.babacit.alarm.utils.PhoneNumCheckUtil;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginRegisterActivity extends BaseActivity implements
		OnClickListener {
	private SharedConfig config;
	// private ImageView mIvBack;
	/** 登录布局 */
	private LinearLayout mUserLoginLayout;
	/** 忘记密码布局 */
	private LinearLayout mResetPwdLayout;
	/** 注册布局 */
	private LinearLayout mRegisterLayoutOne;
	private TextView mTvRegister, mTvLogin;
	private TextView mTvLoginTitle;
	/** 登录编辑框 */
	private EditText mEtLoginPhoneNum, mEtLoginPassword;
	/** 注册编辑框 */
	private EditText mEtRegisterPhoneNumber, mEtRegisterVerificationCode,
			mEtRegisterPassword, mEtRegisterConfirmPassword;
	/** 重置密码编辑框 */
	private EditText mEtResetPhoneNumber, mEtResetVerificationCode,
			mEtNewPassword, mEtConfirmNewPassword;
	/** 注册 获取验证码 */
	private Button mBtRegisterFetchVerificationCode;
	/** 重置密码 获取验证码 */
	private Button mBtResetFetchVerificationCode;
	/** 登录 手机号码 */
	private String loginPhoneStr;
	/** 找回密码 手机号码 */
	private String getPwdPhoneStr;
	/** 注册 手机号码 */
	private String regPhoneNum;
	private InputMethodManager imm;
	private Timer timer;
	private static int mCountDown;
	private static final int MSG_LOGIN_SUCCESS = 2001;
	private static final int MSG_ENABLE_REGISTER_FETCH_BTN = 2002;
	private static final int MSG_ENABLE_PASSWORD_FETCH_BTN = 2003;
	private static final int MSG_REFRESH_BTN_TEXT = 2004;
	private static final int MSG_VERIFY_STU_ID_SUCCESS = 2005;
	private static final int MSG_VERIFY_STU_ID_FAIL = 2006;
	private static final int MSG_FETCH_USER_INFO_SUCCESS = 2007;
	private static final int MSG_FETCH_USER_INFO_FAIL = 2008;
	private static final int MSG_LOGIN_FAILED = 2009;
	private static final int MSG_RESET_PWD_SUCCESS = 2010;
	private static final int MSG_RESET_PWD_FAIL = 2011;
	private static final int MSG_REGISTER_SUCCESS = 2012;
	private static final int MSG_REGISTER_FAIL = 2013;
	private static final int MSG_REGISTER_FETCH_VERIFICATION_CODE_SUCCESS = 2014;
	private static final int MSG_REGISTER_FETCH_VERIFICATION_CODE_FAIL = 2015;

	private RequestCallBack loginCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			String userId = (String) obj;
			config.setUserId(userId);
			List<UserInfo> userInfos = UserDao.getInstance().queryList();
			if (userInfos == null || userInfos.size() < 1) {
				UserInfo user = new UserInfo();
				user.setUserId(userId);
				user.setPhoneNo(loginPhoneStr);
				UserDao.getInstance().deteleAll();
				UserDao.getInstance().insertUser(user);
			} else {
				UserInfo user = userInfos.get(0);
				user.setUserId(userId);
				user.setPhoneNo(loginPhoneStr);
				UserDao.getInstance().updateUser(user);
				if (config.getInitialState() == 0) {
					config.setInitialState(1);
					new UpdateUserInfoServer().start(1, UserDao.getInstance()
							.queryUserByUserId(userId), null);
				}
			}

			mHandler.sendEmptyMessage(MSG_LOGIN_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_LOGIN_FAILED);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack fetchCodeCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(MSG_REGISTER_FETCH_VERIFICATION_CODE_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler
					.obtainMessage(MSG_REGISTER_FETCH_VERIFICATION_CODE_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack registerCallBcak = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(MSG_REGISTER_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_REGISTER_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack resetPwdCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(MSG_RESET_PWD_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_RESET_PWD_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOGIN_SUCCESS:
				config.setloginState(1);
				dismissProgress();
				Intent intent = new Intent(LoginRegisterActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case MSG_LOGIN_FAILED:
				ToastUtil.showToast(LoginRegisterActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			case MSG_REGISTER_SUCCESS:
				dismissProgress();
				config.setPhone(regPhoneNum);
				config.setPwd(mEtRegisterPassword.getText().toString().trim());
				Intent accountInfo = new Intent(LoginRegisterActivity.this,
						AccountInfoActivity.class);
				startActivity(accountInfo);
				finish();
				break;
			case MSG_REGISTER_FAIL:
				ToastUtil.showToast(LoginRegisterActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			case MSG_RESET_PWD_SUCCESS:
				ToastUtil.showToast(LoginRegisterActivity.this, "重置密码成功！");
				dismissProgress();
				finish();
				break;
			case MSG_RESET_PWD_FAIL:
				ToastUtil.showToast(LoginRegisterActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			case MSG_REGISTER_FETCH_VERIFICATION_CODE_SUCCESS:
				ToastUtil.showToast(LoginRegisterActivity.this, "获取验证码成功");
				break;
			case MSG_REGISTER_FETCH_VERIFICATION_CODE_FAIL:
				ToastUtil.showToast(LoginRegisterActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				stopTimer();
				mBtRegisterFetchVerificationCode.setText(getResources()
						.getString(R.string.txt_fetch_verification_code));
				mBtRegisterFetchVerificationCode.setEnabled(true);
				mBtResetFetchVerificationCode.setText(getResources().getString(
						R.string.txt_fetch_verification_code));
				mBtResetFetchVerificationCode.setEnabled(true);
				break;
			case MSG_ENABLE_REGISTER_FETCH_BTN:
				mBtRegisterFetchVerificationCode.setText(getResources()
						.getString(R.string.txt_fetch_verification_code));
				mBtRegisterFetchVerificationCode.setEnabled(true);
				stopTimer();
				break;
			case MSG_ENABLE_PASSWORD_FETCH_BTN:
				mBtResetFetchVerificationCode.setText(getResources().getString(
						R.string.txt_fetch_verification_code));
				mBtResetFetchVerificationCode.setEnabled(true);
				stopTimer();
				break;
			case MSG_REFRESH_BTN_TEXT:
				switch (msg.arg1) {
				case 0:
					if (msg.arg2 == 0) {
						mHandler.sendEmptyMessage(MSG_ENABLE_REGISTER_FETCH_BTN);
					}
					mBtRegisterFetchVerificationCode.setText(String
							.valueOf(msg.arg2)
							+ "  "
							+ getResources().getString(
									R.string.txt_retry_after_seconds));
					break;
				case 1:
					if (msg.arg2 == 0) {
						mHandler.sendEmptyMessage(MSG_ENABLE_PASSWORD_FETCH_BTN);
					}
					mBtResetFetchVerificationCode.setText(String
							.valueOf(msg.arg2)
							+ "  "
							+ getResources().getString(
									R.string.txt_retry_after_seconds));
					break;

				default:
					break;
				}
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_register);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		config = new SharedConfig(this);
		if (config.getLoginState() == 1) {

		}
		bindField();
		initData();
	}

	private void initData() {
		mEtLoginPhoneNum.setText(config.getPhone());
		mEtLoginPassword.setText(config.getPwd());
	}

	private void bindField() {
		// mIvBack = (ImageView) findViewById(R.id.btn_login_back);
		mEtLoginPhoneNum = (EditText) findViewById(R.id.et_login_phone_num);
		mEtLoginPassword = (EditText) findViewById(R.id.et_login_password);

		mEtRegisterPhoneNumber = (EditText) findViewById(R.id.et_register_phone_number);
		mEtRegisterVerificationCode = (EditText) findViewById(R.id.et_register_verification_code);
		mEtRegisterPassword = (EditText) findViewById(R.id.et_register_input_password);
		mEtRegisterConfirmPassword = (EditText) findViewById(R.id.et_register_reinput_password);

		mEtResetPhoneNumber = (EditText) findViewById(R.id.et_reset_pwd_phone_number);
		mEtResetVerificationCode = (EditText) findViewById(R.id.et_reset_pwd_verification_code);
		mEtNewPassword = (EditText) findViewById(R.id.et_reset_pwd_input_password);
		mEtConfirmNewPassword = (EditText) findViewById(R.id.et_reset_pwd_reinput_password);

		mBtRegisterFetchVerificationCode = (Button) findViewById(R.id.btn_register_fetch_verification_code);
		mBtResetFetchVerificationCode = (Button) findViewById(R.id.btn_reset_pwd_fetch_verification_code);
		mUserLoginLayout = (LinearLayout) findViewById(R.id.ll_user_login);
		mTvLoginTitle = (TextView) findViewById(R.id.tv_login_title);
		mTvRegister = (TextView) findViewById(R.id.tv_register);
		mTvLogin = (TextView) findViewById(R.id.tv_login);
		mResetPwdLayout = (LinearLayout) findViewById(R.id.ll_reset_pwd);
		mRegisterLayoutOne = (LinearLayout) findViewById(R.id.ll_register_one);

		mTvRegister.setOnClickListener(this);
		mTvLogin.setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		// findViewById(R.id.btn_login_back).setOnClickListener(this);
		findViewById(R.id.tv_foget_password).setOnClickListener(this);
		findViewById(R.id.btn_reset_pwd_fetch_verification_code)
				.setOnClickListener(this);

		findViewById(R.id.btn_register_fetch_verification_code)
				.setOnClickListener(this);
		findViewById(R.id.btn_register).setOnClickListener(this);
		findViewById(R.id.btn_reset_pwd).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 登录 */
		case R.id.btn_login:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			loginPhoneStr = mEtLoginPhoneNum.getText().toString().trim();
			String loginPwd = mEtLoginPassword.getText().toString().trim();
			if (loginPhoneStr.equals("")) {
				showToast(getResources().getString(
						R.string.txt_phone_number_cannot_be_empty));
				return;
			}
			if (loginPwd.equals("")) {
				showToast(getResources().getString(
						R.string.txt_password_cannot_be_empty));
				return;
			}

			if (!PhoneNumCheckUtil.isMobileNum(loginPhoneStr)) {
				showToast(getResources().getString(
						R.string.txt_phone_number_wrong_format));
				return;
			}
			showProgressDialog("", false);
			new LoginServer().start(loginPhoneStr, loginPwd, loginCallBack);
			break;
		/** 注册 */
		case R.id.tv_register:
			// mIvBack.setVisibility(View.VISIBLE);
			mTvLogin.setVisibility(View.VISIBLE);
			mTvRegister.setVisibility(View.GONE);
			mUserLoginLayout.setVisibility(View.GONE);
			mTvRegister.setVisibility(View.GONE);
			mRegisterLayoutOne.setVisibility(View.VISIBLE);
			mTvLoginTitle.setText(getResources().getString(
					R.string.txt_register));
			break;
		case R.id.tv_login:
			mTvLogin.setVisibility(View.GONE);
			mResetPwdLayout.setVisibility(View.GONE);
			mTvRegister.setVisibility(View.VISIBLE);
			mRegisterLayoutOne.setVisibility(View.GONE);
			mUserLoginLayout.setVisibility(View.VISIBLE);
			mTvLoginTitle.setText(getResources().getString(R.string.txt_login));
			break;
		/** 忘记密码 */
		case R.id.tv_foget_password:
			// mIvBack.setVisibility(View.VISIBLE);
			mUserLoginLayout.setVisibility(View.GONE);
			mResetPwdLayout.setVisibility(View.VISIBLE);
			mTvRegister.setVisibility(View.GONE);
			mTvLogin.setVisibility(View.VISIBLE);
			mTvLoginTitle.setText(getResources().getString(
					R.string.txt_reset_password));
			break;

		/** 忘记密码---获取验证码 */
		case R.id.btn_reset_pwd_fetch_verification_code:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			getPwdPhoneStr = mEtResetPhoneNumber.getText().toString().trim();
			if (getPwdPhoneStr.equals("")) {
				showToast(getResources().getString(
						R.string.txt_phone_number_cannot_be_empty));
				return;
			}
			if (!PhoneNumCheckUtil.isMobileNum(getPwdPhoneStr)) {
				showToast(getResources().getString(
						R.string.txt_phone_number_wrong_format));
				return;
			}
			startTimer(1);
			mBtResetFetchVerificationCode.setEnabled(false);
			new GetVerifyTypeServer().start(getPwdPhoneStr, 1,
					fetchCodeCallBack);
			break;
		/** 重置密码 */
		case R.id.btn_reset_pwd:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			String newPwdStr = mEtNewPassword.getText().toString().trim();
			String confirmNewPwdStr = mEtConfirmNewPassword.getText()
					.toString().trim();
			getPwdPhoneStr = mEtResetPhoneNumber.getText().toString().trim();
			String forgetPwdVeriCode = mEtResetVerificationCode.getText()
					.toString().trim();
			if (getPwdPhoneStr.equals("")) {
				showToast(getResources().getString(
						R.string.txt_phone_number_cannot_be_empty));
				return;
			}
			if (!PhoneNumCheckUtil.isMobileNum(getPwdPhoneStr)) {
				showToast(getResources().getString(
						R.string.txt_phone_number_wrong_format));
				return;
			}
			if (forgetPwdVeriCode.equals("")) {
				showToast(getResources().getString(
						R.string.txt_verification_code_cannot_be_empty));
				return;
			}
			if (newPwdStr.equals("") || confirmNewPwdStr.equals("")) {
				showToast(getResources().getString(
						R.string.txt_password_cannot_be_empty));
				return;
			}
			if (!confirmNewPwdStr.equals(newPwdStr)) {
				showToast(getResources().getString(
						R.string.txt_password_not_equal));
				return;
			}
			if (confirmNewPwdStr.length() < 8 || newPwdStr.length() < 8) {
				showToast(getResources().getString(
						R.string.txt_password_length_short_than_eight));
				return;
			}
			if (!PasswordUtils.isValid(newPwdStr)) {
				ToastUtil.showToast(LoginRegisterActivity.this, getResources()
						.getString(R.string.txt_password_not_valid));
				return;
			}
			new PwdSettingServer().start(getPwdPhoneStr, forgetPwdVeriCode,
					newPwdStr, "", 3, resetPwdCallBack);
			break;

		/** 注册--获取验证码 */
		case R.id.btn_register_fetch_verification_code:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			regPhoneNum = mEtRegisterPhoneNumber.getText().toString().trim();
			if (regPhoneNum.equals("")) {
				ToastUtil.showToast(LoginRegisterActivity.this, getResources()
						.getString(R.string.txt_phone_number_cannot_be_empty));
				return;
			}
			if (!PhoneNumCheckUtil.isMobileNum(regPhoneNum)) {
				showToast(getResources().getString(
						R.string.txt_phone_number_wrong_format));
				return;
			}
			mBtRegisterFetchVerificationCode.setEnabled(false);
			startTimer(0);
			new GetVerifyTypeServer().start(regPhoneNum, 0, fetchCodeCallBack);
			break;

		case R.id.btn_register:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			regPhoneNum = mEtRegisterPhoneNumber.getText().toString().trim();
			String regVerifyCode = mEtRegisterVerificationCode.getText()
					.toString().trim();
			String regPwd = mEtRegisterPassword.getText().toString().trim();
			String regConfirmPwd = mEtRegisterConfirmPassword.getText()
					.toString().trim();
			if (regPhoneNum.equals("")) {
				showToast(getResources().getString(
						R.string.txt_phone_number_cannot_be_empty));
				return;
			}
			if (!PhoneNumCheckUtil.isMobileNum(regPhoneNum)) {
				showToast(getResources().getString(
						R.string.txt_phone_number_wrong_format));
				return;
			}
			if (regVerifyCode.equals("")) {
				showToast(getResources().getString(
						R.string.txt_verification_code_cannot_be_empty));
				return;
			}
			if (regPwd.equals("") || regConfirmPwd.equals("")) {
				showToast(getResources().getString(
						R.string.txt_password_cannot_be_empty));
				return;
			}
			if (!regPwd.equals(regConfirmPwd)) {
				showToast(getResources().getString(
						R.string.txt_password_not_equal));
				return;
			}
			if (regPwd.length() < 8 || regConfirmPwd.length() < 8) {
				showToast(getResources().getString(
						R.string.txt_password_length_short_than_eight));
				return;
			}
			if (!PasswordUtils.isValid(regPwd)) {
				ToastUtil.showToast(LoginRegisterActivity.this, getResources()
						.getString(R.string.txt_password_not_valid));
				return;
			}
			showProgressDialog("", false);
			new PwdSettingServer().start(regPhoneNum, regVerifyCode, regPwd,
					"", 1, registerCallBcak);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (mUserLoginLayout.getVisibility() == View.VISIBLE) {
				finish();
			} else if (mResetPwdLayout.getVisibility() == View.VISIBLE) {
				mResetPwdLayout.setVisibility(View.GONE);
				mUserLoginLayout.setVisibility(View.VISIBLE);
				mTvRegister.setVisibility(View.VISIBLE);
				mTvLogin.setVisibility(View.GONE);
				mTvLoginTitle.setText(getResources().getString(
						R.string.txt_login));
			} else if (mRegisterLayoutOne.getVisibility() == View.VISIBLE) {
				mRegisterLayoutOne.setVisibility(View.GONE);
				mUserLoginLayout.setVisibility(View.VISIBLE);
				mTvLoginTitle.setText(getResources().getString(
						R.string.txt_login));
				mTvRegister.setVisibility(View.VISIBLE);
				mTvLogin.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 
	 * @param which
	 *            0:注册 1:找回密码
	 */
	private void startTimer(final int which) {
		mCountDown = 30;
		if (timer == null) {
			timer = new Timer();
		}
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Message msg = mHandler.obtainMessage(MSG_REFRESH_BTN_TEXT);
				msg.arg1 = which;
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

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
