package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateHoneyInfoResponse extends BaseResponse {
	/**
	 * 更新后的信息， 可能更新失败而返回原始信息
	 */
	private HoneyInfoMsg honeyInfo;

	public HoneyInfoMsg getHoneyInfo() {
		return honeyInfo;
	}

	public void setHoneyInfo(HoneyInfoMsg honeyInfo) {
		this.honeyInfo = honeyInfo;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("honeyInfo")) {
					JSONObject honeyInfoJson = json.getJSONObject("honeyInfo");
					if (honeyInfoJson != null) {
						honeyInfo = new HoneyInfoMsg();
						honeyInfo.parseJson(honeyInfoJson);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
