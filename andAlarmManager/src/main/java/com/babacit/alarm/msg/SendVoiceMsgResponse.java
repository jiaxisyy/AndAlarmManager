package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class SendVoiceMsgResponse extends BaseResponse {
	/** 闹钟列表 */
	private MessageMsg msg;

	public MessageMsg getMsg() {
		return msg;
	}

	public void setMsg(MessageMsg msg) {
		this.msg = msg;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("msg")) {
					JSONObject msgJson = json.getJSONObject("msg");
					msg = new MessageMsg();
					msg.parseJson(msgJson);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
