package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UpdateHistoryInfoTask;

public class UpdateHistoryInfoServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, String clockId,
			int operType, RequestCallBack callBack) {
		mCallBack = callBack;
		UpdateHistoryInfoTask task = new UpdateHistoryInfoTask(userId,
				alarmCode, clockId, operType);
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
