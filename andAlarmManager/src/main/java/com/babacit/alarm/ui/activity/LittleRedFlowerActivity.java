package com.babacit.alarm.ui.activity;

import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.RewardInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.QueryRewardInfoListServer;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LittleRedFlowerActivity extends BaseActivity implements
		OnClickListener {
	private TextView mTvCurWeekCount, mTvCurMonthCount, mTvCurYearCount;
	private TextView mTvWeekTarget, mTvMonthTarget, mTvYearTarget;
	private TextView mTvWeekTargetReward, mTvMonthTargetReward,
			mTvYearTargetReward;
	private List<RewardInfo> rewardInfos;
	private SharedConfig config;

	private RequestCallBack queryRewardInfoBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHandler.obtainMessage(0);
			msg.obj = obj;
			msg.sendToTarget();
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
				rewardInfos = (List<RewardInfo>) msg.obj;
				refreshRewardInfos();
				dismissProgress();
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

	private void refreshRewardInfos() {
		for (RewardInfo rewardInfo : rewardInfos) {
			if (rewardInfo.getTimeType() == 1) {
				mTvWeekTarget.setText(String.valueOf(rewardInfo
						.getTargetCount()));
				mTvCurWeekCount
						.setText(String.valueOf(rewardInfo.getCurCount()));
				mTvWeekTargetReward.setText(rewardInfo.getEncourage());
			} else if (rewardInfo.getTimeType() == 2) {
				mTvMonthTarget.setText(String.valueOf(rewardInfo
						.getTargetCount()));
				mTvCurMonthCount.setText(String.valueOf(rewardInfo
						.getCurCount()));
				mTvMonthTargetReward.setText(rewardInfo.getEncourage());
			} else {
				mTvYearTarget.setText(String.valueOf(rewardInfo
						.getTargetCount()));
				mTvCurYearCount
						.setText(String.valueOf(rewardInfo.getCurCount()));
				mTvYearTargetReward.setText(rewardInfo.getEncourage());
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_red_flower);
		bindField();
		showProgressDialog("", true);
		config = new SharedConfig(this);
		new QueryRewardInfoListServer().start(config.getUserId(),
				config.getAlarmCode(), queryRewardInfoBack);
	}

	private void bindField() {
		findViewById(R.id.btn_red_flower_back).setOnClickListener(this);
//		findViewById(R.id.btn_view_statistic).setOnClickListener(this);
		findViewById(R.id.rl_target_flowers_setting).setOnClickListener(this);

		mTvCurWeekCount = (TextView) findViewById(R.id.txt_curr_week_flowers);
		mTvCurMonthCount = (TextView) findViewById(R.id.txt_curr_month_flowers);
		mTvCurYearCount = (TextView) findViewById(R.id.txt_curr_year_flowers);
		mTvWeekTarget = (TextView) findViewById(R.id.txt_week_target_flowers);
		mTvWeekTargetReward = (TextView) findViewById(R.id.txt_week_reward);
		mTvMonthTarget = (TextView) findViewById(R.id.txt_month_target_flowers);
		mTvMonthTargetReward = (TextView) findViewById(R.id.txt_month_reward);
		mTvYearTarget = (TextView) findViewById(R.id.txt_year_target_flowers);
		mTvYearTargetReward = (TextView) findViewById(R.id.txt_year_reward);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_red_flower_back:
			finish();
			break;
		case R.id.btn_view_statistic:
//			Intent statistic = new Intent(LittleRedFlowerActivity.this,
//					FlowerStatisticActivity.class);
//			startActivity(statistic);
			break;
		case R.id.rl_target_flowers_setting:
			Intent setting = new Intent(LittleRedFlowerActivity.this,
					FlowerTargetSettingActivity.class);
			setting.putExtra("week_target", mTvWeekTarget.getText().toString());
			setting.putExtra("month_target", mTvMonthTarget.getText()
					.toString());
			setting.putExtra("year_target", mTvYearTarget.getText().toString());
			setting.putExtra("week_reward", mTvWeekTargetReward.getText()
					.toString());
			setting.putExtra("month_reward", mTvMonthTargetReward.getText()
					.toString());
			setting.putExtra("year_reward", mTvYearTargetReward.getText()
					.toString());
			startActivityForResult(setting, 0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle;
		if (resultCode == 1010) {
			bundle = data.getExtras();
			mTvWeekTarget.setText(bundle.getString("week_target"));
			mTvWeekTargetReward.setText(bundle.getString("week_target_reward"));
			mTvMonthTarget.setText(bundle.getString("month_target"));
			mTvMonthTargetReward.setText(bundle
					.getString("month_target_reward"));
			mTvYearTarget.setText(bundle.getString("year_target"));
			mTvYearTargetReward.setText(bundle.getString("year_target_reward"));
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