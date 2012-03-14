package com.telenav.mdb.store.converters;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

public interface FieldConverter {

	/**
	 * write field number and value into output.
	 * 
	 * @param fieldNumber
	 * @param value
	 * @param output
	 * @throws Exception
	 */
	public void write(int fieldNumber, Object value, CodedOutputStream output)
			throws Exception;

	/**
	 * read value from input.
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public Object read(CodedInputStream input) throws Exception;

}
