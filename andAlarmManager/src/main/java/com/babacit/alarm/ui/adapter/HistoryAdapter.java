package com.babacit.alarm.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.HistoryInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.UpdateHistoryInfoServer;
import com.babacit.alarm.ui.activity.MainActivity;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ToastUtil;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<HistoryInfo> _historyInfos = new ArrayList<HistoryInfo>();
	private Handler mHandler;
	private SharedConfig config;
	private int pos = -1;

	private RequestCallBack updateHistoryInfoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			mHandler.sendEmptyMessage(MainActivity.MSG_UPDATE_HISTORY_STATUS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			mHandler.sendEmptyMessage(MainActivity.MSG_UPDATE_HISTORY_STATUS);
		}
	};

	public void setMainHandler(Handler _handler) {
		mHandler = _handler;
	}

	public HistoryAdapter(Context context, List<HistoryInfo> historyInfos) {
		mContext = context;
		config = new SharedConfig(context);
		_historyInfos = historyInfos;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateList(List<HistoryInfo> hInfos) {
		_historyInfos = hInfos;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return _historyInfos == null ? 0 : _historyInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			pos = (Integer) v.getTag();
			switch (v.getId()) {
			case R.id.btn_history_finish:
				new UpdateHistoryInfoServer().start(config.getUserId(), config
						.getAlarmCode(), _historyInfos.get(pos).getClockId(),
						2, updateHistoryInfoCallBack);
				break;
			// case R.id.iv_history_noflower:
			// new UpdateHistoryInfoServer().start(config.getUserId(), config
			// .getAlarmCode(), _historyInfos.get(pos).getClockId(),
			// 0, updateHistoryInfoCallBack);
			// break;
			case R.id.iv_history_flower:
				if (!NetworkUtils.isNetWorkOk(mContext)) {
					ToastUtil.showToast(mContext, "请检查您的网络！");
					return;
				}
				new UpdateHistoryInfoServer().start(config.getUserId(), config
						.getAlarmCode(), _historyInfos.get(pos).getClockId(),
						1, updateHistoryInfoCallBack);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (holder == null) {
			convertView = mInflater.inflate(R.layout.history_list_item, null);
			holder = new ViewHolder();
			holder.mTvHistoryDate = (TextView) convertView
					.findViewById(R.id.tv_history_date);
			holder.mTvHistoryTime = (TextView) convertView
					.findViewById(R.id.tv_history_time);
			holder.mTvHistoryTaskName = (TextView) convertView
					.findViewById(R.id.tv_history_task);
			holder.mBtFinish = (Button) convertView
					.findViewById(R.id.btn_history_finish);
			holder.mIvFlower = (ImageView) convertView
					.findViewById(R.id.iv_history_flower);
			holder.mIvNoFlower = (ImageView) convertView
					.findViewById(R.id.iv_history_noflower);
			holder.mBtFinish.setTag(position);
			holder.mIvFlower.setTag(position);
			holder.mIvNoFlower.setTag(position);
			holder.mBtFinish.setOnClickListener(listener);
			holder.mIvFlower.setOnClickListener(listener);
			// holder.mIvNoFlower.setOnClickListener(listener);
			convertView.setTag(hashCode());
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (_historyInfos.get(position).getFinishStatus() == 0) {
			holder.mBtFinish.setVisibility(View.VISIBLE);
			holder.mIvFlower.setVisibility(View.GONE);
			holder.mIvNoFlower.setVisibility(View.GONE);
		} else {
			holder.mBtFinish.setVisibility(View.GONE);
			if (_historyInfos.get(position).getRewardStatus() == 1) {
				holder.mIvFlower.setVisibility(View.VISIBLE);
				holder.mIvNoFlower.setVisibility(View.GONE);
			} else if (_historyInfos.get(position).getRewardStatus() == 2) {
				holder.mIvFlower.setVisibility(View.GONE);
				holder.mIvNoFlower.setVisibility(View.VISIBLE);
			} else {
				holder.mIvFlower.setVisibility(View.GONE);
				holder.mIvNoFlower.setVisibility(View.GONE);
			}
		}
		holder.mTvHistoryTaskName.setText(_historyInfos.get(position)
				.getTitle());
		holder.mTvHistoryDate.setText(_historyInfos.get(position).getDate());
//		holder.mTvHistoryTime.setText(_historyInfos.get(position).getTime());
		return convertView;
	}

	public class ViewHolder {
		private TextView mTvHistoryDate;
		private TextView mTvHistoryTime;
		private TextView mTvHistoryTaskName;
		private Button mBtFinish;
		private ImageView mIvFlower;
		private ImageView mIvNoFlower;
	}
}