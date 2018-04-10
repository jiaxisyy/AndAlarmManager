package com.babacit.alarm.server;

import java.util.HashMap;
import java.util.List;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.HeartBeatResponse;
import com.babacit.alarm.msg.PushInfoMsg;
import com.babacit.alarm.task.HeartBeatTask;
import com.babacit.alarm.task.TaskManager;

public class HeartBeatServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, String monitor, RequestCallBack callBack) {
		mCallBack = callBack;
		HeartBeatTask task = new HeartBeatTask(userId, alarmCode, monitor);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof HeartBeatResponse) {
				HeartBeatResponse response = (HeartBeatResponse) obj;
				int onlineStatus = response.getOnlineStatus();
				String batteryPercent = response.getBatteryPercent();
				int chargeStatus = response.getChargeStatus();
				List<PushInfoMsg> pushList = response.getPushList();
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("onlineStatus", onlineStatus);
				map.put("batteryPercent", batteryPercent);
				map.put("chargeStatus", chargeStatus);
				map.put("pushList", pushList);
				if (mCallBack != null)
					mCallBack.onSuccess(map);
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
