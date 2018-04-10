package com.babacit.alarm.db.dao;

import java.sql.SQLException;
import java.util.List;

import com.babacit.alarm.db.DatabaseHelper;
import com.babacit.alarm.entity.HoneyInfo;

public class HoneyDao extends BaseDao<HoneyInfo, Long> {
	private static HoneyDao mHoneyDao;

	public static HoneyDao getInstance() {
		if (mHoneyDao == null) {
			mHoneyDao = new HoneyDao();
		}
		return mHoneyDao;
	}

	public HoneyDao() {
		super(DatabaseHelper.getInstance(), "Honey", HoneyInfo.class);
	}

	public void insertHoneyData(List<HoneyInfo> _honeys) {
		if (_honeys == null) {
			return;
		}
		for (HoneyInfo honey : _honeys) {
			insert(honey);
		}
	}

	public HoneyInfo queryHoneyByCode(String code) {
		try {
			List<HoneyInfo> hInfos = (List<HoneyInfo>) dao.queryForEq("alarmCode", code);
			if (hInfos != null && hInfos.size() > 0) {
				return hInfos.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
