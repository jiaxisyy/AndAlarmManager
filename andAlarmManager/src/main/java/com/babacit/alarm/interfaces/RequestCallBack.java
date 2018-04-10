package com.babacit.alarm.interfaces;

public interface RequestCallBack {
	public void onSuccess(Object obj);

	public void onFail(Object object, int errCode);
}
