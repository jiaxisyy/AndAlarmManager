package com.babacit.alarm.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.babacit.alarm.db.DatabaseHelper;
import com.babacit.alarm.entity.Holiday;

public class HolidayDao extends BaseDao<Holiday, Long> {
	private static HolidayDao mHolidayDao;

	public static HolidayDao getInstance() {
		if (mHolidayDao == null) {
			mHolidayDao = new HolidayDao();
		}
		return mHolidayDao;
	}

	public HolidayDao() {
		super(DatabaseHelper.getInstance(), "Holiday", Holiday.class);
	}

	public void insertHolidayData(List<Holiday> _holidays) {
		if (_holidays == null) {
			return;
		}
		for (Holiday holiday : _holidays) {
			insert(holiday);
		}
	}

	public ArrayList<Holiday> queryHolidayByType(int dateType) {
		try {
			ArrayList<Holiday> hDays = (ArrayList<Holiday>)dao.queryForEq("dateType", dateType);
			if (hDays != null && hDays.size() > 0) {
				return hDays;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
