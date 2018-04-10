package com.babacit.alarm.ui.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.entity.AlarmInfo;
import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.server.CreateAlarmClockListServer;
import com.babacit.alarm.server.UpdateAlarmClockListServer;
import com.babacit.alarm.ui.adapter.WheelArrayAdapter;
import com.babacit.alarm.ui.adapter.WheelNumericAdapter;
import com.babacit.alarm.ui.dialog.CircleWheelDialogFragment;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.ui.dialog.TimeWheelDialogFragment;
import com.babacit.alarm.update2.WeekUtils;
import com.babacit.alarm.utils.DateUtils;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.SimpleLunarCalendar;
import com.babacit.alarm.utils.ToastUtil;
import com.example.hekd.kotlinapp.ai.ReminderConstant;
import com.example.hekd.kotlinapp.ai.ReminderMsg;
import com.kyleduo.switchbutton.SwitchButton;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnniversaryAlarmActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {
	// Time scrolled flag
	private Calendar calendar = Calendar.getInstance();
	private WheelView year, month, day;
	private WheelNumericAdapter yearAdapter, monthAdapter, dayAdapter;
	private LinearLayout mLlGregorian, mLlLunar;
	private CheckBox mCbLunar;
	private boolean isLunar = false;
	private static final int TEXT_SIZE = 25;
	private String[] lunarYears = new String[200];
	private String[] lunarMonths;
	private String[] lunarDays = new String[30];
	private List<String> monthList = new ArrayList<String>();

	private WheelView mWvLunarYear, mWvLunarMonth, mWvLunarDay;
	private WheelArrayAdapter lunarYearAdapter, lunarMonthAdapter,
			lunarDayAdapter;

	private TextView mTvAlarmLabel, mTvAlarmTime, mTvAlarmEarlyTime,
			mTvAlarmRing, mTvAlarmRemark;
	private SwitchButton mSbRemind;
	private int needRemind = 1;
	private Button mBtRecord, mBtChooseRing;
	private List<AlarmInfo> _alarmList = new ArrayList<AlarmInfo>();
	private AlarmInfo alarmInfo;
	private List<RingInfo> _ringList = new ArrayList<RingInfo>();
	private SharedConfig config;

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
				ToastUtil.showToast(AnniversaryAlarmActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			case 2:
				setResult(MainActivity.RESULT_CODE_FROM_ANNIVERSARY_ALARM);
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
		setContentView(R.layout.activity_anniversary_alarm);
		config = new SharedConfig(this);
		bindField();
		SimpleLunarCalendar lunar;
		lunar = new SimpleLunarCalendar(new Date());
		for (int i = 1900; i < 2100; i++) {
			lunarYears[i - 1900] = lunar.getLunarYearString(i);
		}
		getLunarMonthsByYear(lunar);
		initLunarView();
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
		String dates[] = alarmInfo.getDate().split("-");
		// String dates[] = DateUtils.getDateStringYearMonthDay(
		// DateUtils.string2Date(alarmInfo.getSortTime())).split("-");
		if (alarmInfo.getDateType() == 0) {
			mCbLunar.setChecked(false);
			isLunar = false;
			mLlGregorian.setVisibility(View.VISIBLE);
			mLlLunar.setVisibility(View.GONE);
//			year.setCurrentItem(Integer.parseInt(dates[0]) - 1900);
			month.setCurrentItem(Integer.parseInt(dates[0]) - 1);
			day.setCurrentItem(Integer.parseInt(dates[1]) - 1);
		} else {
			mCbLunar.setChecked(true);
			isLunar = true;
			mLlGregorian.setVisibility(View.GONE);
			mLlLunar.setVisibility(View.VISIBLE);
//			mWvLunarYear.setCurrentItem(Integer.parseInt(dates[0]) - 1900);
			mWvLunarMonth.setCurrentItem(Integer.parseInt(dates[0]) - 1);
			mWvLunarDay.setCurrentItem(Integer.parseInt(dates[1]) - 1);
		}
		if (alarmInfo.getNeedRemind() == 0) {
			mSbRemind.setChecked(false);
		} else {
			mSbRemind.setChecked(true);
		}
		mTvAlarmLabel.setText(alarmInfo.getTitle());
		mTvAlarmRemark.setText(alarmInfo.getRemark());

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
		mTvAlarmTime.setText(alarmInfo.getTimes().get(0));
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
			String month = date.substring(5, 7);
			String day = date.substring(8, 10);
			this.month.setCurrentItem(Integer.valueOf(month.trim()));
			this.day.setCurrentItem(Integer.valueOf(day.trim()));
			//周期
			alarmInfo = new AlarmInfo();
			alarmInfo.setClockType(type);
			alarmInfo.setStatus(1);
			alarmInfo.setTimeType(period);
			alarmInfo.setDelayTime(0);


		}else{


			alarmInfo = new AlarmInfo();
			alarmInfo.setClockType(6);
			alarmInfo.setStatus(1);
			alarmInfo.setTimeType(4);
			alarmInfo.setDelayTime(0);
		}
		mBtChooseRing.setText("  铃  声  ");
		mBtChooseRing.setSelected(true);
		RingInfo ring = new RingInfo();
		ring.setRingName("音乐011");
		ring.setRingType(1);
		ring.setRingCategory(2);
		ring.setRingId(2011);
		ring.setRingUrl(Constant.BASE_URL + "uploads/factory/music/music011.ogg");
		_ringList.add(ring);
		alarmInfo.setRingList(_ringList);
	}

	private void collectAlarmInfos() {
		alarmInfo.setNeedRemind(needRemind);
		alarmInfo.setRemark(mTvAlarmRemark.getText().toString());
		alarmInfo.setTitle(mTvAlarmLabel.getText().toString());
		String time = mTvAlarmTime.getText().toString();
		List<String> times = new ArrayList<String>();
		times.add(time);
		alarmInfo.setTimes(times);
		if (!isLunar) {
			alarmInfo.setDate(String.format("%02d-%02d",
					month.getCurrentItem() + 1, day.getCurrentItem() + 1));
			alarmInfo.setDateType(0);
		} else {
			alarmInfo.setDate(String.format("%02d-%02d",
					mWvLunarMonth.getCurrentItem() + 1,
					mWvLunarDay.getCurrentItem() + 1));
			alarmInfo.setDateType(1);
		}

		alarmInfo.setRingList(_ringList);
		_alarmList.clear();
		_alarmList.add(alarmInfo);
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
		mTvAlarmLabel = (TextView) findViewById(R.id.tv_anniversary_alarm_title);
		mTvAlarmTime = (TextView) findViewById(R.id.tv_anniversary_alarm_time);
		mTvAlarmEarlyTime = (TextView) findViewById(R.id.tv_anniversary_alarm_early_time);
		mTvAlarmRemark = (TextView) findViewById(R.id.tv_anniversary_alarm_remark);

		mLlGregorian = (LinearLayout) findViewById(R.id.ll_gregorian_calendar);
		mLlLunar = (LinearLayout) findViewById(R.id.ll_lunar_calendar);
		mCbLunar = (CheckBox) findViewById(R.id.cb_lunar);
		mCbLunar.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.selector_lunar_switch, 0, 0, 0);
		mCbLunar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					isLunar = true;
					mLlLunar.setVisibility(View.VISIBLE);
					mLlGregorian.setVisibility(View.GONE);
				} else {
					isLunar = false;
					mLlLunar.setVisibility(View.GONE);
					mLlGregorian.setVisibility(View.VISIBLE);
				}
			}
		});
		findViewById(R.id.btn_anniversary_alarm_back).setOnClickListener(this);
		findViewById(R.id.tv_anniversary_alarm_finish).setOnClickListener(this);
		findViewById(R.id.rl_anniversary_alarm_label).setOnClickListener(this);
		findViewById(R.id.rl_anniversary_alarm_time).setOnClickListener(this);
		findViewById(R.id.rl_anniversary_alarm_early_time).setOnClickListener(
				this);
		findViewById(R.id.rl_anniversary_alarm_remark).setOnClickListener(this);
		mBtChooseRing = (Button) findViewById(R.id.btn_choose_ring);
		mBtRecord = (Button) findViewById(R.id.btn_record_voice);
		mBtChooseRing.setOnClickListener(this);
		mBtRecord.setOnClickListener(this);

		year = (WheelView) findViewById(R.id.wv_anniversary_alarm_year);
		final WheelView yearSep = (WheelView) findViewById(R.id.wv_anniversary_alarm_year_mark);
		month = (WheelView) findViewById(R.id.wv_anniversary_alarm_month);
		final WheelView monthSep = (WheelView) findViewById(R.id.wv_anniversary_alarm_month_mark);
		day = (WheelView) findViewById(R.id.wv_anniversary_alarm_day);
		final WheelView daySep = (WheelView) findViewById(R.id.wv_anniversary_alarm_day_mark);
		// month.setCyclic(true);
		// day.setCyclic(true);

		yearSep.setViewAdapter(new WheelArrayAdapter(this, 1,
				new String[] { "年" }, 0));
		monthSep.setViewAdapter(new WheelArrayAdapter(this, 1,
				new String[] { "月" }, 0));
		daySep.setViewAdapter(new WheelArrayAdapter(this, 1,
				new String[] { "日" }, 0));

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};

		// year
		int curYear = calendar.get(Calendar.YEAR);
		yearAdapter = new WheelNumericAdapter(this, 1900, curYear, curYear);
		yearAdapter.setGravity(Gravity.CENTER_HORIZONTAL);
		year.setViewAdapter(yearAdapter);
		year.setCurrentItem(curYear - 1900);
		year.addChangingListener(listener);

		// month
		int curMonth = calendar.get(Calendar.MONTH);
		monthAdapter = new WheelNumericAdapter(this, 1, 12, curMonth);
		monthAdapter.setGravity(Gravity.CENTER_HORIZONTAL);
		month.setViewAdapter(monthAdapter);
		// month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);

		// day
		updateDays(year, month, day);
		day.addChangingListener(listener);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		reminderMsg = (ReminderMsg) getIntent().getSerializableExtra(ReminderConstant.Companion
				.getREMINDERMSG());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_anniversary_alarm_finish:
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
		case R.id.btn_anniversary_alarm_back:
			finish();
			break;
		case R.id.rl_anniversary_alarm_label:
			Intent title = new Intent(AnniversaryAlarmActivity.this,
					CommonSettingActivity.class);
			title.putExtra("title", "设置标题");
			title.putExtra("content", mTvAlarmLabel.getText().toString());
			startActivityForResult(title, 0);
			break;
		case R.id.rl_anniversary_alarm_time:
			TimeWheelDialogFragment time = new TimeWheelDialogFragment();
			time.show(getFragmentManager(), "anniversary_time");
			break;
		case R.id.rl_anniversary_alarm_early_time:
			CircleWheelDialogFragment pre = new CircleWheelDialogFragment();
			Bundle a = new Bundle();
			a.putStringArray("wheel",
					getResources().getStringArray(R.array.pre_remind));
			pre.setArguments(a);
			pre.show(getFragmentManager(), "anniversary_early_time");
			break;
		case R.id.rl_anniversary_alarm_remark:
			Intent remark = new Intent(AnniversaryAlarmActivity.this,
					CommonSettingActivity.class);
			remark.putExtra("title", "设置备注");
			remark.putExtra("content", mTvAlarmRemark.getText().toString());
			startActivityForResult(remark, 2);
			break;
		case R.id.btn_choose_ring:
			Intent ring = new Intent(AnniversaryAlarmActivity.this,
					ChooseRingActivity.class);
			ring.putExtra("ring", _ringList.get(0));
			startActivityForResult(ring, 1);
			break;
		case R.id.btn_record_voice:
			Intent voice = new Intent(AnniversaryAlarmActivity.this,
					RecordVoiceActivity.class);
			startActivityForResult(voice, 3);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	void updateDays(WheelView year, WheelView month, WheelView day) {
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		yearAdapter.setCurValue(year.getCurrentItem());
		monthAdapter.setCurValue(month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayAdapter = new WheelNumericAdapter(this, 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		dayAdapter.setGravity(Gravity.CENTER_HORIZONTAL);
		day.setViewAdapter(dayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		dayAdapter.setCurValue(curDay - 1);

	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("anniversary_early_time")) {
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
		} else if (tag.equals("anniversary_time")) {
			mTvAlarmTime.setText(message);
		}
	}

	private void initLunarView() {
		mWvLunarYear = (WheelView) findViewById(R.id.wv_lunar_year);
		mWvLunarMonth = (WheelView) findViewById(R.id.wv_lunar_month);
		mWvLunarDay = (WheelView) findViewById(R.id.wv_lunar_day);

		OnWheelChangedListener lunarListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateLunarMonth(mWvLunarYear, mWvLunarMonth);
				updateLunarDays(mWvLunarYear, mWvLunarMonth, mWvLunarDay);
			}
		};
		// year
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		lunarYearAdapter = new WheelArrayAdapter(this, curYear - 1900 + 1,
				lunarYears, curYear);
		lunarYearAdapter.setTextSize(TEXT_SIZE);
		mWvLunarYear.setViewAdapter(lunarYearAdapter);
		mWvLunarYear.setCurrentItem(curYear - 1900);
		mWvLunarYear.setVisibleItems(3);
		mWvLunarYear.addChangingListener(lunarListener);
		// month
		updateLunarMonth(mWvLunarYear, mWvLunarMonth);
		int curMonth = calendar.get(Calendar.MONTH);
		mWvLunarMonth.setCurrentItem(curMonth);
		mWvLunarMonth.setVisibleItems(3);
		mWvLunarMonth.setCyclic(true);
		mWvLunarMonth.addChangingListener(lunarListener);
		// day
		updateLunarDays(mWvLunarYear, mWvLunarMonth, mWvLunarDay);
		mWvLunarDay.setVisibleItems(3);
		mWvLunarDay.addChangingListener(lunarListener);
		mWvLunarDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

	}

	private void getLunarMonthsByYear(SimpleLunarCalendar lunar) {
		int leapMonth = lunar.getLunarLeapMonth(lunar.getYear());
		monthList.clear();
		if (leapMonth == 0) {
			lunarMonths = new String[12];
		} else
			lunarMonths = new String[13];
		for (int i = 1; i <= 12; i++) {
			if (leapMonth == i) {
				monthList.add(lunar.getLunarMonthString(i));
				monthList.add("闰" + lunar.getLunarMonthString(i));
			} else
				monthList.add(lunar.getLunarMonthString(i));
		}
		for (int i = 0; i < monthList.size(); i++) {
			lunarMonths[i] = monthList.get(i);
		}
	}

	private void getLunarDays(SimpleLunarCalendar lunar) {
		for (int i = 0; i < lunar.getMaxDayInMonth(); i++) {
			lunarDays[i] = lunar.getLunarDayString(i + 1);
		}
	}

	private void updateLunarMonth(WheelView year, WheelView month) {
		calendar.set(Calendar.YEAR, 1900 + year.getCurrentItem());
		SimpleLunarCalendar lunar = new SimpleLunarCalendar(
				calendar.getTimeInMillis());
		Logger.d("log.test calendar.getTimeInMillis():"
				+ calendar.getTimeInMillis());
		lunarYearAdapter.setCurValue(year.getCurrentItem());
		getLunarMonthsByYear(lunar);
		// lunarMonthAdapter = new WheelArrayAdapter(this, lunarMonths,
		// month.getCurrentItem());
		lunarMonthAdapter = new WheelArrayAdapter(this, lunarMonths.length,
				lunarMonths, month.getCurrentItem());
		lunarMonthAdapter.setTextSize(TEXT_SIZE);
		mWvLunarMonth.setViewAdapter(lunarMonthAdapter);
	}

	private void updateLunarDays(WheelView year, WheelView month, WheelView day) {
		calendar.set(Calendar.YEAR, 1900 + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		lunarYearAdapter.setCurValue(year.getCurrentItem());
		lunarMonthAdapter.setCurValue(month.getCurrentItem());
		SimpleLunarCalendar lunar = new SimpleLunarCalendar(calendar.getTime());
		int maxDays = lunar.isBigMonth() ? 30 : 29;
		lunarDays = new String[maxDays];
		getLunarDays(lunar);
		lunarDayAdapter = new WheelArrayAdapter(this, lunarDays.length,
				lunarDays, day.getCurrentItem());
		lunarDayAdapter.setTextSize(TEXT_SIZE);
		day.setViewAdapter(lunarDayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		lunarDayAdapter.setCurValue(curDay - 1);
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
