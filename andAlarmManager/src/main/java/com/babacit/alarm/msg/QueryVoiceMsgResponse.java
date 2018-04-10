package com.babacit.alarm.msg;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryVoiceMsgResponse extends BaseResponse {
	/** 闹钟列表 */
	private List<MessageMsg> messageList;

	public List<MessageMsg> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<MessageMsg> messageList) {
		this.messageList = messageList;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("messageList")) {
					JSONArray messageJsons = json.getJSONArray("messageList");
					if (messageJsons != null && messageJsons.length() > 0) {
						messageList = new ArrayList<MessageMsg>();
						for (int i = 0; i < messageJsons.length(); i++) {
							JSONObject messageJson = messageJsons
									.getJSONObject(i);
							if (messageJson != null) {
								MessageMsg rewardInfo = new MessageMsg();
								rewardInfo.parseJson(messageJson);
								messageList.add(rewardInfo);
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
