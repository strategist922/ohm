package com.telenav.mdb.store.converters;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

/**
 * marsal and unmarsal pojo through Protocol Buffer
 * 
 * 
 * @author leef
 */
public interface Converter {
	/**
	 * 
	 * @return
	 */
	public BeanContext getContext();

	/**
	 * marshal pojo. Writer need be flushed after this call.
	 * 
	 * @param source
	 * @param writer
	 */
	void marshal(Object source, CodedOutputStream writer) throws Exception;

	/**
	 * unmarshal pojo.
	 * 
	 * @param requireType
	 * @param reader
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Object unmarshal(Class requireType, CodedInputStream reader)
			throws Exception;

}
