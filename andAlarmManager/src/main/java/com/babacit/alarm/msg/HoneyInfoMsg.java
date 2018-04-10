package com.babacit.alarm.msg;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class HoneyInfoMsg extends BaseMsg {
	/** 闹钟的序列号 */
	private String alarmCode;
	/** 宝贝名称 */
	private String name;
	/** 宝贝头像地址 */
	private String imgUrl;
	/** 昵称 */
	private String nickName;
	/**
	 * 性别 0：男；1：女
	 */
	private int sex;
	/**
	 * 生日 格式：yyyy-MM-dd
	 */
	private String birthday;
	/** 学校名称 */
	private String schoolName;
	/** 年级 */
	private String gradeName;
	/** 角色名称 */
	private String roleName;
	/** 兴趣爱好 */
	private String interest;

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json != null) {
			try {
				if (json.has("alarmCode")) {
					alarmCode = json.getString("alarmCode");
				}
				if (json.has("name")) {
					name = json.getString("name");
				}
				if (json.has("imgUrl")) {
					imgUrl = json.getString("imgUrl");
				}
				if (json.has("nickName")) {
					nickName = json.getString("nickName");
				}
				if (json.has("sex")) {
					sex = json.getInt("sex");
				}
				if (json.has("birthday")) {
					birthday = json.getString("birthday");
				}
				if (json.has("schoolName")) {
					schoolName = json.getString("schoolName");
				}
				if (json.has("gradeName")) {
					gradeName = json.getString("gradeName");
				}
				if (json.has("roleName")) {
					roleName = json.getString("roleName");
				}
				if (json.has("interest")) {
					interest = json.getString("interest");
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
			if (!TextUtils.isEmpty(alarmCode)) {
				json.put("alarmCode", alarmCode);
			}
			if (!TextUtils.isEmpty(name)) {
				json.put("name", name);
			}
			if (!TextUtils.isEmpty(imgUrl)) {
				json.put("imgUrl", imgUrl);
			}
			if (!TextUtils.isEmpty(nickName)) {
				json.put("nickName", nickName);
			}
			json.put("sex", sex);
			if (!TextUtils.isEmpty(birthday)) {
				json.put("birthday", birthday);
			}
			if (!TextUtils.isEmpty(schoolName)) {
				json.put("schoolName", schoolName);
			}
			if (!TextUtils.isEmpty(gradeName)) {
				json.put("gradeName", gradeName);
			}
			if (!TextUtils.isEmpty(roleName)) {
				json.put("roleName", roleName);
			}
			if (!TextUtils.isEmpty(interest)) {
				json.put("interest", interest);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return super.toJson(json);
	}
}
