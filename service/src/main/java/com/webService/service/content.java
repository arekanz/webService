package com.webService.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.type.ImageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.sql.DataSource;
import javax.swing.text.html.ImageView;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
//import java.util.List;
import java.util.Map;
import com.webService.service.userInterfaceSection.login;
import com.webService.service.userSection.*;
import com.webService.service.ratingSection.*;
import com.webService.service.shopSection.*;

import java.awt.Image;
import java.io.*;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

@org.springframework.stereotype.Controller
public class content {
	@Autowired
	private EnvUtil serverconfigInfo;
	private String server;
	
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() throws UnknownHostException {
		String ip = serverconfigInfo.getHostname();
		int port = serverconfigInfo.getPortAsInt();
		server = "http://"+ip+":"+port;
	}
	Map<String,sessionObjects> accus = new HashMap<>();
	Map<String,Object> model;
	
	
	
	
	private List<Integer> reservedidsP = new ArrayList<>();

	public String categorylinedrop;
	
	
	@Autowired
	private communicatesrepository Communicates;
	@Autowired
	private categoriesRepository AllCategories;
	@Autowired
	private reportsrepository Reports;
	@Autowired
	private deliveryoptionsRepository Deliverys;
	@Autowired
	private userinforepository UsersInfo;
	@Autowired
	private userrepository Users;
	@Autowired
	private shopentityrepository Products;
	@Autowired
	private shopentitycontentrepository ProductsContent;
	@Autowired
	private shopentityinforepository ProductsInfo;
	@Autowired
	private commentsrepository Comments;
	@Autowired
	private login loginInterface;
	@Autowired
	private previllagesrepository UsersPrev;
	private boolean changeInCat = true;
	public sessionObjects getSessionObject(HttpServletRequest req) {
		String usridd=(String)req.getSession().getAttribute("ssidd");
		if(usridd==null || usridd.isBlank())
		{
				accus.put(req.getSession().getId() , new sessionObjects(null,null));
				usridd=req.getSession().getId();
				req.getSession().setAttribute("ssidd", usridd);
		}
		return accus.get(req.getSession().getId());
	}
	public sessionObjects getSessionObject(String req) {
		return accus.get(req);
	}
	public int objcount() {
		return accus.size();
	}
	@GetMapping("/main")
	public ModelAndView firstPage(HttpServletRequest req) {
		sessionObjects obiekt = getSessionObject(req);
		req.getSession().setAttribute("logged", obiekt.logged);
		obiekt.lastURL="/main";
		return new ModelAndView("main");
	}
	
	@GetMapping("/newproduct/{id}")
	public ModelAndView newProduct(@PathVariable(value="id",required=false) String id, HttpServletRequest req) {
		
		if(id.isBlank() || getSessionObject(req).NewProduct.getImg_src()==null)
			id="1";
		String urlpom = "newproduct"+id;
		if(getSessionObject(req).logged==false)
		{
			getSessionObject(req).lastURL="newproduct/"+id;
			return logInFalse();
		}
		
		return new ModelAndView(urlpom);
	}
	@RequestMapping(value = "/getImg/{id_product}/{img_name}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImageAsResponseEntity(@PathVariable("id_product") String path_f,@PathVariable("img_name") String name) throws IOException {
	    HttpHeaders headers = new HttpHeaders();
	    File convertFile = new File("./src/main/resources/static/show/productscontents/img_"+path_f+name);
	      convertFile.getAbsolutePath();
	    FileInputStream in = new FileInputStream(convertFile);
	    byte[] media = IOUtils.toByteArray(in);
	    in.close();
	    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	    
	    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
	    return responseEntity;
	}
	
	@RequestMapping("/loginfalse")
	public ModelAndView logInFalse() {
		return new ModelAndView("loginformonly");
	}
	
	
	@RequestMapping("/register/submit")
	public RedirectView registerF(@RequestParam("login") String loginP, @RequestParam("password") String passwordP, @RequestParam("email") String email, @RequestParam("confirmp") String confirmP, HttpServletRequest req) {
		//for(int i=1;i<=Users.findAll().size();i++)
		if(Users.getUserByLogin(loginP)!=null)
			//loginexists=true;
		//if(loginexists)
			return new RedirectView("/register");
		else {
			Users.insertUser(loginP,passwordP,email);
			UsersInfo.insertUser(Users.getUserByLogin(loginP).getId());
			//sendConfirmationEmail(email,loginP);
		return new RedirectView(getSessionObject(req).lastURL);
		}
	}
	
