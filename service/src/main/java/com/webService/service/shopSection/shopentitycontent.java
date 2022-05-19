package com.webService.service.shopSection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class shopentitycontent {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_entity;
	private String id_delivery;
	private String content_path;
	
	public int getId() {
		return id_entity;
	}
	public void setId(int id) {
		this.id_entity = id;
	}
	public String getId_delivery() {
		return id_delivery;
	}
	public void setId_delivery(String id_delivery) {
		this.id_delivery = id_delivery;
	}
	public String getContent_path() {
		return content_path;
	}
	public void setContent_path(String content_path) {
		this.content_path = content_path;
	}
}
