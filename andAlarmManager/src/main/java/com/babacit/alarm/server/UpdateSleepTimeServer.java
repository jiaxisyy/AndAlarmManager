package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UpdateSleepTimeTask;

public class UpdateSleepTimeServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, String sleepStartTime,
			String sleepEndTime, RequestCallBack callBack) {
		mCallBack = callBack;
		UpdateSleepTimeTask task = new UpdateSleepTimeTask(userId, alarmCode,
				sleepStartTime, sleepEndTime);
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
