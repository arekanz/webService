/*package com.webService.service.shopSection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class entityMethods implements entities {
	private shopentity CurrentProduct = new shopentity();
	@Autowired
	private shopentityrepository Products;
	@GetMapping("/product/{id}")
    @ResponseBody
    public ModelAndView productView(@PathVariable("id") int id,HttpServletRequest request,
            HttpServletResponse response) {
    	String path;
    	path=""+id;
    	if(!path.isBlank()) {
    		CurrentProduct = Products.getById(id);
    		ModelAndView model = new ModelAndView("product");
    	System.out.println(CurrentProduct.getName()+" "+CurrentProduct.getDescription());
    	//CurrentProductInfo = ProductsInfo.getById(id);
        return model;
    	}
    	else
    		return new ModelAndView("producterror");
    }
	public shopentity getEntity() {
		return CurrentProduct;
	}
}
*/