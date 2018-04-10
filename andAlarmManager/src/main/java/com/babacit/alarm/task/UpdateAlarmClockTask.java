package com.babacit.alarm.task;

import java.util.List;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.AlarmClockMsg;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.UpdateAlarmClockRequest;

public class UpdateAlarmClockTask extends
		BaseTask<UpdateAlarmClockRequest, BaseResponse> {

	private final String TASKID = "12_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 闹钟列表 */
	private List<AlarmClockMsg> mAlarmClockList;

	public UpdateAlarmClockTask(String userId, String alarmCode,
			List<AlarmClockMsg> alarmClockList) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mAlarmClockList = alarmClockList;
	}

	@Override
	public void run() {
		request = new UpdateAlarmClockRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setAlarmClockList(mAlarmClockList);
		response = new BaseResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.UPDATE_ALARM_CLOCK_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
