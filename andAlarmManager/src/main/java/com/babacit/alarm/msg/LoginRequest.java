package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class LoginRequest extends BaseRequest {
	/** 手机号码 */
	private String phoneNo;
	/** 密码（md5） */
	private String pwd;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json != null) {
			try {
				if (!TextUtils.isEmpty(phoneNo)) {
					json.put("phoneNo", phoneNo);
				}
				json.put("pwd", pwd);
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
		builder.append(baseInfo.getDeviceCode());
		builder.append(baseInfo.getAppPackageName());
		builder.append(phoneNo);
		builder.append(pwd);
		return DesUtils.MD5(builder.toString());
	}
}
