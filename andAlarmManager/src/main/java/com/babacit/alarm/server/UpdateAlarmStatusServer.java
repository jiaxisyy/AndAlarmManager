package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UpdateAlarmStatusTask;

public class UpdateAlarmStatusServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, int touchScreenStatus,
			int reportStatus, RequestCallBack callBack) {
		mCallBack = callBack;
		UpdateAlarmStatusTask task = new UpdateAlarmStatusTask(userId,
				alarmCode, touchScreenStatus, reportStatus);
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
