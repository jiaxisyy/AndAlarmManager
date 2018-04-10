package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.CompletionInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.CompletionInfoMsg;
import com.babacit.alarm.msg.QueryCompletionPieInfoResponse;
import com.babacit.alarm.task.QueryCompletionPieInfoTask;
import com.babacit.alarm.task.TaskManager;

public class QueryCompletionPieInfoServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryCompletionPieInfoTask task = new QueryCompletionPieInfoTask(
				userId, alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof QueryCompletionPieInfoResponse) {
				QueryCompletionPieInfoResponse response = (QueryCompletionPieInfoResponse) obj;
				List<CompletionInfoMsg> completionInfoMsgs = response
						.getCompletionInfoList();
				List<CompletionInfo> completionInfos = new ArrayList<CompletionInfo>();
				if (completionInfoMsgs != null && completionInfoMsgs.size() > 0) {
					for (CompletionInfoMsg completionInfoMsg : completionInfoMsgs) {
						CompletionInfo completionInfo = new CompletionInfo();
						completionInfo.setCompleteCount(completionInfoMsg
								.getCompleteCount());
						completionInfo.setIncompleteCount(completionInfoMsg
								.getIncompleteCount());
						completionInfo.setType(completionInfoMsg.getType());
						completionInfos.add(completionInfo);
					}
				}
				if (mCallBack != null)
					mCallBack.onSuccess(completionInfos);
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
