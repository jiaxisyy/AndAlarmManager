package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class DayCompletionInfoMsg extends BaseMsg {
	/** 完成数 */
	private int completeCount;
	/**
	 * 周几 0：周一，1：周二……
	 */
	private int day;

	public int getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(int completeCount) {
		this.completeCount = completeCount;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("completeCount")) {
					completeCount = json.getInt("completeCount");
				}
				if (json.has("day")) {
					day = json.getInt("day");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
