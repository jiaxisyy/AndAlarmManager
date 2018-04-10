package com.babacit.alarm.ui.activity;

import com.umeng.analytics.MobclickAgent;
import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.db.dao.UserDao;
import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.ui.dialog.DateWheelDialogFragment;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ToastUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class AccountInfoActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {
	private RadioGroup mRg1, mRg2;
	private RadioButton mRbPa, mRbMa, mRbGrandPa, mRbGrandMa, mRbOther;
	private Spinner mSp;
	private String[] occupations;
	private ArrayAdapter<String> adapter;
	private Button btnBirthdayPick;
	public static final int MSG_BIRTHDAY_SET = 2000;
	public static final int MSG_SAVE_USER_INFO_SUCCESS = 2001;
	public static final int MSG_SAVE_USER_INFO_FAIL = 2002;
	private SharedConfig config;
	private EditText mEtNickName, mEtRealName;
	private int selectedJobType;
	private int role;
	private EditText mEtRole;

	private RequestCallBack saveUserInfoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			dismissProgress();
			mHandler.sendEmptyMessage(MSG_SAVE_USER_INFO_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			mHandler.sendEmptyMessage(MSG_SAVE_USER_INFO_FAIL);
			dismissProgress();
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SAVE_USER_INFO_SUCCESS:
				dismissProgress();
				Intent intent1 = new Intent(AccountInfoActivity.this,
						MainActivity.class);
				startActivity(intent1);
				finish();
				break;
			case MSG_SAVE_USER_INFO_FAIL:
				dismissProgress();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_info);
		config = new SharedConfig(this);
		bindField();
		initData();
	}

	private void bindField() {
		mEtNickName = (EditText) findViewById(R.id.et_account_info_nickname);
		mEtRealName = (EditText) findViewById(R.id.et_account_info_name);
		mEtRole = (EditText) findViewById(R.id.et_role);
		mSp = (Spinner) findViewById(R.id.sp_career);
		mSp.setOnItemSelectedListener(new SpinnerSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				super.onItemSelected(arg0, arg1, pos, arg3);
				selectedJobType = pos;
			}
		});
		btnBirthdayPick = (Button) findViewById(R.id.btn_birthday_pick);
		btnBirthdayPick.setOnClickListener(this);
		findViewById(R.id.btn_complete_later).setOnClickListener(this);
		findViewById(R.id.btn_save_account).setOnClickListener(this);

		mRg1 = (RadioGroup) findViewById(R.id.rg_1);
		mRg2 = (RadioGroup) findViewById(R.id.rg_2);
		mRbPa = (RadioButton) findViewById(R.id.rb_role_father);
		mRbMa = (RadioButton) findViewById(R.id.rb_role_mother);
		mRbGrandPa = (RadioButton) findViewById(R.id.rb_role_grandpa);
		mRbGrandMa = (RadioButton) findViewById(R.id.rb_role_grandma);
		mRbOther = (RadioButton) findViewById(R.id.rb_role_other);
		mRbPa.setOnClickListener(new RadioSelected("1"));
		mRbMa.setOnClickListener(new RadioSelected("2"));
		mRbGrandPa.setOnClickListener(new RadioSelected("3"));
		mRbGrandMa.setOnClickListener(new RadioSelected("4"));
		mRbOther.setOnClickListener(new RadioSelected("5"));
	}

	private void initData() {
		selectedJobType = 0;
		role = 1;
		occupations = getResources().getStringArray(R.array.occupations);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, occupations);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp.setAdapter(adapter);
	}

	class RadioSelected implements OnClickListener {
		final public String btnId;

		public RadioSelected(String str) {
			btnId = str;
		}

		@Override
		public void onClick(View v) {
			if (btnId.equals("1") || btnId.equals("2") || btnId.equals("3")) {
				mRg2.clearCheck();
				if (btnId.equals("1")) {
					mEtRole.setVisibility(View.GONE);
					mRbPa.setChecked(true);
					role = 1;
				} else if (btnId.equals("2")) {
					mEtRole.setVisibility(View.GONE);
					mRbMa.setChecked(true);
					role = 2;
				} else {
					mEtRole.setVisibility(View.GONE);
					mRbGrandPa.setChecked(true);
					role = 3;
				}
			} else if (btnId.equals("4") || btnId.equals("5")) {
				mRg1.clearCheck();
				if (btnId.equals("4")) {
					mEtRole.setVisibility(View.GONE);
					mRbGrandMa.setChecked(true);
					role = 4;
				} else {
					mEtRole.setVisibility(View.VISIBLE);
					mRbOther.setChecked(true);
					role = 5;
				}
			}
		}
	};

	private class SpinnerSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// ToastUtil.showToast(AccountInfoActivity.this, occupations[arg2]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_birthday_pick:
			DateWheelDialogFragment date = new DateWheelDialogFragment();
			date.show(getFragmentManager(), "account_info_birthday");
			break;
		case R.id.btn_complete_later:
			Intent intent = new Intent(AccountInfoActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_save_account:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			UserInfo userInfo = new UserInfo();
			userInfo.setNickName(mEtNickName.getText().toString().trim());
			userInfo.setName(mEtRealName.getText().toString().trim());
			String birth = btnBirthdayPick.getText().toString().trim();
			userInfo.setBirthday(birth.replace("年", "-").replace("月", "-")
					.replace("日", "-"));
			userInfo.setJobType(selectedJobType);
			userInfo.setRoleType(role);
			if (role == 5) {
				userInfo.setRoleInfo(mEtRole.getText().toString());
			}
			UserDao.getInstance().insertUser(userInfo);

			// new UpdateUserInfoServer().start(1, userInfo,
			// saveUserInfoCallBack);
			// showProgressDialog("", true);
			mHandler.sendEmptyMessage(MSG_SAVE_USER_INFO_SUCCESS);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(AccountInfoActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("account_info_birthday")) {
			btnBirthdayPick.setText(message);
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