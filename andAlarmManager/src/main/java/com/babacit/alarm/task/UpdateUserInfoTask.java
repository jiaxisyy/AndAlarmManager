package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.QueryUserInfoResponse;
import com.babacit.alarm.msg.UpdateUserInfoRequest;
import com.babacit.alarm.msg.UserInfoMsg;

public class UpdateUserInfoTask extends
		BaseTask<UpdateUserInfoRequest, BaseResponse> {

	private final String TASKID = "5" + hashCode();

	/**
	 * 类型 1：初始设置；2：修改
	 */
	private int mType;
	private UserInfoMsg mUserInfoMsg;

	public UpdateUserInfoTask(int type, UserInfoMsg userInfoMsg) {
		mUserInfoMsg = userInfoMsg;
		mType = type;
	}

	@Override
	public void run() {
		request = new UpdateUserInfoRequest();
		request.setType(mType);
		request.setUserInfo(mUserInfoMsg);
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
		return Constant.UPDATE_USER_INFO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
