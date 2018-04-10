package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryCompletionBarInfoRequest;
import com.babacit.alarm.msg.QueryCompletionBarInfoResponse;

public class QueryCompletionBarInfoTask extends
		BaseTask<QueryCompletionBarInfoRequest, QueryCompletionBarInfoResponse> {

	private final String TASKID = "24_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public QueryCompletionBarInfoTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new QueryCompletionBarInfoRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new QueryCompletionBarInfoResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_COMPLETION_BAR_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
