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
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

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
import com.webService.service.shopSection.*;
import java.io.*;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

@org.springframework.stereotype.Controller
public class content {
	
	Map<String,Object> model;
	private user ActiveUser = new user();
	List<shopentity> searchList;
	private String searchedtext;
	private String searchListLine;
	private String searchedpages;
	private String lastURL="/main";
	private String searchListS;
	private int currentCat=0;
	private int emailcode;
	private boolean loginexists=false;
	private boolean searchedBool=false;
	private int currentproductid;
	private boolean logged=false;
	private boolean wrongl=false;
	private String categorylinedrop;
	private boolean changeInCat=false;
	
	private shopentity CurrentProduct = new shopentity();
	private shopentitycontent CurrentProductContent = new shopentitycontent();
	private shopentity NewProduct = new shopentity();
	private shopentitycontent NewProductContent = new shopentitycontent();
	private shopentityinfo CurrentProductInfo = new shopentityinfo();
	
	
	private previllages UserPrevillages=new previllages();
	
	
	private categories CurrentCategory = new categories();
	@Autowired
	private categoriesRepository AllCategories;
	@Autowired
	private deliveryoptionsRepository Deliverys;
	@Autowired
	private userrepository Users;
	@Autowired
	private previllagesrepository UsersPrev;
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
	@RequestMapping("/main")
	public ModelAndView firstPage() {
		lastURL="/main";
		return new ModelAndView("main");
	}
	@GetMapping("/newproduct/{id}")
	public ModelAndView newProduct(@PathVariable(value="id",required=false) String id) {
		if(id.isBlank() || NewProduct.getImg_src()==null)
			id="1";
		String urlpom = "newproduct"+id;
		if(logged==false)
		{
			lastURL="newproduct/"+id;
			return logInFalse();
		}
		
		return new ModelAndView(urlpom);
	}
	@RequestMapping("/loginfalse")
	public ModelAndView logInFalse() {
		return new ModelAndView("loginformonly");
	}
	
