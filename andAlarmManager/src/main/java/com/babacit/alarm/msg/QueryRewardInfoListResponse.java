package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryRewardInfoListResponse extends BaseResponse {
	/** 闹钟列表 */
	private List<RewardInfoMsg> rewardInfoList;

	public List<RewardInfoMsg> getRewardInfoList() {
		return rewardInfoList;
	}

	public void setRewardInfoList(List<RewardInfoMsg> rewardInfoList) {
		this.rewardInfoList = rewardInfoList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("rewardInfoList")) {
					JSONArray rewardInfoJsons = json
							.getJSONArray("rewardInfoList");
					if (rewardInfoJsons != null && rewardInfoJsons.length() > 0) {
						rewardInfoList = new ArrayList<RewardInfoMsg>();
						for (int i = 0; i < rewardInfoJsons.length(); i++) {
							JSONObject rewardInfoJson = rewardInfoJsons
									.getJSONObject(i);
							if (rewardInfoJson != null) {
								RewardInfoMsg rewardInfo = new RewardInfoMsg();
								rewardInfo.parseJson(rewardInfoJson);
								rewardInfoList.add(rewardInfo);
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
