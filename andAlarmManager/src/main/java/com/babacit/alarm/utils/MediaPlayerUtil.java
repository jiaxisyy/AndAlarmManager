package com.babacit.alarm.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class MediaPlayerUtil {
	private static MediaPlayer mediaPlayer = null;
	private static MediaPlayerUtil instance = null;

	public static MediaPlayerUtil getInstance() {
		if (instance == null) {
			instance = new MediaPlayerUtil();
			mediaPlayer = new MediaPlayer();
		}
		return instance;
	}

	public void playMusic(String name) {
		try {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			} else {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(name);
				mediaPlayer.prepareAsync();
				mediaPlayer.setOnPreparedListener(mOnPreparedListener);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer = null;
			instance = null;
		}
	}
	
	private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.start();
		}
	};
	
}
