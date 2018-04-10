package com.babacit.alarm.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.entity.RingInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RingAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<RingInfo> _ringInfos = new ArrayList<RingInfo>();

	public RingAdapter(Context context, List<RingInfo> ringInfos) {
		mContext = context;
		_ringInfos = ringInfos;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return _ringInfos == null ? 0 : _ringInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateList(List<RingInfo> hInfos) {
		_ringInfos = hInfos;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (holder == null) {
			convertView = mInflater.inflate(R.layout.ring_list_item, null);
			holder = new ViewHolder();
			holder.mTvRingName = (TextView) convertView
					.findViewById(R.id.tv_ring_name);
			holder.mIvCheck = (ImageView) convertView
					.findViewById(R.id.iv_check);
			convertView.setTag(hashCode());
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTvRingName.setText(_ringInfos.get(position).getRingName());
		if (_ringInfos.get(position).isCheck()) {
			holder.mIvCheck.setVisibility(View.VISIBLE);
		} else {
			holder.mIvCheck.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	public class ViewHolder {
		private ImageView mIvCheck;
		private TextView mTvRingName;
	}
}