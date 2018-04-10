package com.babacit.alarm.ui.view;

import com.babacit.alarm.R;
import com.babacit.alarm.utils.UnitUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class MyHorizontalScrollView extends HorizontalScrollView {

	private View view;
	private int windowWitdh = 0;
	private RadioGroup rg_nav_content;
	private LayoutInflater mInflater;
	private int indicatorWidth;

	public MyHorizontalScrollView(Context context) {
		super(context);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setParams(int[] tabTitle, int[] images, int count,
			Activity context) {
		mInflater = LayoutInflater.from(context);
		this.view = mInflater.inflate(R.layout.horizontal_item, null);
		this.addView(view);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWitdh = dm.widthPixels;
		indicatorWidth = windowWitdh / count;
		init(tabTitle, images);
	}

	private void init(int[] tabTitle, int[] images) {
		rg_nav_content = (RadioGroup) view.findViewById(R.id.hori_radio_group);
		rg_nav_content.removeAllViews();
		int len = tabTitle.length;
		for (int i = 0; i < len; i++) {
			RadioButton rb = (RadioButton) mInflater.inflate(
					R.layout.radio_item, null);
			rb.setId(i);
			rb.setText(getResources().getString(tabTitle[i]));
			rb.setCompoundDrawablesWithIntrinsicBounds(0, images[i], 0, 0);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					indicatorWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
			rb.setLayoutParams(lp);
			switch (i) {
			case 1:
				rb.setPadding(0, 0, UnitUtils.dip2px(getContext(), 40), 0);
				break;
			case 2:
				rb.setPadding(UnitUtils.dip2px(getContext(), 40), 0, 0, 0);
				break;
			default:
				break;
			}
			rg_nav_content.addView(rb);
		}
		((RadioButton) rg_nav_content.getChildAt(0)).setChecked(true);
	}

	public int getIndicatorWidth() {
		return indicatorWidth;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		return false;
	}
}
