package com.babacit.alarm.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class WheelArrayAdapter extends ArrayWheelAdapter<String> {
	int currentItem;
	int currentValue;

	public WheelArrayAdapter(Context context, int max, String[] items, int current) {
		super(context, max, items);
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
			view.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		}
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

}
