package com.telenav.mdb.store.converters.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.telenav.mdb.store.converters.FieldMeta;

/**
 * sort field by field name.
 * 
 * TODO, load filed number from a file
 * 
 * @author leef
 * 
 */
@SuppressWarnings("rawtypes")
public class BeanFieldMeta implements FieldMeta {
	int[] fieldNumber = null;

	Map<Integer, Field> fieldMap = null;

	Class clz;

	public BeanFieldMeta(Class clz) {
		this.clz = clz;

		initFieldMap();
	}

	void initFieldMap() {
		Field[] fields = clz.getDeclaredFields();
		Arrays.sort(fields, fieldNameComparator);

		fieldMap = new HashMap<Integer, Field>();
		fieldNumber = new int[fields.length];

		// TODO can not add field or remove field
		// it would cause tag change
		for (int i = 0; i < fields.length; i++) {
			int tag = i + 1;
			fieldMap.put(tag, fields[i]);
			fieldNumber[i] = tag; // tag number from 1
		}
	}

	static Comparator<Field> fieldNameComparator = new Comparator<Field>() {

		public int compare(Field o1, Field o2) {
			return o1.getName().compareTo(o2.getName());
		}

	};

	/**
	 * all the field number, sorted from small to big.
	 * 
	 * @return
	 */
	public int[] fieldNumbers() {
		return fieldNumber;
	}

	/**
	 * 
	 * @param fieldNumber
	 * @return
	 */
	public String getFieldName(int fieldNumber) {
		return fieldMap.get(fieldNumber).getName();
	}

	public int getFieldNumber(String fieldName) {
		int fn = 0;
		for (Entry<Integer, Field> entry : fieldMap.entrySet()) {
			if (entry.getValue().getName().equals(fieldName)) {
				fn = entry.getKey();
				break;
			}
		}

		return fn;
	}

	public Type getFieldType(int fieldNumber) {
		return fieldMap.get(fieldNumber).getType();
	}

	public Class getTargetClass() {
		return clz;
	}

	public Object getFieldValue(int fieldNumber, Object source) {
		Field field = this.fieldMap.get(fieldNumber);

		try {
			field.setAccessible(true);
			return field.get(source);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void setFieldValue(int fieldNumber, Object target, Object value) {
		Field field = this.fieldMap.get(fieldNumber);

		try {
			field.setAccessible(true);
			field.set(target, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public Field getField(int fieldNumber) {
		return this.fieldMap.get(fieldNumber);
	}
}
