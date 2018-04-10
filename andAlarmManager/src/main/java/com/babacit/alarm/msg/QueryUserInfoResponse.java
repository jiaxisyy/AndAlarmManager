package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class QueryUserInfoResponse extends BaseResponse {
	/** 用户id */
	private UserInfoMsg userInfo;

	public UserInfoMsg getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfoMsg userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("user")) {
					JSONObject userInfoJson = json.getJSONObject("user");
					if (userInfoJson != null) {
						userInfo = new UserInfoMsg();
						userInfo.parseJson(userInfoJson);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
