package com.babacit.alarm.ui.activity;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.utils.BitmapCompressUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class GuideActivity extends Activity implements OnClickListener {
	private ImageView mIv1, mIv2, mIv3;
	private SharedConfig config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		config = new SharedConfig(this);
		mIv1 = (ImageView) findViewById(R.id.iv_guide_1);
		mIv2 = (ImageView) findViewById(R.id.iv_guide_2);
		mIv3 = (ImageView) findViewById(R.id.iv_guide_3);
//		mIv1.setBackgroundResource(R.drawable.guide_1);
//		mIv2.setBackgroundResource(R.drawable.guide_2);
//		mIv3.setBackgroundResource(R.drawable.guide_3);

		mIv1.setImageBitmap(BitmapCompressUtil.decodeBitmapFromResource(
				Bitmap.Config.ARGB_8888, getResources(), R.drawable.guide_1,
				720, 1280));
		mIv2.setImageBitmap(BitmapCompressUtil.decodeBitmapFromResource(
				Bitmap.Config.ARGB_8888, getResources(), R.drawable.guide_2,
				720, 1280));
		mIv3.setImageBitmap(BitmapCompressUtil.decodeBitmapFromResource(
				Bitmap.Config.ARGB_8888, getResources(), R.drawable.guide_3,
				720, 1280));

		mIv1.setOnClickListener(this);
		mIv2.setOnClickListener(this);
		mIv3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_guide_1:
			mIv1.setVisibility(View.GONE);
			mIv2.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_guide_2:
			mIv2.setVisibility(View.GONE);
			mIv3.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_guide_3:
			mIv3.setVisibility(View.GONE);
			config.setGuideState(true);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}
}
