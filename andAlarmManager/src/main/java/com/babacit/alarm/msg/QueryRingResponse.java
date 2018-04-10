package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryRingResponse extends BaseResponse {
	/** 闹钟列表 */
	private List<RingMsg> ringList;
	/** 最后更新时间 2015-10-19 10:00:00 */
	private String lastTime;

	public List<RingMsg> getRingList() {
		return ringList;
	}

	public void setRingList(List<RingMsg> ringList) {
		this.ringList = ringList;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("ringList")) {
					JSONArray ringJsons = json.getJSONArray("ringList");
					if (ringJsons != null && ringJsons.length() > 0) {
						ringList = new ArrayList<RingMsg>();
						for (int i = 0; i < ringJsons.length(); i++) {
							JSONObject ringJson = ringJsons.getJSONObject(i);
							if (ringJson != null) {
								RingMsg ring = new RingMsg();
								ring.parseJson(ringJson);
								ringList.add(ring);
							}
						}
					}
				}
				if (json.has("lastTime")) {
					lastTime = json.getString("lastTime");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
