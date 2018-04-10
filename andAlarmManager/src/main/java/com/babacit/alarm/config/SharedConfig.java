package com.babacit.alarm.config;

import java.util.HashSet;
import java.util.Set;

import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.utils.DesUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedConfig {
	private DesUtils des;
	private final boolean HAS_DES = false;
	private SharedPreferences share = null;
	private final String SHARED_LOCK_NAME = "shared_data";
	private Editor editor = null;

	public SharedConfig(Context context) {
		share = context.getSharedPreferences(SHARED_LOCK_NAME,
				Context.MODE_PRIVATE);
		editor = share.edit();
		des = new DesUtils("Xh2D3heL");
	}

	public void clear() {
		if (editor != null) {
			editor.clear();
			editor.commit();
		}
	}

	public void setGuideState(boolean state) {
		putBoolean(Constant.PREFERENCE_GUIDE_STATE, state);
	}

	public boolean getGuideState() {
		return getBoolean(Constant.PREFERENCE_GUIDE_STATE, false);
	}

	public void setPhone(String phone) {
		putString(Constant.PREFERENCE_USER_PHONE, phone);
	}

	public String getPhone() {
		return getString(Constant.PREFERENCE_USER_PHONE, "");
	}

	public void setPwd(String pwd) {
		putString(Constant.PREFERENCE_USER_PWD, pwd);
	}

	public String getPwd() {
		return getString(Constant.PREFERENCE_USER_PWD, "");
	}

	/**
	 * 设置userId
	 * 
	 * @param path
	 */
	public void setUserId(String userId) {
		putString(Constant.PREFERENCE_USER_ID, userId);
	}

	/**
	 * 获取userId
	 * 
	 * @return
	 */
	public String getUserId() {
		return getString(Constant.PREFERENCE_USER_ID, "");
	}

	/**
	 * 初始设置状态
	 * 
	 * @param state
	 */
	public void setInitialState(int state) {
		putInt(Constant.PREFERENCE_INITIAL_USER_SETTING, state);
	}

	/**
	 * 获取初始设置状态
	 * 
	 * @return
	 */
	public int getInitialState() {
		return getInt(Constant.PREFERENCE_INITIAL_USER_SETTING, 0);
	}

	/**
	 * 设置alarmCode
	 * 
	 */
	public void setAlarmCode(String alarmCode) {
		putString(Constant.PREFERENCE_ALARM_CODE, alarmCode);
	}

	/**
	 * 获取alarmCode
	 * 
	 * @return
	 */
	public String getAlarmCode() {
		return getString(Constant.PREFERENCE_ALARM_CODE, "");
	}

	/**
	 * 设置铃声列表最后更新时间
	 * 
	 */
	public void setRingLatestUpdateTime(String dateTime) {
		putString(Constant.PREFERENCE_RING_LATEST_UPDATE_TIME, dateTime);
	}

	/**
	 * 获取铃声列表最后更新时间
	 * 
	 * @return
	 */
	public String getRingLatestUpdateTime() {
		return getString(Constant.PREFERENCE_RING_LATEST_UPDATE_TIME,
				"2015-01-01 00:00:00");
	}

	/**
	 * 设置用户头像地址
	 * 
	 * @param path
	 */
	public void setPortraitPath(String path) {
		putString(Constant.PREFERENCE_PORTRAIT_PATH, path);
	}

	/**
	 * 获取用户头像地址
	 * 
	 * @return
	 */
	public String getPortraitPath() {
		return getString(Constant.PREFERENCE_PORTRAIT_PATH, "");
	}

	/**
	 * 设置宝贝头像地址
	 * 
	 * @param path
	 */
	public void setHoneyPortraitPath(String path) {
		putString(Constant.PREFERENCE_HONEY_PORTRAIT_PATH, path);
	}

	/**
	 * 获取宝贝头像地址
	 * 
	 * @return
	 */
	public String getHoneyPortraitPath() {
		return getString(Constant.PREFERENCE_HONEY_PORTRAIT_PATH, "");
	}

	public String getUUID() {
		return getString(Constant.PREFERENCE_UUID, "");
	}

	public void setUUID(String uuid) {
		putString(Constant.PREFERENCE_UUID, uuid);
	}

	/**
	 * 获取登录状态
	 * 
	 * @return
	 */
	public int getLoginState() {
		return getInt(Constant.PREFERENCE_LOGIN_SATTE, 0);
	}

	/**
	 * 设置登录状态
	 * 
	 * @return
	 */
	public void setloginState(int value) {
		putInt(Constant.PREFERENCE_LOGIN_SATTE, value);
	}

	/**
	 * 获取手势密码状态
	 * 
	 * @return
	 */
	public int getGestureState() {
		return getInt(Constant.PREFERENCE_GESTURE_STATUS, 0);
	}

	/**
	 * 设置手势密码状态
	 * 
	 * @return
	 */
	public void setGestureState(int value) {
		putInt(Constant.PREFERENCE_GESTURE_STATUS, value);
	}

	/**
	 * 获取监控次数
	 * 
	 * @return
	 */
	public int getVideoTimes() {
		return getInt(Constant.PREFERENCE_VIDEO_TIMES, 10);
	}

	/**
	 * 设置监控次数
	 * 
	 * @return
	 */
	public void setVideoTimes(int value) {
		putInt(Constant.PREFERENCE_VIDEO_TIMES, value);
	}

	private String getString(String key, String defValue) {
		if (share == null) {
			return defValue;
		}
		if (HAS_DES) {
			String s = share.getString(key, null);
			if (s != null && !s.equals("")) {
				return decodeToString(s);
			} else {
				return defValue;
			}
		} else {
			return share.getString(key, defValue);
		}
	}

	private void putString(String key, String value) {
		if (editor == null) {
			return;
		}
		if (HAS_DES) {
			String encodeStr = encodeString(value);
			editor.putString(key, encodeStr);
		} else {
			editor.putString(key, value);
		}
		editor.commit();
	}

	private int getInt(String key, int defValue) {
		if (share == null) {
			return defValue;
		}
		if (HAS_DES) {
			String iStr = share.getString(key, null);
			if (iStr != null && !iStr.equals("")) {
				return decodeToInt(iStr);
			} else {
				return defValue;
			}
		} else {
			return share.getInt(key, defValue);
		}
	}

	private void putInt(String key, int value) {
		if (editor == null) {
			return;
		}
		if (HAS_DES) {
			String encodeStr = encodeInt(value);
			editor.putString(key, encodeStr);
		} else {
			editor.putInt(key, value);
		}
		editor.commit();
	}

	private long getLong(String key, long defValue) {
		if (share == null) {
			return defValue;
		}
		if (HAS_DES) {
			String lStr = share.getString(key, null);
			if (lStr != null && !lStr.equals("")) {
				return decodeToLong(lStr);
			} else {
				return defValue;
			}
		} else {
			return share.getLong(key, defValue);
		}
	}

	private void putLong(String key, long value) {
		if (editor == null) {
			return;
		}
		if (HAS_DES) {
			String encodeStr = encodeLong(value);
			editor.putString(key, encodeStr);
		} else {
			editor.putLong(key, value);
		}
		editor.commit();
	}

	private boolean getBoolean(String key, boolean defValue) {

		if (share == null) {
			return defValue;
		}
		if (HAS_DES) {
			String boolStr = share.getString(key, null);
			if (boolStr != null && !boolStr.equals("")) {
				return decodeToBool(boolStr);
			} else {
				return defValue;
			}
		} else {
			return share.getBoolean(key, defValue);
		}
	}

	private void putBoolean(String key, boolean value) {

		if (editor == null) {
			return;
		}
		if (HAS_DES) {
			String encodeStr = encodeBoolean(value);
			editor.putString(key, encodeStr);
		} else {
			editor.putBoolean(key, value);
		}
		editor.commit();
	}

	public void putStringSet(String key, Set<String> set) {
		if (editor == null) {
			return;
		}
		if (HAS_DES) {
			Set<String> enCodeSet = new HashSet<String>();
			if (set != null && set.size() > 0) {
				for (String value : set) {
					enCodeSet.add(encodeString(value));
				}
			}
			editor.putStringSet(key, enCodeSet);
		} else {
			editor.putStringSet(key, set);
		}
		editor.commit();
	}

	public Set<String> getStringSet(String key, Set<String> defaultValut) {
		Set<String> set = share.getStringSet(key, defaultValut);
		if (HAS_DES) {
			Set<String> decodeSet = new HashSet<String>();
			if (set != null && set.size() > 0) {
				for (String value : set) {
					decodeSet.add(decodeToString(value));
				}
			}
			return decodeSet;
		} else {
			return set;
		}
	}

	private String encodeBoolean(boolean bool) {
		String boolStr = String.valueOf(bool);
		return encodeString(boolStr);
	}

	private String encodeInt(int i) {
		String iStr = String.valueOf(i);
		return encodeString(iStr);
	}

	private String encodeLong(long l) {
		String lStr = String.valueOf(l);
		return encodeString(lStr);
	}

	private String encodeString(String msg) {
		if (msg == null) {
			msg = "";
		}
		try {
			msg = msg.trim();
			return des.encrypt(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private boolean decodeToBool(String msg) {
		String boolStr = decodeToString(msg);
		if (boolStr == null || boolStr.equals("")) {
			return false;
		}
		return Boolean.valueOf(boolStr);
	}

	private int decodeToInt(String msg) {
		String iStr = decodeToString(msg);

		if (iStr == null || iStr.equals("")) {
			return 0;
		}
		return Integer.valueOf(iStr);
	}

	private long decodeToLong(String msg) {
		String lStr = decodeToString(msg);
		if (lStr == null || lStr.equals("")) {
			return 0l;
		}
		return Long.valueOf(lStr);
	}

	private String decodeToString(String msg) {
		if (msg == null) {
			return "";
		}
		try {
			msg = msg.trim();
			return des.decrypt(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
