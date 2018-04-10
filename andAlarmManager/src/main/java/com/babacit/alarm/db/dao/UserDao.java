package com.babacit.alarm.db.dao;

import java.sql.SQLException;
import java.util.List;

import com.babacit.alarm.db.DatabaseHelper;
import com.babacit.alarm.entity.UserInfo;

public class UserDao extends BaseDao<UserInfo, Long> {
	private static UserDao mUserDao;

	public static UserDao getInstance() {
		if (mUserDao == null) {
			mUserDao = new UserDao();
		}
		return mUserDao;
	}

	public UserDao() {
		super(DatabaseHelper.getInstance(), "User", UserInfo.class);
	}

	public void insertUser(UserInfo user) {
		if (user == null) {
			return;
		}
		insert(user);
	}

	public void updateUser(UserInfo userInfo) {
		if (userInfo == null)
			return;
		else
			try {
				dao.update(userInfo);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public UserInfo queryUserByUserId(String userId) {
		try {
			List<UserInfo> hInfos = (List<UserInfo>) dao.queryForEq("userId",
					userId);
			if (hInfos != null && hInfos.size() > 0) {
				return hInfos.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<UserInfo> queryList() {
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
