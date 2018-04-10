package com.babacit.alarm.server;

import com.babacit.alarm.entity.AlarmBaseInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.AlarmBaseInfoMsg;
import com.babacit.alarm.msg.QueryAlarmBaseInfoResponse;
import com.babacit.alarm.task.QueryAlarmBaseInfoTask;
import com.babacit.alarm.task.TaskManager;

public class QueryAlarmBaseInfoServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryAlarmBaseInfoTask task = new QueryAlarmBaseInfoTask(userId,
				alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof QueryAlarmBaseInfoResponse) {
				QueryAlarmBaseInfoResponse response = (QueryAlarmBaseInfoResponse) obj;
				AlarmBaseInfoMsg alarmBaseInfoMsg = response.getAlarmBaseInfo();
				AlarmBaseInfo alarmBaseInfo = new AlarmBaseInfo();
				if (alarmBaseInfoMsg != null) {
					alarmBaseInfo.setSleepStartTime(alarmBaseInfoMsg
							.getSleepStartTime());
					alarmBaseInfo.setSleepEndTime(alarmBaseInfoMsg
							.getSleepEndTime());
					alarmBaseInfo.setBatteryPercent(alarmBaseInfoMsg
							.getBatteryPercent());
					alarmBaseInfo.setChargeStatus(alarmBaseInfoMsg
							.getChargeStatus());
					alarmBaseInfo.setTouchScreenStatus(alarmBaseInfoMsg
							.getTouchScreenStatus());
					alarmBaseInfo.setReportStatus(alarmBaseInfoMsg
							.getReportStatus());
					alarmBaseInfo.setReportStartTime(alarmBaseInfoMsg
							.getReportStartTime());
					alarmBaseInfo.setReportEndTime(alarmBaseInfoMsg
							.getReportEndTime());
					alarmBaseInfo.setSummerVacationTime(alarmBaseInfoMsg
							.getSummerVacationTime());
					alarmBaseInfo.setWinterVacationTime(alarmBaseInfoMsg
							.getWinterVacationTime());
				}
				if (mCallBack != null)
					mCallBack.onSuccess(alarmBaseInfo);
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
