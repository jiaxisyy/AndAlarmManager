package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.ApplyBindAlarmTask;
import com.babacit.alarm.task.TaskManager;

public class ApplyBindAlarmServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		ApplyBindAlarmTask task = new ApplyBindAlarmTask(userId, alarmCode);
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
