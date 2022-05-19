<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>

function setAction(id,URL){
	document.getElementById(id).action=URL;
}

</script>
<c:if test="${logged==true }">
<c:if test="${activated==0}">
<div id="activationalert"><p class="warning">Konto nie zostało jeszcze aktywowane!</p></div>
</c:if></c:if>
<c:if test="${wrongl==true}"><p class="warning">Błędny login i/lub hasło</p></c:if>

<div id="menuUser">

	<a id="banner" href="main"><img id="logo" /></a>
	<script>document.getElementById("banner").href=window.location.origin+"/main";document.getElementById("logo").src=window.location.origin+"/img/logo.png";</script>
	<div id="searchContainer">
	<form id="searchForm" method="post" action="">
	<div style="float:left;display:flex">
	<c:if test="${searchedtext!=null}">
	<input class="searchclass" id="search" type="text" value="${searchedtext}" name="search" placeholder="Wyszukaj słowo lub frazę"/>
	</c:if>
	<c:if test="${searchedtext==null}">
	<input class="searchclass" id="search" type="text" id="search" name="search" placeholder="Wyszukaj słowo lub frazę"/>
	</c:if>
	</div>
	<div style="float:left;display:flex">
	<c:if test="${currentcat!=0}"><input class="searchclass" type="text" id="category" value="${currentcat}" name="category"></input></c:if>
	<c:if test="${currentcat==0}"><input class="searchclass" type="text" id="category" name="category"></input></c:if>
	<div id="popselect">
		<div id="nav">
		<c:import url="http://localhost:8080/getCategories/"></c:import>
		</div>
	</div>
	</div>
	<div style="float:left;display:flex">
	<input id="buttonsearch" type="submit" value="0\">
	</div>
	<div style="float:none;">
	</div>
	
	<script>
	setAction("searchForm",window.location.origin+'/search');
	</script>
	</form>
	</div>
	<div id="userContainer">
 	  <c:if test="${logged==true}"><div id="hellouser"><h3>Zalogowano jako, ${userlogin}!</h3></div> <div id="usrsetcon"> <a id="logout">Wyloguj się</a><a id="settings">Ustawienia</a></div><script>document.getElementById("logout").href=window.location.origin+"/logout";
 		document.getElementById("settings").href=window.location.origin+"/settings";</script></c:if>
 	  <c:if test="${logged==false}">
 	  <form id="loginForm" method="post" action="">
 	  <div id="nav2">
 	  <a class="dropdown-toggle" href="#">Zaloguj Się</a>
 	  	<ul class="dropdown"><li>
		<input class="loginform" type="text" name="login" placeholder="login"></input></li>
		<li><input class="loginform" type="password" name="password" placeholder="password"></input></li>
		<li><input id="loginformB" type="submit" value="Zaloguj się">
		</li>
		</ul>
		</div>
		</form>
		
		<a id="register">Zarejestruj Się</a>
		<script>
			setAction("loginForm",window.location.origin+'/login');
			document.getElementById("register").href=window.location.origin+"/register";
		</script>
		</c:if>
	</div>
		<div style="float:none"></div>
</div>

<hr>

<script>
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
</script>