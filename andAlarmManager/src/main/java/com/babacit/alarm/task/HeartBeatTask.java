package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.HeartBeatRequest;
import com.babacit.alarm.msg.HeartBeatResponse;

public class HeartBeatTask extends
		BaseTask<HeartBeatRequest, HeartBeatResponse> {

	private final String TASKID = "31_" + hashCode();

	/** 手机号码 */
	private String mUserId;
	
	/** 闹钟的序列号 */
	private String mAlarmCode;
	
	/** 监控标识:监控中则为on,其他情况可为空 */
	private String mMonitor;

	public HeartBeatTask(String userId, String alarmCode, String monitor) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mMonitor = monitor;
	}

	@Override
	public void run() {
		request = new HeartBeatRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setMonitor(mMonitor);
		response = new HeartBeatResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.HEART_BEAT_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
