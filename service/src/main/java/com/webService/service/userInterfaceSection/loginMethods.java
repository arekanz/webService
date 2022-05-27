package com.webService.service.userInterfaceSection;

import com.webService.service.userSection.user;
import com.webService.service.userSection.userrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class loginMethods implements login{
	public boolean logIn(String login, String Password,userrepository userRep) {
		user User=new user();
			User=userRep.getUserByLogin(login);
		if(User!=null)
		if(Password.equals(User.getPassword()))
		{
			return true;
		}
		
	return false;
}
	public void logout(user User) {
		User=null;
	}
}
