package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class UserInfoMsg extends BaseMsg {
	/** 用户ID */
	private String userId;
	/** 昵称 */
	private String nickName;
	/** 头像地址 */
	private String imgUrl;
	/** 姓名 */
	private String name;
	/**
	 * 角色名称 爸爸、妈妈、爷爷、奶奶、自定义
	 */
	private String roleInfo;
	/**
	 * 角色id
	 */
	private int roleType;
	/**
	 * 生日 格式：yyyy-MM-dd
	 */
	private String birthday;
	/** 手机号码 */
	private String phoneNo;
	/** 职业 */
	private int jobType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(String roleInfo) {
		this.roleInfo = roleInfo;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public int getJobType() {
		return jobType;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("userId")) {
					userId = json.getString("userId");
				}
				if (json.has("nickName")) {
					nickName = json.getString("nickName");
				}
				if (json.has("imgUrl")) {
					imgUrl = json.getString("imgUrl");
				}
				if (json.has("name")) {
					name = json.getString("name");
				}
				if (json.has("roleInfo")) {
					roleInfo = json.getString("roleInfo");
				}
				if (json.has("roleType")) {
					roleType = json.getInt("roleType");
				}
				if (json.has("birthday")) {
					birthday = json.getString("birthday");
				}
				if (json.has("phoneNo")) {
					phoneNo = json.getString("phoneNo");
				}
				if (json.has("jobType")) {
					jobType = json.getInt("jobType");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.parseJson(json);
	}

	@Override
	public JSONObject toJson(JSONObject json) {
		if (json == null) {
			json = new JSONObject();
		}
		try {
			if (!TextUtils.isEmpty(userId)) {
				json.put("userId", userId);
			}
			if (!TextUtils.isEmpty(nickName)) {
				json.put("nickName", nickName);
			}
			if (!TextUtils.isEmpty(imgUrl)) {
				json.put("imgUrl", imgUrl);
			}
			if (!TextUtils.isEmpty(name)) {
				json.put("name", name);
			}
			if (!TextUtils.isEmpty(roleInfo)) {
				json.put("roleInfo", roleInfo);
			}
			json.put("roleType", roleType);
			if (!TextUtils.isEmpty(birthday)) {
				json.put("birthday", birthday);
			}
			if (!TextUtils.isEmpty(phoneNo)) {
				json.put("phoneNo", phoneNo);
			}
			json.put("jobType", jobType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return super.toJson(json);
	}
}
