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
import java.time.LocalDate;
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
	private newproductrepository newproducts;
	@Autowired
	private newproductcontentrepository newproductscontent;
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
		if(newproducts.findByUserId(getSessionObject(req).User.getId())!=null && newproducts.findByUserId(getSessionObject(req).User.getId()).size()>0 && !getSessionObject(req).newproductchecked)
		{
			return new ModelAndView("newproduct");
		}
		if(getSessionObject(req).newproductchecked==false)
			this.createNewProduct(req.getSession().getId());
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
	@RequestMapping("/serveroptions/setPrevillages")
	public ModelAndView previllages(HttpServletRequest req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)==255) {
		getSessionObject(req).lastURL="/serveroptions/previllages";
		return new ModelAndView("previllages");
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
	@RequestMapping("/serveroptions/addCategories")
	public ModelAndView addCategories(HttpServletRequest req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
		getSessionObject(req).lastURL="/serveroptions/addCategories";
		return new ModelAndView("categories");
		}
		return firstPage(req);
	}
	@RequestMapping("/serveroptions/deliverys")
	public ModelAndView deliverysoptions(HttpServletRequest req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)==255) {
		getSessionObject(req).lastURL="/serveroptions/deliverys";
		return new ModelAndView("deliverys");
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
			if(type.equals("all") || type.isBlank())
			{
				reps=Reports.findAll();
			}
			else if(type.equals("products")) {
				reps=Reports.findByType(0);
			}
			else if(type.equals("comments")) {
				reps=Reports.findByType(5000);
			}
			else if(type.equals("users")) {
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
			if(type.equals("products")) {
				reps=Communicates.findByType(0);
			}
			else if(type.equals("comments")) {
				reps=Communicates.findByType(5000);
			}
			else if(type.equals("users")) {
				reps=Communicates.findByType(10000);
			}
			if(reps.size()>0)
			for(int i=0;i<reps.size();i++)
			{
				repp=reps.get(i);
				if(!repp.getText().isBlank())
					pom+="<input class=\"communicate\" id=\"communicate"+repp.getId()+"\" value=\""+repp.getText()+"\" read-only=\"true\" placeholder=\"Podaj treść komunikatu\" onclick='editComm(\""+type+"\","+repp.getId()+")'><button style=\"display: none;\" id=\"baton"+repp.getId()+"\" onclick='addComm(\""+type+"\","+repp.getId()+")'>Zapisz</button>";
				else
					pom+="<input type=\"text\" class=\"communicate\" id=\"communicate"+repp.getId()+"\" placeholder=\"Podaj treść komunikatu\"></input><button onclick='addComm(\""+type+"\","+repp.getId()+")'>Dodaj</button>";
			}
			return ResponseEntity.ok(String.valueOf("<div class=\"count\">"+(reps.size()-1)+"</div>"+pom));
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
					if(Communicates.exist(id+1)==null)
						Communicates.insertCommunicates(id+1);
				return ResponseEntity.ok("success");
				}
			}
			return ResponseEntity.ok("failure");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/serveroptions/getCategories")
	@ResponseBody
	public ResponseEntity<?> getCategories(@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			List<categories> cats = new ArrayList<>();
			cats=this.AllCategories.findAll();
			String pom = "";
			if(cats!=null)
			{
				for(int i=0;(i<9 && i<cats.size());i++) {
					pom+="<ul class=\"list\" style=\"display: block; border:1px solid black;\">";
					pom+="<li><input type='text' readonly='true' placeholder='Podaj nazwę kategorii' id='category"+cats.get(i).getId()+"' value='"+cats.get(i).getName()+"'>"
							+ "<div id='concategorybutt"+cats.get(i).getId()+"'>"
							+ "<button id='categorybutt"+cats.get(i).getId()+"' onclick='edit("+cats.get(i).getId()+")'>Edytuj</button>"
							+ "</div>"		
								+ "<button style=\"display: none\" onclick='wykonaj("+cats.get(i).getId()+",\"changeCategories\")' id=\"addbutt"+cats.get(i).getId()+"\">Zapisz zmianę</button>";
					pom+=getCategoriesMethodPom(cats.get(i).getId(),cats);
					pom+="</li></ul>";
				}
				return ResponseEntity.ok(pom);
			}
			return ResponseEntity.ok("Nie znaleziono kategorii");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	private String getCategoriesMethodPom(int id,List<categories> lista) {
			String pom="";
			List<Integer> czyjest = new ArrayList<>();
			int licznik=0;
			for(int i=9;i<lista.size();i++) {
				if(lista.get(i).getId()>=id*10 && lista.get(i).getId()<id*10+10) {
					pom+="<ul class=\"list\" style=\"display: block; margin: 0 5px; border:1px solid black;\">"
							+ "<li><input type='text' readonly='true' placeholder='Podaj nazwę subkategorii' id='category"+lista.get(i).getId()+"' value='"+lista.get(i).getName()+"'> - podkategoria dla: "+AllCategories.getById(id).getName()+""
									+ "<div id='concategorybutt"+lista.get(i).getId()+"'>"
											+ "<button id='categorybutt"+lista.get(i).getId()+"' onclick='edit("+lista.get(i).getId()+")'>Edytuj</button>"
											+ "<button onclick='deleteC("+lista.get(i).getId()+")'>Usuń</button></div>"
													+ "<button style=\"display: none\" onclick='wykonaj("+lista.get(i).getId()+",\"changeCategories\")' id=\"addbutt"+lista.get(i).getId()+"\">Zapisz zmianę</button>";
					pom+=getCategoriesMethodPom(lista.get(i).getId(),lista);
			    	pom+="</li></ul>";
			    	czyjest.add(lista.get(i).getId());
				}
			}
			for(int i=id*10;i<id*10+10;i++)
				if(!czyjest.contains(i))
				{
					licznik=i;
					break;
				}
			if(licznik!=0)
			{
				pom+="<ul class=\"list\" style=\"display: block; margin: 0 5px;\">"
						+ "<li><input type='text' placeholder='Podaj nazwę subkategorii' id='category"+licznik+"'> - podkategoria dla: "+AllCategories.getById(id).getName()+"<button id='categorybutt"+licznik+"' onclick='setCat("+licznik+")'>Dodaj</button>";
		    	pom+="</li></ul>";
			}
		return pom;
	}
	@RequestMapping("/setCategories")
	@ResponseBody
	public ResponseEntity<?> setCategories(@RequestParam("id") int id,@RequestParam("text") String text,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			if(!text.isBlank())
			{
				if(id!=0) {
					AllCategories.insertcategories(id, text);
				this.changeInCat=true;
				return ResponseEntity.ok("success");
				}
			}
			return ResponseEntity.ok("failure");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/changeCategories")
	@ResponseBody
	public ResponseEntity<?> changeCategories(@RequestParam("id") int id,@RequestParam("text") String text,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			if(!text.isBlank())
			{
				if(id!=0) {
					AllCategories.updatecategories(id, text);
				this.changeInCat=true;
				return ResponseEntity.ok("success");
				}
			}
			return ResponseEntity.ok("failure");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/deleteCategories")
	@ResponseBody
	public ResponseEntity<?> deleteCategories(@RequestParam("id") int id,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
				if(id!=0) {
					AllCategories.deletecategories(id);
					this.changeInCat=true;
				return ResponseEntity.ok("success");
				}
			return ResponseEntity.ok("failure");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/serveroptions/getDeliverys")
	@ResponseBody
	public ResponseEntity<?> serveroption_getDeliverys(@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)==255) {
			List<deliveryoptions> cats = new ArrayList<>();
			cats=this.Deliverys.findAll();
			String pom = "";
			if(cats!=null)
			{
				for(int i=0;i<cats.size();i++) {
					pom+="<div style='display: flex;'>"
						+ "<input type='text' readonly='true' id='deliveryname"+cats.get(i).getId()+"' value='"+cats.get(i).getService_name()+"' placeholder='Nazwa usługi'>"
						+ "<input type='text' readonly='true' id='deliveryC"+cats.get(i).getId()+"' value='"+cats.get(i).getDeliver()+"' placeholder='Nazwa usługodawcy'>"
						+ "<input type='number' step='1' min='0' readonly='true' id='deliverytime"+cats.get(i).getId()+"' value='"+cats.get(i).getTime()+"' placeholder='Czas realizacji usługi'>"
						+ "<input type='number' min='0' step='0.01' readonly='true' id='deliveryprice"+cats.get(i).getId()+"' value='"+cats.get(i).getPrice()+"' placeholder='Cena usługi'>"
						+ "<div style='display: flex;' id='butt"+cats.get(i).getId()+"'><button onclick='edit("+cats.get(i).getId()+")'>Edytuj</button>"
						+ "<button onclick='deleteC("+cats.get(i).getId()+")'>Usuń</button></div>"
						+ "<button style='display:none;' onclick='wyslij("+cats.get(i).getId()+",\"changeDeliverys\")' id='send"+cats.get(i).getId()+"'>Zapisz</button>"
						+ "</div>";
				}
				pom+="<div style='display: flex;'>"
						+ "<input type='text' id='deliveryname"+(cats.size()+1)+"' placeholder='Nazwa usługi'>"
						+ "<input type='text' id='deliveryC"+(cats.size()+1)+"' placeholder='Nazwa usługodawcy'>"
						+ "<input type='number' step='1' min='0' id='deliverytime"+(cats.size()+1)+"' placeholder='Czas realizacji usługi'>"
						+ "<input type='number' min='0' step='0.01' id='deliveryprice"+(cats.size()+1)+"' placeholder='Cena usługi'>"
						+ "<button onclick='wyslij("+(cats.size()+1)+",\"setDeliverys\")' id='send"+(cats.size()+1)+"'>Dodaj</button>"
						+ "</div>";
				return ResponseEntity.ok(pom);
			}
			return ResponseEntity.ok("Nie znaleziono kategorii");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/setDeliverys")
	@ResponseBody
	public ResponseEntity<?> setDeliverys(@RequestParam("id") int id,@RequestParam("name") String name,@RequestParam("deliver") String deliver,@RequestParam("time") int time,@RequestParam("price") float price,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			if(!name.isBlank())
			{
				if(id!=0) {
					Deliverys.insertDeliverys(id, name,deliver,time,price);
				return ResponseEntity.ok("success");
				}
			}
			return ResponseEntity.ok("failure");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/changeDeliverys")
	@ResponseBody
	public ResponseEntity<?> changeDeliverys(@RequestParam("id") int id,@RequestParam("name") String name,@RequestParam("deliver") String deliver,@RequestParam("time") int time,@RequestParam("price") float price,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			if(!name.isBlank())
			{
				if(id!=0) {
					Deliverys.updateDeliverys(id, name, deliver, time, price);
				return ResponseEntity.ok("success");
				}
			}
			return ResponseEntity.ok("failure");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/deleteDeliverys")
	@ResponseBody
	public ResponseEntity<?> deleteDeliverys(@RequestParam("id") int id,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
				if(id!=0) {
					Deliverys.deleteDeliverys(id);
				return ResponseEntity.ok("success");
				}
			return ResponseEntity.ok("failure");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/getUsers/{type}")
	@ResponseBody
	public ResponseEntity<?> getUsers(@PathVariable("type") String type,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250) {
			List<previllages> reps = new ArrayList<>();
			user searchedUser = null;
			String pom="";
			previllages repp;
			if(type.equals("all") || type.isBlank()) {
				reps=UsersPrev.findAll();
			}
			else{
				searchedUser=Users.getUserByLogin(type);
			}
			if(reps.size()>0 || searchedUser!=null) {
				for(int i=0;i<reps.size();i++)
				{
					repp=reps.get(i);
					pom+="<div class=\"Usercontainer\"><div class=\"userinfo\"><div id='userlogin"+repp.getId_user()+"'>"+Users.getById(repp.getId_user()).getLogin()+"</div> - poziom uprawnień: "+repp.getLevel()+"</div>";
					if(getSessionObject(req).getUserPrevillages(UsersPrev)==255 && !getSessionObject(req).User.getLogin().equals(Users.getById(repp.getId_user()).getLogin()))
					pom+="<button onclick='changePrevs("+repp.getId_user()+")'>Zmień uprawnienia</button><button onclick='deletePrevs("+repp.getId()+","+repp.getId_user()+")'>Usuń uprawnienia</button>";
					pom+="</div>";
				}
				if(searchedUser!=null)
				{
					repp=UsersPrev.findByUserId(searchedUser.getId());
					pom+="<div class=\"Usercontainer\"><div class=\"userinfo\"><div id='userlogin"+searchedUser.getId()+"'>"+searchedUser.getLogin()+"</div> - poziom uprawnień: ";
					if(repp!=null)
						pom+=repp.getLevel()+"</div>";
					else
						pom+="0</div>";
					if(getSessionObject(req).getUserPrevillages(UsersPrev)==255 && !getSessionObject(req).User.getLogin().equals(searchedUser.getLogin())) {
						if(repp!=null)
							pom+="<button onclick='changePrevs("+repp.getId_user()+")'>Zmień uprawnienia</button><button onclick='deletePrevs("+repp.getId()+","+repp.getId_user()+")'>Usuń uprawnienia</button>";
						else
							pom+="<button onclick='setPrevs("+searchedUser.getId()+")'>Nadaj uprawnienia</button>";
					}
					pom+="</div>";
				}
			return ResponseEntity.ok(pom);
			}
			return ResponseEntity.ok(String.valueOf("<p>Nie znaleziono wyników</p>"));
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/setPrevs/{id}")
	@ResponseBody
	public ResponseEntity<?> setPrevs(@PathVariable("id") int id,@RequestParam("level") int level,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)==255) {
			if(UsersPrev.findByUserId(id)==null)
				UsersPrev.insertPrev(id,(short) level);
			ResponseEntity.ok("success");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/changePrevs/{id}")
	@ResponseBody
	public ResponseEntity<?> changePrevs(@PathVariable("id") int id,@RequestParam("level") int level,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)==255) {
			if(UsersPrev.findByUserId(id)!=null)
				UsersPrev.updatePrev(id,(short) level);
			ResponseEntity.ok("success");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@RequestMapping("/deletePrevs/{id}")
	@ResponseBody
	public ResponseEntity<?> deletePrevs(@PathVariable("id") int id,@RequestParam("ssidd") String req) {
		if(getSessionObject(req).getUserPrevillages(UsersPrev)==255) {
			if(UsersPrev.findByUserId(id)!=null) {
				UsersPrev.deletePrev(id);
				UsersPrev.deletePrevM(UsersPrev.findAll().size(), id);
			}
			ResponseEntity.ok("success");
		}
		return ResponseEntity.ok("Nie posiadasz uprawnień");
	}
	@GetMapping("/adminSettings")
	@ResponseBody
	public String adminSettings(@RequestParam("ssidd") String req) {
		String pom="<p style=\"text-align: center\">Panel Administracyjny</p><br>";
		if(getSessionObject(req).getUserPrevillages(UsersPrev)>250)
		{
			pom+="<button class=\"settings\" onclick=\"reports()\">Zgłoszenia</button>";
			pom+="<button class=\"settings\" onclick=\"addcommunicates()\">Komunikaty zgłoszeń</button>";
			pom+="<button class=\"settings\" onclick=\"addcategories()\">Dodaj kategorie</button>";
			if(getSessionObject(req).getUserPrevillages(UsersPrev)==255) {
				pom+="<button class=\"settings\" onclick=\"adddeliverys()\">Dodaj opcje wysyłek</button>";
				pom+="<button class=\"settings\" onclick=\"setprevillages()\">Nadaj uprawnienia</button>";
			}
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
    	if(getSessionObject(req).User!=null) {
    		getSessionObject(req).clearSession();
    		return new RedirectView(getSessionObject(req).lastURL);
    	}
    	return new RedirectView("/main");
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
  
    @GetMapping("/showNewProductsMiniatures")
    @ResponseBody
    public String showNewProductsMiniatures(@RequestParam("ssidd") String req) {
    	List<newproduct> listka = newproducts.findByUserId(getSessionObject(req).User.getId());
    	String pom ="Zapisane wersje Robocze<br>";
    	newproduct pomp = new newproduct();
    	if(listka!=null && listka.size()>0)
    		for(int j=0;j<listka.size();j++) {
    			pomp = listka.get(j);
    			pom += "<a href='"+server+"/editNewProduct/"+pomp.getId()+"'><div style='display: grid; width: 50%;height: 200px;overflow-y: hidden;'><a class=\"closebutton\" href=\""+server+"/deleteNewProduct/"+pomp.getId()+"\">x</a>";
    			pom+="<ul class=\"slides\">";
    			
    		String[] pom2 = null;
    		if(pomp.getImg_src()!=null)
    		pom2=splitString(pomp.getImg_src());
    		if(pom2!=null)
    		for(int i=0;i<pom2.length;i++)
    			pom+="<li class=\"slide\"><div class=\"productimages\"><img src=\""+server+"/getImg/"+pomp.getReserved_id()+"_/"+pom2[i]+"\"></div></li>";
    		pom+="</ul>";
    		pom+="<div class=\"productinfo\">"+pomp.getName()+"</div>";
    	
    	pom+="</div></a>";
    	}
        return pom;
    }
    @GetMapping("/deleteNewProduct/{id}")
    public RedirectView deletenewproduct(@PathVariable("id") int id, HttpServletRequest req){
    	if(getSessionObject(req).User!=null && getSessionObject(req).User.getId()== newproducts.getById(id).getId_user()) {
    		int size = newproducts.findAll().size();
    		int lastr = newproducts.getById(id).getReserved_id();
    		if(newproducts.existsById(id)) {
	    		newproducts.deleteProduct(id);
	    		newproductscontent.deleteProduct(id);
	    		if(size!=0) {
	    			newproductscontent.setMethod(size, id);
	    			newproducts.setMethod(size , id, lastr);
	    		}
    		}
    	}
    	return new RedirectView(server+"/newproduct/1");
    }
    @GetMapping("/editNewProduct/{id}")
    public RedirectView editnewproduct(@PathVariable("id") int id, HttpServletRequest req){
    		if(getSessionObject(req).User!=null && newproducts.existsById(id) && getSessionObject(req).User.getId()== newproducts.getById(id).getId_user()) {
    			getSessionObject(req).NewProduct=newproducts.getById(id);
    			getSessionObject(req).NewProductContent=newproductscontent.getById(getSessionObject(req).NewProduct.getId());
    			getSessionObject(req).newproductchecked=true;
    			System.out.println(id+" "+newproducts.getById(id).getId()+newproductscontent.getById(id).getId());
    		}
    	return new RedirectView(server+"/newproduct/1");
    }
    @GetMapping("/createNewProduct")
    public RedirectView createNewProduct(@RequestParam("ssidd") String req){
    	if(getSessionObject(req).User!=null) {
    	int id=newproducts.findAll().size()+Products.findAll().size()+1;
    			newproducts.insertProduct(getSessionObject(req).User.getId(), id);
    			newproducts.flush();
    			getSessionObject(req).NewProduct=newproducts.findByReservedId(id);
    			getSessionObject(req).newproductchecked=true;
    	}
    	return new RedirectView(getSessionObject(req).lastURL);
    }
    @GetMapping("/savechanges")
    @ResponseBody
    public String saveChanges(@RequestParam("ssidd") String req){
    	String pom = "<script></script>";
    		getSessionObject(req).newproductchecked=false;
        return pom;
    }
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
    	List<categories> wyniki = AllCategories.findAll();
		if(categorylinedrop==null || changeInCat)
		{
			categorylinedrop="";
			if( obiekt==null||obiekt.currentCat==0)
				categorylinedrop+="<a class=\"dropdown-toggle\" id=\"categoryplaceholder\" href=\"#\">Wszystkie Kategorie</a>";
			else
				categorylinedrop+="<a class=\"dropdown-toggle\" id=\"categoryplaceholder\" href=\"#\">"+AllCategories.getById(obiekt.currentCat).getName()+"</a>";
			categorylinedrop+="<ul class=\"dropdown\"><li><a href=\"#\" id='cat0' onclick='setCategory(0)'> Wszystkie Kategorie</a></li>";
			for(int i=0;i<9;i++) {
					categorylinedrop+="<li><a class=\"dropdown-toggle\" href=\"#\" >"+wyniki.get(i).getName()+"</a>\r\n"
		    				+ "    <ul class=\"dropdown\"><li><a href=\"#\" id='cat"+wyniki.get(i).getId()+"' onclick='setCategory("+wyniki.get(i).getId()+")'> Wszystkie w "+wyniki.get(i).getName()+"</a></li>";
					categorylinedrop+=getOptGrDrop(wyniki.get(i).getId());
					categorylinedrop+="</ul></li>";
			}
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
						getpom+=this.productMiniatureTemplate(Proposition.get(j));
						licznik++;
						
					}
			}
		}
		if(licznik<50) {
			getpom+="<hr><h3>Najpopularniejsze:</h3><hr>";
			List<shopentity> Proposition = new ArrayList<>();
			Proposition = Products.getMostVisited("");
			if(Proposition!=null && Proposition.size()>0)
				for(int i=0;(i<50 && licznik<50 && i<Proposition.size());i++) {
					getpom+=this.productMiniatureTemplate(Proposition.get(i));
					licznik++;
				}
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
			if(pom!=null && pom.length>0 && !pom[0].isBlank())
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
		if(wyniki.get(i).getId()>=id*10 && wyniki.get(i).getId()<id*10+10) {
    		pom+="<li><a class=\"dropdown-toggle\" href=\"#\" >"+wyniki.get(i).getName()+"</a>\r\n"
    				+ "    <ul class=\"dropdown\"><li><a href=\"#\" id='cat"+wyniki.get(i).getId()+"' onclick='setCategory("+wyniki.get(i).getId()+")'> Wszystkie w "+wyniki.get(i).getName()+"</a></li>";
    		pom+=getOptGrDrop(wyniki.get(i).getId());
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
    	newproducts.updatename(getSessionObject(req).NewProduct.getId(), name);
    	return ResponseEntity.ok("Success");
    }
    @RequestMapping("setNewProductCategory")
    @ResponseBody
    public ResponseEntity<?> setNewProductCategory(@RequestParam("category") String category, @RequestParam("ssidd") String req) {
    	if(category.isBlank())
    		return ResponseEntity.ok("Blank parameter");
    	getSessionObject(req).NewProduct.setId_category(Integer.valueOf(category));
    	newproducts.updatecategory(getSessionObject(req).NewProduct.getId(), Integer.valueOf(category));
    	return ResponseEntity.ok("Success");
    }
    @RequestMapping("setNewProductPrice")
    @ResponseBody
    public ResponseEntity<?> setNewProductPrice(@RequestParam("price") String price, @RequestParam("ssidd") String req) {
    	if(price.isBlank())
    		return ResponseEntity.ok("Blank parameter");
    	else
    	getSessionObject(req).NewProduct.setPrice(Float.valueOf(price));
    	newproducts.updateprice(getSessionObject(req).NewProduct.getId(), Float.valueOf(price));
    	return ResponseEntity.ok("Success");
    }
    @RequestMapping("setNewProductDescription")
    @ResponseBody
    public ResponseEntity<?> setNewProductDesc(@RequestParam("description") String description, @RequestParam("ssidd") String req) {
    	getSessionObject(req).NewProduct.setDescription(description);
    	newproducts.updatedescription(getSessionObject(req).NewProduct.getId(), description);
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
    	newproductscontent.updatedelivery(getSessionObject(req).NewProduct.getId(),getSessionObject(req).NewProductContent.getId_delivery());
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
    	if(getSessionObject(req).NewProduct.getId_category()==0) {
    		pom+="		<input style=\"display:none\" type=\"text\" id=\"category\" name=\"id_category\" onchange=\"setCategoryF()\">"
    				+"<div id=\"popselect\">"
    			    + "			<div id=\"nav\">"
    			    +this.getCategoriesMethodDrop(req)
    			    +"			</div>"
    			    + "		</div>";
    	}
    	else {
    		pom+="		<input style=\"display:none\" type=\"text\" id=\"category\" name=\"id_category\" value=\""+getSessionObject(req).NewProduct.getId_category()+"\" onchange=\"setCategoryF()\">"
    				+"<div id=\"popselect\">"
    			    + "			<div id=\"nav\">"
    			    +this.getCategoriesMethodDrop(req)
    			    +"			</div>"
    			    + "		</div>"
    			    + "<script> setCategoryP("+getSessionObject(req).NewProduct.getId_category()+") </script>";
    	}
    		if(getSessionObject(req).NewProduct.getDescription()==null) {
        		pom+="<input type=\"text\" id=\"productdesc\" name=\"description\" placeholder=\"Dodaj opis produktu\" onchange=\"setDescription()\">";
        	}
        	else {
        		pom+="<input type=\"text\" id=\"productdesc\" name=\"description\" value=\""+getSessionObject(req).NewProduct.getDescription()+"\" placeholder=\"Dodaj opis produktu\" onchange=\"setDescription()\">";
        	}
    		if(getSessionObject(req).NewProduct.getPrice()>0) {
    			pom+="		<div style=\"display:flex\">"
        			    + "		<input type=\"number\" min-value='0' step='0.01' id=\"productprice\" name=\"price\" value=\""+getSessionObject(req).NewProduct.getPrice()+"\" placeholder=\"Podaj cenę produktu\" onchange=\"setPrice()\"><label for=\"price\">zł</label>"
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
    	if(!newproductscontent.existsById(getSessionObject(req).NewProduct.getId())) {
    	newproductscontent.insertProduct(getSessionObject(req).NewProduct.getId(), LocalDate.now());
		newproductscontent.flush();
    	}
		getSessionObject(req).NewProductContent=newproductscontent.getById(getSessionObject(req).NewProduct.getId());
    		pom+="<select id=\"delopt\" name=\"delivery\" multiple>"
    				+ showdeliverys()
    				+ "		</select>\r\n"
    				+ "		<select id=\"deloptpom\" multiple>\r\n"
    				+ "			<option id=\"deloptpom0\" onclick=\"re_setAll()\">Wybierz Wszystkie</option>"
    				+ showdeliveryspom()
    				+ "		</select>";
    	if(getSessionObject(req).NewProductContent!=null && getSessionObject(req).NewProductContent.getId_delivery()!=null && !getSessionObject(req).NewProductContent.getId_delivery().isBlank())
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
    	File convertFile = new File("./src/main/resources/static/show/productscontents/product"+getSessionObject(req).NewProduct.getReserved_id()+".html");
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
    					+ "href=\"/deleteimg/"+i+"\">x</a><img src=\""+server+"/getImg/"+getSessionObject(req).NewProduct.getReserved_id()+"_/"+pom2[i]+"\"></div></li>";
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
    			imgpom+="<li class=\"slide\"><div class=\"productimages\"><img id=\"img"+i+"\" src=\""+server+"/getImg/"+getSessionObject(req).NewProduct.getReserved_id()+"_/"+pom2[i]+"\"</div></li>";
    		if(!getSessionObject(req).contentimg.isBlank()) {
    			pom2=splitString(getSessionObject(req).contentimg);
    			for(int i=0;i<pom2.length;i++)
        			imgpom+="<li class=\"slide\"><div class=\"productimages\"><img id=\"imgc"+i+"\" src=\""+server+"/getconImg/"+getSessionObject(req).NewProduct.getReserved_id()+"_/"+pom2[i]+"\"></div></li>";
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
    public RedirectView delimgs(@PathVariable("id") int id, HttpServletRequest req) {
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
    				deleteFile(pom2[i], req.getSession().getId());
    		}
    		getSessionObject(req).NewProduct.setImg_src(imgpom);
    		newproducts.updateimg_src(getSessionObject(req).NewProduct.getId(), getSessionObject(req).NewProduct.getImg_src());
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
    		Products.insertProduct(getSessionObject(req).NewProduct.getReserved_id(),getSessionObject(req).NewProduct.getName(),
    				getSessionObject(req).NewProduct.getDescription(), getSessionObject(req).NewProduct.getImg_src(),
    				getSessionObject(req).NewProduct.getId_category(), getSessionObject(req).NewProduct.getPrice());
	    	ProductsContent.insertProductCon(getSessionObject(req).NewProduct.getReserved_id(), getSessionObject(req).NewProductContent.getId_delivery(), getSessionObject(req).NewProductContent.getContent_path());
	    	String idpom = UsersInfo.getById(getSessionObject(req).User.getId()).getId_products();
	    	UsersInfo.createdpages(getSessionObject(req).User.getId(), idpom+getSessionObject(req).NewProduct.getReserved_id()+"#");
	    	String[] pom=this.splitString(getSessionObject(req).NewProductContent.getId_delivery());
	    	min_cost=Deliverys.getById(Integer.valueOf(pom[0])).getPrice();
	    	for(int i=1;i<pom.length;i++)
	    		if(min_cost>Deliverys.getById(Integer.valueOf(pom[i])).getPrice())
	    			min_cost=Deliverys.getById(Integer.valueOf(pom[i])).getPrice();
	    	ProductsInfo.insertProductInfo(getSessionObject(req).NewProduct.getReserved_id(), java.time.LocalDate.now(), min_cost);
	    	newproducts.deleteProduct(getSessionObject(req).NewProduct.getId());
	    	newproductscontent.deleteProduct(getSessionObject(req).NewProduct.getId());
	    	getSessionObject(req).newproductchecked=false;
	      return ResponseEntity.ok("Page created successfully.");
	   }
    @PostMapping("/uploadimg")
    public ResponseEntity<?> imgFilesUpload(@RequestParam("file") List<MultipartFile> files, @RequestParam("ssidd") String req) throws IOException {
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
    	newproducts.updateimg_src(getSessionObject(req).NewProduct.getId(), imgpom);
    	return ResponseEntity.ok("Files uploaded successfully.");
    }
    @PostMapping("/uploadcontentimg")
    public ResponseEntity<?> imgFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("ssidd") String req) throws IOException {
    	String imgpom="";
    	if(getSessionObject(req).contentimg!=null && !getSessionObject(req).contentimg.isBlank())
    	imgpom = getSessionObject(req).contentimg;
    	imgpom+= file.getOriginalFilename()+"#";
    	File convertFile = new File("./src/main/resources/static/show/productscontents/con_"+getSessionObject(req).NewProduct.getReserved_id()+"_"+file.getOriginalFilename());
	      convertFile.getAbsolutePath();
	      convertFile.createNewFile();
	      FileOutputStream fout = new FileOutputStream(convertFile);
	      fout.write(file.getBytes());
	      fout.close();
	      getSessionObject(req).contentimg=imgpom;
	      newproductscontent.updatecontent_img(getSessionObject(req).NewProduct.getReserved_id(),getSessionObject(req).contentimg);
    	return ResponseEntity.ok("File uploaded successfully.");
    }
    @PostMapping("/uploadhtml")
    public ResponseEntity<?> htmlfileUpload(@RequestParam("file") MultipartFile file, @RequestParam("ssidd") String req) throws IOException {
    	getSessionObject(req).NewProductContent.setContent_path("../productscontents/product"+getSessionObject(req).NewProduct.getReserved_id()+".html");
	      File convertFile = new File("./src/main/resources/static/show/productscontents/product"+getSessionObject(req).NewProduct.getReserved_id()+".html");
	      convertFile.getAbsolutePath();
	      if(convertFile.exists())
	    	  convertFile.delete();
	      convertFile.createNewFile();
	      FileOutputStream fout = new FileOutputStream(convertFile);
	      fout.write(file.getBytes());
	      fout.close();
	      newproductscontent.updatecontent(getSessionObject(req).NewProduct.getId(),getSessionObject(req).NewProductContent.getContent_path());
	      return ResponseEntity.ok("File uploaded successfully.");
	   }
    @RequestMapping("/uploadhtmltext")
      public ResponseEntity<?> htmltextUpload(@RequestParam("file") String content, @RequestParam("ssidd") String req) throws IOException {
    	getSessionObject(req).NewProductContent.setContent_path("../productscontents/product"+getSessionObject(req).NewProduct.getReserved_id()+".html");
  	      File convertFile = new File("./src/main/resources/static/show/productscontents/product"+getSessionObject(req).NewProduct.getReserved_id()+".html");
  	      convertFile.getAbsolutePath();
  	      if(convertFile.exists())
  	    	  convertFile.delete();
  	      convertFile.createNewFile();
  	      FileOutputStream fout = new FileOutputStream(convertFile);
  	      fout.write(content.getBytes());
  	      fout.close();
  	      newproductscontent.updatecontent(getSessionObject(req).NewProduct.getId(),getSessionObject(req).NewProductContent.getContent_path());
  	      return ResponseEntity.ok("File created successfully.");
  	   }
    	   public void imgfileUpload( MultipartFile file, @RequestParam("ssidd") String req) throws IOException {
    	      File convertFile = new File("./src/main/resources/static/show/productscontents/img_"+getSessionObject(req).NewProduct.getReserved_id()+"_"+file.getOriginalFilename());
    	      convertFile.getAbsolutePath();
    	      convertFile.createNewFile();
    	      FileOutputStream fout = new FileOutputStream(convertFile);
    	      fout.write(file.getBytes());
    	      fout.close();
    	   }
    	   public void deleteFile( String pathsrc, @RequestParam("ssidd") String req) {
     	      File convertFile = new File("./src/main/resources/static/show/productscontents/img_"+getSessionObject(req).NewProduct.getReserved_id()+"_"+pathsrc);
     	      convertFile.getAbsolutePath();
     	      convertFile.delete();
     	   }
}