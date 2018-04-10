package com.babacit.alarm.server;

import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.task.PwdSettingTask;
import com.babacit.alarm.task.TaskManager;

public class PwdSettingServer {

	private RequestCallBack mCallBack;

	/**
	 * 
	 * @param phoneNo
	 *            手机号码
	 * @param verifyCode
	 *            激活码
	 * @param pwd
	 *            密码（md5）
	 * @param oldPwd
	 *            原始密码（md5），修改密码时有效
	 * @param type
	 *            1：设置密码，2：修改密码，3：忘记密码
	 * @param callBack
	 *            回调
	 */
	public void start(String phoneNo, String verifyCode, String pwd,
			String oldPwd, int type, RequestCallBack callBack) {
		mCallBack = callBack;
		PwdSettingTask task = new PwdSettingTask(phoneNo, verifyCode, pwd,
				oldPwd, type);
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
