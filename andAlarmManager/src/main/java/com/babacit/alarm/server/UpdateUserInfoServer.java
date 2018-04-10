package com.babacit.alarm.server;

import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.UserInfoMsg;
import com.babacit.alarm.task.TaskManager;
import com.babacit.alarm.task.UpdateUserInfoTask;

public class UpdateUserInfoServer {

	private RequestCallBack mCallBack;

	/**
	 * 
	 * @param type
	 *            类型 1：初始设置；2：修改
	 * @param userInfo
	 *            用户信息
	 * @param callBack
	 */
	public void start(int type, UserInfo userInfo, RequestCallBack callBack) {
		mCallBack = callBack;
		UserInfoMsg userInfoMsg = new UserInfoMsg();
		userInfoMsg.setUserId(userInfo.getUserId());
		userInfoMsg.setNickName(userInfo.getNickName());
		userInfoMsg.setImgUrl(userInfo.getImgUrl());
		userInfoMsg.setName(userInfo.getName());
		userInfoMsg.setRoleInfo(userInfo.getRoleInfo());
		userInfoMsg.setRoleType(userInfo.getRoleType());
		userInfoMsg.setBirthday(userInfo.getBirthday());
		userInfoMsg.setPhoneNo(userInfo.getPhoneNo());
		userInfoMsg.setJobType(userInfo.getJobType());
		UpdateUserInfoTask task = new UpdateUserInfoTask(type, userInfoMsg);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (mCallBack != null)
				mCallBack.onSuccess(null);
		}

		@Override
		public void onFail(Object object, int errCode) {
			if (mCallBack != null) {
				mCallBack.onFail(object, errCode);
			}
		}
	};
}
