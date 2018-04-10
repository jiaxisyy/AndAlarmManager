package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.UploadSharedInfoRequest;
import com.babacit.alarm.msg.UploadSharedInfoResponse;

public class UploadSharedInfoTask extends
		BaseTask<UploadSharedInfoRequest, UploadSharedInfoResponse> {

	private final String TASKID = "27_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public UploadSharedInfoTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new UploadSharedInfoRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new UploadSharedInfoResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.UPLOAD_SHARED_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
