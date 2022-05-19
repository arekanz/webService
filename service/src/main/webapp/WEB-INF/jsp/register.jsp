<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
$(function() { // Dropdown toggle
	$('input').click(function() { $(this).next('.helpbox').slideToggle();
	});

	$(document).hover(function(e) 
	{ 
	var target = e.target; 
	if (!$(target).is('input') && !$(target).parents().is('.helpbox') && !$(target).is('.helpbox')) 
	//{ $('.dropdown').hide(); }
	  { $('.helpbox').slideUp();}
	});
	});
function emailValidate(){
	var vemailerror = document.getElementById("vemailerror").value;
	var pom=false;
	for(var i=0;i<vemailerror.length;i++){
	if(vemailerror[i]=='@')
		pom=true;
	if(pom && vemailerror[i]=='.')
		return true;
	}
	return false;
}
function passValidate(){
	var vpasserror = document.getElementById("vpasserror").value;
	var pom=false,pom2=false,pom3=false;
	for(var i=0;i<vpasserror.length;i++){
		if(vpasserror[i]>='a' && vpasserror[i]<='z')
			pom=true;
		if(vpasserror[i]>='A' && vpasserror[i]<='Z')
			pom2=true;
	if(vpasserror[i]>='0' && vpasserror[i]<='9')
		pom3=true;
	}
	if(pom && pom2 && pom3)
		return true;
	return false;
}
function register(){
	var login1 = document.getElementById("login1");
	var login2 = document.getElementById("login2");
	var passerror = document.getElementById("passerror");
	var passerror2 = document.getElementById("passerror2");
	var emailerror = document.getElementById("emailerror");
	var vlogin1 = document.getElementById("vlogin1");
	var vpasserror = document.getElementById("vpasserror");
	var vpasserror2 = document.getElementById("vpasserror2");
	var vemailerror = document.getElementById("vemailerror");
	var logb;
	var passb;
	var passb2;
	var emb;
	if(vlogin1.value.length>=8){
		logb=true;
		login2.style.display="none";
	}
	else{
		login2.style.display="block";
		logb=false;
	}
	if(vpasserror.value==vpasserror2.value){
		passerror2.style.display="none";
		passb2=true;
	}
	else{
		passerror2.style.display="block";
		passb2=false;
	}
	if(vpasserror.value.length>=8 && passValidate()==true)
		{
		passerror.style.display="none";
		passb=true;
		}
	else{
		passerror.style.display="block";
		passb=false;
	}
	if(emailValidate()==true)
		{
		emailerror.style.display="none";
		emb=true;
		}
	else{
		emailerror.style.display="block";
		emb=false;
	}
	if(logb==true && passb==true && passb2==true && emb==true)
		{
			document.getElementById("registerForm").submit();
		}
}				
</script>
<meta charset="utf-8">
<title>Zarejestruj się - sklep</title>
<link rel="stylesheet"  href="css/registerstyle.css">
</head>
<body>
	<div id="container">
	<h1>Rejestracja</h1>
		<form id="registerForm" method="post" action="register/submit">
		<input id="vlogin1" type="text" placeholder="Login" name="login" maxlength="20">
		<p class="helpbox">Login musi mieć od 8 do 20 znaków</p>
		<c:if test="${loginerror==true}"><p id="login1" class="alert">Użytkownik o podanym loginie już istnieje!</p></c:if>
		<p id="login2" style="display: none" class="alert">Login musi posiadać przynajmniej 8 znaków!</p>
		<input id="vpasserror" type="password" placeholder="Hasło" name="password" maxlength="30">
		<p class="helpbox">Hasło musi mieć od 8 do 30 znaków, oraz powinno zawierać: małe i duże litery, cyfry</p>
		<p id="passerror" style="display: none" class="alert">Hasło nie spełnia wymagań!</p>
		<input id="vpasserror2" type="password" placeholder="Powtórz hasło" name="confirmp" maxlength="30">
		<p id="passerror2" style="display: none" class="alert">Hasła się nie zgadzają!</p>
		<input id="vemailerror" type="text" placeholder="Email" name="email" maxlength="40">
		<p id="emailerror" style="display: none" class="alert">Podany email jest nieprawidłowy!</p>
		</form>
		<a class="abutton" onclick="register()">Zarejestruj się</a>
	</div>
	<script type="text/javascript">document.getElementById("vlogin1").focus()</script>
</body>
</html>