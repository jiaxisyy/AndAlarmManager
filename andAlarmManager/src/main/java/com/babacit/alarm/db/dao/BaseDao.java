package com.babacit.alarm.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.text.TextUtils;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class BaseDao<T, ID> implements IBaseDao<T, ID> {

	/**
	 * table name
	 */
	protected String tableName;

	/**
	 * class name
	 */
	protected String entityClassName;

	/**
	 * dao
	 */
	protected Dao<T, ID> dao;

	/**
	 * Constructor
	 * 
	 * @param dao
	 */
	// 构造的时候
	public BaseDao(OrmLiteSqliteOpenHelper databaseHelper, String tableName,
			Class<T> clazz) {
		if (databaseHelper == null) {
			return;
		}

		// this.databaseHelper = databaseHelper;
		this.tableName = tableName;
		try {
			this.dao = databaseHelper.getDao(clazz);

		} catch (SQLException e) {

		}
		this.entityClassName = clazz.getName();
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * findAll with orderBy
	 * 
	 * @param orderBy
	 * @return
	 */
	public List<T> findAll(String orderBy) {
		QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
		queryBuilder.orderByRaw(orderBy);
		try {
			return queryBuilder.query();
		} catch (SQLException e) {

		}
		return null;
	}

	/**
	 * 根据字段查找
	 * 
	 * @param columnName
	 * @param value
	 * @return
	 */
	public List<T> findAllByWhere(String columnName, String value,
			String orderBy) {
		QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
		try {
			queryBuilder.where().eq(columnName, value);
			if (!TextUtils.isEmpty(orderBy)) {

				queryBuilder.orderByRaw(orderBy);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			return queryBuilder.query();
		} catch (SQLException e) {

		}
		return null;
	}

	/**
	 * getter
	 */
	public String getEntityClassName() {
		return entityClassName;
	}

	/**
	 * 删除某一条记录
	 */
	public void detele(T entity) {
		try {
			dao.delete(entity);
		} catch (SQLException e) {

		}
	}

	/**
	 * 按id查询
	 */
	public T findById(ID id) {
		try {
			return dao.queryForId(id);
		} catch (SQLException e) {

		}
		return null;
	}

	/**
	 * 查询所有记录 {@inheritDoc}
	 * 
	 */
	public List<T> findAll() {
		try {
			return dao.queryForAll();
		} catch (SQLException e) {

		}
		return null;
	}

	/**
	 * 通过id删除记录
	 * 
	 * @param id
	 *            要删除记录的id
	 */
	public void deteleById(ID id) {
		try {
			dao.deleteById(id);
		} catch (SQLException e) {

		}
	}

	/**
	 * 插入数据 {@inheritDoc}
	 * 
	 */
	public T insert(T entity) {
		try {
			int i = dao.create(entity);
			if (i != -1) {
				return entity;
			}
		} catch (SQLException e) {

		}
		return null;
	}

	/**
	 * 更新数据 {@inheritDoc}
	 * 
	 */
	public T update(T entity) {
		try {
			int i = dao.update(entity);
			if (i != -1) {
				return entity;
			}
		} catch (SQLException e) {

		}
		return null;
	}

	/**
	 * 
	 */
	public void executeSql(String sql, String... s) {
		try {
			dao.executeRaw(sql, s);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除全部数据
	 */
	public void deteleAll() {
		try {
			dao.delete(findAll());
			// dao.executeRaw("DELETE FROM supr.t_product", "");
		} catch (SQLException e) {

		}
	}

	/**
	 * 删除菜单下面全部数据
	 */
	public void deteleForObjectList(List<T> t) {
		try {
			dao.delete(t);
		} catch (SQLException e) {

		}
	}

}
