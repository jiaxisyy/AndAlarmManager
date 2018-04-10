package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.UploadMonitorStatusRequest;
import com.babacit.alarm.msg.UploadMonitorStatusResponse;

public class UploadMonitorStatusTask extends
		BaseTask<UploadMonitorStatusRequest, BaseResponse> {

	private final String TASKID = "34_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 0：视频监控开始，1视频监控结束 */
	private int mType;

	public UploadMonitorStatusTask(String userId, String alarmCode, int type) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mType = type;
	}

	@Override
	public void run() {
		request = new UploadMonitorStatusRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setType(mType);
		response = new UploadMonitorStatusResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.UPLOAD_MONITOR_STATUS_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
