package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryRewardInfoListRequest;
import com.babacit.alarm.msg.QueryRewardInfoListResponse;

public class QueryRewardInfoListTask extends
		BaseTask<QueryRewardInfoListRequest, QueryRewardInfoListResponse> {

	private final String TASKID = "21_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;

	public QueryRewardInfoListTask(String userId, String alarmCode) {
		mUserId = userId;
		mAlarmCode = alarmCode;
	}

	@Override
	public void run() {
		request = new QueryRewardInfoListRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		response = new QueryRewardInfoListResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_REWARD_LIST_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
