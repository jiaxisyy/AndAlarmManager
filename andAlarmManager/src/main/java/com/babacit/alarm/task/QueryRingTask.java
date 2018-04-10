package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryRingRequest;
import com.babacit.alarm.msg.QueryRingResponse;

public class QueryRingTask extends
		BaseTask<QueryRingRequest, QueryRingResponse> {

	private final String TASKID = "26_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 最后更新时间 2015-10-19 10:00:00 */
	private String mLastTime;

	public QueryRingTask(String userId, String alarmCode, String lastTime) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mLastTime = lastTime;
	}

	@Override
	public void run() {
		request = new QueryRingRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setLastTime(mLastTime);
		response = new QueryRingResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_RING_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
