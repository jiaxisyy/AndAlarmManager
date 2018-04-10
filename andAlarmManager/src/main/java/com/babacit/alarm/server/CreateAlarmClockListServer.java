package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.AlarmInfo;
import com.babacit.alarm.entity.RingInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.AlarmClockMsg;
import com.babacit.alarm.msg.CreateAlarmClockResponse;
import com.babacit.alarm.msg.RingMsg;
import com.babacit.alarm.task.CreateAlarmClockTask;
import com.babacit.alarm.task.TaskManager;

public class CreateAlarmClockListServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode,
			List<AlarmInfo> alarmInfos, RequestCallBack callBack) {
		mCallBack = callBack;
		List<AlarmClockMsg> alarmClockMsgs = parse(alarmInfos);
		CreateAlarmClockTask task = new CreateAlarmClockTask(userId, alarmCode,
				alarmClockMsgs);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private List<AlarmClockMsg> parse(List<AlarmInfo> alarmInfos) {
		List<AlarmClockMsg> alarmClockMsgs = new ArrayList<AlarmClockMsg>();
		if (alarmInfos != null && alarmInfos.size() > 0) {
			for (AlarmInfo alarmInfo : alarmInfos) {
				AlarmClockMsg alarmClockMsg = new AlarmClockMsg();
				alarmClockMsg.setClockId(alarmInfo.getClockId());
				alarmClockMsg.setTitle(alarmInfo.getTitle());
				alarmClockMsg.setStatus(alarmInfo.getStatus());
				alarmClockMsg.setTimes(alarmInfo.getTimes());
				alarmClockMsg.setClockType(alarmInfo.getClockType());
				alarmClockMsg.setTimeType(alarmInfo.getTimeType());
				alarmClockMsg.setDateType(alarmInfo.getDateType());
				alarmClockMsg.setHolidayId(alarmInfo.getHolidayId());
				alarmClockMsg.setHolidayFilter(alarmInfo.getHolidayFilter());
				alarmClockMsg.setDate(alarmInfo.getDate());
				alarmClockMsg.setDay(alarmInfo.getDay());
				alarmClockMsg.setDelayTime(alarmInfo.getDelayTime());
				alarmClockMsg.setEarlyTime(alarmInfo.getEarlyTime());
				alarmClockMsg.setNeedRemind(alarmInfo.getNeedRemind());
				alarmClockMsg.setRemark(alarmInfo.getRemark());
				List<RingInfo> ringInfos = alarmInfo.getRingList();
				if (ringInfos != null && ringInfos.size() > 0) {
					List<RingMsg> ringMsgs = new ArrayList<RingMsg>();
					for (RingInfo ringInfo : ringInfos) {
						RingMsg ringMsg = new RingMsg();
						ringMsg.setRingCategory(ringInfo.getRingCategory());
						ringMsg.setRingId(ringInfo.getRingId());
						ringMsg.setRingName(ringInfo.getRingName());
						ringMsg.setRingType(ringInfo.getRingType());
						ringMsg.setRingUrl(ringInfo.getRingUrl());
						ringMsgs.add(ringMsg);
					}
					alarmClockMsg.setRingList(ringMsgs);
				}
				alarmClockMsgs.add(alarmClockMsg);
			}
		}
		return alarmClockMsgs;
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof CreateAlarmClockResponse) {
				CreateAlarmClockResponse response = (CreateAlarmClockResponse) obj;
				List<String> ids = response.getClockIdList();
				if (mCallBack != null)
					mCallBack.onSuccess(ids);
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
