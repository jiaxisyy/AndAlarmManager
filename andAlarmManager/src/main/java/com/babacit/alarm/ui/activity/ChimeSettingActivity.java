package com.babacit.alarm.ui.activity;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.UpdateReportTimeServer;
import com.babacit.alarm.server.UpdateSleepTimeServer;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.ui.dialog.TimeWheelDialogFragment;
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

public class ChimeSettingActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {
	private TextView mTvStartTime, mTvEndTime;
	private SharedConfig config;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Intent data = new Intent();
				data.putExtra("chime_duration", String.format("%s-%s",
						mTvStartTime.getText().toString(), mTvEndTime.getText()
								.toString()));
				setResult(MainActivity.RESULT_CODE_FROM_CHIME_SETTING, data);
				dismissProgress();
				finish();
				break;
			case 1:
				ToastUtil.showToast(ChimeSettingActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			default:
				break;
			}
		}
	};

	private RequestCallBack updateChimeTimeCallBack = new RequestCallBack() {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chime_setting);
		config = new SharedConfig(this);
		bindField();
		initData();
	}

	private void initData() {
		String time = getIntent().getStringExtra("chime");
		if (time != null && !time.equals("")) {
			String[] times = time.split("-");
			mTvStartTime.setText(times[0]);
			mTvEndTime.setText(times[1]);
		}
	}

	private void bindField() {
		findViewById(R.id.btn_chime_setting_back).setOnClickListener(this);
		findViewById(R.id.tv_chime_setting_finish).setOnClickListener(this);
		findViewById(R.id.rl_chime_start_time).setOnClickListener(this);
		findViewById(R.id.rl_chime_end_time).setOnClickListener(this);
		mTvStartTime = (TextView) findViewById(R.id.tv_chime_start_time);
		mTvEndTime = (TextView) findViewById(R.id.tv_chime_end_time);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chime_setting_back:
			finish();
			break;
		case R.id.rl_chime_start_time:
			TimeWheelDialogFragment sleepStart = new TimeWheelDialogFragment();
			sleepStart.show(getFragmentManager(), "start");
			break;
		case R.id.rl_chime_end_time:
			TimeWheelDialogFragment sleepEnd = new TimeWheelDialogFragment();
			sleepEnd.show(getFragmentManager(), "end");
			break;
		case R.id.tv_chime_setting_finish:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			showProgressDialog("", false);
			new UpdateReportTimeServer().start(config.getUserId(),
					config.getAlarmCode(), mTvStartTime.getText().toString(),
					mTvEndTime.getText().toString(), updateChimeTimeCallBack);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("start")) {
			mTvStartTime.setText(message);
		} else if (tag.equals("end")) {
			mTvEndTime.setText(message);
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
