package com.webService.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.webService.service.shopSection.categories;
import com.webService.service.shopSection.shopentity;
import com.webService.service.shopSection.shopentitycontent;
import com.webService.service.shopSection.shopentityinfo;
import com.webService.service.userSection.previllages;
import com.webService.service.userSection.previllagesrepository;
import com.webService.service.userSection.user;

public class sessionObjects {
	public user User;
	public previllages UserPrevillages;
	public shopentity NewProduct = new shopentity();
	public String contentimg="";
	public shopentity CurrentProduct = new shopentity();
	public shopentitycontent CurrentProductContent = new shopentitycontent();
	public shopentitycontent NewProductContent = new shopentitycontent();
	public String searchedtext;
	public String searchListLine;
	public String searchedpages;
	public String lastURL="/main";
	public int currentCat;
	public boolean loginexists=false;
	public boolean searchedBool=false;
	public int currentproductid;
	public boolean logged=false;
	public boolean wrongl=false;

	public categories CurrentCategory = new categories();
	public shopentityinfo CurrentProductInfo = new shopentityinfo();
	public shopentityinfo NewProductInfo = new shopentityinfo();
	public List<shopentity> searchList;
	public int searchedpage;
	sessionObjects(user User,previllagesrepository UsersPrev){
		this.User=User;
		if(User!=null)
		if(UsersPrev.findByUserId(User.getId())!=null) {
			UserPrevillages=UsersPrev.findByUserId(User.getId());
		}
		else {
			UserPrevillages.setId_user(User.getId());
			UserPrevillages.setLevel((short) 0);
		}
	}
	public short getUserPrevillages(previllagesrepository UsersPrev) {
		if(User!=null)
			if(UsersPrev.findByUserId(User.getId())!=null) {
			UserPrevillages=UsersPrev.findByUserId(User.getId());
			return UserPrevillages.getLevel();
			}
		return 0;
	}
	public void clearSession() {
		User=null;
		logged=false;
		UserPrevillages=null;
		NewProduct=null;
		CurrentProduct = new shopentity();
		CurrentProductContent = new shopentitycontent();
		NewProductContent = new shopentitycontent();
		searchedtext=null;
		searchListLine=null;
		searchedpages=null;
		lastURL="/main";
		currentCat=0;
		loginexists=false;
		searchedBool=false;
		currentproductid=0;
		wrongl=false;
	}
}
