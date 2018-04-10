package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadMonitorStatusResponse extends BaseResponse {
	
	/** 监控时长，单位秒*/
	private int monitorTime;

	public int getMonitorTime() {
		return monitorTime;
	}

	public void setMonitorTime(int monitorTime) {
		this.monitorTime = monitorTime;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("monitorTime"))
					monitorTime = json.getInt("monitorTime");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
	
}
