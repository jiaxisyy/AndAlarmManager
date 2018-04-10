package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.babacit.alarm.entity.CompletionInfo;
import com.babacit.alarm.entity.DayCompletionInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.CompletionInfoMsg;
import com.babacit.alarm.msg.DayCompletionInfoMsg;
import com.babacit.alarm.msg.QueryCompletionBarInfoResponse;
import com.babacit.alarm.task.QueryCompletionBarInfoTask;
import com.babacit.alarm.task.TaskManager;

public class QueryCompletionBarInfoServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryCompletionBarInfoTask task = new QueryCompletionBarInfoTask(
				userId, alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof QueryCompletionBarInfoResponse) {
				QueryCompletionBarInfoResponse response = (QueryCompletionBarInfoResponse) obj;
				List<CompletionInfoMsg> monthCompletionInfoMsgs = response
						.getMonthCompletionInfoList();
				List<CompletionInfo> monthCompletionInfos = new ArrayList<CompletionInfo>();
				if (monthCompletionInfoMsgs != null
						&& monthCompletionInfoMsgs.size() > 0) {
					for (CompletionInfoMsg completionInfoMsg : monthCompletionInfoMsgs) {
						CompletionInfo completionInfo = new CompletionInfo();
						completionInfo.setCompleteCount(completionInfoMsg
								.getCompleteCount());
						completionInfo.setIncompleteCount(completionInfoMsg
								.getIncompleteCount());
						completionInfo.setType(completionInfoMsg.getType());
						completionInfo.setIndex(completionInfoMsg.getIndex());
						monthCompletionInfos.add(completionInfo);
					}
				}
				List<CompletionInfoMsg> weekCompletionInfoMsgs = response
						.getWeekCompletionInfoList();
				List<CompletionInfo> weekCompletionInfos = new ArrayList<CompletionInfo>();
				if (weekCompletionInfoMsgs != null
						&& weekCompletionInfoMsgs.size() > 0) {
					for (CompletionInfoMsg completionInfoMsg : weekCompletionInfoMsgs) {
						CompletionInfo completionInfo = new CompletionInfo();
						completionInfo.setCompleteCount(completionInfoMsg
								.getCompleteCount());
						completionInfo.setIncompleteCount(completionInfoMsg
								.getIncompleteCount());
						completionInfo.setType(completionInfoMsg.getType());
						completionInfo.setIndex(completionInfoMsg.getIndex());
						weekCompletionInfos.add(completionInfo);
					}
				}
				List<DayCompletionInfoMsg> dayCompletionInfoMsgs = response
						.getDayCompletionInfoList();
				List<DayCompletionInfo> dayCompletionInfos = new ArrayList<DayCompletionInfo>();
				if (dayCompletionInfoMsgs != null
						&& dayCompletionInfoMsgs.size() > 0) {
					for (DayCompletionInfoMsg completionInfoMsg : dayCompletionInfoMsgs) {
						DayCompletionInfo completionInfo = new DayCompletionInfo();
						completionInfo.setCompleteCount(completionInfoMsg
								.getCompleteCount());
						completionInfo.setDay(completionInfoMsg.getDay());
						dayCompletionInfos.add(completionInfo);
					}
				}
				HashMap<String, List> map = new HashMap<String, List>();
				map.put("month", monthCompletionInfos);
				map.put("week", weekCompletionInfos);
				map.put("day", dayCompletionInfos);
				if (mCallBack != null)
					mCallBack.onSuccess(map);
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