	@RequestMapping("/register")
	public ModelAndView register() {
		return new ModelAndView("register");
	}
	public String sessID(HttpServletRequest httpServletRequest) {
		final HttpSession session = httpServletRequest.getSession(false);
		if(session!=null)
			return session.getId();
		else
			return null;
	}
	@RequestMapping("/settings")
	public ModelAndView settings(HttpServletRequest req) {
		getSessionObject(req).lastURL="/settings";
		if(getSessionObject(req).logged==false)
			return logInFalse();
		if(req.getSession().getAttribute("logged")==null || (boolean)req.getSession().getAttribute("logged")==false)
			req.getSession().setAttribute("logged", getSessionObject(req).logged);
		return new ModelAndView("settings");
	}
	@RequestMapping("/myproducts")
	public ModelAndView myproducts(HttpServletRequest req) {
		getSessionObject(req).lastURL="/myproducts";
		if(getSessionObject(req).logged==false)
			return logInFalse();
		return new ModelAndView("myproducts");
	}
	@RequestMapping("/serveroptions/reports")
	public ModelAndView reports(HttpServletRequest req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
		getSessionObject(req).lastURL="/serveroptions/reports";
		return new ModelAndView("reports");
		}
		return firstPage(req);
	}
	@RequestMapping("/serveroptions/communicates")
	public ModelAndView communicates(HttpServletRequest req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
		getSessionObject(req).lastURL="/serveroptions/communicates";
		return new ModelAndView("communicates");
		}
		return firstPage(req);
	}
	@RequestMapping("/getReports/{type}")
	@ResponseBody
	public ResponseEntity<?> getReports(@PathVariable("type") String type,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			List<reports> reps = new ArrayList<>();
			String pom="";
			reports repp;
			if(type=="all" || type.isBlank())
			{
				reps=Reports.findAll();
			}
			else if(type=="products") {
				reps=Reports.findByType(0);
			}
			else if(type=="comments") {
				reps=Reports.findByType(5000);
			}
			else if(type=="users") {
				reps=Reports.findByType(10000);
			}
			if(reps.size()>0)
			for(int i=0;i<reps.size();i++)
			{
				repp=reps.get(i);
				if(repp.getId_reason()>10000) {
				pom+="<a class=\"report\">";
				pom+="<div class=\"reason\">"+Communicates.getById(repp.getId_reason()).getText()+" - "+Users.getById(repp.getId_reported()).getLogin()+
						"&nbsp<a href=\""+server+"/deleteReport/"+reps.get(i).getId()+"\">delete</a>"+"</div>";
				pom+="<div class=\"reported\"><a class=\"button\" href=\""+server+"/blockuser/"+Users.getById(repp.getId_reported()).getId()+">Zablokuj użytkownika</a> || "
						+ "<a class=\"button\" href=\""+server+"/getUserInfo/"+Users.getById(repp.getId_reported()).getId()+">Wyświetl informacje użytkownika</a></div>";
				pom+="</a>";
				}
				else if(repp.getId_reason()>5000) {
					pom+="<a href=\""+server+"/show/product/"+Comments.getById(repp.getId_reported()).getId_entity()+"\" class=\"report\">";
					pom+="<div class=\"reason\">"+Communicates.getById(repp.getId_reason()).getText()+" - "+Comments.getById(repp.getId_reported()).getId_user()+
							"&nbsp<a href=\""+server+"/deleteReport/"+reps.get(i).getId()+"\">delete</a>"+"</div>";
					pom+="<div class=\"reported\">"+Comments.getById(repp.getId_reported()).getText()+"</div>";
					pom+="</a>";
					}
				else {
					pom+="<a href=\""+server+"/show/product/"+repp.getId_reported()+"\" class=\"report\">";
					pom+="<div class=\"reason\">"+Communicates.getById(repp.getId_reason()).getText()+" - "+Products.getById(repp.getId_reported()).getName()+
							"&nbsp<a href=\""+server+"/deleteReport/"+reps.get(i).getId()+"\">delete</a>"+"</div>";;
					pom+="<div class=\"reported\">"+Products.getById(repp.getId_reported()).getDescription()+"</div>";
					pom+="</a>";
				}
			}
			if(!pom.isBlank())
			return ResponseEntity.ok(String.valueOf("<div class=\"count\">"+reps.size()+"</div>"+pom));
			else
				return ResponseEntity.ok("Nie ma żadnych zgłoszeń");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/getCommunicates/{type}")
	@ResponseBody
	public ResponseEntity<?> getCommunicates(@PathVariable("type") String type,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			List<communicates> reps = new ArrayList<>();
			String pom="";
			communicates repp;
			if(type=="products") {
				reps=Communicates.findByType(0);
			}
			else if(type=="comments") {
				reps=Communicates.findByType(5000);
			}
			else if(type=="users") {
				reps=Communicates.findByType(10000);
			}
			if(reps.size()>0)
			for(int i=0;i<reps.size();i++)
			{
				repp=reps.get(i);
				if(!repp.getText().isBlank())
					pom+="<div class=\"communicate\" id=\"communicate"+repp.getId()+"\" onclick='editComm(\""+type+"\","+repp.getId()+")'>"+repp.getText()+"</div>";
				else
					pom+="<input type=\"text\" class=\"communicate\" id=\"communicate"+repp.getId()+"\">"+repp.getText()+"</input><button onclick='addComm(\""+type+"\","+repp.getId()+")'>Dodaj</button>";
			}
			return ResponseEntity.ok(String.valueOf("<div class=\"count\">"+reps.size()+"</div>"+pom));
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/setCommunicates/{type}")
	@ResponseBody
	public ResponseEntity<?> setCommunicates(@PathVariable("type") String type,@RequestParam("id") int id,@RequestParam("text") String text,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			if(!text.isBlank())
			{
				if(id!=0) {
				Communicates.updateCommunicates(id, text);
				Communicates.insertCommunicates(id+1);
				return ResponseEntity.ok("success");
				}
			}
			return ResponseEntity.ok("failure");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@GetMapping("/adminSettings")
	@ResponseBody
	public String adminSettings(@RequestParam("ssidd") String req) {
		String pom="";
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250)
		{
			pom+="<button class=\"settings\" onclick=\"reports()\">Zgłoszenia</button>";
			pom+="<button class=\"settings\" onclick=\"addcommunicates()\">Komunikaty zgłoszeń</button>";
			pom+="<button class=\"settings\" onclick=\"addcategories()\">Dodaj kategorie</button>";
			pom+="<button class=\"settings\" onclick=\"adddeliverys()\">Dodaj opcje wysyłek</button>";
			pom+="<button class=\"settings\" onclick=\"setprevillages()\">Nadaj uprawnienia</button>";
			return pom;
		}
		return "Nie posiadasz uprawnień";
	}
	 @GetMapping("/email/activate/{login}/{id}")
	    @ResponseBody
	    public RedirectView emailactivation(@PathVariable("login") String login,@PathVariable("id") int id, HttpServletRequest req) {
	    		Users.getUserByLogin(login).setActivated(1);
	    		if(getSessionObject(req).User!=null)
	    			getSessionObject(req).User.setActivated(1);
	    		return new RedirectView(getSessionObject(req).lastURL);
	    }
	 @RequestMapping("/login")
	    @ResponseBody
	    public RedirectView login(@RequestParam("login") String loginP, @RequestParam("password") String passwordP, HttpServletRequest req/*, @RequestParam("lastpath") String lastPath*/,Model model) {
		 getSessionObject(req).wrongl=false;	 	
		 if(loginInterface.logIn(loginP,passwordP,Users)) {
	    	 		if(getSessionObject(req)==null)
	    			accus.put(this.sessID(req),  new sessionObjects(Users.getUserByLogin(loginP),UsersPrev));
	    	 		else
	    	 			getSessionObject(req).User = Users.getUserByLogin(loginP);
	    			//this.getEntity(model);
	    	 		getSessionObject(req).logged=true;
	    	 		req.getSession().setAttribute("userlogin", getSessionObject(req).User.getLogin());
	    			}
	    		else
	    			getSessionObject(req).wrongl=true;
	    		return new RedirectView(getSessionObject(req).lastURL);
	    }
	 @RequestMapping("/loginonly")
	    @ResponseBody
	    public RedirectView loginonly(@RequestParam("login") String loginP, @RequestParam("password") String passwordP, HttpServletRequest req/*, @RequestParam("lastpath") String lastPath*/,Model model) {
		 getSessionObject(req).wrongl=false;
    		if(loginInterface.logIn(loginP,passwordP,Users)) {
    			if(getSessionObject(req)==null)
	    			accus.put(this.sessID(req),  new sessionObjects(Users.getUserByLogin(loginP), UsersPrev));
	    	 		else
	    	 			getSessionObject(req).User = Users.getUserByLogin(loginP);
    			getSessionObject(req).logged=true;
    	 		req.getSession().setAttribute("userlogin", getSessionObject(req).User.getLogin());
    			}
	    		else {
	    			getSessionObject(req).wrongl=true;
	    			return new RedirectView("/loginfalse");
	    		}
	    		return new RedirectView(getSessionObject(req).lastURL);
	    }
    
    @RequestMapping("/logout")
    @ResponseBody
    public RedirectView logout(Model model, HttpServletRequest req) {
    	accus.get(req.getSession().getId()).clearSession();
    		return new RedirectView(getSessionObject(req).lastURL);
    }
    
    private String fillPrice(float price) {
    	String pom=String.valueOf(price);
    	String res = "";
    	int i=pom.lastIndexOf(".");
    	if(i==-1)
    	{
    		res+=".00";
    	}
    	else if(pom.length()-i==1)
    		res+="00";	
    	else if(pom.length()-i==2)
    		res+="0";
    	return res;
    }
    private void addUserCategory(int id_category, @RequestParam("ssidd") String req) {
    	if(getSessionObject(req).User!=null && getSessionObject(req).User.getId()>0) {
    		String pom="";
    		String pom2[]=null;
    		if(UsersInfo.getById(getSessionObject(req).User.getId()).getId_visited_categories()!=null && !UsersInfo.getById(getSessionObject(req).User.getId()).getId_visited_categories().isBlank())
    		{
    			pom = UsersInfo.getById(getSessionObject(req).User.getId()).getId_visited_categories();
    			pom2 = splitString(UsersInfo.getById(getSessionObject(req).User.getId()).getId_visited_categories());
    		}
    		if(pom2!=null && pom2.length>0)
    			for(int i=0;i<pom2.length;i++)
    				if(pom2[i].equals(String.valueOf(id_category)))
    					return;
    	UsersInfo.updateUserInfo(getSessionObject(req).User.getId(), id_category+"#"+pom);
    	}
    }
    
    private String productMiniatureTemplate(shopentity product) {
    	String prodpom="<a href='"+server+"/show/product/"+product.getId()+"'>";
    	prodpom+="<div class='productminiature'>";
    	prodpom+="<div style=\"display:flex;\"><div class='productname'><h4>"+product.getName()+"</h4></div>";
    	prodpom+="<div class='productprice'><h5>";
    	prodpom+=product.getPrice();
    	prodpom+=fillPrice(product.getPrice());
    	prodpom+="zł</h5></div></div>";
    	prodpom+="<div style=\"display:flex;\"><div style=\"display:flex;overflow:scroll;overflow-y:hidden;height: 220px;width: 50%\">";
		String[] pom = this.splitString(product.getImg_src());
		for(int j=0;j<pom.length;j++)
			prodpom+="<div class='productimg'><img style=\"height: 180px\" class='slideimg' src='"+server+"/getImg/"+product.getId()+"_/"+pom[j]+"'></div>";
		prodpom+="</div><div class='mindelcost'><h7>&nbspDostawa już od: "+ProductsInfo.getById(product.getId()).getMin_delivery_cost()+fillPrice(ProductsInfo.getById(product.getId()).getMin_delivery_cost())+"zł&nbsp<br></h7></div>"
				+ "<div class='productdescription'><h7>"+product.getDescription()+"</h7></div>";
		prodpom+="</div></div></a>";
		return prodpom;
    }
    @GetMapping("/show/searching/")
    @ResponseBody
    private String getProductsAttributes(@RequestParam("ssidd") String req) {
    	List<shopentity> listaProduktow = getSessionObject(req).searchList;
    	int page = getSessionObject(req).searchedpage;
    	getSessionObject(req).searchListLine="";
    	shopentity prodpom;
    	for(int i=(0+(30*(page-1)));(i<listaProduktow.size() && i<(30*page));i++) {
    		prodpom=listaProduktow.get(i);
    		getSessionObject(req).searchListLine+=productMiniatureTemplate(prodpom);
    	}
    	float licznik=0;
    	int pom=listaProduktow.size();
    	licznik=pom/30;
    	getSessionObject(req).searchedpages="";
    	if(licznik>(int)licznik)
    		licznik=(int)(licznik+1);
    	if(page>1) {
    		getSessionObject(req).searchedpages+="<a href='"+this.server+"/search/show/";
    		if(getSessionObject(req).searchedtext!=null && !getSessionObject(req).searchedtext.isBlank())
    		getSessionObject(req).searchedpages+=getSessionObject(req).searchedtext+"/";
    		getSessionObject(req).searchedpages+=getSessionObject(req).currentCat+"/"+(page-1)+"'>&lt;Poprzednia</a>";
    	}
    	getSessionObject(req).searchedpages+="<a>"+page+" of "+licznik+"</a>";
    	if(page<licznik) {
    		getSessionObject(req).searchedpages+="<a href='"+this.server+"/search/show/";
    		if(getSessionObject(req).searchedtext!=null && !getSessionObject(req).searchedtext.isBlank())
    		getSessionObject(req).searchedpages+=getSessionObject(req).searchedtext+"/";
    		getSessionObject(req).searchedpages+=getSessionObject(req).currentCat+"/"+(page+1)+"'>Następna&gt;</a>";
    	}
    		if(getSessionObject(req).searchListLine==null || getSessionObject(req).searchListLine.isBlank())
    		getSessionObject(req).searchListLine="<p>Nie udało się znaleźć pasujących wyników</p>";
    	return String.valueOf((getSessionObject(req).searchListLine+"<div id=\"pagenavigator\">"+getSessionObject(req).searchedpages+"</div>"));
    }
    
    @RequestMapping("/search")
    @ResponseBody
    public RedirectView productViewMethod(String page,@RequestParam(value="search",required=false) String searched,@RequestParam("category") String catID, HttpServletRequest req) {
    	if(!catID.isBlank()) {
    		getSessionObject(req).currentCat=Integer.valueOf(catID);
    		req.getSession().setAttribute("currentcat", getSessionObject(req).currentCat);
    		getSessionObject(req).searchList=Products.findByAttributes(searched, catID);
    	}
    	if(catID.isBlank() || catID=="0") {
    		getSessionObject(req).currentCat=0;
    		catID="";
    		getSessionObject(req).searchList=Products.findByAttributes(searched, "");
    	}
    	if(page==null || page.isBlank())
        	page="1";
    	//getProductsAttributes(Products.findByAttributes(searched, catID),1);
    	req.getSession().setAttribute("logged", getSessionObject(req).logged);
    	if(searched.isBlank())
    		searched=null;
    	getSessionObject(req).searchedtext=searched;
    	req.getSession().setAttribute("searchedtext", getSessionObject(req).searchedtext);
    	getSessionObject(req).searchedBool=true;
    	req.getSession().setAttribute("searchedBool", getSessionObject(req).searchedBool);
    	getSessionObject(req).searchedpage=Integer.valueOf(page);
    	if(searched==null || searched.isBlank())
    		return new RedirectView("/show/search/"+getSessionObject(req).currentCat+"/"+page);
    		return new RedirectView("/show/search/"+searched+"/"+getSessionObject(req).currentCat+"/"+page);
    }
    @RequestMapping("/changeMail")
    @ResponseBody
    public ResponseEntity<?> changeMail(@RequestParam("email") String email,@RequestParam("pass") String pass, @RequestParam("ssidd")String req) {
		int usrid=getSessionObject(req).User.getId();
		String usrpass=getSessionObject(req).User.getPassword();
    	if(usrpass.equals(pass))
    	{
    		Users.changeEmail(usrid, email);
    		return ResponseEntity.ok("Email changed");
    	}
    	return ResponseEntity.ok("Error");
    }
    @RequestMapping("/changePass")
    @ResponseBody
    public ResponseEntity<?> changePass(@RequestParam("pass") String pass,@RequestParam("oldpass") String oldpass, @RequestParam("ssidd")String req) {
		int usrid=getSessionObject(req).User.getId();
		String usrpass=getSessionObject(req).User.getPassword();
    	if(usrpass.equals(oldpass))
    	{
    		Users.changePass(usrid, pass);
    		return ResponseEntity.ok("Password changed");
    	}
    	return ResponseEntity.ok("Error");
    }
    @GetMapping("/show/search/{searchedtext}/{category}/{page}")
    @ResponseBody
    public ModelAndView productViewMethodShow(@PathVariable("searchedtext") String text,@PathVariable("category") String category,@PathVariable("page") int page, HttpServletRequest req) {
    	getSessionObject(req).lastURL="/show/search/"+text+"/"+category+"/"+page;
    	if(getSessionObject(req).searchedBool==false) {
    		this.productViewMethod(String.valueOf(page), text, category, req);
    	}
    	getSessionObject(req).searchedBool=false;
    	return new ModelAndView("search");
    }
    @GetMapping("/show/search/{category}/{page}")
    @ResponseBody
    public ModelAndView searchByCategory(@PathVariable("category") String category,@PathVariable("page") int page, HttpServletRequest req) {
    	getSessionObject(req).lastURL="/show/search/"+category+"/"+page;
    	if(getSessionObject(req).searchedBool==false) {
    		this.productViewMethod(String.valueOf(page), "", category, req);
    	}
    	getSessionObject(req).searchedBool=false;
    	return new ModelAndView("search");
    }
    /*private List<shopentity> searchProducts(String search,String cat_Id) {
    	String sql = "SELECT * FROM shopentity WHERE category_id like '"+cat_Id+"%' and (name like '%"+search+"%' or description like '%"+search+"%')";
    	List<Map<String, Object>> rows = sup.getJdbcTemplate().queryForList(sql);
		
		List<shopentity> result = new ArrayList<shopentity>();
		for(Map<String, Object> row:rows){
			shopentity Prod = new shopentity();
			Prod.setId((int)row.get("id"));
			Prod.setName((String)row.get("name"));
			Prod.setDescription((String)row.get("description"));
			Prod.setPrice((float)row.get("price"));
			Prod.setId_category((int)row.get("id_category"));
			Prod.setImg_src((String)row.get("img_src"));
			result.add(Prod);
		}
		
		return result;
	
    }*/
    @GetMapping("/getContent")
    @ResponseBody
    public String getcontent(@RequestParam("ssidd") String req) throws IOException {
    	String pom = "";
    	File convertFile = new File("./src/main/resources/static/show/productscontents/product"+getSessionObject(req).CurrentProduct.getId()+".html");
	      convertFile.getAbsolutePath();
	      if(convertFile.exists())
	      {
	      convertFile.createNewFile();
	      FileInputStream fout = new FileInputStream(convertFile);
	      pom=new String(fout.readAllBytes());
	      fout.close();
	      }
        return pom;
    }
    @RequestMapping("/getCategories")
    @ResponseBody
    public String getCategoriesMethodDrop(@RequestParam("ssidd") String ssidd) {
    	sessionObjects obiekt = getSessionObject(ssidd);
		if(categorylinedrop==null || changeInCat)
		{
			categorylinedrop="";
			if( obiekt==null||obiekt.currentCat==0)
				categorylinedrop+="<a class=\"dropdown-toggle\" id=\"categoryplaceholder\" href=\"#\">Wszystkie Kategorie</a>";
			else
				categorylinedrop+="<a class=\"dropdown-toggle\" id=\"categoryplaceholder\" href=\"#\">"+AllCategories.getById(obiekt.currentCat).getName()+"</a>";
			categorylinedrop+="<ul class=\"dropdown\"><li><a href=\"#\" id='cat0' onclick='setCategory(0)'> Wszystkie Kategorie</a></li>";
			for(int i=1;i<10;i++)
				categorylinedrop+=getOptGrDrop(i);
			categorylinedrop+="</ul>";
			changeInCat=false;
		}
		return categorylinedrop;
}
	@GetMapping("/getMainProducts")
    @ResponseBody
    public String getPopularProducts(@RequestParam("ssidd") String req) {
		String getpom="";
		int licznik=0;
		if(getSessionObject(req).User!=null && getSessionObject(req).User.getId()>0)
		{
			String[] pom = null;
			pom = this.splitString(UsersInfo.getById(getSessionObject(req).User.getId()).getId_visited_categories());
			if(pom!=null && pom.length>0)
			{
				getpom+="<h3>Wybrane dla Ciebie:</h3><hr>";
				List<shopentity> Proposition = new ArrayList<>();
				for(int i=0;(i<pom.length  && licznik<50);i++) 
					Proposition = Products.getMostVisited(pom[i]);
					if(Proposition!=null && Proposition.size()>0)
					for(int j=0;(j<Proposition.size() && j<10 && licznik<50);j++)
					{
						licznik++;
						getpom+=this.productMiniatureTemplate(Proposition.get(j));
					}
			}
		}
		if(licznik<50) {
			getpom+="<hr><h3>Najpopularniejsze:</h3><hr>";
			List<shopentity> Proposition = new ArrayList<>();
			Proposition = Products.getMostVisited("");
			if(Proposition!=null && Proposition.size()>0)
				for(int i=0;(i<50 && licznik<50 && i<Proposition.size());i++)
					getpom+=this.productMiniatureTemplate(Proposition.get(i));
		}
		if(licznik<50){
			List<shopentity> Proposition = new ArrayList<>();
			Proposition = Products.findAll();
			if(Proposition!=null && Proposition.size()>0)
				for(int i=0;(i<50 && licznik<50 && i<Proposition.size());i++)
					getpom+=this.productMiniatureTemplate(Proposition.get(i));
		}
		return getpom;
}
	@GetMapping("/getMyProducts")
    @ResponseBody
    public String getMyProducts(@RequestParam("ssidd") String req) {
		String getpom="";
		if(getSessionObject(req).User!=null && getSessionObject(req).User.getId()>0)
		{
			String[] pom = null;
			pom = this.splitString(UsersInfo.getById(getSessionObject(req).User.getId()).getId_products());
			if(pom!=null && pom.length>0)
			{
				List<shopentity> Proposition = Products.findAll();
				for(int i=0;(i<pom.length);i++) 
					getpom+=this.productMiniatureTemplate(Proposition.get(Integer.valueOf(pom[i])-1));
			}
			else {
				getpom="<p> Nie znaleziono wyników </p>";
			}
		}
		return getpom;
}
private String getOptGrDrop(int id) {
	String pom="";
	List<categories> wyniki=AllCategories.findByAttributes(String.valueOf(id));
	for(int i=0;i<wyniki.size();i++) {
		//System.out.println(wyniki.get(i).getName());
		if(wyniki.get(i).getId()==id) {
    		pom+="<li><a class=\"dropdown-toggle\" href=\"#\" >"+wyniki.get(i).getName()+"</a>\r\n"
    				+ "    <ul class=\"dropdown\"><li><a href=\"#\" id='cat"+wyniki.get(i).getId()+"' onclick='setCategory("+wyniki.get(i).getId()+")'> Wszystkie w "+wyniki.get(i).getName()+"</a></li>";
    		pom+=getOptGrDrop(id*10+1);
	    	pom+="</ul></li>";
		}
	}
	return pom;
}
    
    
    @RequestMapping("show/product/addcomment")
    @ResponseBody
    public RedirectView addcomment(@RequestParam("commenttext") String text, @RequestParam("ssidd") String req) {
    	if(getSessionObject(req).User!=null) {
    		int idUser=getSessionObject(req).User.getId();
    		int idProd=getSessionObject(req).CurrentProduct.getId();
    		Comments.insertComment(idProd, idUser, text);
    	}
        return new RedirectView(getSessionObject(req).lastURL);
    }
    @RequestMapping(value="show/editcomment/{id}")
    @ResponseBody
    public RedirectView editcomment(@PathVariable("id") int id,@RequestParam(value="commenttext") String text, @RequestParam("ssidd") String req) {
    	if(Comments.getById(id)!=null) {
    		Comments.updateComment(id, text);
    	}
        return new RedirectView(getSessionObject(req).lastURL);
    }
    @RequestMapping("show/deletecomment/{id}")
    @ResponseBody
    public RedirectView deletecomment(@PathVariable("id") int id, @RequestParam("ssidd") String req) {
    	if(Comments.getById(id)!=null) {
    		Comments.deleteById(id);
    	}
        return new RedirectView(getSessionObject(req).lastURL);
    }
    
    @GetMapping("/getDelivery")
    @ResponseBody
    public String showdeliverys() {
    	String pom = "";
    	if(Deliverys.findAll()!=null) {
    		List<deliveryoptions> listaD = Deliverys.findAll();
    		deliveryoptions delopt;
    		pom+="<option id=\"delopt0\" value=\""+listaD.size()+"\"></option>";
    		for(int i=0;i<listaD.size();i++)
    		{
    			delopt=listaD.get(i);
    			pom+="<option id=\"delopt"+delopt.getId()+"\" value=\""+delopt.getId()+"\"></option>";
    		}
    	}
        return pom;
    }
    @RequestMapping("setNewProductName")
    @ResponseBody
    public ResponseEntity<?> setNewProductName(@RequestParam("name") String name, @RequestParam("ssidd") String req) {
    	getSessionObject(req).NewProduct.setName(name);
    	return ResponseEntity.ok("Success");
    }
    @RequestMapping("setNewProductCategory")
    @ResponseBody
    public ResponseEntity<?> setNewProductCategory(@RequestParam("category") String category, @RequestParam("ssidd") String req) {
    	if(category.isBlank())
    		return ResponseEntity.ok("Blank parameter");
    	getSessionObject(req).NewProduct.setId_category(Integer.valueOf(category));
    	return ResponseEntity.ok("Success");
    }
    @RequestMapping("setNewProductPrice")
    @ResponseBody
    public ResponseEntity<?> setNewProductPrice(@RequestParam("price") String price, @RequestParam("ssidd") String req) {
    	if(price.isBlank())
    		return ResponseEntity.ok("Blank parameter");
    	else
    		System.out.println(price);
    	getSessionObject(req).NewProduct.setPrice(Float.valueOf(price));
    	return ResponseEntity.ok("Success");
    }
    @RequestMapping("setNewProductDescription")
    @ResponseBody
    public ResponseEntity<?> setNewProductDesc(@RequestParam("description") String description, @RequestParam("ssidd") String req) {
    	getSessionObject(req).NewProduct.setDescription(description);
    	return ResponseEntity.ok("Success");
    }
    @RequestMapping("setNewProductDelivery")
    @ResponseBody
    public ResponseEntity<?> setNewProductDelivery(@RequestParam("deliverys") String deliverys[], @RequestParam("ssidd") String req) {
    	String pom=null;
    	for(int i=0;i<deliverys.length;i++)
    		if(pom==null)
    			pom=deliverys[i]+"#";
    		else
    			pom+=deliverys[i]+"#";
    	getSessionObject(req).NewProductContent.setId_delivery(pom);
    	return ResponseEntity.ok("Success");
    }
    
    @GetMapping("/getnewprod")
    @ResponseBody
    public String getnewprod(@RequestParam("ssidd") String req) {
    	String pom = "";
    	if(getSessionObject(req).NewProduct.getName()==null) {
    		pom+="<input type=\"text\" id=\"productname\" name=\"name\" placeholder=\"Wpisz nazwę produktu\" onchange=\"setName()\">";
    	}
    	else {
    		pom+="<input type=\"text\" id=\"productname\" name=\"name\" placeholder=\"Wpisz nazwę produktu\" value=\""+getSessionObject(req).NewProduct.getName()+"\" onchange=\"setName()\">"
    				+"<script>document.getElementById(\"title\").innerHTML=\""+getSessionObject(req).NewProduct.getName()+" - sklep\";</script>";
    	}
    	if(getSessionObject(req).NewProduct.getId_categorie()==0) {
    		pom+="		<input style=\"display:none\" type=\"text\" id=\"category\" name=\"id_category\" onchange=\"setCategoryF()\">"
    				+"<div id=\"popselect\">"
    			    + "			<div id=\"nav\">"
    			    +this.getCategoriesMethodDrop(req)
    			    +"			</div>"
    			    + "		</div>";
    	}
    	else {
    		pom+="		<input style=\"display:none\" type=\"text\" id=\"category\" name=\"id_category\" value=\""+getSessionObject(req).NewProduct.getId_categorie()+"\" onchange=\"setCategoryF()\">"
    				+"<div id=\"popselect\">"
    			    + "			<div id=\"nav\">"
    			    +this.getCategoriesMethodDrop(req)
    			    +"			</div>"
    			    + "		</div>"
    			    + "<script> setCategoryP("+getSessionObject(req).NewProduct.getId_categorie()+") </script>";
    	}
    		if(getSessionObject(req).NewProduct.getDescription()==null) {
        		pom+="<input type=\"text\" id=\"productdesc\" name=\"description\" placeholder=\"Dodaj opis produktu\" onchange=\"setDescription()\">";
        	}
        	else {
        		pom+="<input type=\"text\" id=\"productdesc\" name=\"description\" value=\""+getSessionObject(req).NewProduct.getDescription()+"\" placeholder=\"Dodaj opis produktu\" onchange=\"setDescription()\">";
        	}
    		if(getSessionObject(req).NewProduct.getPrice()>0) {
    			pom+="		<div style=\"display:flex\">"
        			    + "		<input type=\"text\" id=\"productprice\" name=\"price\" value=\""+getSessionObject(req).NewProduct.getPrice()+"\" placeholder=\"Podaj cenę produktu\" onchange=\"setPrice()\"><label for=\"price\">zł</label>"
        			    + "		</div>";
        	}
        	else {
        		pom+="		<div style=\"display:flex\">"
        			    + "		<input type=\"text\" id=\"productprice\" name=\"price\" placeholder=\"Podaj cenę produktu\" onchange=\"setPrice()\"><label for=\"price\">zł</label>"
        			    + "		</div>";
        	}
    		
        return pom;
    }
    @GetMapping("/getnewproddeliverys")
    @ResponseBody
    public String getnewproddeliverys(@RequestParam("ssidd") String req) {
    	String pom = "";
    		pom+="<select id=\"delopt\" name=\"delivery\" multiple>"
    				+ showdeliverys()
    				+ "		</select>\r\n"
    				+ "		<select id=\"deloptpom\" multiple>\r\n"
    				+ "			<option id=\"deloptpom0\" onclick=\"re_setAll()\">Wybierz Wszystkie</option>"
    				+ showdeliveryspom()
    				+ "		</select>";
    	if(getSessionObject(req).NewProductContent.getId_delivery()!=null && !getSessionObject(req).NewProductContent.getId_delivery().isBlank())
    	{
    		String pom2 [] = this.splitString(getSessionObject(req).NewProductContent.getId_delivery());
    		pom+="<script>";
    		if(pom2.length==Deliverys.findAll().size())
    			pom+="re_setAllP();";
    		else
    		for(int i=0;i<pom2.length;i++) {
    			pom+="selectoptionP("+pom2[i]+");";
    		}
    		pom+="</script>";
    	}
    	else
    		pom+="<script>selectoptionP(1);</script>";
        return pom;
    }
    @GetMapping("/getContentText")
    @ResponseBody
    public String getcontenttext(@RequestParam("ssidd")String req) throws IOException {
    	String pom = "";
    	File convertFile = new File("./src/main/resources/static/show/productscontents/product"+getSessionObject(req).NewProduct.getId()+".html");
	      convertFile.getAbsolutePath();
	      if(convertFile.exists())
	      {
	      convertFile.createNewFile();
	      FileInputStream fout = new FileInputStream(convertFile);
	      pom=new String(fout.readAllBytes());
	      fout.close();
	      }
        return pom;
    }
    /*<select id="delopt" name="delivery" multiple>
			<c:import url="http://localhost:8080/getDelivery"></c:import>
		</select>
		<select id="deloptpom" multiple>
			<option id="deloptpom0" onclick="re_setAll()">Wybierz Wszystkie</option>
			<c:import url="http://localhost:8080/getDeliveryPom"></c:import>
		</select>*/
    @GetMapping("/getDeliveryPom")
    @ResponseBody
    public String showdeliveryspom() {
    	String pom = "";
    	if(Deliverys.findAll()!=null) {
    		List<deliveryoptions> listaD = Deliverys.findAll();
    		deliveryoptions delopt;
    		for(int i=0;i<listaD.size();i++)
    		{
    			delopt=listaD.get(i);
    			pom+="<option id=\"deloptpom"+delopt.getId()+"\" onclick=\"selectoption("+delopt.getId()+")\">"+delopt.getService_name()+" - "+delopt.getDeliver()+" - "+delopt.getPrice()+"</option>";
    		}
    	}
        return pom;
    }
    @GetMapping("/show/comments")
    @ResponseBody
    public String showcomment(@RequestParam("ssidd") String req) {
    	String pom = "<p id=\"nocomments\">Nie znalezionio komentarzy dla tego produktu</p>";
    	if(getSessionObject(req).CurrentProduct!=null) {
    		List<comments> komentarze = Comments.findByProduct(getSessionObject(req).CurrentProduct.getId());
    		comments komentarz;
    		if(komentarze.size()>0) {
    			pom="";
    		for(int i=0;i<komentarze.size();i++) {
    			komentarz = komentarze.get(i);
    			pom+="<div class=\"commentdivblock\"><form action=\""+server+"/show/editcomment/"+komentarz.getId()+"\" method=\"post\" id=\"comment"+i+"\" class=\"commentsentry\">"
    					+ "<div style=\"display: flex;\"> <div id=\"commentwritebyuser"+i+"\" class=\"commentwritebyuser\">"+Users.getUserById(komentarz.getId_user()).getLogin()+"</div>";
    			if(getSessionObject(req).User!=null && getSessionObject(req).User.getId()!=komentarz.getId_user() && getSessionObject(req).getUserPrevillages(UsersPrev)<253 && !komentarz.getText().isBlank())
    				pom+="<a class=\"editcomment\" href=\""+server+"/report/comment/"+komentarz.getId()+"\">Zgłoś</a>";
    			if((getSessionObject(req).User!=null && getSessionObject(req).User.getId()==komentarz.getId_user() && !komentarz.getText().isBlank())|| getSessionObject(req).getUserPrevillages(UsersPrev)>=253) {
    			pom+= "<a class=\"editcomment\" id=\"editcomment"+i+"\" onclick=\"editcomment("+i+")\">Edytuj</a>";
    			pom+="<a style=\"display: none;\" class=\"editcomment\" id=\"editcommentsubmit"+i+"\" onclick=\"editcommentsubmit("+i+")\">Zapisz</a>";
    			}
    			if(getSessionObject(req).getUserPrevillages(UsersPrev)>=253 && getSessionObject(req).User.getId()!=komentarz.getId_user())
    				pom+="<a style=\"display: none;\" class=\"editcomment\" id=\"deletecomment"+i+"\" onclick=\"deletecommentsubmitmoderator("+i+")\">Skasuj komentarz</a>";
    			else if(getSessionObject(req).User!= null && getSessionObject(req).User.getId()==komentarz.getId_user()){
    				pom+="<a style=\"display: none;\" class=\"editcomment\" id=\"deletecomment"+i+"\" href=\""+server+"/show/deletecomment/"+komentarz.getId()+"\">Skasuj komentarz</a>";
    			}
    			pom+="</div>";
    			if(!komentarz.getText().isBlank())
    			pom+="<textarea name=\"commenttext\" readonly maxlength=\"200\" id=\"commenttext"+i+"\" class=\"commenttext\">"+komentarz.getText()+"</textarea>" ;
    			else
    				pom+="<textarea name=\"commenttext\" readonly maxlength=\"200\" id=\"commenttext"+i+"\" class=\"commenttext\">Komentarz został skasowany przez Moderatora!</textarea>" ;
    			pom+="</form></div>";
    			}
    		}
    	}
        return pom;
    }
    
	public List<String> splitID(String id_to_split){
    	List<String> listaid = new ArrayList<String>();
    	if(id_to_split!=null) {
    	String pom=id_to_split,pom2="";
    	int n = pom.length();
    	int size=0;
    	for(int i=0;i<n;i++) {
    		if(pom.charAt(i)!=';')
    		{
    			pom2+=pom.charAt(i);
    		}
    		else {
    			listaid.add(size,pom2);
    			size++;
    			pom2="";
    		}
    	}
    	return listaid;
    	}
    	return null;
    }
    
    private String getDeliverys(@RequestParam("ssidd") String req) {
    	String[] IDs = splitString(getSessionObject(req).CurrentProductContent.getId_delivery());
    	String result="";
    	result+="<div id=\"deliveryTable\">";
    	if(IDs!=null && IDs.length>0)
    	for(int i=0;i<IDs.length;i++)
    	{
    	deliveryoptions delopt=Deliverys.getById(Integer.valueOf(IDs[i]) );
    	result+="<div class=\"deliveryClass\">";
    	result+="<p id=\"deliveryName\">"+delopt.getService_name()+" - </p>";
    	if(!delopt.getDeliver().isBlank())
    	result+="<p id=\"deliver\">"+delopt.getDeliver()+" - </p>";
    	if(delopt.getTime()!=0)
    	result+="<p id=\"deliveryTime\">"+delopt.getTime()+" - </p>";
    	if(delopt.getPrice()!=0)
    	result+="<p id=\"deliveryPrice\">"+delopt.getPrice()+" - </p>";
    	result+="</div>";
    	}
    	result+="</div>";
    	return result;
    }
    
    /*@GetMapping("/product/view/{id}")
    @ResponseBody
    public ModelAndView productViewMethod(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
    	CurrentProduct = Products.getById(id);
    	System.out.println(CurrentProduct.getName());
    	CurrentProductContent = ProductsContent.getById(id);
    	//CurrentProductInfo = ProductsInfo.getById(id);
    	redirectAttributes.addFlashAttribute("currentproduct", CurrentProduct);
    	redirectAttributes.addFlashAttribute("currentproductcontent", CurrentProductContent);
        return new ModelAndView("product");
    }*/
    @GetMapping(path="/all")
    public @ResponseBody List<user> getAllProducts() {
    	return Users.findAll();
    }
    @GetMapping("/show/getProduct")
    @ResponseBody
    public String getProductView(@RequestParam("ssidd") String req) {
    	String pom="";
    	if(getSessionObject(req).CurrentProduct!=null)
    	{
    		String[] pom2 = this.splitString(getSessionObject(req).CurrentProduct.getImg_src());
    		pom+="<div class='slider'>";
    		if(pom2!=null)
    		for(int i=0;i<pom2.length;i++)
    		pom+="<img class=\"slide\" id=\"pgallery"+i+"\" src=\""+server+"/getImg/"+getSessionObject(req).CurrentProduct.getId()+"_/"+pom2[i]+"\">\r\n";
    			pom	+= "</div>	<h1 id=\"ptitle\">"+getSessionObject(req).CurrentProduct.getName()+"</h1>\r\n"
    				+ " <script>document.getElementById(\"tytulstrony\").innerHTML=document.getElementById(\"ptitle\").innerHTML+\" - Sklep\"</script>"
    				+ "		<p id=\"pdescription\">"+getSessionObject(req).CurrentProduct.getDescription()+"</p>\r\n"
    				+ "		<p id=\"pprice\">"+getSessionObject(req).CurrentProduct.getPrice()+this.fillPrice(getSessionObject(req).CurrentProduct.getPrice())+"zł</p>\r\n"
    				+ "<div class=\"deliverylist\">"+getDeliverys(req)+"</div>";
    				
    	}
    	return pom;
    }
    @GetMapping("/show/product/")
    @ResponseBody
    public RedirectView productViewH(HttpServletRequest req) {
        return new RedirectView("/show/product/"+getSessionObject(req).currentproductid);
    }
    @GetMapping("/showIMGs")
    @ResponseBody
    public String showimgs(@RequestParam("ssidd") String req) {
    	String imgpom="<ul class=\"slides\">";
    	if(getSessionObject(req).NewProduct!=null)
    	{
    		String[] pom2 = null;
    		if(getSessionObject(req).NewProduct.getImg_src()!=null)
    		pom2=splitString(getSessionObject(req).NewProduct.getImg_src());
    		if(pom2!=null)
    		for(int i=0;i<pom2.length;i++)
    			imgpom+="<li class=\"slide\"><div class=\"productimages\"><a class=\"closebutton\" "
    					+ "href=\"/deleteimg/"+i+"\">x</a><img src=\""+server+"/getImg/"+getSessionObject(req).NewProduct.getId()+"_/"+pom2[i]+"\"></div></li>";
    	}
    		imgpom+="</ul>";
        return imgpom;
    }
    @GetMapping("/showINFO")
    @ResponseBody
    public String showinfo(@RequestParam("ssidd") String req) {
    	String imgpom="";
    	imgpom+="<div class=\"productinfo\">"+getSessionObject(req).NewProduct.getName()+"</div>";
    	imgpom+="<div class=\"productinfo\">"+getSessionObject(req).NewProduct.getDescription()+"</div>";
    	imgpom+="<div class=\"productinfo\">"+getSessionObject(req).NewProduct.getPrice()+this.fillPrice(getSessionObject(req).NewProduct.getPrice())+"</div>";
    	String[] pom=this.splitString(getSessionObject(req).NewProductContent.getId_delivery());
    	imgpom+="<ul id='deliverylist'>";
    	for(int i=0;i<pom.length;i++)
    		imgpom+="<li>"+Deliverys.getById(Integer.valueOf(pom[i])).getService_name()+" - "+Deliverys.getById(Integer.valueOf(pom[i])).getPrice()+this.fillPrice(Deliverys.getById(Integer.valueOf(pom[i])).getPrice())+"zł - do "+Deliverys.getById(Integer.valueOf(pom[i])).getTime()+" dni"+"</li>";
    	imgpom+="</ul>";
        return imgpom;
    }
    ;
    @GetMapping("/getContentImgs")
    @ResponseBody
    public String showimgscontent(@RequestParam("ssidd") String req) {
    	String imgpom="<ul class=\"slides\">";
    	if(getSessionObject(req).NewProduct!=null)
    	{
    		String[] pom2 = null;
    		if(getSessionObject(req).NewProduct.getImg_src()!=null)
    		pom2=splitString(getSessionObject(req).NewProduct.getImg_src());
    		if(pom2!=null)
    		for(int i=0;i<pom2.length;i++)
    			imgpom+="<li class=\"slide\"><div class=\"productimages\"><img id=\"img"+i+"\" src=\""+server+"/getImg/"+getSessionObject(req).NewProduct.getId()+"_/"+pom2[i]+"\"</div></li>";
    		if(!getSessionObject(req).contentimg.isBlank()) {
    			pom2=splitString(getSessionObject(req).contentimg);
    			for(int i=0;i<pom2.length;i++)
        			imgpom+="<li class=\"slide\"><div class=\"productimages\"><img id=\"imgc"+i+"\" src=\""+server+"/getconImg/"+getSessionObject(req).NewProduct.getId()+"_/"+pom2[i]+"\"></div></li>";
    		}
			imgpom+="<li class=\"slide\"><div class=\"productimages\"><div id=\"addimg\" onclick=\"addImg()\">+</div></div></li>";
    	}
    		imgpom+="</ul>";
        return imgpom;
    }
    @RequestMapping(value = "/getconImg/{id_product}/{img_name}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getConImageAsResponseEntity(@PathVariable("id_product") String path_f,@PathVariable("img_name") String name) throws IOException {
	    HttpHeaders headers = new HttpHeaders();
	    File convertFile = new File("./src/main/resources/static/show/productscontents/con_"+path_f+name);
	      convertFile.getAbsolutePath();
	    FileInputStream in = new FileInputStream(convertFile);
	    byte[] media = IOUtils.toByteArray(in);
	    in.close();
	    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	    
	    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
	    return responseEntity;
	}
    @GetMapping("/deleteimg/{id}")
    @ResponseBody
    public RedirectView delimgs(@PathVariable("id") int id, @RequestParam("ssidd") String req) {
    	String imgpom=null;
    	String[] pom2 = null;
    		if(getSessionObject(req).NewProduct.getImg_src()!=null)
    			pom2=splitString(getSessionObject(req).NewProduct.getImg_src());
    		if(pom2!=null)
    		for(int i=0;i<pom2.length;i++) {
    			if(i!=id) {
    				if(imgpom==null)
    					imgpom=pom2[i]+"#";
    				else
    				imgpom+=pom2[i]+"#";
    			}
    			else
    				deleteFile(pom2[i], req);
    		}
    		getSessionObject(req).NewProduct.setImg_src(imgpom);
        return new RedirectView("/"+getSessionObject(req).lastURL);
    }
    private String[] splitString(String toSplit){
    	String[] list= null;
    	if(toSplit!=null)
    		list = toSplit.split("#");
    	return list;
    }
    @GetMapping("/show/product/{id}")
    @ResponseBody
    public ModelAndView productView(@PathVariable("id") int id, HttpServletRequest req) {
    	String path=""+id;
    	getSessionObject(req).lastURL="/show/product/"+id;
    	if(path.isBlank())
    	{
    		productView(getSessionObject(req).currentproductid, req);
    	}
    	else
    	{
    		getSessionObject(req).currentproductid=id;
    	}
    	getSessionObject(req).CurrentProduct = Products.getById(id);
    	getSessionObject(req).CurrentProductContent = ProductsContent.getById(id);
		ProductsInfo.getById(id).setViews(ProductsInfo.getById(id).getViews()+1);
		ProductsInfo.flush();	
		this.addUserCategory(Products.getById(id).getId_categorie(), req.getSession().getId());
        return new ModelAndView("product");
    }
    @PostMapping("/createnewproduct")
    public ResponseEntity<?> createnewproduct(@RequestParam("ssidd") String req) {
    		float min_cost=0;
    		Products.insertProduct(getSessionObject(req).NewProduct.getId(),getSessionObject(req). NewProduct.getName(),
    				getSessionObject(req).NewProduct.getDescription(), getSessionObject(req).NewProduct.getImg_src(),
    				getSessionObject(req).NewProduct.getId_categorie(), getSessionObject(req).NewProduct.getPrice());
	    	ProductsContent.insertProductCon(getSessionObject(req).NewProduct.getId(), getSessionObject(req).NewProductContent.getId_delivery(), getSessionObject(req).NewProductContent.getContent_path());
	    	String idpom = UsersInfo.getById(getSessionObject(req).User.getId()).getId_products();
	    	UsersInfo.createdpages(getSessionObject(req).User.getId(), idpom+getSessionObject(req).NewProduct.getId()+"#");
	    	String[] pom=this.splitString(getSessionObject(req).NewProductContent.getId_delivery());
	    	min_cost=Deliverys.getById(Integer.valueOf(pom[0])).getPrice();
	    	for(int i=1;i<pom.length;i++)
	    		if(min_cost>Deliverys.getById(Integer.valueOf(pom[i])).getPrice())
	    			min_cost=Deliverys.getById(Integer.valueOf(pom[i])).getPrice();
	    	ProductsInfo.insertProductInfo(getSessionObject(req).NewProduct.getId(), java.time.LocalDate.now(), min_cost);
	    	for(int i=0;i<reservedidsP.size();i++)
	    		if(reservedidsP.get(i)==getSessionObject(req).NewProduct.getId())
	    	reservedidsP.remove(i);
	    	getSessionObject(req).NewProduct = new shopentity();
	    	getSessionObject(req).NewProductContent = new shopentitycontent();
	      return ResponseEntity.ok("Page created successfully.");
	   }
    @PostMapping("/uploadimg")
    public ResponseEntity<?> imgFilesUpload(@RequestParam("file") List<MultipartFile> files, @RequestParam("ssidd") String req) throws IOException {
    	int ssss=Products.findAll().size()+1+reservedidsP.size();
    	this.reservedidsP.add(ssss);
    	getSessionObject(req).NewProduct.setId(ssss);
    	getSessionObject(req).NewProduct.setId_category(0);
    	getSessionObject(req).NewProduct.setPrice((float) 0.0);
    	String imgpom=null;
    	if(getSessionObject(req).NewProduct.getImg_src()!=null)
    		imgpom=getSessionObject(req).NewProduct.getImg_src();
    	for(int i=0;i<files.size();i++) {
    		imgfileUpload(files.get(i), req);
    		if(imgpom==null)
    			imgpom=files.get(i).getOriginalFilename()+"#";
    		else
    		imgpom+=files.get(i).getOriginalFilename()+"#";
    	}
    	getSessionObject(req).NewProduct.setImg_src(imgpom);
    	return ResponseEntity.ok("Files uploaded successfully.");
    }
    @PostMapping("/uploadcontentimg")
    public ResponseEntity<?> imgFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("ssidd") String req) throws IOException {
    	String imgpom="";
    	if(getSessionObject(req).contentimg!=null && !getSessionObject(req).contentimg.isBlank())
    	imgpom = getSessionObject(req).contentimg;
    	imgpom+= file.getOriginalFilename()+"#";
    	File convertFile = new File("./src/main/resources/static/show/productscontents/con_"+getSessionObject(req).NewProduct.getId()+"_"+file.getOriginalFilename());
	      convertFile.getAbsolutePath();
	      convertFile.createNewFile();
	      FileOutputStream fout = new FileOutputStream(convertFile);
	      fout.write(file.getBytes());
	      fout.close();
	      getSessionObject(req).contentimg=imgpom;
    	return ResponseEntity.ok("File uploaded successfully.");
    }
    @PostMapping("/uploadhtml")
    public ResponseEntity<?> htmlfileUpload(@RequestParam("file") MultipartFile file, @RequestParam("ssidd") String req) throws IOException {
    	getSessionObject(req).NewProductContent.setContent_path("../productscontents/product"+getSessionObject(req).NewProduct.getId()+".html");
	      File convertFile = new File("./src/main/resources/static/show/productscontents/product"+getSessionObject(req).NewProduct.getId()+".html");
	      convertFile.getAbsolutePath();
	      if(convertFile.exists())
	    	  convertFile.delete();
	      convertFile.createNewFile();
	      FileOutputStream fout = new FileOutputStream(convertFile);
	      fout.write(file.getBytes());
	      fout.close();
	      return ResponseEntity.ok("File uploaded successfully.");
	   }
    @RequestMapping("/uploadhtmltext")
      public ResponseEntity<?> htmltextUpload(@RequestParam("file") String content, @RequestParam("ssidd") String req) throws IOException {
    	getSessionObject(req).NewProductContent.setContent_path("../productscontents/product"+getSessionObject(req).NewProduct.getId()+".html");
  	      File convertFile = new File("./src/main/resources/static/show/productscontents/product"+getSessionObject(req).NewProduct.getId()+".html");
  	      convertFile.getAbsolutePath();
  	      if(convertFile.exists())
  	    	  convertFile.delete();
  	      convertFile.createNewFile();
  	      FileOutputStream fout = new FileOutputStream(convertFile);
  	      fout.write(content.getBytes());
  	      fout.close();
  	      return ResponseEntity.ok("File created successfully.");
  	   }
    	   public void imgfileUpload( MultipartFile file, @RequestParam("ssidd") String req) throws IOException {
    	      File convertFile = new File("./src/main/resources/static/show/productscontents/img_"+getSessionObject(req).NewProduct.getId()+"_"+file.getOriginalFilename());
    	      convertFile.getAbsolutePath();
    	      convertFile.createNewFile();
    	      FileOutputStream fout = new FileOutputStream(convertFile);
    	      fout.write(file.getBytes());
    	      fout.close();
    	   }
    	   public void deleteFile( String pathsrc, @RequestParam("ssidd") String req) {
     	      File convertFile = new File("./src/main/resources/static/show/productscontents/img_"+getSessionObject(req).NewProduct.getId()+"_"+pathsrc);
     	      convertFile.getAbsolutePath();
     	      convertFile.delete();
     	   }
}