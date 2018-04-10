package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateAlarmClockResponse extends BaseResponse {
	/** 宝贝信息列表 */
	private List<String> clockIdList;

	public List<String> getClockIdList() {
		return clockIdList;
	}

	public void setClockIdList(List<String> clockIdList) {
		this.clockIdList = clockIdList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("clockIdList")) {
					JSONArray clockIdJsons = json.getJSONArray("clockIdList");
					if (clockIdJsons != null && clockIdJsons.length() > 0) {
						clockIdList = new ArrayList<String>();
						for (int i = 0; i < clockIdJsons.length(); i++) {
							String clockId = clockIdJsons.getString(i);
							if (clockId != null) {
								clockIdList.add(clockId);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
