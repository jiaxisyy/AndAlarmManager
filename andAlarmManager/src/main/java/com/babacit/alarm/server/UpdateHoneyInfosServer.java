package com.babacit.alarm.server;

import com.babacit.alarm.entity.HoneyInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.HoneyInfoMsg;
import com.babacit.alarm.msg.UpdateHoneyInfoResponse;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UpdateHoneyInfoTask;

public class UpdateHoneyInfosServer {

	private RequestCallBack mCallBack;

	public void start(String userId, HoneyInfo honeyInfo,
			RequestCallBack callBack) {
		mCallBack = callBack;
		HoneyInfoMsg honeyInfoMsg = new HoneyInfoMsg();
		honeyInfoMsg.setAlarmCode(honeyInfo.getAlarmCode());
		honeyInfoMsg.setName(honeyInfo.getName());
		honeyInfoMsg.setNickName(honeyInfo.getNickName());
		honeyInfoMsg.setImgUrl(honeyInfo.getImgUrl());
		honeyInfoMsg.setSex(honeyInfo.getGender());
		honeyInfoMsg.setBirthday(honeyInfo.getBirthday());
		honeyInfoMsg.setSchoolName(honeyInfo.getSchoolName());
		honeyInfoMsg.setGradeName(honeyInfo.getGradeName());
		honeyInfoMsg.setRoleName(honeyInfo.getRoleName());
		honeyInfoMsg.setInterest(honeyInfo.getInterest());
		UpdateHoneyInfoTask task = new UpdateHoneyInfoTask(userId, honeyInfoMsg);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj instanceof UpdateHoneyInfoResponse) {
				UpdateHoneyInfoResponse response = (UpdateHoneyInfoResponse) obj;
				HoneyInfoMsg honeyInfoMsg = response.getHoneyInfo();
				HoneyInfo honeyInfo = new HoneyInfo();
				honeyInfo.setAlarmCode(honeyInfoMsg.getAlarmCode());
				honeyInfo.setName(honeyInfoMsg.getName());
				honeyInfo.setNickName(honeyInfoMsg.getNickName());
				honeyInfo.setImgUrl(honeyInfoMsg.getImgUrl());
				honeyInfo.setGender(honeyInfoMsg.getSex());
				honeyInfo.setBirthday(honeyInfoMsg.getBirthday());
				honeyInfo.setSchoolName(honeyInfoMsg.getSchoolName());
				honeyInfo.setGradeName(honeyInfoMsg.getGradeName());
				honeyInfo.setRoleName(honeyInfoMsg.getRoleName());
				honeyInfo.setInterest(honeyInfoMsg.getInterest());
				if (mCallBack != null)
					mCallBack.onSuccess(honeyInfo);
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
