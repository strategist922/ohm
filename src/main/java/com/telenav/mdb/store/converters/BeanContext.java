package com.telenav.mdb.store.converters;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class BeanContext {

	Set<Type> clzSet = null;

	public BeanContext() {
		clzSet = new HashSet<Type>();
	}

	public void addType(Type type) {
		clzSet.add(type);
	}

	public boolean isSupport(Type type) {
		return clzSet.contains(type);
	}

}
