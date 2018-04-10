package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryCompletionPieInfoResponse extends BaseResponse {
	/** 完成情况 */
	private List<CompletionInfoMsg> completionInfoList;

	public List<CompletionInfoMsg> getCompletionInfoList() {
		return completionInfoList;
	}

	public void setCompletionInfoList(List<CompletionInfoMsg> completionInfoList) {
		this.completionInfoList = completionInfoList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("completionInfoList")) {
					JSONArray completionInfoJsons = json
							.getJSONArray("completionInfoList");
					if (completionInfoJsons != null
							&& completionInfoJsons.length() > 0) {
						completionInfoList = new ArrayList<CompletionInfoMsg>();
						for (int i = 0; i < completionInfoJsons.length(); i++) {
							JSONObject completionInfoJson = completionInfoJsons
									.getJSONObject(i);
							if (completionInfoJson != null) {
								CompletionInfoMsg completionInfo = new CompletionInfoMsg();
								completionInfo.parseJson(completionInfoJson);
								completionInfoList.add(completionInfo);
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
