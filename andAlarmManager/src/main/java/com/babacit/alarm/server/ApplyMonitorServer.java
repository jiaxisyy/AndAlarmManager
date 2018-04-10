package com.babacit.alarm.server;
//
//public class ApplyMonitorServer {
//
//	private RequestCallBack mCallBack;
//
//	public void start(String userId, String alarmCode, RequestCallBack callBack) {
//		mCallBack = callBack;
//		ApplyMonitorTask task = new ApplyMonitorTask(userId, alarmCode);
//		task.setCallBack(requestCallBack);
//		TaskManager.getInstance().exec(task);
//	}
//
//	private RequestCallBack requestCallBack = new RequestCallBack() {
//
//		@Override
//		public void onSuccess(Object obj) {
//			if (obj != null && obj instanceof ApplyMonitorResponse) {
//				ApplyMonitorResponse response = (ApplyMonitorResponse) obj;
//				if (mCallBack != null)
//					mCallBack.onSuccess(response.getUrl());
//			}
//		}
//
//		@Override
//		public void onFail(Object object, int errCode) {
//			if (mCallBack != null) {
//				mCallBack.onFail(object, errCode);
//			}
//		}
//	};
//}
