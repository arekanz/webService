package com.webService.service.shopSection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class shopentityinfo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_entity;
	private int views;
	private Date creating_date;
	private float min_delivery_cost; 
	
	public int getId() {
		return id_entity;
	}
	public void setId(int id) {
		this.id_entity = id;
	}
	public float getMin_delivery_cost() {
		return min_delivery_cost;
	}
	public void setMin_delivery_cost(float min_delivery_cost) {
		this.min_delivery_cost = min_delivery_cost;
	}
	public Date getCreating_date() {
		return creating_date;
	}
	public void setCreating_date(Date creating_date) {
		this.creating_date = creating_date;
	}
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
}
