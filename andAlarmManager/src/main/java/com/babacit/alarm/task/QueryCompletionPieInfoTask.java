package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryCompletionPieInfoRequest;
import com.babacit.alarm.msg.QueryCompletionPieInfoResponse;

public class QueryCompletionPieInfoTask extends
		BaseTask<QueryCompletionPieInfoRequest, QueryCompletionPieInfoResponse> {

	private final String TASKID = "23_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public QueryCompletionPieInfoTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new QueryCompletionPieInfoRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new QueryCompletionPieInfoResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_COMPLETION_PIE_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
