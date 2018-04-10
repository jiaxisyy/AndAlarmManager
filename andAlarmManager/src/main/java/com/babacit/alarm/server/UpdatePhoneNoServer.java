package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UpdatePhoneNoTask;

public class UpdatePhoneNoServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String oldPhone, String pwd,
			String newPhoneNo, String verifyCode, RequestCallBack callBack) {
		mCallBack = callBack;
		UpdatePhoneNoTask task = new UpdatePhoneNoTask(userId, oldPhone, pwd,
				newPhoneNo, verifyCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (mCallBack != null) {
				mCallBack.onSuccess(null);
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
