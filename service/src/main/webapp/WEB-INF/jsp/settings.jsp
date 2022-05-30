<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<meta charset="utf-8">
<title>Panel Użytkownika</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/style.css">
<script>

async function changeMailS(mail,pass){
	  let formData = new FormData();
	  formData.append("email", mail);
	  formData.append("pass", pass);
	  formData.append("ssidd","${ssidd}");
	  let response = await fetch('/changeMail', {
	    method: "POST", 
	    body: formData
	  }); 
	  let responseText = await getTextFromStream(response.body);
	  loading.style.display="grid";
	  if (response.status == 200 && responseText=="Email changed") {
		  loading.style.display="none";
		  confirm.style.display="grid";
	  }	
	  else{
		  loading.style.display="none";
	  	  window.alert("Zmiana adreu email nie powiodła się");
	  }
}
function emailValidate(){
	var vemailerror = document.getElementById("mchFinp").value;
	var pom=false;
	for(var i=0;i<vemailerror.length;i++){
	if(vemailerror[i]=='@')
		pom=true;
	if(pom && vemailerror[i]=='.')
		return true;
	}
	return false;
}
function changeMail(){
	document.getElementById("mchF").style.display="grid";
}
function cancel(){
	document.getElementById("mchF").style.display="none";
	document.getElementById("pchF").style.display="none";
}
function setMail(){
	var email = document.getElementById("mchFinp").value;
	var pass = document.getElementById("mchFpass").value;
	if(pass!=null && pass!="")
	if(emailValidate())
		{
		document.getElementById("mchFerr").style.display="none";
		changeMailS(email,pass);
		}
	else
		{
		document.getElementById("mchFerr").style.display="block";
		}
}
async function changePassS(mail,pass){
	  let formData = new FormData();
	  formData.append("pass", mail);
	  formData.append("oldpass", pass);
	  formData.append("ssidd","${ssidd}");
	  let response = await fetch('/changePass', {
	    method: "POST", 
	    body: formData
	  });
	  let responseText = await getTextFromStream(response.body);
	  loading.style.display="grid";
	  if (response.status == 200 && responseText=="Password changed") {
		  loading.style.display="none";
		  confirm.style.display="grid";
	  }
	  else{
		  loading.style.display="none";
		  window.alert("Zmiana hasła nie powiodła się");
		  
	  }
}
async function getTextFromStream(readableStream) {
    let reader = readableStream.getReader();
    let utf8Decoder = new TextDecoder();
    let nextChunk;
    
    let resultStr = '';
    
    while (!(nextChunk = await reader.read()).done) {
        let partialData = nextChunk.value;
        resultStr += utf8Decoder.decode(partialData);
    }
    
    return resultStr;
}
function changePassword(){
	document.getElementById("pchF").style.display="grid";
}
function setPass(){
	var email = document.getElementById("pchFinp").value;
	var pass = document.getElementById("pchFpass").value;
	if(pass!=null && pass!="" )
	if(email!=null && email!="" && email.length>=8)
		{
		document.getElementById("pchFerr").style.display="none";
		changePassS(email,pass);
		}
	else
		{
		document.getElementById("pchFerr").style.display="block";
		}
}
function ok(){
	confirm.style.display="none";
	document.getElementById("mchF").style.display="none";
	document.getElementById("pchF").style.display="none";
}
function newProduct(){
	document.location.replace("${pageContext.request.contextPath}/newproduct");
}
function myProducts(){
	document.location.replace("${pageContext.request.contextPath}/myproducts");
}
//funkcje administratora
function reports(){
	document.location.replace("${pageContext.request.contextPath}/serveroptions/reports");
}
function addcategories(){
	document.location.replace("${pageContext.request.contextPath}/serveroptions/addCategories");
}
function adddeliverys(){
	document.location.replace("${pageContext.request.contextPath}/serveroptions/deliverys");
}
function setprevillages(){
	document.location.replace("${pageContext.request.contextPath}/serveroptions/setPrevillages");
}
function addcommunicates(){
	document.location.replace("${pageContext.request.contextPath}/serveroptions/communicates");
}
</script>
</head>
<body>
<div id="loading"><h2>Przetwarzanie żądania.</h2><div id="loadanim"></div></div>
<div id="confirm" style="display:none; width: 40%; margin: 0 auto; position: fixed; top: 200px; background-color: white; border: 1px #777 solid; left: 25%;padding: 5px; text-align:center;"><h2>Zmiana wprowadzona pomyślnie</h2><button class="settings" onclick="ok()">OK</button></div>
<div id="mchF" style="display:none; width: 40%; margin: 0 auto; position: fixed; top: 200px; background-color: white; border: 1px #777 solid; left: 25%;padding: 5px; text-align:center;">
	<h3>Zmiana adresu email</h3>
	<input id="mchFinp" type="text" placeholder="Podaj email">
	<input id="mchFpass" type="password" placeholder="Podaj hasło">
	<button onclick="setMail()">Zmień email</button>
	<p id="mchFerr" style="display: none">Email jest niepoprawny i/lub nie podano hasła</p>
	<button onclick="cancel()">Anuluj</button>
</div>
<div id="pchF" style="display:none; width: 40%; margin: 0 auto; position: fixed; top: 200px; background-color: white; border: 1px #777 solid; left: 25%;padding: 5px; text-align:center;">
	<h3>Zmiana hasła</h3>
	<input id="pchFinp" type="password" placeholder="Podaj hasło">
	<input id="pchFpass" type="password" placeholder="Podaj stare hasło">
	<button onclick="setPass()">Zmień hasło</button>
	<p id="pchFerr" style="display: none">Hasło jest niepoprawne i/lub hasła się nie zgadzają</p>
	<button onclick="cancel()">Anuluj</button>
</div>
	<jsp:include page="UserModule.jsp"></jsp:include>
	<div id="settingscontainer" style='display: grid;'>
		<button class="settings" onclick="changeMail()">Zmień adres email</button>
		<button class="settings" onclick="changePassword()">Zmień hasło</button>
		<button class="settings" onclick="newProduct()">Dodaj Ogłoszenie</button>
		<button class="settings" onclick="myProducts()">Moje Ogłoszenia</button>
		<div id="AdminSettings">
			<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/adminSettings">
		<c:param name="ssidd" value="${ssidd}"></c:param></c:import>
		</div>
	</div>
	<script>var loading = document.getElementById("loading");
	var confirm = document.getElementById("confirm");</script>
</body>
</html>