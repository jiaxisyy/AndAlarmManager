package com.babacit.alarm.ui.activity;

import java.io.File;
import java.io.IOException;

import com.babacit.alarm.R;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.ui.dialog.MyDialogFragment;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.ui.view.WaveformView;
import com.babacit.alarm.utils.AudioRecorder2Mp3Util;
import com.babacit.alarm.utils.ToastUtil;
import com.buihha.audiorecorder.Mp3Recorder;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class RecordVoiceActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {
	private Button mBtRecord;
	// The sampling rate for the audio recorder.
	private static final int SAMPLING_RATE = 22050;

	private WaveformView mWaveformView;

	private RecordingThread mRecordingThread;
	private final Mp3Recorder recorder = new Mp3Recorder();
	private int mBufferSize;
	private short[] mAudioBuffer;
	/** 录音开始和结束时间 */
	private long startVoiceT, endVoiceT;
	private String voiceName;
	private int recordFlag = 0;
	private int recordTime = 0;
	private boolean canClean = false;

	private AudioRecorder2Mp3Util util = null;
	private long touchTimeStamp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_voice);
		bindField();
		mBufferSize = AudioRecord.getMinBufferSize(SAMPLING_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		mAudioBuffer = new short[mBufferSize / 2];

	}

	private void bindField() {
		mBtRecord = (Button) findViewById(R.id.btn_record);
		mBtRecord.setClickable(false);
		mBtRecord.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		mWaveformView = (WaveformView) findViewById(R.id.waveform_view);
		findViewById(R.id.btn_record_voice_back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_record_voice_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int[] location = new int[2];
		mBtRecord.getLocationInWindow(location);
		int btnX = location[0];
		int btnY = location[1];
		int btnWidth = mBtRecord.getMeasuredWidth();
		int btnHeight = mBtRecord.getMeasuredHeight();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!Environment.getExternalStorageDirectory().exists()) {
				ToastUtil.showToast(getApplicationContext(), "没有SD卡");
				return false;
			}
			if (event.getY() > btnY && event.getX() > btnX
					&& btnY + btnHeight > event.getY()
					&& btnX + btnWidth > event.getX()) {
				if (System.currentTimeMillis() - touchTimeStamp < 1000) {
					return false;
				}
				File dir = new File(Constant.VOICE_PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				recordFlag = 1;
				mBtRecord.setText("   正在录音...");
				startVoiceT = System.currentTimeMillis();
				voiceName = String.valueOf(startVoiceT);
				try {
					recorder.startRecording(Constant.VOICE_PATH + File.separator + voiceName + ".mp3");
					// startRecord(voiceName);
					// if (util == null) {
					// util = new AudioRecorder2Mp3Util(null, Constant.VOICE_PATH
					// + File.separator + voiceName + ".raw",
					// Constant.VOICE_PATH + File.separator + voiceName
					// + ".mp3");
					// }
					// if (canClean) {
					// util.cleanFile(AudioRecorder2Mp3Util.MP3
					// | AudioRecorder2Mp3Util.RAW);
					// }
					// util.startRecording();
					// canClean = true;
					mRecordingThread = new RecordingThread();
					mRecordingThread.start();
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtil.showToast(RecordVoiceActivity.this, "操作失败，请重试");
				}
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			touchTimeStamp = System.currentTimeMillis();
			mBtRecord.setText("按住录音，松开录制完成");
			endVoiceT = System.currentTimeMillis();
			recordTime = (int) ((endVoiceT - startVoiceT) / 1000);
			// if (recordTime >= 1) {
			// util.stopRecordingAndConvertFile();
			// util.cleanFile(AudioRecorder2Mp3Util.RAW);
			// util.close();
			// util = null;
			// }
			if (mRecordingThread != null) {
				mRecordingThread.stopRunning();
				mRecordingThread = null;
			}
			try {
				recorder.stopRecording();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (recordTime < 1) {
				recordFlag = 0;
				ToastUtil.showToast(getApplicationContext(), "录音时间太短!");
				return false;
			}
			if (recordFlag == 1) {
				recordFlag = 0;
				MyDialogFragment finishRecord = new MyDialogFragment();
				Bundle args = new Bundle();
				args.putString(Constant.DLG_CONTENT, "已完成录制");
				args.putString(Constant.DLG_CANCEL_TEXT, "试听");
				args.putString(Constant.DLG_CONFIRM_TEXT, "重录");
				finishRecord.setArguments(args);
				finishRecord.setCancelable(false);
				finishRecord.show(getFragmentManager(), "record_finish");
			}
		}
		return super.onTouchEvent(event);
	}

	private short[] Bytes2shorts(byte[] arr) {
		short[] b = new short[arr.length / 2];
		for (int i = 0; i < b.length; i++) {
			b[i] = (short) ( (arr[i * 2 + 1] << 8) | arr[i * 2]);  
		}
		return b;
	}

	/**
	 * A background thread that receives audio from the microphone and sends it
	 * to the waveform visualizing view.
	 */
	private class RecordingThread extends Thread {

		private boolean mShouldContinue = true;

		@Override
		public void run() {
			android.os.Process
					.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

			while (shouldContinue()) {
//				recorder.getAudioRecord()
//						.read(mAudioBuffer, 0, mBufferSize / 2);
				mWaveformView.updateAudioData(Bytes2shorts(recorder.getBytes()));
			}

		}

		/**
		 * Gets a value indicating whether the thread should continue running.
		 *
		 * @return true if the thread should continue running or false if it
		 *         should stop
		 */
		private synchronized boolean shouldContinue() {
			return mShouldContinue;
		}

		/**
		 * Notifies the thread that it should stop running at the next
		 * opportunity.
		 */
		public synchronized void stopRunning() {
			mShouldContinue = false;
		}

	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("record_finish")) {
			if (message.toString().equals("重录")) {

			} else if (message.toString().equals("试听")) {
				Intent audition = new Intent(RecordVoiceActivity.this,
						AuditionActivity.class);
				audition.putExtra("voiceName", voiceName + ".mp3");
				audition.putExtra("voiceDuration", recordTime);
				startActivityForResult(audition, 0);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			setResult(resultCode, data);
			finish();
			break;
		default:
			break;
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