package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.UpdatePhoneNoRequest;
import com.babacit.alarm.utils.DesUtils;

public class UpdatePhoneNoTask extends
		BaseTask<UpdatePhoneNoRequest, BaseResponse> {

	private final String TASKID = "6_" + hashCode();
	/** 用户唯一标示 */
	private String mUserId;
	/** 原始手机号码 */
	private String mOldPhoneNo;
	/** 密码（md5单项加密） */
	private String mPwd;
	/** 新手机号码 */
	private String mNewPhoneNo;
	/** 新手机号码接收到的激活码 */
	private String mVerifyCode;

	public UpdatePhoneNoTask(String userId, String oldPhone, String pwd,
			String newPhoneNo, String verifyCode) {
		mUserId = userId;
		mOldPhoneNo = oldPhone;
		mPwd = pwd;
		mNewPhoneNo = newPhoneNo;
		mVerifyCode = verifyCode;
	}

	@Override
	public void run() {
		request = new UpdatePhoneNoRequest();
		request.setUserId(mUserId);
		request.setOldPhoneNo(mOldPhoneNo);
		request.setPwd(DesUtils.MD5(mPwd));
		request.setNewPhoneNo(mNewPhoneNo);
		request.setVerifyCode(mVerifyCode);
		response = new BaseResponse();
		int errCode = exec();
		if (errCode == ErrCode.SUCCESS) {
			notifySuccess(response);
		} else {
			notifyFailed(null, errCode);
		}
	}

	@Override
	public String getUrl() {
		return Constant.UPDATE_PHONE_NO_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
