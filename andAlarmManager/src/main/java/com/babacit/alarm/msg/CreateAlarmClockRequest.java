package com.babacit.alarm.msg;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class CreateAlarmClockRequest extends BaseRequest {
	/** 用户唯一标示 */
	private String userId;
	/** 闹钟的序列号 */
	private String alarmCode;
	/** 闹钟列表 */
	private List<AlarmClockMsg> alarmClockList;

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

	public List<AlarmClockMsg> getAlarmClockList() {
		return alarmClockList;
	}

	public void setAlarmClockList(List<AlarmClockMsg> alarmClockList) {
		this.alarmClockList = alarmClockList;
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
				if (alarmClockList != null && alarmClockList.size() > 0) {
					JSONArray alarmClockJsons = new JSONArray();
					for (AlarmClockMsg alarmClockEntity : alarmClockList) {
						alarmClockJsons.put(alarmClockEntity.toJson(null));
					}
					json.put("alarmClockList", alarmClockJsons);
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
		return DesUtils.MD5(builder.toString());
	}
}
