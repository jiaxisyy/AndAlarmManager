package com.babacit.alarm.ui.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.Message;
import com.babacit.alarm.utils.MediaPlayerUtil;
import com.babacit.alarm.utils.UnitUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VoiceMailAdapter extends BaseAdapter {
	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private List<Message> coll;
	private LayoutInflater mInflater;
	private SharedConfig config;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private Context mContext;
	private AudioManager audioManager;
	private Set<String> setMsgId = new HashSet<String>();

	public VoiceMailAdapter(Context context, List<Message> coll) {
		mContext = context;
		audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		setSpeakerphoneOn(true);
		config = new SharedConfig(context);
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		setMsgId = config.getStringSet("message_id", setMsgId);
	}

	public int getCount() {
		return coll == null ? 0 : coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		Message entity = coll.get(position);
		if (entity.getUser() != null && entity.getUser().getUserId().equals(config.getUserId())) {
			return IMsgViewType.IMVT_TO_MSG;
		} else {
			return IMsgViewType.IMVT_COM_MSG;
		}
	}

	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final Message entity = coll.get(position);
		boolean isComMsg = (entity.getUser().getUserId().equals(config.getUserId()) ? false : true);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			if (isComMsg) {
				convertView = mInflater.inflate(R.layout.voice_mail_item_left, null);
				viewHolder.tvRed = convertView.findViewById(R.id.tv_red);
			} else {
				convertView = mInflater.inflate(R.layout.voice_mail_item_right, null);
			}
			viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_userhead);
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
			viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(entity.getCreateTime())) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf1.parse(entity.getCreateTime());
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
				String sendTime = sdf.format(date);
				viewHolder.tvSendTime.setText(sendTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (entity.getUser().getImgUrl() != null && !entity.getUser().getImgUrl().equals("")) {
			ImageLoader.getInstance().displayImage(entity.getUser().getImgUrl(), viewHolder.ivHead);
		}

		viewHolder.tvContent.setText("");
		if (isComMsg) {
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chatto_voice_playing_l, 0, 0, 0);
			// 查询message_id是否存在，如果存在则表示为已读，否则为未读
			int id = entity.getId();
			String strId = String.valueOf(id);
			if (!setMsgId.contains(strId)) {
				viewHolder.tvRed.setVisibility(View.VISIBLE);
			} else 
				viewHolder.tvRed.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing_r, 0);
		}
		viewHolder.tvTime.setText(String.format("%d\"", entity.getVoiceTime()));
		
		LayoutParams params = viewHolder.tvContent.getLayoutParams();
		params.width = UnitUtils.dip2px(mContext, 40) + entity.getVoiceTime() * 5;
		params.height = UnitUtils.dip2px(mContext, 41);
		viewHolder.tvContent.setLayoutParams(params);
		viewHolder.tvContent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// playMusic(entity.getMessageUrl());
				MediaPlayerUtil.getInstance().playMusic(entity.getMessageUrl());
				
				// 用sharedpreferences来存储消息ID，目的是为了区分已读未读状态
				int id = entity.getId();
				String msgId = String.valueOf(id);
				if (!setMsgId.contains(msgId)) {
					setMsgId.add(msgId);
					((View)v.getParent()).findViewById(R.id.tv_red).setVisibility(View.INVISIBLE);
				}
				config.putStringSet("message_id", setMsgId);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public ImageView ivHead;
		public TextView tvSendTime;
		public TextView tvContent;
		public TextView tvTime;
		public boolean isComMsg = true;
		public View tvRed;
	}

	public void updateList(List<Message> l) {
		this.coll = l;
		notifyDataSetChanged();
	}

	/**
	 * @Description
	 * @param name
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			} else {
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(name);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
			// mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			// public void onCompletion(MediaPlayer mp) {
			//
			// }
			// });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stop() {

	}

	private void setSpeakerphoneOn(boolean on) {
		if (on) {
			audioManager.setSpeakerphoneOn(true);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			audioManager.setMode(AudioManager.MODE_IN_CALL);
		}
	}
}
