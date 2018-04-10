package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryUserInfoRequest;
import com.babacit.alarm.msg.QueryUserInfoResponse;

public class QueryUserInfoTask extends
		BaseTask<QueryUserInfoRequest, QueryUserInfoResponse> {

	private final String TASKID = "4" + hashCode();

	/** 手机号码 */
	private String mUserId;

	public QueryUserInfoTask(String userId) {
		mUserId = userId;
	}

	@Override
	public void run() {
		request = new QueryUserInfoRequest();
		request.setUserId(mUserId);
		response = new QueryUserInfoResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_USER_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
