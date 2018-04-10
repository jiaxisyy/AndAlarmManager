package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.SendVoiceMsgRequest;
import com.babacit.alarm.msg.SendVoiceMsgResponse;

public class SendVoiceMsgTask extends
		BaseTask<SendVoiceMsgRequest, SendVoiceMsgResponse> {

	private final String TASKID = "29_" + hashCode();

	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 消息url */
	private String mMessageUrl;
	/** 语音时长，单位：秒 */
	private int mVoiceTime;

	public SendVoiceMsgTask(String userId, String alarmCode, String messageUrl,
			int voiceTime) {
		mUserId = userId;
		mAlarmCode = alarmCode;
		mMessageUrl = messageUrl;
		mVoiceTime = voiceTime;
	}

	@Override
	public void run() {
		request = new SendVoiceMsgRequest();
		request.setUserId(mUserId);
		request.setAlarmCode(mAlarmCode);
		request.setMessageUrl(mMessageUrl);
		request.setVoiceTime(mVoiceTime);
		response = new SendVoiceMsgResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.SEND_VOICE_MSG_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
