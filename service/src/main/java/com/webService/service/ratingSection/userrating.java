package com.webService.service.ratingSection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class userrating {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_user;
	private int count_of_P;
	private int rating;
	
	public int getId() {
		return id_user;
	}
	public void setId(int id) {
		this.id_user = id;
	}
	public int getCount_of_P() {
		return count_of_P;
	}
	public void setCount_of_P(int count_of_P) {
		this.count_of_P = count_of_P;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}
