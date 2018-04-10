package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryAlarmClockListResponse extends BaseResponse {
	/** 闹钟列表 */
	private List<AlarmClockMsg> alarmClockList;

	public List<AlarmClockMsg> getAlarmClockList() {
		return alarmClockList;
	}

	public void setAlarmClockList(List<AlarmClockMsg> alarmClockList) {
		this.alarmClockList = alarmClockList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("alarmClockList")) {
					JSONArray alarmClockJsons = json
							.getJSONArray("alarmClockList");
					if (alarmClockJsons != null && alarmClockJsons.length() > 0) {
						alarmClockList = new ArrayList<AlarmClockMsg>();
						for (int i = 0; i < alarmClockJsons.length(); i++) {
							JSONObject alarmClockJson = alarmClockJsons
									.getJSONObject(i);
							if (alarmClockJson != null) {
								AlarmClockMsg alarmClock = new AlarmClockMsg();
								alarmClock.parseJson(alarmClockJson);
								alarmClockList.add(alarmClock);
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
