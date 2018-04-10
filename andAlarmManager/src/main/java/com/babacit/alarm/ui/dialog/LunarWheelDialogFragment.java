package com.babacit.alarm.ui.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.ui.adapter.WheelArrayAdapter;
import com.babacit.alarm.utils.SimpleLunarCalendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

public class LunarWheelDialogFragment extends DialogFragment implements
		OnClickListener {
	private WheelView mWvLunarYear, mWvLunarMonth, mWvLunarDay;
	private WheelArrayAdapter lunarYearAdapter, lunarMonthAdapter, lunarDayAdapter;
	private Calendar calendar = Calendar.getInstance();
	private static final int WHEEL_TEXT_SIZE = 25;
	private String[] lunarYears = new String[200];
	private String[] lunarMonths;
	private String[] lunarDays = new String[30];
	private List<String> monthList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
		setStyle(style, theme);
		SimpleLunarCalendar lunar;
		lunar = new SimpleLunarCalendar(new Date());
		for (int i = 1900; i < 2100; i++) {
			lunarYears[i - 1900] = lunar.getLunarYearString(i);
		}
		getLunarMonthsByYear(lunar);
	}

	@Override
	public void onStart() {
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		super.onStart();
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lunar_wheel_dialog_fragment,
				container, false);
		mWvLunarYear = (WheelView) view.findViewById(R.id.wv_lunar_year);
		mWvLunarMonth = (WheelView) view.findViewById(R.id.wv_lunar_month);
		mWvLunarDay = (WheelView) view.findViewById(R.id.wv_lunar_day);

		OnWheelChangedListener lunarListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateLunarMonth(mWvLunarYear, mWvLunarMonth);
				updateLunarDays(mWvLunarYear, mWvLunarMonth, mWvLunarDay);
			}
		};
		// year
		int curYear = calendar.get(Calendar.YEAR);
		lunarYearAdapter = new WheelArrayAdapter(getActivity(),curYear-1900, lunarYears, curYear);
		lunarYearAdapter.setTextSize(WHEEL_TEXT_SIZE);
		mWvLunarYear.setViewAdapter(lunarYearAdapter);
		mWvLunarYear.setCurrentItem(curYear - 1900);
		mWvLunarYear.setVisibleItems(5);
		mWvLunarYear.addChangingListener(lunarListener);
		// month
		updateLunarMonth(mWvLunarYear, mWvLunarMonth);
		int curMonth = calendar.get(Calendar.MONTH);
		mWvLunarMonth.setCurrentItem(curMonth);
		mWvLunarMonth.setVisibleItems(5);
		mWvLunarMonth.setCyclic(true);
		mWvLunarMonth.addChangingListener(lunarListener);
		// day
		updateLunarDays(mWvLunarYear, mWvLunarMonth, mWvLunarDay);
		mWvLunarDay.setVisibleItems(5);
		mWvLunarDay.addChangingListener(lunarListener);
		mWvLunarDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

		return view;
	}

	private void updateLunarMonth(WheelView year, WheelView month) {
		calendar.set(Calendar.YEAR, 1900 + year.getCurrentItem());
		SimpleLunarCalendar lunar = new SimpleLunarCalendar(
				calendar.getTimeInMillis());
		Logger.d("log.test calendar.getTimeInMillis():"
				+ calendar.getTimeInMillis());
		lunarYearAdapter.setCurValue(year.getCurrentItem());
		getLunarMonthsByYear(lunar);
		lunarMonthAdapter = new WheelArrayAdapter(getActivity(), lunarMonths.length,lunarMonths,
				month.getCurrentItem());
		lunarMonthAdapter.setTextSize(WHEEL_TEXT_SIZE);
		mWvLunarMonth.setViewAdapter(lunarMonthAdapter);
	}

	private void updateLunarDays(WheelView year, WheelView month, WheelView day) {
		calendar.set(Calendar.YEAR,
				1900 + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		lunarYearAdapter.setCurValue(year.getCurrentItem());
		lunarMonthAdapter.setCurValue(month.getCurrentItem());
		SimpleLunarCalendar lunar = new SimpleLunarCalendar(calendar.getTime());
		Logger.d("log.test " + lunar.getDateString());
		// int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// lunarDayAdapter = new WheelNumericAdapter(getActivity(), 1, maxDays,
		// calendar.get(Calendar.DAY_OF_MONTH) - 1);
		int maxDays = lunar.isBigMonth() ? 30 : 29;
		lunarDays = new String[maxDays];
		getLunarDays(lunar);
		lunarDayAdapter = new WheelArrayAdapter(getActivity(),lunarDays.length, lunarDays,
				day.getCurrentItem());
		lunarDayAdapter.setTextSize(WHEEL_TEXT_SIZE);
		day.setViewAdapter(lunarDayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		lunarDayAdapter.setCurValue(curDay - 1);
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