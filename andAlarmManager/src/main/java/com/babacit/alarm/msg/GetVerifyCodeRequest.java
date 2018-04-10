package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class GetVerifyCodeRequest extends BaseRequest {
	/** 手机号码 */
	private String phoneNo;
	/**
	 * 类型。 0：正常注册流程，1：忘记密码， 2：修改绑定手机号码
	 */
	private int verifyType;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public int getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(int verifyType) {
		this.verifyType = verifyType;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json != null) {
			try {
				if (!TextUtils.isEmpty(phoneNo)) {
					json.put("phoneNo", phoneNo);
				}
				json.put("verifyType", verifyType);
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
		builder.append(verifyType);
		return DesUtils.MD5(builder.toString());
	}
}
