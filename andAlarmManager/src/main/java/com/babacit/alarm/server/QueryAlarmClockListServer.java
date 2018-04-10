package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.AlarmInfo;
import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.AlarmClockMsg;
import com.babacit.alarm.msg.QueryAlarmClockListResponse;
import com.babacit.alarm.msg.RingMsg;
import com.babacit.alarm.task.QueryAlarmClockListTask;
import com.babacit.alarm.task.TaskManager;

public class QueryAlarmClockListServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryAlarmClockListTask task = new QueryAlarmClockListTask(userId,
				alarmCode);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj instanceof QueryAlarmClockListResponse) {
				QueryAlarmClockListResponse response = (QueryAlarmClockListResponse) obj;
				List<AlarmClockMsg> alarmClockMsgs = response
						.getAlarmClockList();
				List<AlarmInfo> alarmInfos = new ArrayList<AlarmInfo>();
				if (alarmClockMsgs != null && alarmClockMsgs.size() > 0) {
					for (AlarmClockMsg alarmClockMsg : alarmClockMsgs) {
						AlarmInfo alarmInfo = new AlarmInfo();
						alarmInfo.setClockId(alarmClockMsg.getClockId());
						alarmInfo.setTitle(alarmClockMsg.getTitle());
						alarmInfo.setStatus(alarmClockMsg.getStatus());
						alarmInfo.setTimes(alarmClockMsg.getTimes());
						alarmInfo.setClockType(alarmClockMsg.getClockType());
						alarmInfo.setTimeType(alarmClockMsg.getTimeType());
						alarmInfo.setDay(alarmClockMsg.getDay());
						alarmInfo.setDateType(alarmClockMsg.getDateType());
						alarmInfo.setHolidayId(alarmClockMsg.getHolidayId());
						alarmInfo.setHolidayFilter(alarmClockMsg
								.getHolidayFilter());
						alarmInfo.setDate(alarmClockMsg.getDate());
						alarmInfo.setDelayTime(alarmClockMsg.getDelayTime());
						alarmInfo.setEarlyTime(alarmClockMsg.getEarlyTime());
						alarmInfo.setNeedRemind(alarmClockMsg.getNeedRemind());
						alarmInfo.setRemark(alarmClockMsg.getRemark());
						alarmInfo.setSortTime(alarmClockMsg.getSortTime());
						List<RingMsg> ringMsgs = alarmClockMsg.getRingList();
						if (ringMsgs != null && ringMsgs.size() > 0) {
							List<RingInfo> ringInfos = new ArrayList<RingInfo>();
							for (RingMsg ringMsg : ringMsgs) {
								RingInfo ringInfo = new RingInfo();
								ringInfo.setRingCategory(ringMsg
										.getRingCategory());
								ringInfo.setRingId(ringMsg.getRingId());
								ringInfo.setRingName(ringMsg.getRingName());
								ringInfo.setRingType(ringMsg.getRingType());
								ringInfo.setRingUrl(ringMsg.getRingUrl());
								ringInfos.add(ringInfo);
							}
							alarmInfo.setRingList(ringInfos);
						}
						alarmInfos.add(alarmInfo);
					}
				}
				if (mCallBack != null)
					mCallBack.onSuccess(alarmInfos);
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
