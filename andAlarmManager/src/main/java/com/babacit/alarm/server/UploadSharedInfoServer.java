package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.UploadSharedInfoResponse;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UploadSharedInfoTask;

public class UploadSharedInfoServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		UploadSharedInfoTask task = new UploadSharedInfoTask(userId, alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof UploadSharedInfoResponse) {
				UploadSharedInfoResponse response = (UploadSharedInfoResponse) obj;
				if (mCallBack != null)
					mCallBack.onSuccess(response.getNumber());
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
