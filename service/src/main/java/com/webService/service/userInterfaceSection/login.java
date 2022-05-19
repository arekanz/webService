package com.webService.service.userInterfaceSection;

import com.webService.service.userSection.*;
import org.springframework.stereotype.Service;

public interface login {		 
	public boolean logIn(String login, String Password,userrepository userRep,user Result);
	public void logout(user User);
}
