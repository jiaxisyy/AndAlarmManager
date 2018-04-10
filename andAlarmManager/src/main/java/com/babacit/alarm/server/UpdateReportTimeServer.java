package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UpdateReportTimeTask;

public class UpdateReportTimeServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, String startTime,
			String endTime, RequestCallBack callBack) {
		mCallBack = callBack;
		UpdateReportTimeTask task = new UpdateReportTimeTask(userId, alarmCode,
				startTime, endTime);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (mCallBack != null)
				mCallBack.onSuccess(null);
		}

		@Override
		public void onFail(Object object, int errCode) {
			if (mCallBack != null) {
				mCallBack.onFail(object, errCode);
			}
		}
	};
}
