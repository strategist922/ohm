package com.telenav.mdb.store.converters.bean;

import java.io.IOException;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.WireFormat;
import com.telenav.mdb.store.converters.FieldConverter;

public abstract class AbstractFieldConverter implements FieldConverter {

	public int readFieldNumber(CodedInputStream input) throws IOException {
		int tag = input.readTag();
		int fieldNumber = WireFormat.getTagFieldNumber(tag);

		return fieldNumber;
	}
}
