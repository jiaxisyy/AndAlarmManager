package com.babacit.alarm.ui.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.entity.AlarmInfo;
import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.CreateAlarmClockListServer;
import com.babacit.alarm.server.UpdateAlarmClockListServer;
import com.babacit.alarm.ui.dialog.ChooseCircleDlgFragment;
import com.babacit.alarm.ui.dialog.CircleWheelDialogFragment;
import com.babacit.alarm.ui.dialog.DateWheelDialogFragment;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.ui.dialog.TimeWheelDialogFragment;
import com.babacit.alarm.update2.StringUtils;
import com.babacit.alarm.update2.WeekUtils;
import com.babacit.alarm.utils.DateUtils;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.NumberUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.example.hekd.kotlinapp.ai.ReminderConstant;
import com.example.hekd.kotlinapp.ai.ReminderMsg;
import com.kyleduo.switchbutton.SwitchButton;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EyesProtectActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {

	private List<AlarmInfo> _alarmList = new ArrayList<AlarmInfo>();
	private AlarmInfo alarmInfo;
	private List<RingInfo> _ringList = new ArrayList<RingInfo>();
	private SharedConfig config;
	private TextView mTvAlarmLabel, mTvAlarmCircle, mTvAlarmEarlyTime,
			mTvAlarmDate, mTvAlarmRemark, mTvStartTime, mTvEndTime;
	private SwitchButton mSbRemind;
	private int needRemind = 1;
	private Button mBtRecord, mBtChooseRing;

	private boolean createFlag = false;
	private ReminderMsg reminderMsg;
	private RequestCallBack createAlarmCallBack = new RequestCallBack() {
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

	private RequestCallBack updateAlarmCallBack = new RequestCallBack() {
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

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Intent intent = new Intent("create_alarm");
				sendBroadcast(intent);
				dismissProgress();
				finish();
				break;
			case 1:
			case 3:
				dismissProgress();
				ToastUtil.showToast(EyesProtectActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			case 2:
				setResult(MainActivity.RESULT_CODE_FROM_EYEPROTECT_ALARM);
				dismissProgress();
				finish();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyes_protect_alarm);
		bindField();
		config = new SharedConfig(this);
		bindField();
		if (!getIntent().getBooleanExtra("modify", false)) {
			createFlag = true;
			initializeAlarmInfo();
		} else {
			alarmInfo = getIntent().getParcelableExtra("alarm");
			refreshValues();
		}
	}

	private void refreshValues() {
		_ringList = alarmInfo.getRingList();
		if (_ringList != null && _ringList.size() > 0) {
			mBtChooseRing.setText("  铃  声  ");
			mBtChooseRing.setSelected(true);
		}
		mTvAlarmLabel.setText(alarmInfo.getTitle());
		mTvStartTime.setText(alarmInfo.getTimes().get(0));
		mTvEndTime.setText(alarmInfo.getTimes().get(1));
		// mTvAlarmCircle.setText("");
//		mTvAlarmDate.setText(alarmInfo.getDate());
		mTvAlarmDate.setText(DateUtils.getDateStringYearMonthDay(DateUtils
				.string2Date(alarmInfo.getSortTime())));
		mTvAlarmEarlyTime.setText("");
		if (alarmInfo.getNeedRemind() == 0) {
			mSbRemind.setChecked(false);
		} else {
			mSbRemind.setChecked(true);
		}

		switch (alarmInfo.getEarlyTime()) {
		case 0:
			mTvAlarmEarlyTime.setText("不提前");
			break;
		case 1:
			mTvAlarmEarlyTime.setText("1分钟");
			break;
		case 5:
			mTvAlarmEarlyTime.setText("5分钟");
			break;
		case 10:
			mTvAlarmEarlyTime.setText("10分钟");
			break;
		case 15:
			mTvAlarmEarlyTime.setText("15分钟");
			break;
		case 30:
			mTvAlarmEarlyTime.setText("30分钟");
			break;
		case 60:
			mTvAlarmEarlyTime.setText("1小时");
			break;
		case 360:
			mTvAlarmEarlyTime.setText("6小时");
			break;
		case 1440:
			mTvAlarmEarlyTime.setText("1天");
			break;
		default:
			break;
		}
		mTvAlarmRemark.setText(alarmInfo.getRemark());

		if (alarmInfo.getHolidayFilter() == 1) {
			mTvAlarmCircle.setText("仅寒暑假提醒");
		} else if (alarmInfo.getHolidayFilter() == 2) {
			mTvAlarmCircle.setText("寒暑假不提醒");
		} else if (alarmInfo.getHolidayFilter() == 3) {
			mTvAlarmCircle.setText("节假日不提醒");
		}
		switch (alarmInfo.getTimeType()) {
		case 1:
			mTvAlarmCircle.setText("仅一次");
			break;
		case 2:
			mTvAlarmCircle.setText("每天");
			break;
		case 3:
			mTvAlarmCircle.setText("每月");
			break;
		case 4:
			mTvAlarmCircle.setText("每年");
			break;
		case 5:
			if (alarmInfo.getDay() != null) {
				String[] weeks = alarmInfo.getDay().split(",");
				int[] weekArray = NumberUtils.stringArrToNumericArr(weeks);
				Arrays.sort(weekArray);
				String week = "";
				for (int i = 0; i < weekArray.length; i++) {
					switch (weekArray[i]) {
					case 0:
						week += "周一 ";
						break;
					case 1:
						week += "周二 ";
						break;
					case 2:
						week += "周三 ";
						break;
					case 3:
						week += "周四 ";
						break;
					case 4:
						week += "周五 ";
						break;
					case 5:
						week += "周六 ";
						break;
					case 6:
						week += "周日 ";
						break;
					default:
						break;
					}
				}
				mTvAlarmCircle.setText(/*"周" + */week);
			}
			break;
		default:
			break;
		}
	}

	private void initializeAlarmInfo() {

		// SYY test
		if (reminderMsg != null) {
			String time = reminderMsg.getTime();
			String hour = time.substring(0, 2);
			String min = time.substring(3, 5);
			String date = reminderMsg.getDate();
			int type = reminderMsg.getType();
			String event = reminderMsg.getEvent();
			ArrayList<Integer> weeks = reminderMsg.getWeeks();
			int period = reminderMsg.getPeriod();
			mTvStartTime.setText(hour+":"+min);
			//周期
			alarmInfo = new AlarmInfo();
			if (period == ReminderConstant.Companion.getPERIOD_USER_DEFINED()) {

				if (weeks != null) {
					if (weeks.size() == 1) {
						alarmInfo.setDay(weeks.get(0).toString().trim());
					} else if (weeks.size() > 1) {
						//week  {1,2,3}
						ArrayList<String> strings = new ArrayList<>();
						for (Integer integer : weeks) {
							strings.add(integer.toString());
						}
						System.out.println("=================newStr=" + StringUtils.listToString(strings));
						alarmInfo.setDay(StringUtils.listToString(strings));
						StringBuffer str = WeekUtils.Companion.listIntToStr(weeks);
						mTvAlarmCircle.setText(str);
					}
				}
			} else {
				alarmInfo.setDay("");
				switch (period) {
					case 1:
						mTvAlarmCircle.setText("仅一次");
						break;
					case 2:
						mTvAlarmCircle.setText("每天");
						break;
					case 3:
						mTvAlarmCircle.setText("每月");
						break;
				}
			}
			mTvAlarmDate.setText(date);
			alarmInfo.setClockType(type);
			alarmInfo.setStatus(1);
			alarmInfo.setTimeType(period);
			alarmInfo.setDelayTime(0);
//			alarmInfo.setNeedRemind(0);



		}else {
			mTvAlarmDate.setText(DateUtils.getDateStringYearMonthDay());
			alarmInfo = new AlarmInfo();
			alarmInfo.setClockType(5);
			alarmInfo.setStatus(1);
			alarmInfo.setTimeType(1);
			alarmInfo.setDelayTime(0);
			alarmInfo.setEarlyTime(0);
		}
		mBtChooseRing.setText("  铃  声  ");
		mBtChooseRing.setSelected(true);
		RingInfo ring = new RingInfo();
		ring.setRingName("眼保健操");
		ring.setRingType(1);
		ring.setRingCategory(3);
		ring.setRingId(11166);
		ring.setRingUrl(Constant.BASE_URL + "uploads/factory/ring/eyeExercises.ogg");
		_ringList.add(ring);
		alarmInfo.setRingList(_ringList);
	}

	private void bindField() {
		mSbRemind = (SwitchButton) findViewById(R.id.sb_remind_confirm);
		mSbRemind.setChecked(true);
		mSbRemind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					needRemind = 1;
				} else
					needRemind = 0;
			}
		});
		mTvAlarmLabel = (TextView) findViewById(R.id.tv_eyes_protect_alarm_title);
		mTvAlarmCircle = (TextView) findViewById(R.id.tv_eyes_protect_alarm_circle);
		mTvAlarmDate = (TextView) findViewById(R.id.tv_eyes_protect_alarm_date);
		mTvAlarmEarlyTime = (TextView) findViewById(R.id.tv_eyes_protect_alarm_early_time);
		mTvAlarmRemark = (TextView) findViewById(R.id.tv_eyes_protect_alarm_remark);
		mTvStartTime = (TextView) findViewById(R.id.tv_eyes_protect_first_time);
		mTvEndTime = (TextView) findViewById(R.id.tv_eyes_protect_second_time);

		findViewById(R.id.ll_eye_protect_first_time).setOnClickListener(this);
		findViewById(R.id.ll_eye_protect_second_time).setOnClickListener(this);
		findViewById(R.id.tv_eyes_protect_alarm_finish)
				.setOnClickListener(this);
		findViewById(R.id.btn_eyes_protect_alarm_back).setOnClickListener(this);
		findViewById(R.id.rl_eyes_protect_alarm_label).setOnClickListener(this);
		findViewById(R.id.rl_eyes_protect_alarm_circle)
				.setOnClickListener(this);
		findViewById(R.id.rl_eyes_protect_alarm_date).setOnClickListener(this);
		findViewById(R.id.rl_eyes_protect_alarm_early_time).setOnClickListener(
				this);
		findViewById(R.id.rl_eyes_protect_alarm_remark)
				.setOnClickListener(this);
		mBtChooseRing = (Button) findViewById(R.id.btn_choose_ring);
		mBtRecord = (Button) findViewById(R.id.btn_record_voice);
		mBtChooseRing.setOnClickListener(this);
		mBtRecord.setOnClickListener(this);
		reminderMsg = (ReminderMsg) getIntent().getSerializableExtra(ReminderConstant.Companion
				.getREMINDERMSG());
	}

	private void collectAlarmInfos() {
		String date = mTvAlarmDate.getText().toString();
		if (alarmInfo.getTimeType() == 3) {
			alarmInfo.setDate(date.substring(8, 10));
		} else if (alarmInfo.getTimeType() == 4) {
			alarmInfo.setDate(date.substring(5, 10));
		} else {
			alarmInfo.setDate(date);
		}
		alarmInfo.setTitle(mTvAlarmLabel.getText().toString());
		alarmInfo.setRemark(mTvAlarmRemark.getText().toString());
		String startTime = mTvStartTime.getText().toString();
		String endTime = mTvEndTime.getText().toString();
		List<String> times = new ArrayList<String>();
		times.add(startTime);
		times.add(endTime);
		alarmInfo.setTimes(times);
		if (alarmInfo.getTimeType() == 1) {
			checkValidate(times);
		}
		alarmInfo.setNeedRemind(needRemind);

		_alarmList.clear();
		_alarmList.add(alarmInfo);
	}

	private void checkValidate(List<String> times) {
		if ((alarmInfo.getDate().compareTo(
				DateUtils.getDateStringYearMonthDay()) < 0)
				&& (times.get(times.size() - 1).compareTo(DateUtils.getTime()) > 0)) {
			alarmInfo.setDate(DateUtils.getDateStringYearMonthDay());
		} else if ((alarmInfo.getDate() + times.get(times.size() - 1))
				.compareTo(DateUtils.getDateStringYearMonthDay()
						+ DateUtils.getTime()) < 0) {
			alarmInfo.setDate(DateUtils.getSpecialDayAfter(DateUtils
					.getDateStringYearMonthDay()));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_eyes_protect_alarm_finish:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			if (_ringList.size() == 0) {
				ToastUtil.showToast(getApplicationContext(), "请选择铃声！");
				return;
			}
			showProgressDialog("", false);
			collectAlarmInfos();
			if (createFlag) {
				new CreateAlarmClockListServer().start(config.getUserId(),
						config.getAlarmCode(), _alarmList, createAlarmCallBack);
			} else {
				new UpdateAlarmClockListServer().start(config.getUserId(),
						config.getAlarmCode(), _alarmList, updateAlarmCallBack);
			}
			break;
		case R.id.btn_eyes_protect_alarm_back:
			finish();
			break;
		case R.id.ll_eye_protect_first_time:
			TimeWheelDialogFragment first = new TimeWheelDialogFragment();
			first.show(getFragmentManager(), "eye_first");
			break;
		case R.id.ll_eye_protect_second_time:
			TimeWheelDialogFragment second = new TimeWheelDialogFragment();
			second.show(getFragmentManager(), "eye_second");
			break;
		case R.id.rl_eyes_protect_alarm_label:
			Intent title = new Intent(EyesProtectActivity.this,
					CommonSettingActivity.class);
			title.putExtra("title", "设置标题");
			title.putExtra("content", mTvAlarmLabel.getText().toString());
			startActivityForResult(title, 0);
			break;
		case R.id.rl_eyes_protect_alarm_circle:
			ChooseCircleDlgFragment circle = new ChooseCircleDlgFragment();
			Bundle args = new Bundle();
			args.putStringArray("grid_1",
					getResources().getStringArray(R.array.alarm_circle_1));
			args.putStringArray("grid_2",
					getResources().getStringArray(R.array.alarm_circle_2));
			args.putParcelable("alarm", alarmInfo);
			circle.setArguments(args);
			circle.setCancelable(false);
			circle.show(getFragmentManager(), "eye_protect_circle");
			break;
		case R.id.rl_eyes_protect_alarm_date:
			DateWheelDialogFragment date = new DateWheelDialogFragment();
			date.show(getFragmentManager(), "eye_protect_date");
			mTvAlarmDate.setText(DateUtils.getDateStringYearMonthDay());
			break;
		case R.id.rl_eyes_protect_alarm_early_time:
			CircleWheelDialogFragment pre = new CircleWheelDialogFragment();
			Bundle a = new Bundle();
			a.putStringArray("wheel",
					getResources().getStringArray(R.array.pre_remind));
			pre.setArguments(a);
			pre.show(getFragmentManager(), "eye_early_time");
			break;
		case R.id.rl_eyes_protect_alarm_remark:
			Intent remark = new Intent(EyesProtectActivity.this,
					CommonSettingActivity.class);
			remark.putExtra("title", "设置备注");
			remark.putExtra("content", mTvAlarmRemark.getText().toString());
			startActivityForResult(remark, 2);
			break;
		case R.id.btn_choose_ring:
			Intent ring = new Intent(EyesProtectActivity.this,
					ChooseRingActivity.class);
			ring.putExtra("ring", _ringList.get(0));
			startActivityForResult(ring, 1);
			break;
		case R.id.btn_record_voice:
			Intent voice = new Intent(EyesProtectActivity.this,
					RecordVoiceActivity.class);
			startActivityForResult(voice, 3);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("eye_early_time")) {
			mTvAlarmEarlyTime.setText(message);
			if (message.equals("不提前")) {
				alarmInfo.setEarlyTime(0);
			} else if (message.equals("1分钟")) {
				alarmInfo.setEarlyTime(1);
			} else if (message.equals("5分钟")) {
				alarmInfo.setEarlyTime(5);
			} else if (message.equals("10分钟")) {
				alarmInfo.setEarlyTime(10);
			} else if (message.equals("15分钟")) {
				alarmInfo.setEarlyTime(15);
			} else if (message.equals("30分钟")) {
				alarmInfo.setEarlyTime(30);
			} else if (message.equals("1小时")) {
				alarmInfo.setEarlyTime(60);
			} else if (message.equals("6小时")) {
				alarmInfo.setEarlyTime(360);
			} else if (message.equals("1天")) {
				alarmInfo.setEarlyTime(1440);
			}
		} else if (tag.equals("eye_protect_date")) {
			mTvAlarmDate.setText(message);
		} else if (tag.equals("eye_protect_circle")) {
			try {
				JSONObject json = new JSONObject(message.toString());
				switch (json.getInt("holidayFilter")) {
				case 1:
					alarmInfo.setHolidayFilter(1);
					mTvAlarmCircle.setText("仅寒暑假提醒");
					break;
				case 2:
					alarmInfo.setHolidayFilter(2);
					mTvAlarmCircle.setText("寒暑假不提醒");
					break;
				case 3:
					alarmInfo.setHolidayFilter(3);
					mTvAlarmCircle.setText("节假日不提醒");
					break;
				default:
					alarmInfo.setHolidayFilter(0);
					break;
				}
				switch (json.getInt("timeType")) {
				case 1:
					mTvAlarmCircle.setText("仅一次");
					alarmInfo.setTimeType(1);
					alarmInfo.setDay("");
					break;
				case 2:
					mTvAlarmCircle.setText("每天");
					alarmInfo.setTimeType(2);
					alarmInfo.setDay("");
					break;
				case 3:
					mTvAlarmCircle.setText("每月");
					alarmInfo.setTimeType(3);
					alarmInfo.setDay("");
					break;
				case 4:
					mTvAlarmCircle.setText("每年");
					alarmInfo.setTimeType(4);
					alarmInfo.setDay("");
					break;
				case 5:
					alarmInfo.setTimeType(5);
					String[] days = json.getString("week").split(",");
					alarmInfo.setDay(json.getString("week"));
					int[] dayNums = NumberUtils.stringArrToNumericArr(days);
					Arrays.sort(dayNums);
					String day = "";
					for (int i = 0; i < dayNums.length; i++) {
						if (dayNums[i] != -1) {
							day += NumberUtils
									.changeNumericToString(dayNums[i]) + " ";
						}
					}
					mTvAlarmCircle.setText(/*"周 " + */day);
					break;
				default:
					alarmInfo.setTimeType(0);
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (tag.equals("eye_first")) {
			mTvStartTime.setText(message);
		} else if (tag.equals("eye_second")) {
			mTvEndTime.setText(message);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (data != null) {
				String common = data.getStringExtra("common");
				if (common != null) {
					mTvAlarmLabel.setText(common);
					alarmInfo.setTitle(common);
				}
			}
			break;
		case 1:
			if (data != null && data.getParcelableExtra("ring") != null) {
				_ringList.clear();
				_ringList.add((RingInfo) data.getParcelableExtra("ring"));
				if (_ringList.size() > 0) {
					alarmInfo.setRingList(_ringList);
					mBtChooseRing.setText("  铃  声  ");
					mBtChooseRing.setSelected(true);
					mBtRecord.setText("录制提醒");
					mBtRecord.setSelected(false);
				}
			}
			break;
		case 2:
			if (data != null) {
				String common = data.getStringExtra("common");
				if (common != null) {
					mTvAlarmRemark.setText(common);
				}
			}
			break;
		case 3:
			if (data != null && data.getParcelableExtra("voice") != null) {
				_ringList.clear();
				_ringList.add((RingInfo) data.getParcelableExtra("voice"));
				alarmInfo.setRingList(_ringList);
				mBtChooseRing.setText("选择提示音");
				mBtChooseRing.setSelected(false);
				mBtRecord.setText("  录  音  ");
				mBtRecord.setSelected(true);
			}
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
