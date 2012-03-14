package com.telenav.mdb.converters.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.telenav.mdb.model.Person;
import com.telenav.mdb.model.PhoneNumber;

public class BeanConverterTest extends Tests {

	@Test
	public void testPrimitiveConverter() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		CodedOutputStream writer = CodedOutputStream.newInstance(output);

		converter.marshal(pn, writer);
		writer.flush();

		byte[] buffer = output.toByteArray();
		ByteArrayInputStream input = new ByteArrayInputStream(buffer);
		CodedInputStream reader = CodedInputStream.newInstance(input);

		PhoneNumber pn2 = (PhoneNumber) converter.unmarshal(PhoneNumber.class,
				reader);

		assertEquals(pn, pn2);
	}

	@Test
	public void testBeanConverter() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		CodedOutputStream writer = CodedOutputStream.newInstance(output);

		converter.marshal(person, writer);
		writer.flush();

		byte[] buffer = output.toByteArray();
		ByteArrayInputStream input = new ByteArrayInputStream(buffer);
		CodedInputStream reader = CodedInputStream.newInstance(input);

		Person p2 = (Person) converter.unmarshal(Person.class, reader);

		assertEquals(person, p2);
	}

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("readme" + "\\.(\\d+)");

		String[] input = new String[] { "readme", "readme.0", "readme.1" };
		for (String i : input) {
			Matcher m = pattern.matcher(i);
			if (m.find()) {
				// System.out.println(m.find());
				System.out.println(m.group(1));
			}
		}

	}
}
