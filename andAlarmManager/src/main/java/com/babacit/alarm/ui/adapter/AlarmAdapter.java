package com.babacit.alarm.ui.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.AlarmInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.SwitchClockStatusServer;
import com.babacit.alarm.ui.activity.MainActivity;
import com.babacit.alarm.utils.DateUtils;
import com.kyleduo.switchbutton.SwitchButton;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class AlarmAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<AlarmInfo> _alarmInfos = new ArrayList<AlarmInfo>();
	private SharedConfig config;
	private Handler mHandler;

	private RequestCallBack switchAlarmStatusCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(MainActivity.MSG_SWITCH_CLOCK_STATUS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			mHandler.sendEmptyMessage(MainActivity.MSG_SWITCH_CLOCK_STATUS);
		}
	};

	public void setMainHandler(Handler _handler) {
		mHandler = _handler;
	}

	public AlarmAdapter(Context context, List<AlarmInfo> alarmInfos) {
		mContext = context;
		config = new SharedConfig(context);
		_alarmInfos = alarmInfos;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (holder == null) {
			convertView = mInflater.inflate(R.layout.alarm_list_item, null);
			holder = new ViewHolder();
			holder.mTvDayLabel = (TextView) convertView
					.findViewById(R.id.tv_alarm_day_label);
			holder.mTvTimeLabel = (TextView) convertView
					.findViewById(R.id.tv_alarm_time_label);
			holder.mTvAlarmName = (TextView) convertView
					.findViewById(R.id.tv_alarm_name);
			holder.mTvAlarmDetail = (TextView) convertView
					.findViewById(R.id.tv_alarm_detail);
			holder.mTvAlarmTime = (TextView) convertView
					.findViewById(R.id.tv_alarm_time);
			holder.mSb = (SwitchButton) convertView
					.findViewById(R.id.sb_alarm_switch);
			holder.mSb
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// if (!NetworkUtils.isNetWorkOk(mContext)) {
							// ToastUtil.showToast(mContext, "请检查您的网络!");
							// return;
							// }
							if (isChecked) {
								new SwitchClockStatusServer().start(
										config.getUserId(),
										config.getAlarmCode(),
										_alarmInfos.get(position).getClockId(),
										1, switchAlarmStatusCallBack);
							} else {
								new SwitchClockStatusServer().start(
										config.getUserId(),
										config.getAlarmCode(),
										_alarmInfos.get(position).getClockId(),
										0, switchAlarmStatusCallBack);
							}
						}
					});
			convertView.setTag(hashCode());
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AlarmInfo alarmInfo = _alarmInfos.get(position);
		holder.mTvAlarmName.setText(alarmInfo.getTitle());
