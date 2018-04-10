package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.QueryRewardInfoListResponse;
import com.babacit.alarm.msg.SwitchClockStatusRequest;

public class SwitchClockStatusTask extends
		BaseTask<SwitchClockStatusRequest, BaseResponse> {
	private final String TASKID = "28_" + hashCode();
	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 闹钟Id */
	private String mClockId;
	/** 闹钟状态：0：关闭 1：开启 2：删除 */
	private int mStatus;

	public SwitchClockStatusTask(String userId, String alarmCode,
			String clockId, int status) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mClockId = clockId;
		mStatus = status;
	}

	@Override
	public void run() {
		request = new SwitchClockStatusRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setClockId(mClockId);
		request.setStatus(mStatus);
		response = new QueryRewardInfoListResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.SWITCH_CLOCK_STATUS_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
