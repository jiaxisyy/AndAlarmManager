package com.babacit.alarm.server;

import com.babacit.alarm.entity.Message;
import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.MessageMsg;
import com.babacit.alarm.msg.SendVoiceMsgResponse;
import com.babacit.alarm.msg.UserInfoMsg;
import com.babacit.alarm.task.SendVoiceMsgTask;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.utils.Utils;

public class SendVoiceMsgServer {

	private RequestCallBack mCallBack;
	/** 0上传文件，1，发送消息 */
	private int mStep = 0;
	/** 手机号码 */
	private String mUserId;

	/** 闹钟的序列号 */
	private String mAlarmCode;
	/** 语音时长，单位：秒 */
	private int mVoiceTime;

	public void start(String userId, String alarmCode, String filePath,
			int voiceTime, RequestCallBack callBack) {
		mCallBack = callBack;
		mUserId = userId;
		mAlarmCode = alarmCode;
		mVoiceTime = voiceTime;
		mStep = 0;
		byte[] bs = Utils.fileToByteArray(filePath);
		if (bs != null) {
			new UploadFileServer().start(Utils.getSuffixName(filePath), bs,
					requestCallBack);
		} else {
			new Thread() {
				public void run() {
					mCallBack.onFail(null, ErrCode.ERROR);
				};
			}.start();
		}
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (mStep == 0) {
				mStep = 1;
				String url = (String) obj;
				SendVoiceMsgTask task = new SendVoiceMsgTask(mUserId,
						mAlarmCode, url, mVoiceTime);
				task.setCallBack(requestCallBack);
				TaskManager.getInstance().exec(task);
			} else {
				SendVoiceMsgResponse response = (SendVoiceMsgResponse) obj;
				MessageMsg messageMsg = response.getMsg();
				if (messageMsg != null) {
					Message message = new Message();
					message.setCreateTime(messageMsg.getCreateTime());
					message.setId(messageMsg.getId());
					message.setMessageUrl(messageMsg.getMessageUrl());
					message.setVoiceTime(messageMsg.getVoiceTime());
					UserInfoMsg userInfoMsg = messageMsg.getUser();
					if (userInfoMsg != null) {
						UserInfo userInfo = new UserInfo();
						userInfo.setUserId(userInfoMsg.getUserId());
						userInfo.setNickName(userInfoMsg.getNickName());
						userInfo.setImgUrl(userInfoMsg.getImgUrl());
						userInfo.setName(userInfoMsg.getName());
						userInfo.setRoleInfo(userInfoMsg.getRoleInfo());
						userInfo.setRoleType(userInfoMsg.getRoleType());
						userInfo.setBirthday(userInfoMsg.getBirthday());
						userInfo.setPhoneNo(userInfoMsg.getPhoneNo());
						userInfo.setJobType(userInfoMsg.getJobType());
						message.setUser(userInfo);
					}
					if (mCallBack != null)
						mCallBack.onSuccess(message);
				} else {
					if (mCallBack != null)
						mCallBack.onSuccess(null);
				}
			}

		}

		@Override
		public void onFail(Object object, int errCode) {
			if (mStep == 0) {
				if (mCallBack != null) {
					mCallBack.onFail(object, errCode);
				}
			} else {
				if (mCallBack != null) {
					mCallBack.onFail(object, errCode);
				}
			}
		}
	};
}
