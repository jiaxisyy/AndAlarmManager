package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadSharedInfoResponse extends BaseResponse {
	/**
	 * 可监控次数
	 */
	private int number;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("number")) {
					number = json.getInt("number");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
