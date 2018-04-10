package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.PwdSettingRequest;
import com.babacit.alarm.utils.DesUtils;

public class PwdSettingTask extends BaseTask<PwdSettingRequest, BaseResponse> {
	/** 手机号码 */
	private String mPhoneNo;
	/** 激活码 */
	private String mVerifyCode;
	/** 密码（md5） */
	private String mPwd;
	/** 原始密码（md5），修改密码时有效 */
	private String mOldPwd;
	/** 1：设置密码，2：修改密码，3：忘记密码 */
	private int mType;
	private final String TASKID = "2_" + hashCode();

	public PwdSettingTask(String phoneNo, String verifyCode, String pwd,
			String oldPwd, int type) {
		mPhoneNo = phoneNo;
		mVerifyCode = verifyCode;
		mPwd = pwd;
		mOldPwd = oldPwd;
		mType = type;
	}

	@Override
	public void run() {
		request = new PwdSettingRequest();
		request.setPhoneNo(mPhoneNo);
		request.setVerifyCode(mVerifyCode);
		request.setPwd(DesUtils.MD5(mPwd));
		request.setOldPwd(DesUtils.MD5(mOldPwd));
		request.setType(mType);
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
		return Constant.PWD_SETTING_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
