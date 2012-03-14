package com.telenav.mdb.store;

import java.util.List;

import com.telenav.mdb.store.query.Criteria;

public interface TStore {

	void createTable(String table, Class clz) throws Exception;

	void insertObject(String table, String key, Object o) throws Exception;

	Object getObject(String table, String key) throws Exception;
	
	Object getObject(String table, String key, int version) throws Exception;

	void deleteObject(String table, String key) throws Exception;

	List<Object> findObject(String table, Criteria criteria) throws Exception;

}
