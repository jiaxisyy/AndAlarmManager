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

public class TimeWheelDialogFragment extends DialogFragment implements
		OnClickListener {
	private WheelView hours, mins, sep;
	private WheelNumericAdapter hourAdapter, minuteAdapter;
	private WheelArrayAdapter sepAdapter;
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
		View view = inflater.inflate(R.layout.time_wheel_dialog_fragment,
				container, false);
		hours = (WheelView) view.findViewById(R.id.wv_time_dlg_hour);
		mins = (WheelView) view.findViewById(R.id.wv_time_dlg_minute);
		sep = (WheelView) view.findViewById(R.id.wv_time_dlg_mark);
		sepAdapter = new WheelArrayAdapter(getActivity(),1, new String[] { ":" },
				0);
		sepAdapter.setTextSize(WHEEL_TEXT_SIZE);
		sep.setViewAdapter(sepAdapter);
		sep.setVisibleItems(5);

		// hour
		int curHour = calendar.get(Calendar.HOUR_OF_DAY);
		hourAdapter = new WheelNumericAdapter(getActivity(), 0, 23, curHour);
		hourAdapter.setTextSize(WHEEL_TEXT_SIZE);
		hourAdapter.setGravity(Gravity.CENTER);
		hours.setViewAdapter(hourAdapter);
		hours.setCurrentItem(curHour);
		hours.setVisibleItems(5);
		hours.setCyclic(true);
		// minute
		int curMinute = calendar.get(Calendar.MINUTE);
		minuteAdapter = new WheelNumericAdapter(getActivity(), 0, 59, curMinute);
		minuteAdapter.setTextSize(WHEEL_TEXT_SIZE);
		minuteAdapter.setGravity(Gravity.CENTER);
		mins.setViewAdapter(minuteAdapter);
		mins.setCurrentItem(curMinute);
		mins.setVisibleItems(5);
		mins.setCyclic(true);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				hourAdapter.setCurValue(hours.getCurrentItem());
				minuteAdapter.setCurValue(mins.getCurrentItem());
				OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
				if (null != getTag()) {
					act.onDialogConfirm(getTag(), false, String.format(
							"%02d:%02d", hours.getCurrentItem(),
							mins.getCurrentItem()));
				}
			}
		};
		hours.addChangingListener(listener);
		mins.addChangingListener(listener);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				
			}
		};
		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);

		return view;
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