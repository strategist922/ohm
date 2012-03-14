package com.telenav.mdb.model;

import com.telenav.mdb.store.converters.BeanContext;
import com.telenav.mdb.store.converters.Converter;
import com.telenav.mdb.store.converters.bean.BeanConverter;

public class BeanContextBuilder {

	public static BeanContext getContext() {
		BeanContext context = new BeanContext();

		context.addType(PhoneNumber.class);
		context.addType(Person.class);

		return context;
	}

	public static Converter getConverter() {
		BeanContext context = getContext();
		BeanConverter converter = new BeanConverter(context);

		return converter;
	}
}
