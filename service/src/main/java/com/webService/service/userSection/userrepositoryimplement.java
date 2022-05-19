/*package com.webService.service.userSection;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userrepositoryimplement implements userrepository{
	@Autowired 
	userService userservice;;
	
	@Override
	public void insertUser(user User) {
		userservice.insertUser(User);
	}
	@Override
	public void insertUsers(final List<user> Users) {
		userservice.insertUsers(Users);
	}
	
	public List<user> getAllUsers(){
		return userservice.getAllUsers();
	}
	@Override
	public user getUserById(String id) {
		user User = userservice.getUserById(id);
		return User;
	}
	@Override
	public user getUserByLogin(String login) {
		user User = userservice.getUserByLogin(login);
		return User;
	}
}*/