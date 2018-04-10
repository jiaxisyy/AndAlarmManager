package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

public class UpdateUserInfoRequest extends BaseRequest {
	/** 用户信息 */
	private UserInfoMsg userInfo;
	/**
	 * 类型 1：初始设置；2：修改
	 */
	private int type;

	public UserInfoMsg getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfoMsg userInfo) {
		this.userInfo = userInfo;
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
				if (userInfo != null) {
					json.put("user", userInfo.toJson(null));
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
		if (userInfo != null)
			builder.append(userInfo.getUserId());
		return DesUtils.MD5(builder.toString());
	}
}
