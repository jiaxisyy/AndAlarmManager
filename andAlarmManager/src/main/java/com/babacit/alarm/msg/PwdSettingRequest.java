package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class PwdSettingRequest extends BaseRequest {
	/** 手机号码 */
	private String phoneNo;
	/** 激活码 */
	private String verifyCode;
	/** 密码（md5） */
	private String pwd;
	/** 原始密码（md5），修改密码时有效 */
	private String oldPwd;
	/** 1：设置密码，2：修改密码，3：忘记密码 */
	private int type;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json != null) {
			try {
				if (!TextUtils.isEmpty(phoneNo)) {
					json.put("phoneNo", phoneNo);
				}
				if (!TextUtils.isEmpty(verifyCode)) {
					json.put("verifyCode", verifyCode);
				}
				if (!TextUtils.isEmpty(pwd)) {
					json.put("pwd", pwd);
				}
				if (!TextUtils.isEmpty(oldPwd)) {
					json.put("oldPwd", oldPwd);
				}
				json.put("type", type);
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
