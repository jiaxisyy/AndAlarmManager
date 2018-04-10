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

public class DateWheelDialogFragment extends DialogFragment implements
		OnClickListener {
	private WheelView mWvYear, mWvMonth, mWvDay;
	private WheelView yearSep, monthSep, daySep;
	private WheelNumericAdapter yearAdapter, monthAdapter, dayAdapter;
	private WheelArrayAdapter yearSepAdapter, monthSepAdapter, daySepAdapter;
	private Calendar calendar = Calendar.getInstance();
	private static final int WHEEL_TEXT_SIZE = 25;
	private static final int WHEEL_TEXT_SIZE_SEP = 12;

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
		View view = inflater.inflate(R.layout.date_wheel_dialog_fragment,
				container, false);
		mWvYear = (WheelView) view.findViewById(R.id.wv_date_dlg_year);

		mWvMonth = (WheelView) view.findViewById(R.id.wv_date_dlg_month);
		mWvDay = (WheelView) view.findViewById(R.id.wv_date_dlg_day);
		yearSep = (WheelView) view.findViewById(R.id.wv_date_dlg_year_mark);
		monthSep = (WheelView) view.findViewById(R.id.wv_date_dlg_month_mark);
		daySep = (WheelView) view.findViewById(R.id.wv_date_dlg_day_mark);
		yearSepAdapter = new WheelArrayAdapter(getActivity(), 1,
				new String[] { "年" }, 0);
		yearSepAdapter.setTextSize(WHEEL_TEXT_SIZE_SEP);
		yearSep.setViewAdapter(yearSepAdapter);
		yearSep.setVisibleItems(5);
		monthSepAdapter = new WheelArrayAdapter(getActivity(), 1,
				new String[] { "月" }, 0);
		monthSepAdapter.setTextSize(WHEEL_TEXT_SIZE_SEP);
		monthSep.setViewAdapter(monthSepAdapter);
		monthSep.setVisibleItems(5);
		daySepAdapter = new WheelArrayAdapter(getActivity(), 1,
				new String[] { "日" }, 0);
		daySepAdapter.setTextSize(WHEEL_TEXT_SIZE_SEP);
		daySep.setViewAdapter(daySepAdapter);
		daySep.setVisibleItems(5);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(mWvYear, mWvMonth, mWvDay);
				OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
				if (null != getTag()
						&& (getTag().equals("account_info_birthday"))) {
					act.onDialogConfirm(
							getTag(),
							false,
							String.format("%04d年%d月%d日",
									mWvYear.getCurrentItem() + 1900,
									mWvMonth.getCurrentItem() + 1,
									mWvDay.getCurrentItem() + 1));
				} else if (null != getTag()) {
					act.onDialogConfirm(
							getTag(),
							false,
							String.format("%04d-%02d-%02d",
									mWvYear.getCurrentItem() + 1900,
									mWvMonth.getCurrentItem() + 1,
									mWvDay.getCurrentItem() + 1));
				}
			}
		};
		// year
		int curYear = calendar.get(Calendar.YEAR);
		if (getTag().equals("account_info_birthday")
				|| getTag().equals("account_setting_birthday")
				|| getTag().equals("baby_info_birthday")) {
			yearAdapter = new WheelNumericAdapter(getActivity(), 1900, curYear,
					curYear);
		} else {
			yearAdapter = new WheelNumericAdapter(getActivity(), 1900, 2099,
					curYear);
		}
		yearAdapter.setGravity(Gravity.LEFT);
		yearAdapter.setTextSize(WHEEL_TEXT_SIZE);
		mWvYear.setViewAdapter(yearAdapter);
		mWvYear.setCurrentItem(curYear - 1900);
		mWvYear.setVisibleItems(5);
		mWvYear.addChangingListener(listener);
		mWvYear.addScrollingListener(scrollListener);
		// month
		int curMonth = calendar.get(Calendar.MONTH);
		monthAdapter = new WheelNumericAdapter(getActivity(), 1, 12, curMonth);
		monthAdapter.setGravity(Gravity.LEFT);
		monthAdapter.setTextSize(WHEEL_TEXT_SIZE);
		mWvMonth.setViewAdapter(monthAdapter);
		mWvMonth.setCurrentItem(curMonth);
		mWvMonth.setVisibleItems(5);
		// mWvMonth.setCyclic(true);
		mWvMonth.addChangingListener(listener);
		mWvMonth.addScrollingListener(scrollListener);
		// day
		updateDays(mWvYear, mWvMonth, mWvDay);
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
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		yearAdapter.setCurValue(year.getCurrentItem());
		monthAdapter.setCurValue(month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayAdapter = new WheelNumericAdapter(getActivity(), 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		dayAdapter.setGravity(Gravity.LEFT);
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