package com.telenav.mdb.store.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * In Protocol Buffer, each field has a number. FieldMeta is to save the number
 * for fields of given class.
 * 
 * @author leef
 * 
 */
public interface FieldMeta {

	/**
	 * 
	 * @return
	 */
	int[] fieldNumbers();

	/**
	 * 
	 * @param fieldNumber
	 * @return
	 */
	String getFieldName(int fieldNumber);

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	int getFieldNumber(String fieldName);

	/**
	 * 
	 * @param fieldNumber
	 * @param source
	 * @return
	 */
	Object getFieldValue(int fieldNumber, Object source);

	/**
	 * 
	 * @param fieldNumber
	 * @param target
	 * @param value
	 */
	void setFieldValue(int fieldNumber, Object target, Object value);

	/**
	 * 
	 * @param fieldNumber
	 * @return
	 */
	Type getFieldType(int fieldNumber);

	/**
	 * 
	 * @param fieldNumber
	 * @return
	 */
	Field getField(int fieldNumber);

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Class getTargetClass();
}
