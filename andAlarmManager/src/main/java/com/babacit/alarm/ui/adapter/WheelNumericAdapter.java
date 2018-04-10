package com.babacit.alarm.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class WheelNumericAdapter extends NumericWheelAdapter {
	int currentItem;
	int currentValue;
	int _gravity;

	public WheelNumericAdapter(Context context, int minValue, int maxValue,
			int current) {
		super(context, minValue, maxValue);
		this.currentValue = current;
		setTextSize(30);
	}

	@Override
	protected void configureTextView(TextView view) {
		super.configureTextView(view);
		if (currentItem == currentValue) {
			view.setTextColor(0xff666666);
			view.setTypeface(Typeface.DEFAULT);
		} else {
			view.setTextColor(0x77cccccc);
			view.setTypeface(Typeface.DEFAULT);
		}
		view.setGravity(_gravity);
		view.setText(String.format("%02d",
				Integer.parseInt(view.getText().toString())));
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		currentItem = index;
		return super.getItem(index, cachedView, parent);
	}

	public void setCurValue(int cur) {
		this.currentValue = cur;
		notifyDataChangedEvent();
	}

	public int getCurValue() {
		return this.currentValue;
	}

	public void setGravity(int gravity) {
		this._gravity = gravity;
		notifyDataChangedEvent();
	}
}
