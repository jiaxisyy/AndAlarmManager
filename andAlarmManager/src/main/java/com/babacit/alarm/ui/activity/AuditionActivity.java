package com.babacit.alarm.ui.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.babacit.alarm.R;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.UploadRingServer;
import com.babacit.alarm.ui.view.visualizer.VisualizerController;
import com.babacit.alarm.ui.view.visualizer.VisualizerView;
import com.babacit.alarm.utils.ToastUtil;
import com.babacit.alarm.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AuditionActivity extends BaseActivity implements OnClickListener {
	private ImageView mIvAuditionPlay;
	private VisualizerController controller;
	private VisualizerView visualizerView;
	private int mVoiceLen, mMins, mSecs;
	private TextView mTvVoiceLen;
	private Timer timer;
	private String mVoiceName;
	private MediaPlayer mPlayer = new MediaPlayer();
	private static final int MSG_UPLOAD_RING_SUCCESS = 10;
	private static final int MSG_UPLOAD_RING_FAIL = 11;
	private RingInfo _ringInfo;
	private AudioManager audioManager;
	private SensorManager sensorManager;
	private Sensor mProximiny = null;

	private RequestCallBack uploadRingCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHander.obtainMessage(MSG_UPLOAD_RING_SUCCESS);
			msg.obj = obj;
			msg.sendToTarget();
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHander.obtainMessage(MSG_UPLOAD_RING_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private Handler mHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// mVoiceLen = mPlayer.getDuration() / 1000;
				calcMinAndSecs(mVoiceLen);
				mTvVoiceLen.setText(String.format("%02d:%02d", mMins, mSecs));
				break;
			case 1:
				if (mVoiceLen == 0) {
					mIvAuditionPlay
							.setImageResource(R.drawable.icon_audition_play);
					stopTimer();
					mVoiceLen = getIntent().getIntExtra("voiceDuration", 0);
				} else {
					calcMinAndSecs(mVoiceLen--);
					mTvVoiceLen.setText(String
							.format("%02d:%02d", mMins, mSecs));
				}
				break;
			case MSG_UPLOAD_RING_SUCCESS:
				dismissProgress();
				_ringInfo = (RingInfo) msg.obj;
				Intent data = new Intent();
				data.putExtra("voice", _ringInfo);
				setResult(0, data);
				finish();
				break;
			case MSG_UPLOAD_RING_FAIL:
				ToastUtil.showToast(getApplicationContext(),
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			default:
				break;
			}
		}
	};

	private void calcMinAndSecs(int voiceLen) {
		mMins = mVoiceLen / 60;
		mSecs = mVoiceLen % 60;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audition);
		mVoiceName = getIntent().getStringExtra("voiceName");
		mVoiceLen = getIntent().getIntExtra("voiceDuration", 0);
		bindField();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		setSpeakerphoneOn(true);
		controller = new VisualizerController();
		controller.addView(visualizerView);
		startPlay();
		controller.colorUpdate(255, 0, 0);
	}

	private void startPlay() {
		if (mPlayer.isPlaying()) {
			mPlayer.stop();
		}
		try {
			mPlayer.reset();
			mPlayer.setDataSource(Constant.VOICE_PATH + File.separator
					+ mVoiceName);
			mPlayer.prepare();
			mPlayer.start();
			controller.link(mPlayer);
			mHander.sendEmptyMessage(0);
			startTimer();
		} catch (Exception e) {
		}
	}

	private void bindField() {
		visualizerView = (VisualizerView) findViewById(R.id.visualizerView);
		mTvVoiceLen = (TextView) findViewById(R.id.tv_record_voice_time_length);
		findViewById(R.id.btn_audition_back).setOnClickListener(this);
		mIvAuditionPlay = (ImageView) findViewById(R.id.iv_audition_play);
		mIvAuditionPlay.setOnClickListener(this);
		findViewById(R.id.btn_re_record).setOnClickListener(this);
		findViewById(R.id.btn_use_this_record).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_audition_back:
			stopTimer();
			finish();
			break;
		case R.id.btn_re_record:
			stopTimer();
			finish();
			break;
		case R.id.btn_use_this_record:
			showProgressDialog("", false);
			stopTimer();
			new UploadRingServer().start(
					"mp3",
					Utils.fileToByteArray(Constant.VOICE_PATH + File.separator
							+ mVoiceName), uploadRingCallBack);
			break;
		case R.id.iv_audition_play:
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
				mIvAuditionPlay.setImageResource(R.drawable.icon_audition_play);
				stopTimer();
			} else {
				// mPlayer.start();
				mIvAuditionPlay
						.setImageResource(R.drawable.icon_audition_pause);
				startPlay();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			stopTimer();
			finish();
			break;

		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void startTimer() {
		if (timer == null) {
			timer = new Timer();
		}
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				mHander.sendEmptyMessage(1);
			}
		}, 0, 1000);
	}

	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void setSpeakerphoneOn(boolean on) {
		if (on) {
			audioManager.setSpeakerphoneOn(true);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			audioManager.setMode(AudioManager.MODE_IN_CALL);
		}
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
}
