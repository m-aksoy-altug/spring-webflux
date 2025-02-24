package org.spring.webflux.entity;

import java.time.LocalDate;

public class DummyEntity {
	private int id;
	private String name;
	private LocalDate highOn;
	private int price;
	
	public DummyEntity() {}
	
	public DummyEntity(int id, String name, LocalDate highOn, int price) {
		super();
		this.id = id;
		this.name = name;
		this.highOn = highOn;
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getHighOn() {
		return highOn;
	}
	public void setHighOn(LocalDate highOn) {
		this.highOn = highOn;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "DummyEntity [id=" + id + ", name=" + name + ", highOn=" + highOn + ", price=" + price + "]";
	}
	
	

}
