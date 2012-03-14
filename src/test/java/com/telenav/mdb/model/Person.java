package com.telenav.mdb.model;

import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Person {
	private String firstname;
	private String lastname;
	private List<String> project;
	private PhoneNumber phone;
	private PhoneNumber fax;

	public List<String> getProject() {
		return project;
	}

	public void setProject(List<String> project) {
		this.project = project;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public PhoneNumber getPhone() {
		return phone;
	}

	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
	}

	public PhoneNumber getFax() {
		return fax;
	}

	public void setFax(PhoneNumber fax) {
		this.fax = fax;
	}

	public Person() {

	}

	public Person(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public static void main(String[] args) {
		// XStream xstream = new XStream();
		XStream xstream = new XStream(new StaxDriver());

		xstream.alias("person", Person.class);
		xstream.alias("phonenumber", PhoneNumber.class);

		Person joe = new Person("Joe", "Walnes");
		joe.setPhone(new PhoneNumber(123, "1234-456"));
		joe.setFax(new PhoneNumber(123, "9999-999"));

		String xml = xstream.toXML(joe);
		System.out.println(xml);
	}

}
