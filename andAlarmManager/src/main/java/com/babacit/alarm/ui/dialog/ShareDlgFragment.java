package com.babacit.alarm.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.babacit.alarm.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareDlgFragment extends DialogFragment implements OnClickListener {
	private String[] griditem1;
	private GridView mGvOne;
	private int[] img = { R.drawable.share_cap_qq, R.drawable.share_cap_wechat,
			R.drawable.share_cap_qzone, /*R.drawable.share_cap_tencent_weibo,*/
			R.drawable.share_cap_wechat_friends,
			R.drawable.share_cap_sina_weibo };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
		setStyle(style, theme);
	}

	@Override
	public void onStart() {
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.share_grid_dialog_fragment,
				container, false);

		mGvOne = (GridView) view.findViewById(R.id.gv_one);
		mGvOne.setSelector(new ColorDrawable(Color.TRANSPARENT));

		griditem1 = getArguments().getStringArray("grid_1");
		List<Map<String, Object>> items1 = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < griditem1.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img", img[i]);
			map.put("title", griditem1[i]);
			items1.add(map);
		}
		ShareAdapter adapter = new ShareAdapter(getActivity(), items1);
		mGvOne.setAdapter(adapter);

		mGvOne.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
				act.onDialogConfirm("share", false, String.valueOf(position));
			}
		});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			OnMyDialogClickListener act = (OnMyDialogClickListener) activity;
		} catch (Exception e) {

		}
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	private class ShareAdapter extends BaseAdapter {
		List<Map<String, Object>> _items = new ArrayList<Map<String, Object>>();
		LayoutInflater mInflater;

		public ShareAdapter(Context context, List<Map<String, Object>> items) {
			_items = items;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return _items == null ? 0 : _items.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (holder == null) {
				convertView = mInflater.inflate(R.layout.share_grid_item, null);
				holder = new ViewHolder();
				holder.mIvShare = (ImageView) convertView
						.findViewById(R.id.iv_share_item);
				holder.mTvShare = (TextView) convertView
						.findViewById(R.id.tv_share_item);
				holder.mTvShare.setText((String) _items.get(position).get(
						"title"));
				holder.mIvShare.setImageResource((Integer) _items.get(position)
						.get("img"));
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		class ViewHolder {
			ImageView mIvShare;
			TextView mTvShare;
		}
	}
}