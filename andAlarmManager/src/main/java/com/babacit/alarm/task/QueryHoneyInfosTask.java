package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryHoneyInfosRequest;
import com.babacit.alarm.msg.QueryHoneyInfosResponse;

public class QueryHoneyInfosTask extends
		BaseTask<QueryHoneyInfosRequest, QueryHoneyInfosResponse> {

	private final String TASKID = "9" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public QueryHoneyInfosTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new QueryHoneyInfosRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new QueryHoneyInfosResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_HONEY_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
