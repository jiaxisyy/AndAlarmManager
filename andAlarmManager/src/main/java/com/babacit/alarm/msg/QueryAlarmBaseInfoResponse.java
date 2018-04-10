package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class QueryAlarmBaseInfoResponse extends BaseResponse {
	/** 宝贝信息列表 */
	private AlarmBaseInfoMsg alarmBaseInfo;

	public AlarmBaseInfoMsg getAlarmBaseInfo() {
		return alarmBaseInfo;
	}

	public void setAlarmBaseInfo(AlarmBaseInfoMsg alarmBaseInfo) {
		this.alarmBaseInfo = alarmBaseInfo;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("alarmBaseInfo")) {
					JSONObject alarmBaseInfoJson = json
							.getJSONObject("alarmBaseInfo");
					if (alarmBaseInfoJson != null) {
						alarmBaseInfo = new AlarmBaseInfoMsg();
						alarmBaseInfo.parseJson(alarmBaseInfoJson);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
