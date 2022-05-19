/*package com.webService.service.userSection;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.webService.service.shopSection.categories;
import com.webService.service.shopSection.categoriesRepository;

@org.springframework.stereotype.Controller
public class userModuleController {
	private user ActiveUser = new user();
	private previllages UserPrevillages;
	private categories CurrentCategory = new categories();
	@Autowired
	private categoriesRepository AllCategories;
	@Autowired
	private userrepository Users;
	@Autowired
	private previllagesrepository UsersPrev;
	@GetMapping("/userModuleInclude/{login}")
    @ResponseBody
    public ModelAndView productView(@PathVariable("login") String login,Model mod) {
    	if(!login.isBlank()) {
    		ActiveUser = Users.getUserByLogin(login);
    		UserPrevillages = UsersPrev.getById(ActiveUser.getId());
    		ModelAndView model = new ModelAndView("UserModule");
    		System.out.println(ActiveUser.getLogin()+" "+ActiveUser.getEmail()+" "+UserPrevillages.getLevel()+" "+login);
    		this.getEntity(mod);
    	
    	//CurrentProductInfo = ProductsInfo.getById(id);
        return model;
    	}
    	else
    		return new ModelAndView("error");
    }
	@ModelAttribute
	public void getEntity(Model mod) {
		mod.addAttribute("logged", true);
		mod.addAttribute("userid", ActiveUser.getId());
		mod.addAttribute("userlogin", ActiveUser.getLogin());
		mod.addAttribute("useremail", ActiveUser.getEmail());
		mod.addAttribute("categories", AllCategories.findAll());
	}
}*/
