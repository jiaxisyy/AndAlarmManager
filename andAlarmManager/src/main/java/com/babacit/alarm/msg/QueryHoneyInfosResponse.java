package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryHoneyInfosResponse extends BaseResponse {
	/** 宝贝信息列表 */
	private List<HoneyInfoMsg> honeyInfoList;

	public List<HoneyInfoMsg> getHoneyInfoList() {
		return honeyInfoList;
	}

	public void setHoneyInfoList(List<HoneyInfoMsg> honeyInfoList) {
		this.honeyInfoList = honeyInfoList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("honeyInfoList")) {
					JSONArray honeyInfoJsons = json
							.getJSONArray("honeyInfoList");
					if (honeyInfoJsons != null && honeyInfoJsons.length() > 0) {
						honeyInfoList = new ArrayList<HoneyInfoMsg>();
						for (int i = 0; i < honeyInfoJsons.length(); i++) {
							JSONObject honeyInfoJson = honeyInfoJsons
									.getJSONObject(i);
							if (honeyInfoJson != null) {
								HoneyInfoMsg honeyInfo = new HoneyInfoMsg();
								honeyInfo.parseJson(honeyInfoJson);
								honeyInfoList.add(honeyInfo);
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
