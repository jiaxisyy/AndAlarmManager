package com.babacit.alarm.ui.dialog;

import java.util.Calendar;

import com.babacit.alarm.R;
import com.babacit.alarm.ui.adapter.WheelArrayAdapter;
import com.babacit.alarm.ui.adapter.WheelNumericAdapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

public class MonthDayWheelDialogFragment extends DialogFragment implements
		OnClickListener {
	private WheelView mWvMonth, mWvDay;
	private WheelView monthSep, daySep;
	private WheelNumericAdapter monthAdapter, dayAdapter;
	private WheelArrayAdapter monthSepAdapter, daySepAdapter;
	private Calendar calendar = Calendar.getInstance();
	private static final int WHEEL_TEXT_SIZE = 25;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
		setStyle(style, theme);
	}

	@Override
	public void onStart() {
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.month_day_wheel_dialog_fragment,
				container, false);
		mWvMonth = (WheelView) view.findViewById(R.id.wv_date_dlg_month);
		mWvDay = (WheelView) view.findViewById(R.id.wv_date_dlg_day);
		monthSep = (WheelView) view.findViewById(R.id.wv_date_dlg_month_mark);
		daySep = (WheelView) view.findViewById(R.id.wv_date_dlg_day_mark);
		monthSepAdapter = new WheelArrayAdapter(getActivity(),1,
				new String[] { "月" }, 0);
		monthSepAdapter.setTextSize(WHEEL_TEXT_SIZE);
		monthSep.setViewAdapter(monthSepAdapter);
		monthSep.setVisibleItems(5);
		daySepAdapter = new WheelArrayAdapter(getActivity(),1,
				new String[] { "日" }, 0);
		daySepAdapter.setTextSize(WHEEL_TEXT_SIZE);
		daySep.setViewAdapter(daySepAdapter);
		daySep.setVisibleItems(5);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(null, mWvMonth, mWvDay);
				OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
				if (null != getTag()
						&& (getTag().equals("account_info_birthday") || getTag()
								.equals("festival_date"))) {
					act.onDialogConfirm(
							getTag(),
							false,
							String.format("%02d月%02d日",
									mWvMonth.getCurrentItem() + 1,
									mWvDay.getCurrentItem() + 1));
				} else if (null != getTag()) {
					act.onDialogConfirm(
							getTag(),
							false,
							String.format("%02d-%02d",
									mWvMonth.getCurrentItem() + 1,
									mWvDay.getCurrentItem() + 1));
				}
			}
		};
		// year
		int curYear = calendar.get(Calendar.YEAR);
		// month
		int curMonth = calendar.get(Calendar.MONTH);
		monthAdapter = new WheelNumericAdapter(getActivity(), 1, 12, curMonth);
		monthAdapter.setGravity(Gravity.RIGHT);
		monthAdapter.setTextSize(WHEEL_TEXT_SIZE);
		mWvMonth.setViewAdapter(monthAdapter);
		mWvMonth.setCurrentItem(curMonth);
		mWvMonth.setVisibleItems(5);
		mWvMonth.setCyclic(true);
		mWvMonth.addChangingListener(listener);
		mWvMonth.addScrollingListener(scrollListener);
		// day
		updateDays(null, mWvMonth, mWvDay);
		mWvDay.setVisibleItems(5);
		mWvDay.addChangingListener(listener);
		mWvDay.addScrollingListener(scrollListener);
		mWvDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

		return view;
	}

	private OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			
		}
	};

	private void updateDays(WheelView year, WheelView month, WheelView day) {
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		monthAdapter.setCurValue(month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayAdapter = new WheelNumericAdapter(getActivity(), 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		dayAdapter.setGravity(Gravity.RIGHT);
		dayAdapter.setTextSize(WHEEL_TEXT_SIZE);
		day.setViewAdapter(dayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		dayAdapter.setCurValue(curDay - 1);
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			OnMyDialogClickListener act = (OnMyDialogClickListener) activity;
		} catch (Exception e) {

		}
		super.onAttach(activity);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}
	
}