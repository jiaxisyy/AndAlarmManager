package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UploadMonitorStatusTask;

public class UploadMonitorStatusServer {

	private RequestCallBack mCallBack;
	
	/**
	 * @param userId
	 * @param alarmCode
	 * @param type 0申请监控  1监控开始 2监控结束 
	 * @param callBack
	 */
	public void start(String userId, String alarmCode, int type,
			RequestCallBack callBack) {
		mCallBack = callBack;
		UploadMonitorStatusTask task = new UploadMonitorStatusTask(userId,
				alarmCode, type);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (mCallBack != null)
				mCallBack.onSuccess(obj);
		}

		@Override
		public void onFail(Object object, int errCode) {
			if (mCallBack != null) {
				mCallBack.onFail(object, errCode);
			}
		}
	};
}
