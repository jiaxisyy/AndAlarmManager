package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponse extends BaseResponse {
	/** 用户id */
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("userId"))
					userId = json.getString("userId");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
