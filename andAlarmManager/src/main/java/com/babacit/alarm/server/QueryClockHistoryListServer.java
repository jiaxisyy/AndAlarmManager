package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.HistoryInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.ClockHistoryMsg;
import com.babacit.alarm.msg.QueryClockHistoryListResponse;
import com.babacit.alarm.task.QueryClockHistoryListTask;
import com.babacit.alarm.task.TaskManager;

public class QueryClockHistoryListServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryClockHistoryListTask task = new QueryClockHistoryListTask(userId,
				alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj instanceof QueryClockHistoryListResponse) {
				QueryClockHistoryListResponse response = (QueryClockHistoryListResponse) obj;
				List<ClockHistoryMsg> clockHistoryMsgs = response
						.getClockHistoryList();
				List<HistoryInfo> historyInfos = new ArrayList<HistoryInfo>();
				if (clockHistoryMsgs != null && clockHistoryMsgs.size() > 0) {
					for (ClockHistoryMsg clockHistoryMsg : clockHistoryMsgs) {
						HistoryInfo historyInfo = new HistoryInfo();
						historyInfo.setDate(clockHistoryMsg.getDate());
						historyInfo.setFinishStatus(clockHistoryMsg
								.getFinishStatus());
						historyInfo.setTitle(clockHistoryMsg.getTitle());
						historyInfo.setClockId(clockHistoryMsg.getClockId());
						historyInfo.setRewardStatus(clockHistoryMsg
								.getRewardStatus());
						historyInfos.add(historyInfo);
					}
				}
				if (mCallBack != null)
					mCallBack.onSuccess(historyInfos);
			}
		}

		@Override
		public void onFail(Object object, int errCode) {
			if (mCallBack != null) {
				mCallBack.onFail(object, errCode);
			}
		}
	};
}
