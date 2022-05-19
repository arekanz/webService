package com.webService.service.userSection;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "user")
public class user {
 @Id
 @Column(name = "id")
 @GeneratedValue(strategy=GenerationType.AUTO)
  private int id;
 @Column(name = "login")
  private String login;
 @Column(name = "password") 
  private String password;
 @Column(name = "email")
  private String email;
 @Column(name = "activated") 
  private int activated;

  public int getId() {
    return id;
  }
  
  public void setId(int id) {
	    this.id=id;
	  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  @Override
	public String toString() {
		return "User [Id=" + id + ", Login=" + login + ", email=" + email + "]";
	}

public int getActivated() {
	return activated;
}

public void setActivated(int activated) {
	this.activated = activated;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}
}