package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.LoginResponse;
import com.babacit.alarm.task.LoginTask;
import com.babacit.alarm.task.TaskManager;

public class LoginServer {

	private RequestCallBack mCallBack;

	public void start(String phoneNo, String pwd, RequestCallBack callBack) {
		mCallBack = callBack;
		LoginTask task = new LoginTask(phoneNo, pwd);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj instanceof LoginResponse) {
				LoginResponse response = (LoginResponse) obj;
				if (mCallBack != null && response != null) {
					mCallBack.onSuccess(response.getUserId());
				}
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
