package com.telenav.mdb.store.converters.bean;

import java.util.HashMap;
import java.util.Map;

import com.telenav.mdb.store.converters.FieldMeta;

public class BeanFieldMetas {

	public static BeanFieldMetas instance = new BeanFieldMetas();

	Map<Class, FieldMeta> cache = new HashMap<Class, FieldMeta>();

	private BeanFieldMetas() {

	}

	public FieldMeta getMeta(Class clz) {
		FieldMeta meta = cache.get(clz);
		if (meta == null) {
			meta = new BeanFieldMeta(clz);
			cache.put(clz, meta);
		}

		return meta;
	}

}
