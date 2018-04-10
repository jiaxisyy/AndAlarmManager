package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.UpdateAlarmStatusRequest;

public class UpdateAlarmStatusTask extends
		BaseTask<UpdateAlarmStatusRequest, BaseResponse> {

	private final String TASKID = "19_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/**
	 * 终端触屏状态 0：不可用；1：可用
	 */
	private int mTouchScreenStatus;
	/**
	 * 整点报时状态 0：不可用；1：可用
	 */
	private int mReportStatus;

	public UpdateAlarmStatusTask(String userId, String alarmCode,
			int touchScreenStatus, int reportStatus) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mTouchScreenStatus = touchScreenStatus;
		mReportStatus = reportStatus;
	}

	@Override
	public void run() {
		request = new UpdateAlarmStatusRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setTouchScreenStatus(mTouchScreenStatus);
		request.setReportStatus(mReportStatus);
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
		return Constant.UPDATE_ALARM_STATUS_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
