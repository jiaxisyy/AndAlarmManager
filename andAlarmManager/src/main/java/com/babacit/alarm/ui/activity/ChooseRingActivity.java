package com.babacit.alarm.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.QueryRingServer;
import com.babacit.alarm.ui.adapter.RingAdapter;
import com.babacit.alarm.ui.adapter.ViewPagerAdapter;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseRingActivity extends BaseActivity implements OnClickListener {
	private ViewPager viewPager;
	private ImageView mIvCursor;
	private List<View> lists = new ArrayList<View>();
	private ViewPagerAdapter adapter;
	private Bitmap cursor;
	private int offSet;
	private int currentItem;
	private Matrix matrix = new Matrix();
	private int bmWidth;
	private Animation animation;
	private TextView mTvRing, mTvMusic, mTvVoice;
	private ListView mLvRing, mLvMusic, mLvVoice;
	private List<RingInfo> rings = new ArrayList<RingInfo>();
	private List<RingInfo> musics = new ArrayList<RingInfo>();
	private List<RingInfo> voices = new ArrayList<RingInfo>();
	private RingAdapter ringAdapter, musicAdapter, voiceAdapter;
	private List<RingInfo> _ringList = new ArrayList<RingInfo>();
	private SharedConfig config;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private Intent result = new Intent();
	private int ringId = -1;

	private RequestCallBack queryRingCallBack = new RequestCallBack() {
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

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				_ringList = (List<RingInfo>) msg.obj;
				initData();
				ringAdapter.notifyDataSetChanged();
				musicAdapter.notifyDataSetChanged();
				voiceAdapter.notifyDataSetChanged();
				break;
			case 1:

				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_ring);
		config = new SharedConfig(this);
		bindField();
		if (getIntent().getParcelableExtra("ring") != null) {
			ringId = ((RingInfo) getIntent().getParcelableExtra("ring"))
					.getRingId();
		}
		ringAdapter = new RingAdapter(this, rings);
		musicAdapter = new RingAdapter(this, musics);
		voiceAdapter = new RingAdapter(this, voices);
		mLvRing.setAdapter(ringAdapter);
		mLvMusic.setAdapter(musicAdapter);
		mLvVoice.setAdapter(voiceAdapter);

		new QueryRingServer().start(config.getUserId(), config.getAlarmCode(),
				config.getRingLatestUpdateTime(), queryRingCallBack);
	}

	private void bindField() {
		findViewById(R.id.btn_choose_ring_back).setOnClickListener(this);
		mIvCursor = (ImageView) findViewById(R.id.cursor);
		mTvRing = (TextView) findViewById(R.id.tv_ring);
		mTvRing.setTextColor(0xFFFF8D3C);
		mTvMusic = (TextView) findViewById(R.id.tv_music);
		mTvVoice = (TextView) findViewById(R.id.tv_voice);
		// 将页面添加到viewPager集合
		View ring = getLayoutInflater().inflate(R.layout.layout_choose_ring,
				null);
		mLvRing = (ListView) ring.findViewById(R.id.lv_ring);
		mLvRing.setOnItemClickListener(ringitemclick);
		lists.add(ring);
		View music = getLayoutInflater().inflate(R.layout.layout_choose_music,
				null);
		lists.add(music);
		mLvMusic = (ListView) music.findViewById(R.id.lv_music);
		mLvMusic.setOnItemClickListener(musicitemclick);
		View voice = getLayoutInflater().inflate(R.layout.layout_choose_voice,
				null);
		lists.add(voice);
		mLvVoice = (ListView) voice.findViewById(R.id.lv_voice);
		mLvVoice.setOnItemClickListener(voiceitemclick);
		// 初始化滑动图片位置
		initCursor();

		adapter = new ViewPagerAdapter(lists);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(adapter);
		// ViewPager滑动监听器
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			// 当滑动式，顶部的imageView是通过animation缓慢的滑动
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					changeTextColor(0xFFFF8D3C, 0xFFCCCCCC, 0xFFCCCCCC);
					if (currentItem == 1) {
						animation = new TranslateAnimation(
								offSet * 2 + bmWidth, 0, 0, 0);
					} else if (currentItem == 2) {
						animation = new TranslateAnimation(offSet * 4 + 2
								* bmWidth, 0, 0, 0);
					}
					break;
				case 1:
					changeTextColor(0xFFCCCCCC, 0xFFFF8D3C, 0xFFCCCCCC);
					if (currentItem == 0) {
						animation = new TranslateAnimation(0, offSet * 2
								+ bmWidth, 0, 0);
					} else if (currentItem == 2) {
						animation = new TranslateAnimation(4 * offSet + 2
								* bmWidth, offSet * 2 + bmWidth, 0, 0);
					}
					break;
				case 2:
					changeTextColor(0xFFCCCCCC, 0xFFCCCCCC, 0xFFFF8D3C);
					if (currentItem == 0) {
						animation = new TranslateAnimation(0, 4 * offSet + 2
								* bmWidth, 0, 0);
					} else if (currentItem == 1) {
						animation = new TranslateAnimation(
								offSet * 2 + bmWidth, 4 * offSet + 2 * bmWidth,
								0, 0);
					}
				}
				currentItem = arg0;
				animation.setDuration(150); // 光标滑动速度
				animation.setFillAfter(true);
				mIvCursor.startAnimation(animation);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mTvRing.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(0);
				changeTextColor(0xFFFF8D3C, 0xFFCCCCCC, 0xFFCCCCCC);
			}
		});

		mTvMusic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(1);
				changeTextColor(0xFFCCCCCC, 0xFFFF8D3C, 0xFFCCCCCC);
			}
		});

		mTvVoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(2);
				changeTextColor(0xFFCCCCCC, 0xFFCCCCCC, 0xFFFF8D3C);
			}
		});
	}

	private void initData() {
		if (_ringList != null && _ringList.size() > 0) {
			for (RingInfo ring : _ringList) {
				if (ring.getRingId() == ringId) {
					ring.setCheck(true);
				}
				if (ring.getRingCategory() == 1) {
					rings.add(ring);
				} else if (ring.getRingCategory() == 2) {
					musics.add(ring);
				} else if (ring.getRingCategory() == 3) {
					voices.add(ring);
				}
			}
		}
	}

	/**
	 * 计算滑动的图片的位置
	 */
	private void initCursor() {
		cursor = BitmapFactory
				.decodeResource(getResources(), R.drawable.cursor);
		bmWidth = cursor.getWidth();

		DisplayMetrics dm = getResources().getDisplayMetrics();
		// 设置图标的起始位置(3:3个textview。)
		offSet = (dm.widthPixels - 3 * bmWidth) / 6;
		// offSet = dm.widthPixels / 6 - bmWidth / 3;
		matrix.setTranslate(offSet, 0);
		mIvCursor.setImageMatrix(matrix); // 需要imageView的scaleType为matrix
		currentItem = 0;
	}

	private void changeTextColor(int color1, int color2, int color3) {
		mTvRing.setTextColor(color1);
		mTvMusic.setTextColor(color2);
		mTvVoice.setTextColor(color3);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_choose_ring_back:
			mMediaPlayer.stop();
			setResult(1, result);
			finish();
			break;
		default:
			break;
		}
	}

	OnItemClickListener ringitemclick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int pos,
				long arg3) {
			// Intent ring = new Intent();
			// ring.putExtra("ring", rings.get(pos));
			result.putExtra("ring", rings.get(pos));
			new Thread() {
				@Override
				public void run() {
					playMusic(rings.get(pos).getRingUrl());
				}
			}.start();
			int ringSize = rings.size();
			for (int i = 0; i < ringSize; i++) {
				if (i == pos) {
					rings.get(i).setCheck(true);
				} else {
					rings.get(i).setCheck(false);
				}
			}
			int musicSize = musics.size();
			for (int i = 0; i < musicSize; i++) {
				musics.get(i).setCheck(false);
			}
			int voiceSize = voices.size();
			for (int i = 0; i < voiceSize; i++) {
				voices.get(i).setCheck(false);
			}
			ringAdapter.notifyDataSetChanged();
			musicAdapter.notifyDataSetChanged();
			voiceAdapter.notifyDataSetChanged();
			// setResult(1, ring);
			// finish();
		}
	};

	OnItemClickListener musicitemclick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int pos,
				long arg3) {
			// Intent music = new Intent();
			result.putExtra("ring", musics.get(pos));
			new Thread() {
				@Override
				public void run() {
					playMusic(musics.get(pos).getRingUrl());
				}
			}.start();
			int ringSize = rings.size();
			for (int i = 0; i < ringSize; i++) {
				rings.get(i).setCheck(false);
			}
			int musicSize = musics.size();
			for (int i = 0; i < musicSize; i++) {
				if (i == pos) {
					musics.get(i).setCheck(true);
				} else
					musics.get(i).setCheck(false);
			}
			int voiceSize = voices.size();
			for (int i = 0; i < voiceSize; i++) {
				voices.get(i).setCheck(false);
			}
			ringAdapter.notifyDataSetChanged();
			musicAdapter.notifyDataSetChanged();
			voiceAdapter.notifyDataSetChanged();
			// setResult(1, music);
			// finish();
		}
	};

	OnItemClickListener voiceitemclick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int pos,
				long arg3) {
			// Intent voice = new Intent();
			result.putExtra("ring", voices.get(pos));
			new Thread() {
				@Override
				public void run() {
					playMusic(voices.get(pos).getRingUrl());
				}
			}.start();
			int ringSize = rings.size();
			for (int i = 0; i < ringSize; i++) {
				rings.get(i).setCheck(false);
			}
			int musicSize = musics.size();
			for (int i = 0; i < musicSize; i++) {
				musics.get(i).setCheck(false);
			}
			int voiceSize = voices.size();
			for (int i = 0; i < voiceSize; i++) {
				if (i == pos)
					voices.get(i).setCheck(true);
				else
					voices.get(i).setCheck(false);
			}
			ringAdapter.notifyDataSetChanged();
			musicAdapter.notifyDataSetChanged();
			voiceAdapter.notifyDataSetChanged();
			// setResult(1, voice);
			// finish();
		}
	};

	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mMediaPlayer.stop();
			setResult(1, result);
			finish();
		}
		return super.onKeyUp(keyCode, event);
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