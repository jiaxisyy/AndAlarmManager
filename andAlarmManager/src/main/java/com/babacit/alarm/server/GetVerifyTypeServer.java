package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.GetVerifyCodeTask;
import com.babacit.alarm.task.TaskManager;

public class GetVerifyTypeServer {

	private RequestCallBack mCallBack;

	/**
	 * 
	 * @param phoneNo
	 *            手机号码
	 * @param verifyType
	 *            类型。 0：正常注册流程，1：忘记密码， 2：修改绑定手机号码
	 * @param callBack
	 */
	public void start(String phoneNo, int verifyType, RequestCallBack callBack) {
		mCallBack = callBack;
		GetVerifyCodeTask task = new GetVerifyCodeTask(phoneNo, verifyType);
		task.setCallBack(requestCallBack);
		TaskManager.getInstance().exec(task);
	}

	private RequestCallBack requestCallBack = new RequestCallBack() {

		@Override
		public void onSuccess(Object obj) {
			if (mCallBack != null) {
				mCallBack.onSuccess(null);
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
