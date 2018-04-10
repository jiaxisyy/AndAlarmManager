package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryClockHistoryListRequest;
import com.babacit.alarm.msg.QueryClockHistoryListResponse;

public class QueryClockHistoryListTask extends
		BaseTask<QueryClockHistoryListRequest, QueryClockHistoryListResponse> {

	private final String TASKID = "14" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public QueryClockHistoryListTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new QueryClockHistoryListRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new QueryClockHistoryListResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_HISTORY_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
