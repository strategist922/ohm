package com.telenav.mdb.store.hbase;

import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.mdb.converters.bean.Tests;
import com.telenav.mdb.model.BeanContextBuilder;
import com.telenav.mdb.model.Person;
import com.telenav.mdb.store.Stores;
import com.telenav.mdb.store.TStore;
import com.telenav.mdb.store.converters.Converter;
import com.telenav.mdb.store.query.Condition;
import com.telenav.mdb.store.query.Criteria;
import com.telenav.mdb.store.query.Operator;

public class HBaseSimpleStoreTest extends Tests {

	static TStore store = null;

	String table = "person";

	@BeforeClass
	public static void setupStore() throws Exception {
		// store = Stores.getDefault();
		store = Stores.getHBase();

		Converter converter = BeanContextBuilder.getConverter();
		((HBaseSimpleStore) store).setConverter(converter);
	}

	// @Test
	public void testCreateTable() throws Exception {
		store.createTable(table, Person.class);
	}

	String key(Person person) {
		return person.getFirstname() + " " + person.getLastname();
	}

	// @Test
	public void testInsertObject() throws Exception {
		String key = key(person);
		store.insertObject(table, key, person);

		Person ps2 = (Person) store.getObject(table, key);
		super.assertEquals(person, ps2);

		store.deleteObject(table, key);
		Person ps3 = (Person) store.getObject(table, key);
		assertNull(ps3);
	}

	@Test
	public void testFindObject() throws Exception {
		String key = key(person);
		store.insertObject(table, key, person);

		Criteria criteria = new Criteria();
		Condition cond = new Condition();
		cond.op = Operator.eq;
		cond.property = "firstname";
		cond.value = person.getFirstname();
		criteria.addCondition(cond);

		List<Object> objs = store.findObject(table, criteria);

		person.setLastname(person.getLastname() + System.currentTimeMillis());
		key = key(person);
		store.insertObject(table, key, person);

		List<Object> objs2 = store.findObject(table, criteria);
		Assert.assertEquals(objs.size() + 1, objs2.size());

		for (Object obj : objs2) {
			key = key((Person) obj);
			store.deleteObject(table, key);
		}
	}

}
