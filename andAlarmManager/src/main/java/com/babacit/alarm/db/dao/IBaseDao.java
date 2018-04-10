package com.babacit.alarm.db.dao;

import java.util.List;

/**
 * The class <code>IBaseDao</code>

 * @version 1.0
 */
public interface IBaseDao<T, ID> {

	/**
	 * @return the tableName
	 */
	public String getTableName();

	/**
	 * @return the EntityClassName
	 */
	public String getEntityClassName();

	/**
	 * findById
	 * 
	 * @param id
	 *            Id as ID Type
	 * @return Entity as T
	 */
	public T findById(ID id);

	/**
	 * findAll
	 * 
	 * @return List of T
	 */
	public List<T> findAll();

	/**
	 * findAll with orderBy
	 * 
	 * @param orderBy
	 *            Order by clause
	 * @return List of T
	 * @throws SQLException
	 */
	public List<T> findAll(String orderBy);

	/**
	 * delete
	 * 
	 * @param entity
	 *            Entity to be deleted
	 */
	public void detele(T entity);

	/**
	 * delete by id
	 * 
	 * @param id
	 *            Id of entity to be deleted
	 */
	public void deteleById(ID id);

	/**
	 * insert
	 * 
	 * @param entity
	 *            Entity to be inserted
	 * @return Entity inserted
	 */
	public T insert(T entity);

	/**
	 * update
	 * 
	 * @param entity
	 *            Entity to be updated
	 * @return Entity updated
	 */
	public T update(T entity);

}
