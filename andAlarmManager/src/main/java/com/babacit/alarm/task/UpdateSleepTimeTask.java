package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.UpdateSleepTimeRequest;

public class UpdateSleepTimeTask extends
		BaseTask<UpdateSleepTimeRequest, BaseResponse> {

	private final String TASKID = "18_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 睡眠开始时间 */
	private String mSleepStartTime;
	/** 睡眠结束时间 */
	private String mSleepEndTime;

	public UpdateSleepTimeTask(String userId, String alarmCode,
			String sleepStartTime, String sleepEndTime) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mSleepStartTime = sleepStartTime;
		mSleepEndTime = sleepEndTime;
	}

	@Override
	public void run() {
		request = new UpdateSleepTimeRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setSleepStartTime(mSleepStartTime);
		request.setSleepEndTime(mSleepEndTime);
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
		return Constant.UPDATE_SLEEP_TIME_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
