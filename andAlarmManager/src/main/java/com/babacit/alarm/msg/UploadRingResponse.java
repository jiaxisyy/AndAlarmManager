package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadRingResponse extends BaseResponse {
	/**
	 * 更新后的信息， 可能更新失败而返回原始信息
	 */
	private RingMsg ring;

	public RingMsg getRing() {
		return ring;
	}

	public void setRing(RingMsg ring) {
		this.ring = ring;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("ring")) {
					JSONObject ringJson = json.getJSONObject("ring");
					if (ringJson != null) {
						ring = new RingMsg();
						ring.parseJson(ringJson);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}
