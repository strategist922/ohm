package com.telenav.mdb.store.query;

import java.util.Iterator;

import com.telenav.mdb.store.converters.FieldMeta;
import com.telenav.mdb.store.converters.bean.BeanFieldMetas;

/**
 * only support number and string.
 * 
 * @author leef
 * 
 */
public class CriteriaFilter {
	Criteria criteria;

	public CriteriaFilter(Criteria criteria) {
		this.criteria = criteria;
	}

	public boolean isAccept(Object obj) {
		Iterator<Condition> it = criteria.iterator();
		while (it.hasNext()) {
			Condition con = it.next();
			if (!isAccept(con, obj))
				return false;
		}

		return true;
	}

	boolean isAccept(Condition con, Object obj) {
		boolean accept = true;

		Object target = con.value;
		String prop = con.property;

		Object value = getFieldValue(prop, obj);
		// null check
		if (value == null) {
			return target == null;
		} else if (target == null) {
			return false;
		}

		// compare non-null value
		Operator op = con.op;
		switch (op) {
		case eq:
			accept = acceptEq(value, target);
			break;
		case ne:
			accept = !acceptEq(value, target);
			break;
		case lt:
		case gt:
		case le:
		case ge:
			accept = acceptCompare(value, target, op);
			break;
		}

		return accept;
	}

	boolean acceptEq(Object value, Object target) {
		return target.equals(value);
	}

	boolean acceptCompare(Object value, Object target, Operator op) {
		boolean accept = true;

		if (Number.class.isAssignableFrom(value.getClass())) {
			accept = acceptCompareNumber(value, target, op);
		} else {
			accept = acceptCompareString(value, target, op);
		}

		return accept;
	}

	boolean acceptCompareString(Object value, Object target, Operator op) {
		boolean accept = true;

		String vs = value.toString();
		String ts = target.toString();
		int compare = vs.compareTo(ts);

		switch (op) {
		case lt:
			accept = compare < 0;
			break;
		case gt:
			accept = compare > 0;
			break;
		case le:
			accept = compare <= 0;
			break;
		case ge:
			accept = compare >= 0;
			break;
		}

		return accept;
	}

	boolean acceptCompareNumber(Object value, Object target, Operator op) {
		double vd = Double.parseDouble(value.toString());
		double td = Double.parseDouble(target.toString());

		boolean accept = true;

		switch (op) {
		case lt:
			accept = vd < td;
			break;
		case gt:
			accept = vd > td;
			break;
		case le:
			accept = vd <= td;
			break;
		case ge:
			accept = vd >= td;
			break;
		}

		return accept;
	}

	Object getFieldValue(String field, Object source) {
		FieldMeta fieldMeta = BeanFieldMetas.instance
				.getMeta(source.getClass());
		int number = fieldMeta.getFieldNumber(field);

		return fieldMeta.getFieldValue(number, source);
	}
}
