package com.webService.service.ratingSection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class productrating {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_entity;
	private int price;
	private int delivery_time;
	private int quality;
	private int comp_with_descryption;
	private int communication;
	
	public int getId() {
		return id_entity;
	}
	public void setId(int id) {
		this.id_entity = id;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(int delivery_time) {
		this.delivery_time = delivery_time;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getComp_with_descryption() {
		return comp_with_descryption;
	}
	public void setComp_with_descryption(int comp_with_descryption) {
		this.comp_with_descryption = comp_with_descryption;
	}
	public int getCommunication() {
		return communication;
	}
	public void setCommunication(int communication) {
		this.communication = communication;
	}
}
