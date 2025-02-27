package org.spring.webflux.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.spring.webflux.entity.DummyEntity;
import org.spring.webflux.entity.DummyMongo;

public class DummyDTO {
	private int id;
	private String name;
	private LocalDate highOn;
	private int price;
	private String highAgo;
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
	public String getHighAgo() {
		return highAgo;
	}
	public void setHighAgo(String highAgo) {
		this.highAgo = highAgo;
	}
	public DummyDTO(int id, String name, LocalDate highOn, int price, String highAgo) {
		super();
		this.id = id;
		this.name = name;
		this.highOn = highOn;
		this.price = price;
		this.highAgo = highAgo;
	}
	public DummyDTO() {}
	
	public static DummyDTO entityToDto(DummyEntity dummyentity) {
		DummyDTO dummyDTO = new DummyDTO();
		dummyDTO.setId(dummyentity.getId());
		dummyDTO.setName(dummyentity.getName());
		dummyDTO.setHighOn(dummyentity.getHighOn());
		dummyDTO.setPrice(dummyentity.getPrice());
		dummyDTO.setHighAgo(
				ChronoUnit.DAYS.between(dummyentity.getHighOn(), LocalDate.now())+" days");
		return dummyDTO;
	}
	
	public DummyEntity dtoToEntity() {
		DummyEntity dummyEntity = new DummyEntity();
		dummyEntity.setId(id);
		dummyEntity.setName(name);
		dummyEntity.setHighOn(highOn);
		dummyEntity.setPrice(price);
		return dummyEntity;
	}

	public static DummyDTO mongoEntityToDto(DummyMongo dummyMongo) {
		DummyDTO dummyDTO = new DummyDTO();
		dummyDTO.setId(dummyMongo.getId());
		dummyDTO.setName(dummyMongo.getName());
		dummyDTO.setHighOn(dummyMongo.getHighOn());
		dummyDTO.setPrice(dummyMongo.getPrice());
		dummyDTO.setHighAgo(
				ChronoUnit.DAYS.between(dummyMongo.getHighOn(), LocalDate.now())+" days");
		return dummyDTO;
	}
	
	
	public DummyMongo dtoToMongoEntity() {
		DummyMongo dummyMongo = new DummyMongo();
		dummyMongo.setId(id);
		dummyMongo.setName(name);
		dummyMongo.setHighOn(highOn);
		dummyMongo.setPrice(price);
		return dummyMongo;
	}
	
	@Override
	public String toString() {
		return "DummyDTO [id=" + id + ", name=" + name + ", highOn=" + highOn + ", price=" + price + ", highAgo="
				+ highAgo + "]";
	}
}
