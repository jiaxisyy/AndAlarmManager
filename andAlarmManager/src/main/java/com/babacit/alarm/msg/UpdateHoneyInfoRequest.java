package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class UpdateHoneyInfoRequest extends BaseRequest {
	/** 用户唯一标示 */
	private String userId;
	/** 宝贝信息 */
	private HoneyInfoMsg honeyInfo;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public HoneyInfoMsg getHoneyInfo() {
		return honeyInfo;
	}

	public void setHoneyInfo(HoneyInfoMsg honeyInfo) {
		this.honeyInfo = honeyInfo;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json != null) {
			try {
				if (!TextUtils.isEmpty(userId)) {
					json.put("userId", userId);
				}
				if (honeyInfo != null) {
					json.put("honeyInfo", honeyInfo.toJson(null));
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
