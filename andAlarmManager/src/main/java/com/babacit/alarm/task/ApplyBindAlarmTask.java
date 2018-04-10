package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.ApplyBindAlarmRequest;
import com.babacit.alarm.msg.BaseResponse;

public class ApplyBindAlarmTask extends
		BaseTask<ApplyBindAlarmRequest, BaseResponse> {

	private final String TASKID = "7_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public ApplyBindAlarmTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new ApplyBindAlarmRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
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
		return Constant.APPLY_BIND_ALARM_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
