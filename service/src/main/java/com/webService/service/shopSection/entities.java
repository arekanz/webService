/*package com.webService.service.shopSection;

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

@org.springframework.stereotype.Controller
public class entities {
	private shopentity CurrentProduct = new shopentity();
	@Autowired
	private shopentityrepository Products;
	@GetMapping("/product/{id}")
    @ResponseBody
    public ModelAndView productView(@PathVariable("id") int id,Model mod) {
    	String path;
    	path=""+id;
    	if(!path.isBlank()) {
    		CurrentProduct = Products.getById(id);
    		ModelAndView model = new ModelAndView("product");
    		//this.getEntity(mod);
    	System.out.println(CurrentProduct.getName()+" "+CurrentProduct.getDescription());
    	//CurrentProductInfo = ProductsInfo.getById(id);
        return model;
    	}
    	else
    		return new ModelAndView("producterror");
    }
	
	@ModelAttribute
	public void getEntity(Model mod) {
		mod.addAttribute("currentproductname", CurrentProduct.getName());
		mod.addAttribute("currentproductdesc", CurrentProduct.getDescription());
		mod.addAttribute("currentproductimg", CurrentProduct.getImg_src());
		mod.addAttribute("currentproductprice", CurrentProduct.getPrice());
	}
}*/
