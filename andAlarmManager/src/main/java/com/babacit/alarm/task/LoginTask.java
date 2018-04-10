package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.msg.LoginRequest;
import com.babacit.alarm.msg.LoginResponse;
import com.babacit.alarm.utils.DesUtils;

public class LoginTask extends BaseTask<LoginRequest, LoginResponse> {

	private final String TASKID = "3_" + hashCode();

	/** 手机号码 */
	private String mPhoneNo;
	/**
	 * 密码
	 */
	private String mPwd;

	public LoginTask(String phoneNo, String pwd) {
		mPhoneNo = phoneNo;
		mPwd = pwd;
	}

	@Override
	public void run() {
		request = new LoginRequest();
		request.setPhoneNo(mPhoneNo);
		request.setPwd(DesUtils.MD5(mPwd));
		response = new LoginResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.LOGIN_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
