package com.webService.service.shopSection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class newproduct {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private int id_user;
	private int reserved_id;
	private String name;
	private String description;
	private String img_src;
	private Integer id_category;
	private Float price;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImg_src() {
		return img_src;
	}
	public void setImg_src(String img_src) {
		this.img_src = img_src;
	}
	public Integer getId_category() {
		return id_category;
	}
	public void setId_category(Integer id_category) {
		this.id_category = id_category;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	 @Override
		public String toString() {
			return "Product [Id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + "]";
		}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public int getReserved_id() {
		return reserved_id;
	}
	public void setReserved_id(int reserved_id) {
		this.reserved_id = reserved_id;
	}
}
