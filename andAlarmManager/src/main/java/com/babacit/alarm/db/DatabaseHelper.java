package com.babacit.alarm.db;

import android.database.sqlite.SQLiteDatabase;

import com.babacit.alarm.MainApplication;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.entity.Holiday;
import com.babacit.alarm.entity.HoneyInfo;
import com.babacit.alarm.entity.UserInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static DatabaseHelper instance = new DatabaseHelper();

	public static DatabaseHelper getInstance() {
		return instance;
	}

	public DatabaseHelper() {
		super(MainApplication.getInstance(), Constant.DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, Holiday.class);
			TableUtils.createTableIfNotExists(connectionSource, HoneyInfo.class);
			TableUtils.createTableIfNotExists(connectionSource, UserInfo.class);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		
	}

}
