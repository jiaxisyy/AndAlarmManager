package com.babacit.alarm.msg;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class RewardSettingRequest extends BaseRequest {
	/** 用户唯一标示 */
	private String userId;
	/** 闹钟的序列号 */
	private String alarmCode;
	/** 奖励信息 */
	private List<RewardInfoMsg> rewardInfoList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public List<RewardInfoMsg> getRewardInfoList() {
		return rewardInfoList;
	}

	public void setRewardInfoList(List<RewardInfoMsg> rewardInfoList) {
		this.rewardInfoList = rewardInfoList;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json != null) {
			try {
				if (!TextUtils.isEmpty(userId)) {
					json.put("userId", userId);
				}
				if (!TextUtils.isEmpty(alarmCode)) {
					json.put("alarmCode", alarmCode);
				}
				if (rewardInfoList != null && rewardInfoList.size() > 0) {
					JSONArray rewardInfoJsons = new JSONArray();
					for (RewardInfoMsg rewardInfo : rewardInfoList) {
						rewardInfoJsons.put(rewardInfo.toJson(null));
					}
					json.put("rewardInfoList", rewardInfoJsons);
				}
				json.put("sign", getSign());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return super.toJson(json);
	}

	@Override
	protected String getSign() {
		final StringBuilder builder = new StringBuilder();
		builder.append(baseInfo.getChannelCode());
		builder.append(userId);
		return DesUtils.MD5(builder.toString());
	}
}
