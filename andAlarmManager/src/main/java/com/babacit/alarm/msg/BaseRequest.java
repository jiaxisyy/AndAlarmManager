package com.babacit.alarm.msg;

import org.json.JSONObject;

import com.babacit.alarm.profile.ProfileTool;

public abstract class BaseRequest extends BaseMsg {
	protected BaseInfo baseInfo;

	public BaseRequest() {
		ProfileTool profileTool = ProfileTool.getInstance();
		baseInfo = profileTool.getBaseInfo();
	}

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json == null)
			json = new JSONObject();
		if (baseInfo != null) {
			json = baseInfo.toJson(json);
		}
		return json;
	}

	protected abstract String getSign();

}
