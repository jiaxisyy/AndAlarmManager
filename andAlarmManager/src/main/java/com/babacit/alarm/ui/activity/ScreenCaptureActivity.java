package com.babacit.alarm.ui.activity;

import java.util.HashMap;
import java.util.Map;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.UploadFileServer;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.ui.dialog.ShareDlgFragment;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.babacit.alarm.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ScreenCaptureActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {
	
	private SharedConfig config;
	private TextView mTvTitle;
	private ImageView mIvCapture;
	private Map<String, SHARE_MEDIA> mPlatformsMap = new HashMap<String, SHARE_MEDIA>();
	private String[] shares;
	private SHARE_MEDIA[] medias = { SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,
			SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.WEIXIN_CIRCLE,
			SHARE_MEDIA.SINA };
	private String path;
	private int i;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgress();
			if (msg.what == 0) {
				String url = (String) msg.obj;
				switch (i) {
				case 0:
					directShare(SHARE_MEDIA.QQ, url);
					break;
				case 1://TODO 要做
					directShare(SHARE_MEDIA.WEIXIN, url);
					break;
				case 2:
					directShare(SHARE_MEDIA.QZONE, url);
					break;
//				case 3:
//					directShare(SHARE_MEDIA.TENCENT, url);
//					break;
				case 3://TODO 要做
					directShare(SHARE_MEDIA.WEIXIN_CIRCLE, url);
					break;
				case 4:
					directShare(SHARE_MEDIA.SINA, url);
					break;
				}
//				mController.postShare(ScreenCaptureActivity.this,
//						mPlatformsMap.get(shares[i]), mShareListener);
			}
		}
	};
	
	private void directShare(SHARE_MEDIA platform, String url) {
		UMImage thumb = new UMImage(ScreenCaptureActivity.this, url);
		ShareContent shareContent = new ShareContent();
		shareContent.subject = "记忆猫";
		shareContent.mText = getResources().getString(R.string.txt_product_name);
		new ShareAction(ScreenCaptureActivity.this).setPlatform(platform)
		.setShareContent(shareContent)
        .withText(shareContent.subject)
        .withMedia(thumb)
        .setCallback(mShareListener)
        .share();
	}
	
	private UMShareListener mShareListener = new UMShareListener() {
		
		@Override
		public void onStart(SHARE_MEDIA arg0) {
			ToastUtil.showToast(ScreenCaptureActivity.this, "开始分享");
		}
		
		@Override
		public void onResult(SHARE_MEDIA arg0) {
			ToastUtil.showToast(ScreenCaptureActivity.this, "分享成功");
		}
		
		@Override
		public void onError(SHARE_MEDIA arg0, Throwable arg1) {
			ToastUtil.showToast(ScreenCaptureActivity.this, "分享失败");
		}

		@Override
		public void onCancel(SHARE_MEDIA arg0) {
		}

	};

	private RequestCallBack uploadFileCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHandler.obtainMessage(0);
			msg.obj = obj;
			msg.sendToTarget();
		}

		@Override
		public void onFail(Object object, int errCode) {
			mHandler.sendEmptyMessage(1);
		}
	};

	private RequestCallBack uploadSharedInfoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			config.setVideoTimes((Integer) obj);
		}

		@Override
		public void onFail(Object object, int errCode) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_capture);
		config = new SharedConfig(this);
		bindField();
		initData();
		
		UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {
			@Override
			public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
				
			}

			@Override
			public void onError(SHARE_MEDIA platform, int action, Throwable t) {
				
			}

			@Override
			public void onCancel(SHARE_MEDIA platform, int action) {
				
			}

			@Override
			public void onStart(SHARE_MEDIA arg0) {
				
			}
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		UMShareAPI.get(this).onSaveInstanceState(outState);
	}
	
	private void initPlatformMap() {
		for (int i = 0; i < shares.length; i++) {
			mPlatformsMap.put(shares[i], medias[i]);
		}
	}

	private void bindField() {
		mTvTitle = (TextView) findViewById(R.id.tv_capture_title);
		mIvCapture = (ImageView) findViewById(R.id.iv_capture);
		findViewById(R.id.btn_screen_capture_back).setOnClickListener(this);
		findViewById(R.id.iv_share_capture).setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	private void initData() {
		shares = getResources().getStringArray(R.array.share);
		initPlatformMap();
		String title = getIntent().getStringExtra("title");
		path = getIntent().getStringExtra("path");
		if (title != null) {
			mTvTitle.setText(title);
		}
		if (path != null) {
			mIvCapture.setImageBitmap(BitmapFactory.decodeFile(path));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_screen_capture_back:
			finish();
			break;
		case R.id.iv_share_capture:
			ShareDlgFragment share = new ShareDlgFragment();
			Bundle bundle = new Bundle();
			bundle.putStringArray("grid_1", shares);
			share.setArguments(bundle);
			share.show(getFragmentManager(), "share");
			break;
		default:
			break;
		}
	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
			ToastUtil.showToast(getApplicationContext(), "请检查您的网络!");
			return;
		}
		if (tag.equals("share")) {
			showProgressDialog("", true);
			i = Integer.parseInt(message.toString());
			new UploadFileServer().start(getSurffix(path),
					Utils.fileToByteArray(path), uploadFileCallBack);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		UMShareAPI.get(this).release();
	}

	private String getSurffix(String src) {
		if (src != null && src.length() > 0) {
			return src.substring(src.lastIndexOf(".") + 1, src.length());
		} else
			return null;
	}
}
