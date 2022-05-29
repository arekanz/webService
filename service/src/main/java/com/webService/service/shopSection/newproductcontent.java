package com.webService.service.shopSection;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class newproductcontent {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_newproduct;
	private String id_delivery;
	private String content_path;
	private Date added_date;
	private String content_img;
	public int getId() {
		return id_newproduct;
	}
	public void setId(int id) {
		this.id_newproduct = id;
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
	public String getContent_img() {
		return content_img;
	}
	public void setContent_img(String content_img) {
		this.content_img = content_img;
	}
	public Date getAdded_date() {
		return added_date;
	}
	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
	}
}
