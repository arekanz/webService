<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
function setCategory(id){
	var select = document.getElementById("category");
	document.getElementById("categoryplaceholder").innerHTML=document.getElementById("cat"+id).innerHTML;
	select.value=id;
	setCategoryF();
}
function setCategoryP(id){
	var select = document.getElementById("category");
	document.getElementById("categoryplaceholder").innerHTML=document.getElementById("cat"+id).innerHTML;
	select.value=id;
}
$(function() { // Dropdown toggle
	$('.dropdown-toggle').click(function() { $(this).next('.dropdown').slideToggle();
	});

	$(document).click(function(e) 
	{ 
	var target = e.target; 
	if (!$(target).is('.dropdown-toggle') && !$(target).parents().is('.dropdown-toggle') && !$(target).is('#category') && !$(target).is('.loginform')) 
	//{ $('.dropdown').hide(); }
	  { $('.dropdown').slideUp();}
	});
	});
async function fileUpload(){
	  let formData = new FormData();
	  for(var i=0;i<productimg.files.length;i++){
		  
		  if(validate(productimg.files[i])==true)
			  formData.append("file", productimg.files[i]);
		  formData.append("ssidd", "${ssidd}");
	  
	  }
	  document.getElementById("loading").style.display="grid";
	  let response = await fetch('/uploadimg', {
	    method: "POST", 
	    body: formData
	  }); 

	  //if (response.status == 200) {
	    //document.location.reload(true);
	  //}		
}
async function setName(){
	let formData = new FormData();
	var name=document.getElementById("productname").value;
	if(name!=null){
		formData.append("name", name);
		formData.append("ssidd", "${ssidd}");
		let response = await fetch('/setNewProductName', {
		    method: "POST", 
		    body: formData
		  }); 

		  if (response.status == 200) {
		    document.getElementById("title").innerHTML = name + " - sklep";
		  }		
	}
}
async function setCategoryF(){
	let formData = new FormData();
	var name=document.getElementById("category").value;
	if(name!=null){
		formData.append("category", name);
		formData.append("ssidd", "${ssidd}");
		let response = await fetch('/setNewProductCategory', {
		    method: "POST", 
		    body: formData
		  }); 

		//if (response.status == 200) {
	    //document.location.reload(true);
	  //}		
	}
}
async function setDescription(){
	let formData = new FormData();
	var name=document.getElementById("productdesc").value;
	if(name!=null){
		formData.append("description", name);
		formData.append("ssidd", "${ssidd}");
		let response = await fetch('/setNewProductDescription', {
		    method: "POST", 
		    body: formData
		  }); 

		//if (response.status == 200) {
	    //document.location.reload(true);
	  //}		
	}
}
async function setPrice(){
	let formData = new FormData();
	var name=document.getElementById("productprice").value;
	if(name!=null){
		formData.append("price", name);
		formData.append("ssidd", "${ssidd}");
		let response = await fetch('/setNewProductPrice', {
		    method: "POST", 
		    body: formData
		  }); 

		//if (response.status == 200) {
	    //document.location.reload(true);
	  //}	
	}
}
async function sendDeliverys(){
	let formData = new FormData();
	var name=document.getElementById("delopt").value;
	if(name!=null){
		formData.append("deliverys", name);
		formData.append("ssidd", "${ssidd}");
		let response = await fetch('/setNewProductDelivery', {
		    method: "POST", 
		    body: formData
		  }); 

		//if (response.status == 200) {
	    //document.location.reload(true);
	  //}	
	}
}
function selectoption(id){
	var size=document.getElementById("delopt0").value;
	var opt=document.getElementById("delopt"+id);
	var optpom=document.getElementById("deloptpom"+id);
	if(opt.selected==false)
		opt.selected=true;
	else
		opt.selected=false;
	for(var i=0;i<size;i++)
		{
		opt=document.getElementById("delopt"+i);
		optpom=document.getElementById("deloptpom"+i);
		if(opt.selected)
			{
			optpom.selected=true;
			}
		}
	boolpom=true;
	sendDeliverys()
}
function selectoptionP(id){
	var size=document.getElementById("delopt0").value;
	var opt=document.getElementById("delopt"+id);
	var optpom=document.getElementById("deloptpom"+id);
	if(opt.selected==false)
		opt.selected=true;
	else
		opt.selected=false;
	for(var i=0;i<size;i++)
		{
		opt=document.getElementById("delopt"+i);
		optpom=document.getElementById("deloptpom"+i);
		if(opt.selected)
			{
			optpom.selected=true;
			}
		}
	boolpom=true;
}
var boolpom=true;
function re_setAll(){
	var size=document.getElementById("delopt0").value;
	var opt;
	var optpom;
	for(var i=0;i<=size;i++)
		{
		opt=document.getElementById("delopt"+i);
		optpom=document.getElementById("deloptpom"+i);
			opt.selected=boolpom;
			optpom.selected=boolpom;
		}
	if(boolpom==true)
		boolpom=false;
	else
		boolpom=true;
	document.getElementById("delopt0").selected=false;
	sendDeliverys();
}
function re_setAllP(){
	var size=document.getElementById("delopt0").value;
	var opt;
	var optpom;
	for(var i=0;i<=size;i++)
		{
		opt=document.getElementById("delopt"+i);
		optpom=document.getElementById("deloptpom"+i);
			opt.selected=boolpom;
			optpom.selected=boolpom;
		}
	if(boolpom==true)
		boolpom=false;
	else
		boolpom=true;
	document.getElementById("delopt0").selected=false;
}
function send(){
	var name=document.getElementById("productname").value;
	var desc=document.getElementById("productdesc").value;
	var category=document.getElementById("category").value;
	var price=document.getElementById("productprice").value;
	var delivery=document.getElementById("delopt").value;
	if(name!=null && desc!=null && category!=null && price!=null && delivery!=null && name!="" && desc!="" && category!="" && price!="" && delivery!="")
		document.location.replace("./3");
	else
		window.alert("Wypełnij wszystkie pola");
}
function back(){
	document.location.replace("./1");
}
</script>
<meta charset="utf-8">
<title id="title">Nowy produkt - sklep</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/newproductstyle.css">
</head>
<body>
	<div id="container" style="display: grid;">
		<a class="abutton" onclick="back()">&ltWstecz</a>
		<div style="display: grid;">
		<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/getnewprod"><c:param name="ssidd" value="${ssidd }"></c:param></c:import>
		</div>
		<hr>
		<div style="display: flex;">
		<c:import  charEncoding="UTF-8" url="${pageContext.request.contextPath}/getnewproddeliverys"><c:param name="ssidd" value="${ssidd }"></c:param></c:import>
		</div>
		<a class="abutton" onclick="send()">Dalej&gt</a>
	</div>
</body>
</html>