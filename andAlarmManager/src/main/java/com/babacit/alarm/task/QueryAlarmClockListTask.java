package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryAlarmClockListRequest;
import com.babacit.alarm.msg.QueryAlarmClockListResponse;

public class QueryAlarmClockListTask extends
		BaseTask<QueryAlarmClockListRequest, QueryAlarmClockListResponse> {

	private final String TASKID = "11" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public QueryAlarmClockListTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new QueryAlarmClockListRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new QueryAlarmClockListResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_ALARM_LIST_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
