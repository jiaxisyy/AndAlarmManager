package com.babacit.alarm.ui.activity;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.UpdateVacationServer;
import com.babacit.alarm.ui.dialog.MonthDayWheelDialogFragment;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VacationSettingActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {
	private TextView mTvSummerStart, mTvSummerEnd, mTvWinterStart,
			mTvWinterEnd;
	private SharedConfig config;
	private String summerStartDate, summerEndDate, winterStartDate,
			winterEndDate;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				new UpdateVacationServer().start(config.getUserId(), config
						.getAlarmCode(), mTvWinterStart.getText().toString(),
						mTvWinterEnd.getText().toString(), 1,
						updateWinterVacationCallBack);
				break;
			case 1:
//			case 3:
				ToastUtil.showToast(VacationSettingActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			case 2:
				Intent vacation = new Intent();
				vacation.putExtra("summer", String.format("%s~%s",
						mTvSummerStart.getText().toString(), mTvSummerEnd
								.getText().toString()));
				vacation.putExtra("winter", String.format("%s~%s",
						mTvWinterStart.getText().toString(), mTvWinterEnd
								.getText().toString()));
				setResult(MainActivity.RESULT_CODE_FROM_VACATION_SETTING,
						vacation);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private RequestCallBack updateSummerVacationCallBack = new RequestCallBack() {
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
	private RequestCallBack updateWinterVacationCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(2);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(3);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vacation_setting);
		config = new SharedConfig(this);
		bindField();
		initData();
	}

	private void bindField() {
		findViewById(R.id.btn_vacation_setting_back).setOnClickListener(this);
		findViewById(R.id.tv_vacation_setting_finish).setOnClickListener(this);
		findViewById(R.id.rl_summer_vacation_start_date).setOnClickListener(
				this);
		findViewById(R.id.rl_summer_vacation_end_date).setOnClickListener(this);
		findViewById(R.id.rl_winter_vacation_start_date).setOnClickListener(
				this);
		findViewById(R.id.rl_winter_vacation_end_date).setOnClickListener(this);

		mTvSummerStart = (TextView) findViewById(R.id.tv_summer_vacation_start_date);
		mTvSummerEnd = (TextView) findViewById(R.id.tv_summer_vacation_end_date);
		mTvWinterStart = (TextView) findViewById(R.id.tv_winter_vacation_start_date);
		mTvWinterEnd = (TextView) findViewById(R.id.tv_winter_vacation_end_date);
	}

	private void initData() {
		String sum = getIntent().getStringExtra("summer");
		String win = getIntent().getStringExtra("winter");
		if (sum != null && !sum.equals("") && win != null && !win.equals("")) {
			String[] summers = sum.split("~");
			String[] winters = win.split("~");
			mTvSummerStart.setText(summers[0]);
			mTvSummerEnd.setText(summers[1]);
			mTvWinterStart.setText(winters[0]);
			mTvWinterEnd.setText(winters[1]);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_vacation_setting_back:
			finish();
			break;
		case R.id.rl_summer_vacation_start_date:
			MonthDayWheelDialogFragment summerStart = new MonthDayWheelDialogFragment();
			summerStart.show(getFragmentManager(), "summer_start");
			break;
		case R.id.rl_summer_vacation_end_date:
			MonthDayWheelDialogFragment summerEnd = new MonthDayWheelDialogFragment();
			summerEnd.show(getFragmentManager(), "summer_end");
			break;
		case R.id.rl_winter_vacation_start_date:
			MonthDayWheelDialogFragment winterStart = new MonthDayWheelDialogFragment();
			winterStart.show(getFragmentManager(), "winter_start");
			break;
		case R.id.rl_winter_vacation_end_date:
			MonthDayWheelDialogFragment winterEnd = new MonthDayWheelDialogFragment();
			winterEnd.show(getFragmentManager(), "winter_end");
			break;
		case R.id.tv_vacation_setting_finish:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			// if (!checkValidate()) {
			// ToastUtil.showToast(VacationSettingActivity.this, "设置有误！请重新设置");
			// return;
			// }
			new UpdateVacationServer().start(config.getUserId(),
					config.getAlarmCode(), mTvSummerStart.getText().toString(),
					mTvSummerEnd.getText().toString(), 0,
					updateSummerVacationCallBack);
			break;
		default:
			break;
		}
	}

	private boolean checkValidate() {
		summerStartDate = mTvSummerStart.getText().toString().replace("-", "");
		summerEndDate = mTvSummerEnd.getText().toString().replace("-", "");
		winterStartDate = mTvWinterStart.getText().toString().replace("-", "");
		winterEndDate = mTvWinterEnd.getText().toString().replace("-", "");
		if ((Integer.parseInt(summerEndDate) < Integer
				.parseInt(summerStartDate))
				|| (Integer.parseInt(winterEndDate) < Integer
						.parseInt(winterStartDate))) {
			return false;
		} else
			return true;
	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("summer_start")) {
			mTvSummerStart.setText(message);
		} else if (tag.equals("summer_end")) {
			mTvSummerEnd.setText(message);
		} else if (tag.equals("winter_start")) {
			mTvWinterStart.setText(message);
		} else if (tag.equals("winter_end")) {
			mTvWinterEnd.setText(message);
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
