package com.telenav.mdb.store.converters.bean;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;
import com.telenav.mdb.exception.PersistenceException;
import com.telenav.mdb.store.converters.BeanContext;
import com.telenav.mdb.store.converters.Converter;
import com.telenav.mdb.store.converters.FieldConverter;
import com.telenav.mdb.store.converters.FieldMeta;

public class BeanConverter implements Converter {
	public static final int end_field_number = 9999;

	PrimitiveConvertorFactory converters = null;
	BeanContext context = null;

	public BeanConverter(BeanContext context) {
		this.context = context;
		this.converters = PrimitiveConvertorFactory.getInstance();
	}

	public FieldConverter getFieldConverter(Class cls, int fieldNumber)
			throws PersistenceException {
		FieldMeta fm = BeanFieldMetas.instance.getMeta(cls);

		// get converter by type
		FieldConverter converter = getFieldConverter(fm.getField(fieldNumber));
		return converter;
	}

	public void marshal(Object source, CodedOutputStream writer)
			throws Exception {
		Class cls = source.getClass();

		FieldConverter primitiveConvertor = converters
				.getPrimitiveConverter(cls);

		if (primitiveConvertor != null) {
			primitiveConvertor.write(1, source, writer);
		} else {

			FieldMeta fm = BeanFieldMetas.instance.getMeta(cls);

			int[] number = fm.fieldNumbers();
			for (int fieldNumber : number) {
				// get value
				Object value = fm.getFieldValue(fieldNumber, source);
				if (value == null)
					continue;

				// get converter by type
				FieldConverter converter = getFieldConverter(fm
						.getField(fieldNumber));

				// write
				converter.write(fieldNumber, value, writer);
			}
		}
	}

	public Object unmarshal(Class requireType, CodedInputStream reader)
			throws Exception {
		FieldConverter primitiveConvertor = converters
				.getPrimitiveConverter(requireType);
		if (primitiveConvertor != null) {
			reader.readTag();

			return primitiveConvertor.read(reader);
		} else {
			FieldMeta fm = BeanFieldMetas.instance.getMeta(requireType);

			Object target = requireType.newInstance();
			while (true) {
				int tag = reader.readTag();
				if (tag == 0)
					break;

				// get converter by type
				int fieldNumber = WireFormat.getTagFieldNumber(tag);
				if (fieldNumber == end_field_number)
					break;
				FieldConverter converter = getFieldConverter(fm
						.getField(fieldNumber));

				// read
				Object value = converter.read(reader);

				// set value
				fm.setFieldValue(fieldNumber, target, value);
			}

			return target;
		}
	}

	FieldConverter getFieldConverter(Field field) throws PersistenceException {
		Class type = field.getType();
		FieldConverter converter = converters.getPrimitiveConverter(type);

		if (converter == null) {
			if (context.isSupport(type)) {
				converter = new RecursiveConverter(type, this);
			} else {
				if (List.class.isAssignableFrom(type)
						|| Set.class.isAssignableFrom(type)) {
					Type gt = field.getGenericType();
					ParameterizedType pt = (ParameterizedType) gt;
					Type[] args = pt.getActualTypeArguments();
					Class elemClz = (Class) args[0];
					Class requireType = List.class.isAssignableFrom(type) ? ArrayList.class
							: HashSet.class;

					converter = new CollectionConvertor(requireType, elemClz,
							this);
				}
			}
		}

		if (converter == null)
			throw new PersistenceException("can not marshall/unmarshall "
					+ type);

		return converter;
	}

	public BeanContext getContext() {
		return context;
	}

}
