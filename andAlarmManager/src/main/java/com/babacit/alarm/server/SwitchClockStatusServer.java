package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.SwitchClockStatusTask;
import com.babacit.alarm.task.TaskManager;

public class SwitchClockStatusServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, String clockId,
			int status, RequestCallBack callBack) {
		mCallBack = callBack;
		SwitchClockStatusTask task = new SwitchClockStatusTask(userId,
				alarmCode, clockId, status);
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
