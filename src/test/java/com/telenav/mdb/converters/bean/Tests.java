package com.telenav.mdb.converters.bean;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;

import com.telenav.mdb.model.BeanContextBuilder;
import com.telenav.mdb.model.Person;
import com.telenav.mdb.model.PhoneNumber;
import com.telenav.mdb.store.converters.Converter;

public class Tests {
	protected Converter converter = null;

	protected PhoneNumber pn = null;

	protected Person person = null;

	public static PhoneNumber makePhoneNumber() {
		PhoneNumber pn = new PhoneNumber(123, "1234-456");
		return pn;
	}

	public static Person makePerson() {
		PhoneNumber pn = makePhoneNumber();
		Person person = new Person("li", "feng");
		person.setPhone(pn);

		List<String> project = new ArrayList<String>();
		project.add("p1");
		project.add("p2");
		project.add("p3");
		person.setProject(project);

		return person;
	}

	@Before
	public void setupBean() {
		converter = BeanContextBuilder.getConverter();
		pn = makePhoneNumber();
		person = makePerson();
	}

	protected void assertEquals(PhoneNumber pn1, PhoneNumber pn2) {
		Assert.assertEquals(pn1.getCode(), pn2.getCode());
		Assert.assertEquals(pn1.getNumber(), pn2.getNumber());
	}

	protected void assertEquals(Person p1, Person p2) {

		Assert.assertEquals(p1.getFirstname(), p2.getFirstname());
		Assert.assertEquals(p1.getLastname(), p2.getLastname());
		assertEquals(p1.getProject(), p2.getProject());

		assertEquals(p1.getPhone(), p2.getPhone());
	}

	protected void assertEquals(List<String> p1, List<String> p2) {
		Assert.assertEquals(p1.size(), p2.size());
		for (int i = 0; i < p1.size(); i++)
			Assert.assertEquals(p1.get(i), p2.get(i));
	}

	protected void assertNull(Object o) {
		Assert.assertNull(o);
	}

}
