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
	
function setName(){
	var title=document.getElementById("title");
	var name=document.getElementById("productname").value;
	if(name!=null){
	title.innerHTML = name+" - sklep";
	}
	else{
		title.innerHTML = "Nowy produkt - sklep";
		}
}
function send(){
	var name=document.getElementById("productname").value;
	var desc=document.getElementById("productdesc").value;
	var category=document.getElementById("category").value;
	var price=document.getElementById("productprice").value;
	var delivery=document.getElementById("delopt").value;
	if(name!=null && desc!=null && category!=null && price!=null && delivery!=null)
		document.getElementById("productForm").submit();
	else
		window.alert("Wypełnij wszystkie pola");
}
function back(){
	document.location.replace("./1");
}
</script>
<meta charset="utf-8">
<title id="title">Nowy produkt - sklep</title>
<link rel="stylesheet"  href="/css/newproductstyle.css">
</head>
<body>
	<div id="container" style="display: grid;">
		<form id="productForm" action="/createproduct" method="post">
		<div style="display: flex;"><input type="text" id="productname" name="name" placeholder="Wpisz nazwę produktu" onchange="setName()">
		<input style="display:none" type="text" id="category" name="id_category">
		<div id="popselect">
			<div id="nav">
				<c:import url="http://localhost:8080/getCategories/"></c:import>
			</div>
		</div>
		<input type="text" id="productdesc" name="description" placeholder="Dodaj opis produktu">
		<input type="text" id="productprice" name="price" placeholder="Podaj cenę produktu"><label for="price">zł</label>
		</div>
		<hr>
		<div style="display: flex;">
		<select id="delopt" name="delivery" multiple>
			<c:import url="http://localhost:8080/getDelivery"></c:import>
		</select>
		</div>
		</form>
		<a class="abutton" onclick="back()">&ltWstecz</a>
		<a class="abutton" onclick="send()">Dalej&rt</a>
	</div>
</body>
</html>