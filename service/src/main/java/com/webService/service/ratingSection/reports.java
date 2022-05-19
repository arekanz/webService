package com.webService.service.ratingSection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class reports {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private int id_reported;
	private int id_reason;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_reported() {
		return id_reported;
	}
	public void setId_reported(int id_reported) {
		this.id_reported = id_reported;
	}
	public int getId_reason() {
		return id_reason;
	}
	public void setId_reason(int id_reason) {
		this.id_reason = id_reason;
	}
}
