package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.QueryVoiceMsgRequest;
import com.babacit.alarm.msg.QueryVoiceMsgResponse;

public class QueryVoiceMsgTask extends
		BaseTask<QueryVoiceMsgRequest, QueryVoiceMsgResponse> {

	private final String TASKID = "30_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 上次查询消息Id 第一次查询可设置为0 */
	private int mMessageId;

	public QueryVoiceMsgTask(String userId, String alarmCode, int messageId) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mMessageId = messageId;
	}

	@Override
	public void run() {
		request = new QueryVoiceMsgRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setMessageId(mMessageId);
		response = new QueryVoiceMsgResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.QUERY_VOICE_MSG_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
