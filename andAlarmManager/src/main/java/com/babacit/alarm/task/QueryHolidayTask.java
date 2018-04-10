package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryHolidayRequest;
import com.babacit.alarm.msg.QueryHolidayResponse;

public class QueryHolidayTask extends
		BaseTask<QueryHolidayRequest, QueryHolidayResponse> {

	private final String TASKID = "25_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public QueryHolidayTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new QueryHolidayRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new QueryHolidayResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_HOLIDAY_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
