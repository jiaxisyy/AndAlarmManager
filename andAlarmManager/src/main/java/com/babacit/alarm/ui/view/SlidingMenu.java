package com.babacit.alarm.ui.view;

import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.ui.activity.MainActivity;
import com.babacit.alarm.utils.ScreenUtils;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * dp
	 */
	private int mMenuRightPadding;
	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;

	private boolean isOpen;

	private boolean once;

	private ViewGroup mMenu;
	private ViewGroup mContent;
	// private ViewGroup mRight;
	private int initial = 0;
	private Handler mainHandler;

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public void setMainHandler(Handler mHandler) {
		mainHandler = mHandler;
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		mMenuRightPadding = mScreenWidth / 5;
		// TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
		// R.styleable.SlidingMenu, defStyle, 0);
		// int n = a.getIndexCount();
		// for (int i = 0; i < n; i++) {
		// int attr = a.getIndex(i);
		// switch (attr) {
		// case R.styleable.SlidingMenu_rightPadding:
		// // 默认50
		// mMenuRightPadding = a.getDimensionPixelSize(attr,
		// (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 50f,
		// getResources().getDisplayMetrics()));// 默认为10DP
		// break;
		// }
		// }
		// a.recycle();
	}

	public SlidingMenu(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 显示的设置一个宽度
		 */
		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(1);
			// mRight = (ViewGroup) wrapper.getChildAt(2);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;
			// mRight.getLayoutParams().width = mScreenWidth / 2;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isOpen && ev.getX() > mScreenWidth * 4 / 5) {
			this.smoothScrollTo(mMenuWidth, 0);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
		case MotionEvent.ACTION_UP:
			mainHandler
					.sendEmptyMessage(MainActivity.MSG_HIDE_RIGHT_SLIDING_MENU);
			int scrollX = getScrollX();
			if (scrollX > mHalfMenuWidth * 5 / 3) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
//		case MotionEvent.ACTION_DOWN:
//			if (isOpen) {
//				this.smoothScrollTo(mMenuWidth, 0);
//			}
//			return false;
			// case MotionEvent.ACTION_MOVE:
			// if (isOpen) {
			// this.smoothScrollTo(mMenuWidth, 0);
			// isOpen = false;
			// }
			// return true;
			// if (!isOpen && getScrollX() > initial) {
			// }
		}

		super.onTouchEvent(ev);
		return false;
	}

	/**
	 * 打开菜单
	 */
	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu() {
		if (isOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}

	/**
	 * 切换菜单状态
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;
		float leftScale = 1 - 0.3f * scale;
		float rightScale = 0.8f + scale * 0.2f;

		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

	}

	public boolean getMenuState() {
		return isOpen;
	}

}
