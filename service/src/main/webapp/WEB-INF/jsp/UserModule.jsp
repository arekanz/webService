<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<c:set var="ssidd" value="${ssidd }"></c:set>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<c:if test="${logged==true }">
<c:if test="${activated==0}">
<div id="activationalert"><p class="warning">Konto nie zostało jeszcze aktywowane!</p></div>
</c:if></c:if>
<c:if test="${wrongl==true}"><p class="warning">Błędny login i/lub hasło</p></c:if>

<div id="menuUser">

	<a id="banner" href="${pageContext.request.contextPath}/main"><img id="logo" src="${pageContext.request.contextPath}/img/logo.png"/></a>
	
	<form id="searchForm" method="post" action="${pageContext.request.contextPath}/search">
	<div id="searchContainer">
	<div style="display:flex">
	<c:if test="${searchedtext!=null}">
	<input class="searchclass" id="search" type="text" value="${searchedtext}" name="search" placeholder="Wyszukaj słowo lub frazę"/>
	</c:if>
	<c:if test="${searchedtext==null}">
	<input class="searchclass" id="search" type="text" id="search" name="search" placeholder="Wyszukaj słowo lub frazę"/>
	</c:if>
	</div>
	<div style="display:flex">
	<c:if test="${currentcat!=0}"><input class="searchclass" type="text" id="category" value="${currentcat}" name="category"></input></c:if>
	<c:if test="${currentcat==0}"><input class="searchclass" type="text" id="category" name="category"></input></c:if>
	<div id="popselect">
		<div id="nav">
		<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/getCategories/"><c:param name="ssidd" value="${ssidd }"></c:param></c:import>
		</div>
	</div>
	</div>
	<div style="display:flex">
	<button id="buttonsearch" type="submit">Szukaj</button>
	</div>
	</div>
	</form>
	
	<div id="userContainer">
 	  <c:if test="${logged!=null && logged==true && userlogin!=null}"><div id="hellouser"><h3>Zalogowano jako, ${userlogin}!</h3></div> 
 	  <div id="usrsetcon"> <a href="${pageContext.request.contextPath}/logout" id="logout">Wyloguj się</a>
 	  <a href="${pageContext.request.contextPath}/settings" id="settings">Ustawienia</a></div></c:if>
 	  <c:if test="${logged==null || logged==false || userlogin==null}">
 	  <form id="loginForm" method="post" action="${pageContext.request.contextPath}/login">
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
		
		<a id="register" href="${pageContext.request.contextPath}/register">Zarejestruj Się</a>
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