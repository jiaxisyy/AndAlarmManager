package com.babacit.alarm.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.HoneyInfo;
import com.babacit.alarm.ui.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class HoneyAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<HoneyInfo> _honeyInfos = new ArrayList<HoneyInfo>();
	private SharedConfig config;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				break;

			default:
				break;
			}
		}
	};

	public HoneyAdapter(Context context, List<HoneyInfo> honeyInfos) {
		mContext = context;
		config = new SharedConfig(mContext);
		_honeyInfos = honeyInfos;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return _honeyInfos == null ? 0 : _honeyInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateList(List<HoneyInfo> hInfos) {
		_honeyInfos = hInfos;
		notifyDataSetChanged();
	}

	private void disPlayImg(final String url, final ImageView imv) {
		ImageLoader.getInstance().displayImage(url, imv);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (holder == null) {
			convertView = mInflater.inflate(R.layout.honey_list_item, null);
			holder = new ViewHolder();
			holder.mTvHoneyName = (TextView) convertView
					.findViewById(R.id.tv_honey_name);
			holder.mIvHoneyPortrait = (RoundImageView) convertView
					.findViewById(R.id.riv_honey_portrait);
			holder.mIvStatus = (ImageView) convertView
					.findViewById(R.id.iv_honey_status);
			holder.mIvHoneyPortrait.setScaleType(ScaleType.CENTER);
			if ((config.getAlarmCode().equals("") && position == 0)
					|| config.getAlarmCode().equals(
							_honeyInfos.get(position).getAlarmCode())) {
				holder.mIvStatus.setVisibility(View.VISIBLE);
			} else {
				holder.mTvHoneyName.setScaleX(0.8f);
				holder.mTvHoneyName.setScaleY(0.8f);
				holder.mTvHoneyName.setTextColor(0xffcccccc);
				holder.mIvHoneyPortrait.setScaleX(0.73f);
				holder.mIvHoneyPortrait.setScaleY(0.73f);
				holder.mIvStatus.setVisibility(View.GONE);
			}
			convertView.setTag(hashCode());
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTvHoneyName.setText(_honeyInfos.get(position).getNickName());
		if (_honeyInfos.get(position).getImgUrl() != null) {
			disPlayImg(_honeyInfos.get(position).getImgUrl(),
					holder.mIvHoneyPortrait);
		}

		return convertView;
	}

	public class ViewHolder {
		private TextView mTvHoneyName;
		private RoundImageView mIvHoneyPortrait;
		private ImageView mIvStatus;
	}
}