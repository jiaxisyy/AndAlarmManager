package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class UpdateAlarmStatusRequest extends BaseRequest {
	/** 用户唯一标示 */
	private String userId;
	/** 闹钟的序列号 */
	private String alarmCode;
	/**
	 * 终端触屏状态 0：不可用；1：可用
	 */
	private int touchScreenStatus;
	/**
	 * 整点报时状态 0：不可用；1：可用
	 */
	private int reportStatus;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public int getTouchScreenStatus() {
		return touchScreenStatus;
	}

	public void setTouchScreenStatus(int touchScreenStatus) {
		this.touchScreenStatus = touchScreenStatus;
	}

	public int getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(int reportStatus) {
		this.reportStatus = reportStatus;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json != null) {
			try {
				if (!TextUtils.isEmpty(userId)) {
					json.put("userId", userId);
				}
				if (!TextUtils.isEmpty(alarmCode)) {
					json.put("alarmCode", alarmCode);
				}
				json.put("touchScreenStatus", touchScreenStatus);
				json.put("reportStatus", reportStatus);
				json.put("sign", getSign());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return super.toJson(json);
	}

	@Override
	protected String getSign() {
		final StringBuilder builder = new StringBuilder();
		builder.append(baseInfo.getDeviceCode());
		builder.append(baseInfo.getAppPackageName());
		builder.append(userId);
		return DesUtils.MD5(builder.toString());
	}
}
