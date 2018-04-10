package com.babacit.alarm.server;

import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.entity.Message;
import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.MessageMsg;
import com.babacit.alarm.msg.QueryVoiceMsgResponse;
import com.babacit.alarm.msg.UserInfoMsg;
import com.babacit.alarm.task.QueryVoiceMsgTask;
import com.babacit.alarm.task.TaskManager;

public class QueryVoiceMsgServer {

	private RequestCallBack mCallBack;

	public void start(String userId, String alarmCode, int messageId,
			RequestCallBack callBack) {
		mCallBack = callBack;
		QueryVoiceMsgTask task = new QueryVoiceMsgTask(userId, alarmCode,
				messageId);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj != null && obj instanceof QueryVoiceMsgResponse) {
				QueryVoiceMsgResponse response = (QueryVoiceMsgResponse) obj;
				List<MessageMsg> messageMsgs = response.getMessageList();
				List<Message> messages = new ArrayList<Message>();
				if (messageMsgs != null && messageMsgs.size() > 0) {
					for (MessageMsg messageMsg : messageMsgs) {
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
						messages.add(message);
					}
				}
				if (mCallBack != null)
					mCallBack.onSuccess(messages);
			}

		}

		@Override
		public void onFail(Object object, int errCode) {
			if (mCallBack != null) {
				mCallBack.onFail(object, errCode);
			}
		}
	};
}
