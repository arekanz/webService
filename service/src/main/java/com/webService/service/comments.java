package com.webService.service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class comments {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private int id_entity;
	private int id_user;
	private String text;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_entity() {
		return id_entity;
	}
	public void setId_entity(int id_entity) {
		this.id_entity = id_entity;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
