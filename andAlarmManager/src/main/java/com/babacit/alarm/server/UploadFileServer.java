package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.UploadFileResponse;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UploadFileTask;

public class UploadFileServer {

	private RequestCallBack mCallBack;

	public void start(String suffixName, byte[] bs, RequestCallBack callBack) {
		mCallBack = callBack;
		UploadFileTask task = new UploadFileTask(suffixName, bs);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof UploadFileResponse) {
				UploadFileResponse response = (UploadFileResponse) obj;
				String url = response.getUrl();
				if (mCallBack != null)
					mCallBack.onSuccess(url);
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
