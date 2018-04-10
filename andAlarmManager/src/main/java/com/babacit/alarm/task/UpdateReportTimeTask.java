package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.UpdateReportTimeRequest;

public class UpdateReportTimeTask extends
		BaseTask<UpdateReportTimeRequest, BaseResponse> {

	private final String TASKID = "35_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 睡眠开始时间 */
	private String mStartTime;
	/** 睡眠结束时间 */
	private String mEndTime;

	public UpdateReportTimeTask(String userId, String alarmCode,
			String startTime, String endTime) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mStartTime = startTime;
		mEndTime = endTime;
	}

	@Override
	public void run() {
		request = new UpdateReportTimeRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setStartTime(mStartTime);
		request.setEndTime(mEndTime);
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
		return Constant.UPDATE_REPORT_TIME_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