	@Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
      String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("noreply@baeldung.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }
	
	private void sendConfirmationEmail(String email,String login) {
		emailcode=(new Random().nextInt(90000))+10000;
		sendSimpleMessage(email,"Potwierdzenie adresu email - Projekt sklepu internetowego","Kliknij w link: <a href=\"http://localhost:8080/email/activate/"+login+"/"+emailcode+"\">Aktywuj Konto</a>!");
	}
	
	@RequestMapping("/register/submit")
	public RedirectView registerF(@RequestParam("login") String loginP, @RequestParam("password") String passwordP, @RequestParam("email") String email, @RequestParam("confirmp") String confirmP) {
		user registerUser=new user();
		loginexists=false;
		//for(int i=1;i<=Users.findAll().size();i++)
		if(Users.getUserByLogin(loginP)!=null)
			//loginexists=true;
		//if(loginexists)
			return new RedirectView("/register");
		else {
			Users.insertUser(loginP,passwordP,email);
			//sendConfirmationEmail(email,loginP);
		return new RedirectView(lastURL);
		}
	}
	
	@RequestMapping("/register")
	public ModelAndView register() {
		return new ModelAndView("register");
	}
	 @GetMapping("/email/activate/{login}/{id}")
	    @ResponseBody
	    public RedirectView emailactivation(@PathVariable("login") String login,@PathVariable("id") int id) {
	    		Users.getUserByLogin(login).setActivated(1);
	    		if(ActiveUser!=null)
	    			ActiveUser.setActivated(1);
	    		return new RedirectView(lastURL);
	    }
	 @RequestMapping("/login")
	    @ResponseBody
	    public RedirectView login(@RequestParam("login") String loginP, @RequestParam("password") String passwordP/*, @RequestParam("lastpath") String lastPath*/,Model model) {
	    	wrongl=false;
	    	logged=loginInterface.logIn(loginP,passwordP,Users,ActiveUser);
	    		if(logged) {
	    			ActiveUser = Users.getUserByLogin(loginP);
	    			//this.getEntity(model);
	    			}
	    		else
	    			wrongl=true;
	    		return new RedirectView(lastURL);
	    }
	 @RequestMapping("/loginonly")
	    @ResponseBody
	    public RedirectView loginonly(@RequestParam("login") String loginP, @RequestParam("password") String passwordP/*, @RequestParam("lastpath") String lastPath*/,Model model) {
	    	wrongl=false;
	    	logged=loginInterface.logIn(loginP,passwordP,Users,ActiveUser);
	    		if(logged) {
	    			ActiveUser = Users.getUserByLogin(loginP);
	    			//this.getEntity(model);
	    			}
	    		else {
	    			wrongl=true;
	    			return new RedirectView("/loginfalse");
	    		}
	    		return new RedirectView(lastURL);
	    }
    
    @RequestMapping("/logout")
    @ResponseBody
    public RedirectView logout(Model model) {
    		logged=false;
    		loginInterface.logout(ActiveUser);
    		return new RedirectView(lastURL);
    }
    
    private boolean getProductsAttributes(List<shopentity> listaProduktow,int page) {
    	searchListLine="";
    	shopentity prodpom;
    	for(int i=(0+(30*(page-1)));(i<listaProduktow.size() && i<(30*page));i++) {
    		prodpom=listaProduktow.get(i);
    		searchListLine+="<a href='../product/"+prodpom.getId()+"'>";
    		searchListLine+="<div class='productminiature'>";
    		searchListLine+="<div style=\"display:flex;\"><div class='productname'><h4>"+prodpom.getName()+"</h4></div>";
    		searchListLine+="<div class='productprice'><h5>"+prodpom.getPrice()+"</h5></div></div>";
    		searchListLine+="<div style=\\\"display:flex;\\\"><div class='productimg'><img alt='"+prodpom.getImg_src()+"'></div>";
    		searchListLine+="<div class='productdescription'><h7>"+prodpom.getDescription()+"</h7></div></div>";
    		searchListLine+="</div></a>";
    	}
    	float licznik=0;
    	int pom=listaProduktow.size();
    	licznik=pom/30;
    	searchedpages="";
    	if(licznik>(int)licznik)
    		licznik=(int)(licznik+1);
    	if(page>1)
    		searchedpages+="<a href='../search/show/"+(page-1)+"'>Poprzednia</a>";
    	for(int i=1;i<=licznik;i++) {
    		if(i<licznik/2 && licznik/2<5 && i!=page && (i+1)!=page)
    		{
    			searchedpages+="<a href='../search/show/"+i+"'> "+i+"</a>";
    		}
    		else if(licznik/2>5 && i==licznik/2 && i!=page && (i+1)!=page)
    		{
    			searchedpages+="<a href='../search/show/"+i+"'> "+i+"</a>";
    		}
    		else if(i>=licznik-4 && i!=page && (i+1)!=page)
    		{
    			searchedpages+="<a href='../search/show/"+i+"'> "+i+"</a>";
    		}
    		else if(i==page)
    		{
    			if(i-1>0)
    			searchedpages+="<a href='../search/show/"+(i-1)+"'> "+(i-1)+"</a>";
    			searchedpages+="<u>"+i+"</u>";
    			if(i+1<=licznik)
    			searchedpages+="<a href='../search/show/"+(i+1)+"'> "+(i+1)+"</a>";
    		}
    	}
    	if(page<licznik)
    		searchedpages+="<a href='../search/show/"+(page+1)+"'>Następna</a>";
    	return true;
    }
    
    @RequestMapping("/search")
    @ResponseBody
    public RedirectView productViewMethod(@RequestParam("search") String searched,@RequestParam("category") String catID) {
    	if(!catID.isBlank()) {
    		currentCat=Integer.valueOf(catID);
    	}
    	searchList=Products.findByAttributes(searched, catID);
    	//getProductsAttributes(Products.findByAttributes(searched, catID),1);
    	searchedtext=searched;
    	searchedBool=true;
    	return new RedirectView("/show/searching/1");
    }
    
    @GetMapping("/show/searching/{page}")
    @ResponseBody
    public RedirectView productViewMethodShowPom(@PathVariable("page") int page) {
    	this.getProductsAttributes(searchList, page);
    	return new RedirectView("/show/search/"+page);
    }
    
    @GetMapping("/show/search/{page}")
    @ResponseBody
    public ModelAndView productViewMethodShow(@PathVariable("page") int page) {
    	lastURL="/show/searching/"+page;
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
    @GetMapping("/getCategories")
    @ResponseBody
    public String getCategoriesMethodDrop() {
		if(categorylinedrop==null || changeInCat)
		{
			categorylinedrop="";
			if(currentCat==0)
				categorylinedrop+="<a class=\"dropdown-toggle\" id=\"categoryplaceholder\" href=\"#\">Wszystkie Kategorie</a>";
			else
				categorylinedrop+="<a class=\"dropdown-toggle\" id=\"categoryplaceholder\" href=\"#\">"+AllCategories.getById(currentCat).getName()+"</a>";
			categorylinedrop+="<ul class=\"dropdown\"><li><a href=\"#\" id='cat0' onclick='setCategory(0)'> Wszystkie Kategorie</a></li>";
			for(int i=1;i<10;i++)
				categorylinedrop+=getOptGrDrop(i);
			categorylinedrop+="</ul>";
			changeInCat=false;
		}
		return categorylinedrop;
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
    
    public int getUserPrevillages() {
    	if(ActiveUser!=null) {
		previllages prev=UsersPrev.findByUserId(ActiveUser.getId());
			if(prev!=null)
				return prev.getLevel();
    	}
		return 0;
	}
    
    
    @RequestMapping("show/product/addcomment")
    @ResponseBody
    public RedirectView addcomment(@RequestParam("commenttext") String text) {
    	if(ActiveUser!=null) {
    		int idUser=ActiveUser.getId();
    		int idProd=CurrentProduct.getId();
    		System.out.println(text);
    		Comments.insertComment(idProd, idUser, text);
    	}
        return new RedirectView(lastURL);
    }
    @RequestMapping(value="show/editcomment/{id}")
    @ResponseBody
    public RedirectView editcomment(@PathVariable("id") int id,@RequestParam(value="commenttext") String text) {
    	if(Comments.getById(id)!=null) {
    		Comments.updateComment(id, text);
    	}
        return new RedirectView(lastURL);
    }
    @RequestMapping("show/deletecomment/{id}")
    @ResponseBody
    public RedirectView deletecomment(@PathVariable("id") int id) {
    	if(Comments.getById(id)!=null) {
    		Comments.deleteById(id);
    	}
        return new RedirectView(lastURL);
    }
    
    @GetMapping("/getDelivery")
    @ResponseBody
    public String showdeliverys() {
    	String pom = "";
    	if(Deliverys.findAll()!=null) {
    		List<deliveryoptions> listaD = Deliverys.findAll();
    		deliveryoptions delopt;
    		for(int i=0;i<listaD.size();i++)
    		{
    			delopt=listaD.get(i);
    			pom+="<option value=\""+delopt.getId()+"\">"+delopt.getService_name()+" - "+delopt.getDeliver()+" - "+delopt.getPrice()+"</option>";
    		}
    	}
        return pom;
    }
    @GetMapping("/show/comments")
    @ResponseBody
    public String showcomment() {
    	String pom = "<p id=\"nocomments\">Nie znalezionio komentarzy dla tego produktu</p>";
    	if(CurrentProduct!=null) {
    		List<comments> komentarze = Comments.findByProduct(CurrentProduct.getId());
    		comments komentarz;
    		if(komentarze.size()>0) {
    			pom="";
    		for(int i=0;i<komentarze.size();i++) {
    			komentarz = komentarze.get(i);
    			pom+="<div class=\"commentdivblock\"><form action=\"http://localhost:8080/show/editcomment/"+komentarz.getId()+"\" method=\"post\" id=\"comment"+i+"\" class=\"commentsentry\">"
    					+ "<div style=\"display: flex;\"> <div id=\"commentwritebyuser"+i+"\" class=\"commentwritebyuser\">"+Users.getUserById(komentarz.getId_user()).getLogin()+"</div>";
    			if(ActiveUser.getId()!=komentarz.getId_user() && getUserPrevillages()<253 && !komentarz.getText().isBlank())
    				pom+="<a class=\"editcomment\" href=\"http://localhost:8080/report/comment/"+komentarz.getId()+"\">Zgłoś</a>";
    			if((ActiveUser.getId()==komentarz.getId_user() && !komentarz.getText().isBlank())|| getUserPrevillages()>=253) {
    			pom+= "<a class=\"editcomment\" id=\"editcomment"+i+"\" onclick=\"editcomment("+i+")\">Edytuj</a>";
    			pom+="<a style=\"display: none;\" class=\"editcomment\" id=\"editcommentsubmit"+i+"\" onclick=\"editcommentsubmit("+i+")\">Zapisz</a>";
    			}
    			if(getUserPrevillages()>=253 && ActiveUser.getId()!=komentarz.getId_user())
    				pom+="<a style=\"display: none;\" class=\"editcomment\" id=\"deletecomment"+i+"\" onclick=\"deletecommentsubmitmoderator("+i+")\">Skasuj komentarz</a>";
    			else if(ActiveUser.getId()==komentarz.getId_user()){
    				pom+="<a style=\"display: none;\" class=\"editcomment\" id=\"deletecomment"+i+"\" href=\"http://localhost:8080/show/deletecomment/"+komentarz.getId()+"\">Skasuj komentarz</a>";
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
    
    private String getDeliverys() {
    	List<String> IDs = splitID(CurrentProductContent.getId_delivery());
    	String result="";
    	result+="<div id=\"deliveryTable\">";
    	if(IDs!=null)
    	for(int i=0;i<IDs.size();i++)
    	{
    	deliveryoptions delopt=Deliverys.getById(Integer.valueOf(IDs.get(i)) );
    	result+="<div class=\"deliveryClass\">";
    	result+="<p id=\"deliveryName\">"+delopt.getService_name()+"</p>";
    	if(!delopt.getDeliver().isBlank())
    	result+="<p id=\"deliver\">"+delopt.getDeliver()+"</p>";
    	if(delopt.getTime()!=0)
    	result+="<p id=\"deliveryTime\">"+delopt.getTime()+"</p>";
    	if(delopt.getPrice()!=0)
    	result+="<p id=\"deliveryPrice\">"+delopt.getPrice()+"</p>";
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
    public String getProductView() {
    	String pom="";
    	if(CurrentProduct!=null)
    	{
    		pom+="<img id=\"pgallery\" src=\""+CurrentProduct.getImg_src()+"\"></img>\r\n"
    				+ "		<h1 id=\"ptitle\">"+CurrentProduct.getName()+"</h1>\r\n"
    				+ " <script>document.getElementById(\"tytulstrony\").innerHTML=document.getElementById(\"ptitle\").innerHTML+\" - Sklep\"</script>"
    				+ "		<p id=\"pdescription\">"+CurrentProduct.getDescription()+"</p>\r\n"
    				+ "		<p id=\"pprice\">"+CurrentProduct.getPrice()+"</p>\r\n"
    				+ getDeliverys();
    	}
    	return pom;
    }
    @GetMapping("/show/product/")
    @ResponseBody
    public RedirectView productViewH() {
        return new RedirectView("/show/product/"+currentproductid);
    }
    @GetMapping("/showIMGs")
    @ResponseBody
    public String showimgs() {
    	String imgpom="";
    	if(NewProduct!=null)
    	{
    		String[] pom2 = null;
    		if(NewProduct.getImg_src()!=null)
    		pom2=splitString(NewProduct.getImg_src());
    		System.out.println(NewProduct.getImg_src()+" _ "+pom2);
    		if(pom2!=null)
    		for(int i=0;i<pom2.length;i++)
    			imgpom+="<img class=\"productimages\" src=\"http://localhost:8080/show/productscontents/img_"+NewProduct.getId()+"_"+pom2[i]+"\">";
    	}
        return imgpom;
    }
    private String[] splitString(String toSplit){
    	String[] list= null;
    	if(toSplit!=null)
    		list = toSplit.split("#");
    	return list;
    }
    @GetMapping("/show/product/{id}")
    @ResponseBody
    public ModelAndView productView(@PathVariable("id") int id) {
    	String path=""+id;
    	lastURL="/show/product/"+id;
    	if(path.isBlank())
    	{
    		productView(currentproductid);
    	}
    	else
    	{
    		currentproductid=id;
    	}
    	CurrentProduct = Products.getById(id);
		CurrentProductContent = ProductsContent.getById(id);
    	if(CurrentProductContent==null)
    		productView(id);	
        return new ModelAndView("product");
    }
    @RequestMapping(value = "/uploadimg", method = RequestMethod.POST, 
    		consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView imgFilesUpload(@RequestParam(value="img",required=false) List<MultipartFile> files) throws IOException {
    	
    	NewProduct.setId(Products.findAll().size()+1);
    	String imgpom=null;
    	if(NewProduct.getImg_src()!=null)
    		imgpom=NewProduct.getImg_src();
    	for(int i=0;i<files.size();i++) {
    		if(imgpom==null)
    			imgpom=files.get(i).getOriginalFilename()+"#";
    		else
    		imgpom+=files.get(i).getOriginalFilename()+"#";
    		imgfileUpload(files.get(i));
    	}
    	NewProduct.setImg_src(imgpom);
    	return new RedirectView("/newproduct/1");
    }
    @RequestMapping(value = "/uploadhtml", method = RequestMethod.POST, 
  	      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void htmlfileUpload(@RequestParam("pagecontent") MultipartFile file) throws IOException {
    	NewProductContent.setContent_path("./src/main/resources/static/show/productscontents/product"+NewProduct.getId()+".html");
	      File convertFile = new File("./src/main/resources/static/show/productscontents/product"+NewProduct.getId()+".html");
	      convertFile.createNewFile();
	      FileOutputStream fout = new FileOutputStream(convertFile);
	      fout.write(file.getBytes());
	      fout.close();
	   }
    	   public void imgfileUpload( MultipartFile file) throws IOException {
    	      File convertFile = new File("./src/main/resources/static/show/productscontents/img_"+(Products.findAll().size()+1)+"_"+file.getOriginalFilename());
    	      convertFile.createNewFile();
    	      System.out.println(convertFile.getAbsolutePath());
    	      FileOutputStream fout = new FileOutputStream(convertFile);
    	      fout.write(file.getBytes());
    	      fout.close();
    	   }
    @ModelAttribute
	public void getEntity(Model mod) {
    	mod.addAttribute("searchedBool", searchedBool);
    	mod.addAttribute("loginerror", loginexists);
		//mod.addAttribute("currentproductdeliverys", getDeliverys());
		mod.addAttribute("currentproductpath", CurrentProductContent.getContent_path());
		mod.addAttribute("logged", logged);
		mod.addAttribute("wrongl", wrongl);
		mod.addAttribute("userprev", getUserPrevillages());
		mod.addAttribute("userlogin", ActiveUser.getLogin());
		mod.addAttribute("useremail", ActiveUser.getEmail());
		mod.addAttribute("activated", ActiveUser.getActivated());
		//mod.addAttribute("categoriesdrop", getCategoriesMethodDrop());
		mod.addAttribute("currentcat", currentCat);
		mod.addAttribute("searchlist", searchListLine);
		mod.addAttribute("searchedpages", searchedpages);
		mod.addAttribute("searchedtext", searchedtext);
		//mod.addAttribute("lasturl", lastURL);
	}
}