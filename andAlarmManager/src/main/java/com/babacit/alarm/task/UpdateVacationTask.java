package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.UpdateVacationRequest;

public class UpdateVacationTask extends
		BaseTask<UpdateVacationRequest, BaseResponse> {

	private final String TASKID = "20_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 开始时间 */
	private String mStartTime;
	/** 截止时间 */
	private String mEndTime;
	/**
	 * 类型 0：暑假，1：寒假
	 */
	private int mType;

	public UpdateVacationTask(String userId, String alarmCode,
			String startTime, String endTime,int type) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mStartTime = startTime;
		mEndTime = endTime;
		mType = type;
	}

	@Override
	public void run() {
		request = new UpdateVacationRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setStartTime(mStartTime);
		request.setEndTime(mEndTime);
		request.setType(mType);
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
		return Constant.UPDATE_VACATION_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