//		Collections.sort(alarmInfo.getTimes());
//		int size = alarmInfo.getTimes().size();
//		if (size == 1) {
//			holder.mTvAlarmTime.setText(alarmInfo.getTimes().get(0));
//			holder.mTvTimeLabel.setText(alarmInfo.getTimes().get(0));
//		} else {
//			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
//			String curTime = df.format(new Date());
//			for (int i = 0; i < size; i++) {
//				if (curTime.compareTo(alarmInfo.getTimes().get(i)) <= 0) {
//					holder.mTvTimeLabel.setText(alarmInfo.getTimes().get(i));
//					holder.mTvAlarmTime.setText(alarmInfo.getTimes().get(i));
//					break;
//				} else {
//					holder.mTvTimeLabel.setText(alarmInfo.getTimes().get(0));
//					holder.mTvAlarmTime.setText(alarmInfo.getTimes().get(0));
//				}
//			}
//		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");
		SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
		if (alarmInfo.getSortTime() != null) {
			Date date = DateUtils.string2Date(alarmInfo.getSortTime());
			holder.mTvTimeLabel.setText(hm.format(date));
			holder.mTvAlarmTime.setText(hm.format(date));
			if (sdf.format(date).equals(DateUtils.getDateStringYearMonthDay())) {
				holder.mTvDayLabel.setText("今天");
			} else {
				try {
					long timeStamp = sdf.parse(sdf.format(date)).getTime();
					if ((timeStamp
							- (sdf.parse(sdf.format(new Date())).getTime()) == 1
							* 24 * 60 * 60 * 1000)) {
						holder.mTvDayLabel.setText("明天");
						holder.mTvDayLabel
								.setBackgroundResource(R.drawable.round_text_bg_3);
					} else if ((timeStamp
							- (sdf.parse(sdf.format(new Date())).getTime()) == 2
							* 24 * 60 * 60 * 1000)) {
						holder.mTvDayLabel.setText("后天");
						holder.mTvDayLabel
								.setBackgroundResource(R.drawable.round_text_bg_3);
					} else {
						holder.mTvDayLabel.setText(df.format(date));
						holder.mTvDayLabel
						.setBackgroundResource(R.drawable.round_text_bg_3);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

//		if (alarmInfo.getDate() != null
//				&& alarmInfo.getDate().equals(
//						DateUtils.getDateStringYearMonthDay())) {
//			holder.mTvDayLabel.setText("今天");
//		} else {
//			if (alarmInfo.getDate() != null) {
//				try {
//					long timeStamp = sdf.parse(alarmInfo.getDate()).getTime();
//					if ((timeStamp
//							- (sdf.parse(sdf.format(new Date())).getTime()) == 1
//							* 24 * 60 * 60 * 1000)) {
//						holder.mTvDayLabel.setText("明天");
//						holder.mTvDayLabel
//								.setBackgroundResource(R.drawable.round_text_bg_3);
//					} else if ((timeStamp
//							- (sdf.parse(sdf.format(new Date())).getTime()) == 2
//							* 24 * 60 * 60 * 1000)) {
//						holder.mTvDayLabel.setText("后天");
//						holder.mTvDayLabel
//								.setBackgroundResource(R.drawable.round_text_bg_3);
//					} else {
//						holder.mTvDayLabel.setText(alarmInfo.getDate()
//								.substring(5));
//						holder.mTvDayLabel
//								.setBackgroundResource(R.drawable.round_text_bg_3);
//					}
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		switch (alarmInfo.getHolidayFilter()) {
		case 1:
			holder.mTvAlarmDetail.setText("仅寒暑假提醒");
			break;
		case 2:
			holder.mTvAlarmDetail.setText("寒暑假不提醒");
			break;
		case 3:
			holder.mTvAlarmDetail.setText("节假日不提醒");
			break;
		default:
			break;
		}
		switch (alarmInfo.getTimeType()) {
		case 1:
			holder.mTvAlarmDetail.setText("仅一次");
			break;
		case 2:
			holder.mTvAlarmDetail.setText("每天");
			break;
		case 3:
			holder.mTvAlarmDetail.setText("每月");
			break;
		case 4:
			holder.mTvAlarmDetail.setText("每年");
			break;
		case 5:
			String week = "";
			if (alarmInfo.getDay() != null) {
				if (alarmInfo.getDay().contains("0")) {
					week += "周一";
				}
				if (alarmInfo.getDay().contains("1")) {
					week += " 周二";
				}
				if (alarmInfo.getDay().contains("2")) {
					week += " 周三";
				}
				if (alarmInfo.getDay().contains("3")) {
					week += " 周四";
				}
				if (alarmInfo.getDay().contains("4")) {
					week += " 周五";
				}
				if (alarmInfo.getDay().contains("5")) {
					week += " 周六";
				}
				if (alarmInfo.getDay().contains("6")) {
					week += " 周日";
				}
			}
			holder.mTvAlarmDetail.setText(week);
			break;
		default:
			break;
		}
		if (alarmInfo.getStatus() == 1) {
			holder.mSb.setChecked(true, false);
		} else {
			holder.mSb.setChecked(false, false);
		}
		return convertView;
	}

	public void updateList(List<AlarmInfo> aInfos) {
		_alarmInfos = aInfos;
		notifyDataSetChanged();
	}

	public class ViewHolder {
		private TextView mTvDayLabel;
		private TextView mTvTimeLabel;
		private TextView mTvAlarmName;
		private TextView mTvAlarmDetail;
		private TextView mTvAlarmTime;
		private SwitchButton mSb;
	}
}