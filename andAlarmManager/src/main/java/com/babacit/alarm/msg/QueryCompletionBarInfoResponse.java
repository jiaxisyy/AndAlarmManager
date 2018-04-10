package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryCompletionBarInfoResponse extends BaseResponse {
	/** 本年每月的完成情况 */
	private List<CompletionInfoMsg> monthCompletionInfoList;
	/** 本月每周的完成情况 */
	private List<CompletionInfoMsg> weekCompletionInfoList;
	/** 本周每天的完成情况 */
	private List<DayCompletionInfoMsg> dayCompletionInfoList;

	public List<CompletionInfoMsg> getMonthCompletionInfoList() {
		return monthCompletionInfoList;
	}

	public void setMonthCompletionInfoList(
			List<CompletionInfoMsg> monthCompletionInfoList) {
		this.monthCompletionInfoList = monthCompletionInfoList;
	}

	public List<CompletionInfoMsg> getWeekCompletionInfoList() {
		return weekCompletionInfoList;
	}

	public void setWeekCompletionInfoList(
			List<CompletionInfoMsg> weekCompletionInfoList) {
		this.weekCompletionInfoList = weekCompletionInfoList;
	}

	public List<DayCompletionInfoMsg> getDayCompletionInfoList() {
		return dayCompletionInfoList;
	}

	public void setDayCompletionInfoList(
			List<DayCompletionInfoMsg> dayCompletionInfoList) {
		this.dayCompletionInfoList = dayCompletionInfoList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("monthCompletionInfoList")) {
					JSONArray completionInfoJsons = json
							.getJSONArray("monthCompletionInfoList");
					if (completionInfoJsons != null
							&& completionInfoJsons.length() > 0) {
						monthCompletionInfoList = new ArrayList<CompletionInfoMsg>();
						for (int i = 0; i < completionInfoJsons.length(); i++) {
							JSONObject completionInfoJson = completionInfoJsons
									.getJSONObject(i);
							if (completionInfoJson != null) {
								CompletionInfoMsg completionInfo = new CompletionInfoMsg();
								completionInfo.parseJson(completionInfoJson);
								monthCompletionInfoList.add(completionInfo);
							}
						}
					}
				}
				if (json.has("weekCompletionInfoList")) {
					JSONArray completionInfoJsons = json
							.getJSONArray("weekCompletionInfoList");
					if (completionInfoJsons != null
							&& completionInfoJsons.length() > 0) {
						weekCompletionInfoList = new ArrayList<CompletionInfoMsg>();
						for (int i = 0; i < completionInfoJsons.length(); i++) {
							JSONObject completionInfoJson = completionInfoJsons
									.getJSONObject(i);
							if (completionInfoJson != null) {
								CompletionInfoMsg completionInfo = new CompletionInfoMsg();
								completionInfo.parseJson(completionInfoJson);
								weekCompletionInfoList.add(completionInfo);
							}
						}
					}
				}
				if (json.has("dayCompletionInfoList")) {
					JSONArray completionInfoJsons = json
							.getJSONArray("dayCompletionInfoList");
					if (completionInfoJsons != null
							&& completionInfoJsons.length() > 0) {
						dayCompletionInfoList = new ArrayList<DayCompletionInfoMsg>();
						for (int i = 0; i < completionInfoJsons.length(); i++) {
							JSONObject completionInfoJson = completionInfoJsons
									.getJSONObject(i);
							if (completionInfoJson != null) {
								DayCompletionInfoMsg completionInfo = new DayCompletionInfoMsg();
								completionInfo.parseJson(completionInfoJson);
								dayCompletionInfoList.add(completionInfo);
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
