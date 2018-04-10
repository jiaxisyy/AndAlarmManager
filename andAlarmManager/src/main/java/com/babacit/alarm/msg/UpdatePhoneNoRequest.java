package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.utils.DesUtils;

import android.text.TextUtils;

public class UpdatePhoneNoRequest extends BaseRequest {
	/**用户唯一标示*/
	private String userId;
	/** 原始手机号码 */
	private String oldPhoneNo;
	/** 密码（md5） */
	private String pwd;
	/** 新手机号码 */
	private String newPhoneNo;
	/** 新手机号码接收到的激活码 */
	private String verifyCode;
	/** 1：设置密码，2：修改密码，3：忘记密码 */
	private int type;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOldPhoneNo() {
		return oldPhoneNo;
	}

	public void setOldPhoneNo(String oldPhoneNo) {
		this.oldPhoneNo = oldPhoneNo;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNewPhoneNo() {
		return newPhoneNo;
	}

	public void setNewPhoneNo(String newPhoneNo) {
		this.newPhoneNo = newPhoneNo;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
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
				if (!TextUtils.isEmpty(userId)) {
					json.put("userId", userId);
				}
				if (!TextUtils.isEmpty(oldPhoneNo)) {
					json.put("oldPhoneNo", oldPhoneNo);
				}
				if (!TextUtils.isEmpty(pwd)) {
					json.put("pwd", pwd);
				}
				if (!TextUtils.isEmpty(newPhoneNo)) {
					json.put("newPhoneNo", newPhoneNo);
				}
				if (!TextUtils.isEmpty(verifyCode)) {
					json.put("verifyCode", verifyCode);
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
		builder.append(baseInfo.getDeviceCode());
		builder.append(baseInfo.getAppPackageName());
		builder.append(userId);
		builder.append(verifyCode);
		return DesUtils.MD5(builder.toString());
	}
}
