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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WaterAndMedicineActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {
	private ListView mLvWaterMedicine;
	private WaterMedicineAdapter adapter;
	private List<WaterAlarmInfo> _alarmInfos = new ArrayList<WaterAlarmInfo>();

	private TextView mTvAlarmLabel, mTvAlarmCircle, mTvAlarmDate,
			mTvAlarmEarlyTime, mTvAlarmRemark;
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
				ToastUtil.showToast(WaterAndMedicineActivity.this,
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
		setContentView(R.layout.activity_water_medicine_alarm);
		config = new SharedConfig(this);
		bindField();
		if (!getIntent().getBooleanExtra("modify", false)) {
			createFlag = true;
			initializeAlarmInfo();
		} else {
			alarmInfo = getIntent().getParcelableExtra("alarm");
			refreshValues();
			refreshListView();
		}
	}

	private void refreshValues() {
		_ringList = alarmInfo.getRingList();
		if (alarmInfo.getRingList() != null
				&& alarmInfo.getRingList().size() > 0) {
			mBtChooseRing.setText("  铃  声  ");
			mBtChooseRing.setSelected(true);
		}
		List<String> timeList = alarmInfo.getTimes();
		for (int i = 0; i < timeList.size(); i++) {
			WaterAlarmInfo info = new WaterAlarmInfo();
			info.setTime(String.format("第%d次", i + 1));
			info.setAlarmTime(timeList.get(i));
			_alarmInfos.add(info);
			adapter.notifyDataSetChanged();
		}
		mTvAlarmLabel.setText(alarmInfo.getTitle());
//		mTvAlarmDate.setText(alarmInfo.getDate());
		mTvAlarmDate.setText(DateUtils.getDateStringYearMonthDay(DateUtils
				.string2Date(alarmInfo.getSortTime())));
		// mTvAlarmCircle.setText("");
		// mTvAlarmEarlyTime.setText("");
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

	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			TimeWheelDialogFragment time = new TimeWheelDialogFragment();
			time.show(getFragmentManager(), "water_time" + arg2);
		}
	};

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

		mTvAlarmLabel = (TextView) findViewById(R.id.tv_water_medicine_alarm_title);
		mTvAlarmCircle = (TextView) findViewById(R.id.tv_water_medicine_alarm_circle);
		mTvAlarmDate = (TextView) findViewById(R.id.tv_water_medicine_alarm_date);
		mTvAlarmEarlyTime = (TextView) findViewById(R.id.tv_water_medicine_alarm_early_time);
		mTvAlarmRemark = (TextView) findViewById(R.id.tv_water_medicine_alarm_remark);

		mLvWaterMedicine = (ListView) findViewById(R.id.lv_water_medicine_alarm);
		adapter = new WaterMedicineAdapter(this);
		mLvWaterMedicine.setAdapter(adapter);
		mLvWaterMedicine.setOnItemClickListener(itemClickListener);
		refreshListView();

		findViewById(R.id.btn_water_medicine_alarm_back).setOnClickListener(
				this);
		findViewById(R.id.tv_water_medicine_alarm_finish).setOnClickListener(
				this);
		findViewById(R.id.rl_water_medicine_alarm_label).setOnClickListener(
				this);
		findViewById(R.id.rl_water_medicine_alarm_circle).setOnClickListener(
				this);
		findViewById(R.id.rl_water_medicine_alarm_date)
				.setOnClickListener(this);
		findViewById(R.id.rl_water_medicine_alarm_early_time)
				.setOnClickListener(this);
		findViewById(R.id.rl_water_medicine_alarm_remark).setOnClickListener(
				this);
		findViewById(R.id.btn_add_water_medicine_alarm)
				.setOnClickListener(this);
		mBtChooseRing = (Button) findViewById(R.id.btn_choose_ring);
		mBtRecord = (Button) findViewById(R.id.btn_record_voice);
		mBtChooseRing.setOnClickListener(this);
		mBtRecord.setOnClickListener(this);
		reminderMsg = (ReminderMsg) getIntent().getSerializableExtra(ReminderConstant.Companion
				.getREMINDERMSG());
	}

	private void refreshListView() {
		if (adapter != null) {
			int totalHeight = 0;
			ViewGroup.LayoutParams params = mLvWaterMedicine.getLayoutParams();
			if (adapter.getCount() > 3) {
				for (int i = 0; i < 3; i++) {
					View item = adapter.getView(i, null, mLvWaterMedicine);
					item.measure(0, 0);
					totalHeight += item.getMeasuredHeight();
				}
				params.height = totalHeight
						+ (mLvWaterMedicine.getDividerHeight() * (3 - 1));
			} else {
				for (int i = 0; i < adapter.getCount(); i++) {
					View item = adapter.getView(i, null, mLvWaterMedicine);
					item.measure(0, 0);
					totalHeight += item.getMeasuredHeight();
				}
				params.height = totalHeight
						+ (mLvWaterMedicine.getDividerHeight() * (adapter
								.getCount() - 1));
			}
			for (int i = 0; i < adapter.getCount(); i++) {
				_alarmInfos.get(i).setTime(String.format("第%d次", i + 1));
			}
			mLvWaterMedicine.setLayoutParams(params);
		}
	}

	private void initializeAlarmInfo() {
		alarmInfo = new AlarmInfo();
		List<String> times = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			WaterAlarmInfo info = new WaterAlarmInfo();
			info.setTime(String.format("第%d次", i + 1));
			if (i == 0) {
				info.setAlarmTime("09:00");
				times.add("09:00");
			} else if (i == 1) {
				info.setAlarmTime("11:00");
				times.add("11:00");
			} else {
				info.setAlarmTime("15:00");
				times.add("15:00");
			}
			// times.add(String.format("%02d:%02d", i * 3 + 7, 0));
			// info.setAlarmTime(String.format("%02d:%02d", i * 3 + 7, 0));
			_alarmInfos.add(info);
		}
		alarmInfo.setTimes(times);
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

		}else {
			mTvAlarmDate.setText(DateUtils.getDateStringYearMonthDay());
			alarmInfo = new AlarmInfo();
			alarmInfo.setClockType(7);
			alarmInfo.setStatus(1);
			alarmInfo.setTimeType(1);
			alarmInfo.setDelayTime(0);
		}
		mBtChooseRing.setText("  铃  声  ");
		mBtChooseRing.setSelected(true);
		RingInfo ring = new RingInfo();
		ring.setRingName("嘟嘟嘟，喝水，喝水喽，保持身体健康每一天");
		ring.setRingType(1);
		ring.setRingCategory(3);
		ring.setRingId(3003);
		ring.setRingUrl(Constant.BASE_URL + "uploads/factory/ring/voice003.mp3");
		_ringList.add(ring);
		alarmInfo.setRingList(_ringList);
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
		_alarmList.clear();

		List<String> times = new ArrayList<String>();
		for (int i = 0; i < adapter.getCount(); i++) {
			String time = _alarmInfos.get(i).getAlarmTime();
			times.add(time);
		}
		alarmInfo.setTimes(times);
		if (alarmInfo.getTimeType() == 1) {
			checkValidate(times);
		}
		alarmInfo.setClockType(7);
		alarmInfo.setRemark(mTvAlarmRemark.getText().toString());
		alarmInfo.setDelayTime(0);
		alarmInfo.setNeedRemind(needRemind);
		alarmInfo.setTitle(mTvAlarmLabel.getText().toString());
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
		case R.id.tv_water_medicine_alarm_finish:
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
		case R.id.btn_water_medicine_alarm_back:
			finish();
			break;
		case R.id.btn_add_water_medicine_alarm:
			int count = _alarmInfos.size();
			WaterAlarmInfo info = new WaterAlarmInfo();
			info.setTime(String.format("第%d次", ++count));
			info.setAlarmTime("20:00");
			_alarmInfos.add(info);
			adapter.notifyDataSetChanged();
			refreshListView();
			mLvWaterMedicine.setSelection(mLvWaterMedicine.getBottom());
			break;
		case R.id.rl_water_medicine_alarm_label:
			Intent title = new Intent(WaterAndMedicineActivity.this,
					CommonSettingActivity.class);
			title.putExtra("title", "设置标题");
			title.putExtra("content", mTvAlarmLabel.getText().toString());
			startActivityForResult(title, 0);
			break;
		case R.id.rl_water_medicine_alarm_circle:
			ChooseCircleDlgFragment circle = new ChooseCircleDlgFragment();
			Bundle s = new Bundle();
			s.putStringArray("grid_1",
					getResources().getStringArray(R.array.alarm_circle_1));
			s.putStringArray("grid_2",
					getResources().getStringArray(R.array.alarm_circle_2));
			s.putParcelable("alarm", alarmInfo);
			circle.setArguments(s);
			circle.setCancelable(false);
			circle.show(getFragmentManager(), "water_circle");
			break;
		case R.id.rl_water_medicine_alarm_date:
			DateWheelDialogFragment date = new DateWheelDialogFragment();
			date.show(getFragmentManager(), "water_date");
			mTvAlarmDate.setText(DateUtils.getDateStringYearMonthDay());
			break;
		case R.id.rl_water_medicine_alarm_early_time:
			CircleWheelDialogFragment pre = new CircleWheelDialogFragment();
			Bundle a = new Bundle();
			a.putStringArray("wheel",
					getResources().getStringArray(R.array.pre_remind));
			pre.setArguments(a);
			pre.show(getFragmentManager(), "water_early_time");
			break;
		case R.id.rl_water_medicine_alarm_remark:
			Intent remark = new Intent(WaterAndMedicineActivity.this,
					CommonSettingActivity.class);
			remark.putExtra("title", "设置备注");
			remark.putExtra("content", mTvAlarmRemark.getText().toString());
			startActivityForResult(remark, 2);
			break;
		case R.id.btn_choose_ring:
			Intent ring = new Intent(WaterAndMedicineActivity.this,
					ChooseRingActivity.class);
			if (_ringList != null && _ringList.size() > 0) {
				ring.putExtra("ring", _ringList.get(0));
			}
			startActivityForResult(ring, 1);
			break;
		case R.id.btn_record_voice:
			Intent voice = new Intent(WaterAndMedicineActivity.this,
					RecordVoiceActivity.class);
			startActivityForResult(voice, 3);
			break;
		default:
			break;
		}
	}

	private class WaterAlarmInfo {
		private String _time;
		private String _alarmTime;

		public String getTime() {
			return _time;
		}

		public void setTime(String _time) {
			this._time = _time;
		}

		public String getAlarmTime() {
			return _alarmTime;
		}

		public void setAlarmTime(String _alarmTime) {
			this._alarmTime = _alarmTime;
		}
	}

	private class WaterMedicineAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater mInflater;

		public WaterMedicineAdapter(Context context) {
			mContext = context;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return _alarmInfos == null ? 0 : _alarmInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (holder == null) {
				convertView = mInflater.inflate(
						R.layout.water_medicine_list_item, null);
				holder = new ViewHolder();
				holder.mTvTimes = (TextView) convertView
						.findViewById(R.id.tv_water_times);
				holder.mTvAlarmTime = (TextView) convertView
						.findViewById(R.id.tv_water_alarm_time);
				holder.mIvDel = (ImageView) convertView
						.findViewById(R.id.btn_del_water_medicine);
				convertView.setTag(hashCode());
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mTvTimes.setText(_alarmInfos.get(position).getTime());
			holder.mTvAlarmTime.setText(_alarmInfos.get(position)
					.getAlarmTime());
			holder.mIvDel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (_alarmInfos.size() < 2) {
						return;
					}
					_alarmInfos.remove(position);
					refreshListView();
					notifyDataSetChanged();
				}
			});
			return convertView;
		}

		public class ViewHolder {
			private TextView mTvTimes;
			private TextView mTvAlarmTime;
			private ImageView mIvDel;
		}
	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("water_early_time")) {
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
		} else if (tag.equals("water_circle")) {
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
		} else if (tag.equals("water_date")) {
			mTvAlarmDate.setText(message);
		} else {
			for (int i = 0; i < adapter.getCount(); i++) {
				if (tag.equals("water_time" + i)) {
					LinearLayout layout = (LinearLayout) mLvWaterMedicine
							.getChildAt(i
									- mLvWaterMedicine
											.getFirstVisiblePosition());
					if (layout != null) {
						TextView time = (TextView) layout
								.findViewById(R.id.tv_water_alarm_time);
						time.setText(message);
						_alarmInfos.get(i).setAlarmTime(message.toString());
						adapter.notifyDataSetChanged();
					}
					break;
				} else {
					continue;
				}
			}
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
