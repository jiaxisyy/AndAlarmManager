package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.UpdateHistoryInfoRequest;

public class UpdateHistoryInfoTask extends
		BaseTask<UpdateHistoryInfoRequest, BaseResponse> {

	private final String TASKID = "15_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 闹钟Id */
	private String mClockId;
	/**
	 * 操作类型 0：取消奖励，1：奖励 2：完成
	 */
	private int mOperType;

	public UpdateHistoryInfoTask(String userId, String alarmCode,
			String clockId, int operType) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mClockId = clockId;
		mOperType = operType;
	}

	@Override
	public void run() {
		request = new UpdateHistoryInfoRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setOperType(mOperType);
		request.setClockId(mClockId);
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
		return Constant.UPDATE_HISTORY_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
