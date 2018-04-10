package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.Holiday;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.HolidayMsg;
import com.babacit.alarm.msg.QueryHolidayResponse;
import com.babacit.alarm.task.QueryHolidayTask;
import com.babacit.alarm.task.TaskManager;

public class QueryHolidayServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryHolidayTask task = new QueryHolidayTask(userId, alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof QueryHolidayResponse) {
				QueryHolidayResponse response = (QueryHolidayResponse) obj;
				List<HolidayMsg> holidayMsgs = response.getHolidayList();
				List<Holiday> holidays = new ArrayList<Holiday>();
				if (holidayMsgs != null && holidayMsgs.size() > 0) {
					for (HolidayMsg holidayMsg : holidayMsgs) {
						Holiday holiday = new Holiday();
						holiday.setHolidayId(holidayMsg.getHolidayId());
						holiday.setHolidayName(holidayMsg.getHolidayName());
						holiday.setHolidayTime(holidayMsg.getHolidayTime());
						holiday.setDateType(holidayMsg.getDateType());
						holidays.add(holiday);
					}
				}
				if (mCallBack != null)
					mCallBack.onSuccess(holidays);
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
