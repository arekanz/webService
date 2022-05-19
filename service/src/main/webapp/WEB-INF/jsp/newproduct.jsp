<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL"><!--<c:import url="http://localhost:8080/showIMGs"></c:import>-->
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
	var ttle=document.getElementById("ttle");
	var name=document.getElementById("productname").value;
	if(name!=null){
	title.innerHTML = name+" - sklep";
	ttle.innerHTML = name;
	}
	else{
		title.innerHTML = "Nowy produkt - sklep";
		ttle.innerHTML = "Nowy produkt";
		}
}
function pageChange(){
	var select=document.getElementById("pagecontent").value;
	var content=document.getElementById("contentcreator");
	if(select==1)
		{
			content.innerHTML = "<label for='content_url'>Wgraj plik .html</label><input type='file' accept='text/html' name='content_url' id='contentid'>";
		}
	else{
		content.innerHTML = "<textarea name='contentText' id='contenttextid' placeholder='Napisz zawartość strony'></textarea>"
	}
}
</script>
<meta charset="utf-8">
<title id="title">Nowy produkt - sklep</title>
<link rel="stylesheet"  href="css/newproductstyle.css">
</head>
<body>
	<div id="container" style="display: grid;">
		<h1 id="ttle">Nowy produkt</h1>
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
		<label for="img">Dodaj zdjęcie produktu</label><input type="file" name="img" id="productimg" accept="image/*">
		<select id="delopt" name="delivery" multiple>
			<c:import url="http://localhost:8080/getDelivery"></c:import>
		</select>
		</div>
		<hr>
		<label for="contenttype">Wybierz jak chcesz dodać zawartość:</label>
		<select id="pagecontent" onchange="pageChange()">
			<option value="1" selected>Wgraj plik .html na serwer</option>
			<option value="2">Utwórz zawartość w tym formularzu</option>
		</select>
		<hr>
		<div id="contentcreator">
			<label for="content_url">Wgraj plik .html</label><input type='file' accept="text/html" name='content_url' id='contentid'>
		</div>
		</form>
		<a class="abutton" onclick="createpage()">Dodaj produkt</a>
	</div>
</body>
</html>