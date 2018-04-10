package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class QueryRingRequest extends BaseRequest {
	/** 用户唯一标示 */
	private String userId;
	/** 闹钟的序列号 */
	private String alarmCode;
	/** 最后更新时间 2015-10-19 10:00:00 */
	private String lastTime;

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

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
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
				if (!TextUtils.isEmpty(lastTime)) {
					json.put("lastTime", lastTime);
				}
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
		builder.append(alarmCode);
		return DesUtils.MD5(builder.toString());
	}
}
