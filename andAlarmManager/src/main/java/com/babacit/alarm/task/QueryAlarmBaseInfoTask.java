package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryAlarmBaseInfoRequest;
import com.babacit.alarm.msg.QueryAlarmBaseInfoResponse;

public class QueryAlarmBaseInfoTask extends
		BaseTask<QueryAlarmBaseInfoRequest, QueryAlarmBaseInfoResponse> {

	private final String TASKID = "17_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public QueryAlarmBaseInfoTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new QueryAlarmBaseInfoRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new QueryAlarmBaseInfoResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_ALARM_BASE_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}