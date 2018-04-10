package com.babacit.alarm.server;

import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.RingMsg;
import com.babacit.alarm.msg.UploadFileResponse;
import com.babacit.alarm.msg.UploadRingResponse;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UploadRingTask;

public class UploadRingServer {

	private RequestCallBack mCallBack;

	public void start(String suffixName, byte[] bs, RequestCallBack callBack) {
		mCallBack = callBack;
		UploadRingTask task = new UploadRingTask(suffixName, bs);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof UploadRingResponse) {
				UploadRingResponse response = (UploadRingResponse) obj;
				RingMsg ringMsg = response.getRing();
				RingInfo ringInfo = new RingInfo();
				ringInfo.setRingCategory(ringMsg.getRingCategory());
				ringInfo.setRingId(ringMsg.getRingId());
				ringInfo.setRingName(ringMsg.getRingName());
				ringInfo.setRingType(ringMsg.getRingType());
				ringInfo.setRingUrl(ringMsg.getRingUrl());
				if (mCallBack != null)
					mCallBack.onSuccess(ringInfo);
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
