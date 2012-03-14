package com.telenav.mdb.store.converters.bean;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.telenav.mdb.store.converters.FieldConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.BoolConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.ByteConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.BytesConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.CharConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.DoubleConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.FloatConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.IntConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.LongConverter;
import com.telenav.mdb.store.converters.bean.PrimitiveConverters.StringConverter;

public class PrimitiveConvertorFactory {
	final Map<Type, FieldConverter> primitiveConverters = new HashMap<Type, FieldConverter>();

	private static PrimitiveConvertorFactory instance = null;

	public static PrimitiveConvertorFactory getInstance() {
		if (instance == null)
			instance = new PrimitiveConvertorFactory();

		return instance;
	}

	public PrimitiveConvertorFactory() {
		primitiveConverters.put(char.class, new CharConverter());
		primitiveConverters.put(Character.class, new CharConverter());

		primitiveConverters.put(byte.class, new ByteConverter());
		primitiveConverters.put(Byte.class, new ByteConverter());

		primitiveConverters.put(byte[].class, new BytesConverter());
		primitiveConverters.put(String.class, new StringConverter());

		primitiveConverters.put(boolean.class, new BoolConverter());
		primitiveConverters.put(Boolean.class, new BoolConverter());

		primitiveConverters.put(int.class, new IntConverter());
		primitiveConverters.put(Integer.class, new IntConverter());

		primitiveConverters.put(long.class, new LongConverter());
		primitiveConverters.put(Long.class, new LongConverter());

		primitiveConverters.put(float.class, new FloatConverter());
		primitiveConverters.put(Float.class, new FloatConverter());

		primitiveConverters.put(double.class, new DoubleConverter());
		primitiveConverters.put(Double.class, new DoubleConverter());
	}

	public FieldConverter getPrimitiveConverter(Type type) {
		return primitiveConverters.get(type);
	}

	public Iterator<Type> keyIterator() {
		return primitiveConverters.keySet().iterator();
	}

}
