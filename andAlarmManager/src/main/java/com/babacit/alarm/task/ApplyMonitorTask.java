package com.babacit.alarm.task;
//
//public class ApplyMonitorTask extends
//		BaseTask<ApplyMonitorRequest, ApplyMonitorResponse> {
//
//	private final String TASKID = "16_" + hashCode();
//
//	/** 手机号码 */
//	private String mUserId;
//
//	/** 闹钟的序列号 */
//	private String mAlarmCode;
//
//	public ApplyMonitorTask(String userId, String alarmCode) {
//		mUserId = userId;
//		mAlarmCode = alarmCode;
//	}
//
//	@Override
//	public void run() {
//		request = new ApplyMonitorRequest();
//		request.setUserId(mUserId);
//		request.setAlarmCode(mAlarmCode);
//		response = new ApplyMonitorResponse();
//		int errCode = exec();
//		if (errCode == ErrCode.SUCCESS) {
//			notifySuccess(response);
//		} else {
//			notifyFailed(null, errCode);
//		}
//	}
//
//	@Override
//	public String getUrl() {
//		return Constant.APPLY_MONITOR_URL;
//	}
//
//	@Override
//	public String getTaskId() {
//		return TASKID;
//	}
//
//}
