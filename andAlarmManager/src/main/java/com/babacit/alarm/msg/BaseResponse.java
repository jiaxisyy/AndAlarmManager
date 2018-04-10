package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseResponse extends BaseMsg {
	/** 错误码。0：成功，非0 ：失败；详情见错误码表 */
	private int code;
	/** 错误描述 */
	private String errMsg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("code")) {
					code = json.getInt("code");
				}
				if (json.has("errMsg")) {
					errMsg = json.getString("errMsg");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}
}