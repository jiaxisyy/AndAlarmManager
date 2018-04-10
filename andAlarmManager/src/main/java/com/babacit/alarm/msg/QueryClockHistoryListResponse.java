package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryClockHistoryListResponse extends BaseResponse {
	/** 宝贝信息列表 */
	private List<ClockHistoryMsg> clockHistoryList;

	public List<ClockHistoryMsg> getClockHistoryList() {
		return clockHistoryList;
	}

	public void setClockHistoryList(List<ClockHistoryMsg> clockHistoryList) {
		this.clockHistoryList = clockHistoryList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("clockHistoryList")) {
					JSONArray clockHistoryJsons = json
							.getJSONArray("clockHistoryList");
					if (clockHistoryJsons != null
							&& clockHistoryJsons.length() > 0) {
						clockHistoryList = new ArrayList<ClockHistoryMsg>();
						for (int i = 0; i < clockHistoryJsons.length(); i++) {
							JSONObject clockHistoryJson = clockHistoryJsons
									.getJSONObject(i);
							if (clockHistoryJson != null) {
								ClockHistoryMsg clockHistory = new ClockHistoryMsg();
								clockHistory.parseJson(clockHistoryJson);
								clockHistoryList.add(clockHistory);
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
