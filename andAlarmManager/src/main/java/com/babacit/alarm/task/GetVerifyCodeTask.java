package com.babacit.alarm.task;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.err.ErrCode;
import com.babacit.alarm.msg.BaseResponse;
import com.babacit.alarm.msg.GetVerifyCodeRequest;

public class GetVerifyCodeTask extends
		BaseTask<GetVerifyCodeRequest, BaseResponse> {

	private final String TASKID = "1" + hashCode();

	/** 手机号码 */
	private String mPhoneNo;
	/**
	 * 类型。 0：正常注册流程，1：忘记密码， 2：修改绑定手机号码
	 */
	private int mVerifyType;

	public GetVerifyCodeTask(String phoneNo, int verifyType) {
		mPhoneNo = phoneNo;
		mVerifyType = verifyType;
	}

	@Override
	public void run() {
		request = new GetVerifyCodeRequest();
		request.setPhoneNo(mPhoneNo);
		request.setVerifyType(mVerifyType);
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
		return Constant.FETCH_CODE_URL;
	}

	@Override
	public String getTaskId() {
		return TASKID;
	}

}
