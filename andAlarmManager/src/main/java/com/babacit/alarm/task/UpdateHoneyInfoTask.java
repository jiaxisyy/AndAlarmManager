package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.HoneyInfoMsg;
import com.babacit.alarm.msg.UpdateHoneyInfoRequest;
import com.babacit.alarm.msg.UpdateHoneyInfoResponse;

public class UpdateHoneyInfoTask extends
		BaseTask<UpdateHoneyInfoRequest, UpdateHoneyInfoResponse> {

	private final String TASKID = "10_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private HoneyInfoMsg mHoneyInfoMsg;

	public UpdateHoneyInfoTask(String userId, HoneyInfoMsg honeyInfoMsg) {
		mUserId = userId;
		mHoneyInfoMsg = honeyInfoMsg;
	}

	@Override
	public void run() {
		request = new UpdateHoneyInfoRequest();
		request.setUserId(mUserId);
		request.setHoneyInfo(mHoneyInfoMsg);
		response = new UpdateHoneyInfoResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.UPDATE_HONEY_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
