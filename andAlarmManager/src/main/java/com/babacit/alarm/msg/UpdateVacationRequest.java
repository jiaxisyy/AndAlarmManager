package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class UpdateVacationRequest extends BaseRequest {
	/** 用户唯一标示 */
	private String userId;
	/** 闹钟的序列号 */
	private String alarmCode;
	/** 开始时间 */
	private String startTime;
	/** 截止时间 */
	private String endTime;
	/**
	 * 类型 0：暑假，1：寒假
	 */
	private int type;

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
				if (!TextUtils.isEmpty(startTime)) {
					json.put("startTime", startTime);
				}
				if (!TextUtils.isEmpty(endTime)) {
					json.put("endTime", endTime);
				}
				json.put("type", type);
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
