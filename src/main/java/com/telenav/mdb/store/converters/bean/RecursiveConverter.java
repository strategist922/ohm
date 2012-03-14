package com.telenav.mdb.store.converters.bean;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.WireFormat;
import com.telenav.mdb.store.converters.Converter;
import com.telenav.mdb.store.converters.FieldConverter;

public class RecursiveConverter implements FieldConverter {
	Converter converter;
	Class clz;

	public RecursiveConverter(Class clz, Converter converter) {
		this.converter = converter;
		this.clz = clz;
	}

	public void write(int fieldNumber, Object value, CodedOutputStream output)
			throws Exception {
		output.writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
		converter.marshal(value, output);

		output.writeTag(BeanConverter.end_field_number,
				WireFormat.WIRETYPE_FIXED32);
	}

	public Object read(CodedInputStream input) throws Exception {
		return converter.unmarshal(clz, input);
	}

}
