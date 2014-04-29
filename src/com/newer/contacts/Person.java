package com.newer.contacts;

public class Person {

	private int id ;
	private String name;
	private String phone;
	public static final String NAME ="name";
	public static final String PHONE="phone";
	public static final String TABLE_NAME = "people";
	public static final String _ID = "id";
	
	public Person() {
		// TODO Auto-generated constructor stub
	}

	public Person(int id, String name, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
	}

	public Person(String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", phone=" + phone + "]";
	}
	
	
}
