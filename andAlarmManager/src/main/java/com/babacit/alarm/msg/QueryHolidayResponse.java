package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryHolidayResponse extends BaseResponse {
	/** 节假日列表 */
	private List<HolidayMsg> holidayList;

	public List<HolidayMsg> getHolidayList() {
		return holidayList;
	}

	public void setHolidayList(List<HolidayMsg> holidayList) {
		this.holidayList = holidayList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("holidayList")) {
					JSONArray holidayJsons = json.getJSONArray("holidayList");
					if (holidayJsons != null && holidayJsons.length() > 0) {
						holidayList = new ArrayList<HolidayMsg>();
						for (int i = 0; i < holidayJsons.length(); i++) {
							JSONObject holidayJson = holidayJsons
									.getJSONObject(i);
							if (holidayJson != null) {
								HolidayMsg holiday = new HolidayMsg();
								holiday.parseJson(holidayJson);
								holidayList.add(holiday);
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
