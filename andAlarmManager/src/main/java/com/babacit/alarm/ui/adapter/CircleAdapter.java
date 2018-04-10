package com.babacit.alarm.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.AlarmInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class CircleAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private SharedConfig config;
	private List<Map<String, Object>> _map = new ArrayList<Map<String, Object>>();

	public CircleAdapter(Context context, List<Map<String, Object>> mapList) {
		mContext = context;
		_map = mapList;
		config = new SharedConfig(context);
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return _map == null ? 0 : _map.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (holder == null) {
			convertView = mInflater.inflate(R.layout.grid_item, null);
			holder = new ViewHolder();
			holder.mBtItem = (Button) convertView.findViewById(R.id.btn_grid_item);
			boolean status = (Boolean) _map.get(position).get("status");
			if (status) {
				holder.mBtItem.setBackgroundResource(R.drawable.round_button_shape_focus);
				holder.mBtItem.setTextColor(0xffffffff);
			} else {
				holder.mBtItem.setBackgroundResource(R.drawable.selector_round_btn1);
				holder.mBtItem.setTextColor(0xff888888);
			}
			holder.mBtItem.setText((String) (_map.get(position).get("title")));
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	public void updateList(List<AlarmInfo> aInfos) {
		notifyDataSetChanged();
	}

	public class ViewHolder {
		Button mBtItem;
	}
}