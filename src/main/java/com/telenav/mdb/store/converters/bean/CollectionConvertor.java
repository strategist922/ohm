package com.telenav.mdb.store.converters.bean;

import java.util.Collection;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;
import com.telenav.mdb.store.converters.Converter;
import com.telenav.mdb.store.converters.FieldConverter;

/**
 * 
 * List, Set
 * 
 * 
 * @author leef
 * 
 */
public class CollectionConvertor implements FieldConverter {
	Converter convertor;
	Class elemClz;
	Class requireType;

	public CollectionConvertor(Class requireType, Class elemClz,
			Converter convertor) {
		this.convertor = convertor;
		this.elemClz = elemClz;
		this.requireType = requireType;
	}

	public void write(int fieldNumber, Object value, CodedOutputStream output)
			throws Exception {
		output.writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);

		Collection c = (Collection) value;
		int size = c.size();
		convertor.marshal(size, output);
		for (Object o : c)
			convertor.marshal(o, output);
	}

	public Object read(CodedInputStream input) throws Exception {
		Object target = requireType.newInstance();
		Collection c = (Collection) target;

		int size = (Integer) convertor.unmarshal(int.class, input);
		for (int i = 0; i < size; i++) {
			Object e = convertor.unmarshal(elemClz, input);
			c.add(e);
		}

		return c;
	}
}
