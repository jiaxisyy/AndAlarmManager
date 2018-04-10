package com.babacit.alarm.server;

import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.msg.QueryUserInfoResponse;
import com.babacit.alarm.msg.UserInfoMsg;
import com.babacit.alarm.task.QueryUserInfoTask;
import com.babacit.alarm.task.TaskManager;

public class QueryUserInfoServer {

	private RequestCallBack mCallBack;

	/**
	 * 
	 * @param userId
	 *            用户id
	 * @param callBack
	 */
	public void start(String userId, RequestCallBack callBack) {
		mCallBack = callBack;
		QueryUserInfoTask task = new QueryUserInfoTask(userId);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (obj instanceof QueryUserInfoResponse) {
				QueryUserInfoResponse response = (QueryUserInfoResponse) obj;
				if (response != null) {
					UserInfoMsg userInfoMsg = response.getUserInfo();
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
						if (mCallBack != null)
							mCallBack.onSuccess(userInfo);
					}
				}
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
