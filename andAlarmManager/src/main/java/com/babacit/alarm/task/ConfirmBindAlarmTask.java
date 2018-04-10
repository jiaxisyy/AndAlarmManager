package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.ConfirmBindAlarmRequest;

public class ConfirmBindAlarmTask extends
		BaseTask<ConfirmBindAlarmRequest, BaseResponse> {

	private final String TASKID = "8_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 安全码 */
	private String mSecurityCode;

	public ConfirmBindAlarmTask(String userId, String alarmCode,
			String securityCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mSecurityCode = securityCode;
	}

	@Override
	public void run() {
		request = new ConfirmBindAlarmRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setSecurityCode(mSecurityCode);
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
		return Constant.CONFIRM_BIND_ALARM_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
