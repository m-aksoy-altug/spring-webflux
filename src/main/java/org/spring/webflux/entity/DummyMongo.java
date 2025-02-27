package org.spring.webflux.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="dummy")
public class DummyMongo {
	@Id
	private int id;
	private String name;
	private LocalDate highOn;
	private int price;
	
	public DummyMongo(){
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

	public DummyMongo(int id, String name, LocalDate highOn, int price) {
		super();
		this.id = id;
		this.name = name;
		this.highOn = highOn;
		this.price = price;
	}

	@Override
	public String toString() {
		return "DummyMongo [id=" + id + ", name=" + name + ", highOn=" + highOn + ", price=" + price + "]";
	}
	
	
}
