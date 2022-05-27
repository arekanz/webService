package com.webService.service.userSection;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class userinfo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String id_visited_categories;
	private String id_products;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getId_visited_categories() {
		return id_visited_categories;
	}
	public void setId_visited_categories(String id_visited_categories) {
		this.id_visited_categories = id_visited_categories;
	}
	public String getId_products() {
		return id_products;
	}
	public void setId_products(String id_products) {
		this.id_products = id_products;
	}
}
