package com.babacit.alarm.ui.dialog;

import com.babacit.alarm.R;
import com.babacit.alarm.ui.adapter.WheelArrayAdapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

public class CircleWheelDialogFragment extends DialogFragment implements OnClickListener {
	private WheelView wv;
	private String[] _wheelItem;
	private WheelArrayAdapter adapter;
	private static final int WHEEL_TEXT_SIZE = 25;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
		setStyle(style, theme);
	}

	@Override
	public void onStart() {
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		super.onStart();
	}

	private OnWheelChangedListener listener = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			adapter.setCurValue(wv.getCurrentItem());
			OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
			if (null != getTag()) {
				act.onDialogConfirm(getTag(), false, _wheelItem[wv.getCurrentItem()]);
			}
		
		}
	};

	private OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.circle_wheel_dialog_fragment, container, false);
		wv = (WheelView) view.findViewById(R.id.wv_in_dialog);
		wv.setVisibleItems(5);
		wv.setCyclic(true);
		wv.addChangingListener(listener);
		wv.addScrollingListener(scrollListener);
		_wheelItem = getArguments().getStringArray("wheel");
		adapter = new WheelArrayAdapter(getActivity(), _wheelItem.length, _wheelItem, 0);
		adapter.setTextSize(WHEEL_TEXT_SIZE);
		wv.setViewAdapter(adapter);
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