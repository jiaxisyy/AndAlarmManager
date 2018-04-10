package com.babacit.alarm.task;

import java.util.List;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.AlarmClockMsg;
import com.babacit.alarm.msg.CreateAlarmClockRequest;
import com.babacit.alarm.msg.CreateAlarmClockResponse;

public class CreateAlarmClockTask extends
		BaseTask<CreateAlarmClockRequest, CreateAlarmClockResponse> {

	private final String TASKID = "13_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 闹钟列表 */
	private List<AlarmClockMsg> mAlarmClockList;

	public CreateAlarmClockTask(String userId, String alarmCode,
			List<AlarmClockMsg> alarmClockList) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mAlarmClockList = alarmClockList;
	}

	@Override
	public void run() {
		request = new CreateAlarmClockRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setAlarmClockList(mAlarmClockList);
		response = new CreateAlarmClockResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.CREATE_ALARM_CLOCK_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
