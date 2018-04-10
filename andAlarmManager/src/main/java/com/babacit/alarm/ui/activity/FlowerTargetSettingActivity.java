package com.babacit.alarm.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.RewardInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.RewardSettingServer;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class FlowerTargetSettingActivity extends BaseActivity implements
		OnClickListener {
	private EditText mEtWeekTarget, mEtMonthTarget, mEtYearTarget;
	private EditText mEtWeekTargetReward, mEtMonthTargetReward,
			mEtYearTargetReward;
	private String mWeekTarget, mMonthTarget, mYearTarget;
	private String mWeekTargetReward, mMonthTargetReward, mYearTargetReward;
	private SharedConfig config;
	private List<RewardInfo> _rewardInfos = new ArrayList<RewardInfo>();

	private RequestCallBack setTargetCallBack = new RequestCallBack() {
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
				Intent intent = new Intent();
				intent.putExtra("week_target", mWeekTarget);
				intent.putExtra("week_target_reward", mWeekTargetReward);
				intent.putExtra("month_target", mMonthTarget);
				intent.putExtra("month_target_reward", mMonthTargetReward);
				intent.putExtra("year_target", mYearTarget);
				intent.putExtra("year_target_reward", mYearTargetReward);
				setResult(1010, intent);
				finish();
				break;
			case 1:
				ToastUtil.showToast(getApplicationContext(),
						ErrUtils.getErrorReasonStr(msg.arg1));
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
		setContentView(R.layout.activity_flower_target_setting);
		bindField();
		config = new SharedConfig(this);
	}

	private void bindField() {
		findViewById(R.id.btn_target_setting_back).setOnClickListener(this);
		findViewById(R.id.tv_target_setting_finish).setOnClickListener(this);

		mEtWeekTarget = (EditText) findViewById(R.id.et_week_target);
		mEtWeekTarget.setText(getIntent().getStringExtra("week_target"));
		mEtMonthTarget = (EditText) findViewById(R.id.et_month_target);
		mEtMonthTarget.setText(getIntent().getStringExtra("month_target"));
		mEtYearTarget = (EditText) findViewById(R.id.et_year_target);
		mEtYearTarget.setText(getIntent().getStringExtra("year_target"));
		mEtWeekTargetReward = (EditText) findViewById(R.id.et_week_target_reward);
		mEtWeekTargetReward.setText(getIntent().getStringExtra("week_reward"));
		mEtMonthTargetReward = (EditText) findViewById(R.id.et_month_target_reward);
		mEtMonthTargetReward
				.setText(getIntent().getStringExtra("month_reward"));
		mEtYearTargetReward = (EditText) findViewById(R.id.et_year_target_reward);
		mEtYearTargetReward.setText(getIntent().getStringExtra("year_reward"));

		mEtWeekTarget.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				char strs[] = str.toCharArray();
				if (str.equals("") || (strs[0] == '0' && str.length() > 1)) {
					mEtWeekTarget.setText("0");
				}
			}
		});

		mEtMonthTarget.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				char strs[] = str.toCharArray();
				if (str.equals("") || (strs[0] == '0' && str.length() > 1)) {
					mEtMonthTarget.setText("0");
				}
			}
		});

		mEtYearTarget.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				char strs[] = str.toCharArray();
				if (str.equals("") || (strs[0] == '0' && str.length() > 1)) {
					mEtYearTarget.setText("0");
				}
			}
		});
	}

	private String trimZero(String str) {
		char strs[] = str.toCharArray();
		int len = str.length();
		int index = 0;
		for (int i = 0; i < len; i++) {
			if ('0' != strs[i]) {
				index = i;
				break;
			}
		}
		return str.substring(index, len);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_target_setting_back:
			finish();
			break;
		case R.id.tv_target_setting_finish:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			mWeekTarget = mEtWeekTarget.getText().toString();
			mMonthTarget = mEtMonthTarget.getText().toString();
			mYearTarget = mEtYearTarget.getText().toString();
			if (Integer.parseInt(mWeekTarget) >= Integer.parseInt(mMonthTarget)) {
				ToastUtil.showToast(getApplicationContext(), "月目标必须大于周目标！");
				return;
			}
			if (Integer.parseInt(mMonthTarget) >= Integer.parseInt(mYearTarget)) {
				ToastUtil.showToast(getApplicationContext(), "年目标必须大于月目标！");
				return;
			}
			showProgressDialog("", true);
			mWeekTargetReward = mEtWeekTargetReward.getText().toString();
			mMonthTargetReward = mEtMonthTargetReward.getText().toString();
			mYearTargetReward = mEtYearTargetReward.getText().toString();
			for (int i = 1; i < 4; i++) {
				RewardInfo rewardInfo = new RewardInfo();
				rewardInfo.setTimeType(i);
				switch (i) {
				case 1:
					rewardInfo.setTargetCount(Integer.parseInt(mWeekTarget));
					rewardInfo.setEncourage(mWeekTargetReward);
					_rewardInfos.add(rewardInfo);
					break;
				case 2:
					rewardInfo.setTargetCount(Integer.parseInt(mMonthTarget));
					rewardInfo.setEncourage(mMonthTargetReward);
					_rewardInfos.add(rewardInfo);
					break;
				case 3:
					rewardInfo.setTargetCount(Integer.parseInt(mYearTarget));
					rewardInfo.setEncourage(mYearTargetReward);
					_rewardInfos.add(rewardInfo);
					break;
				default:
					break;
				}
			}
			new RewardSettingServer().start(config.getUserId(),
					config.getAlarmCode(), setTargetCallBack, _rewardInfos);
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
