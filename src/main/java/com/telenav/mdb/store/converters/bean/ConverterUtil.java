package com.telenav.mdb.store.converters.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.telenav.mdb.store.converters.Converter;
import com.telenav.mdb.store.converters.FieldConverter;

public class ConverterUtil {

	public static byte[] convert(FieldConverter fc, Object obj)
			throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		CodedOutputStream writer = CodedOutputStream.newInstance(output);

		fc.write(1, obj, writer);
		writer.flush();

		byte[] value = output.toByteArray();
		return value;
	}

	public static Object convert(FieldConverter fc, Class clz, byte[] value)
			throws Exception {
		ByteArrayInputStream input = new ByteArrayInputStream(value);
		CodedInputStream reader = CodedInputStream.newInstance(input);

		reader.readTag();
		Object obj = fc.read(reader);

		return obj;
	}

	public static byte[] convert(Converter converter, Object obj)
			throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		CodedOutputStream writer = CodedOutputStream.newInstance(output);

		converter.marshal(obj, writer);
		writer.flush();

		byte[] value = output.toByteArray();
		return value;
	}

	public static Object convert(Converter converter, Class clz, byte[] value)
			throws Exception {
		ByteArrayInputStream input = new ByteArrayInputStream(value);
		CodedInputStream reader = CodedInputStream.newInstance(input);

		Object obj = converter.unmarshal(clz, reader);

		return obj;
	}

}
