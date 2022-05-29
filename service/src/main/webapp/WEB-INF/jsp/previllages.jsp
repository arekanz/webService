<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<meta charset="utf-8">
<title>Uprawnienia</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/style.css">
<script>
var action;
var userId;
function setPrevs(id){
	prevsform.style.display = "block";
	selecteduser.innerHTML = "Obecnie wybrany użytkownik to: "+document.getElementById("userlogin"+id).innerHTML;
	action="setPrevs";
	userId=id;
}
function changePrevs(id){
	prevsform.style.display = "block";
	selecteduser.innerHTML = "Obecnie wybrany użytkownik to: "+document.getElementById("userlogin"+id).innerHTML;
	action="changePrevs";
	userId=id;
}
function deletePrevs(id,pomid){
	confirm.style.display = "flex";
	confirmtext.innerHTML = "Czy na pewno chcesz usunąć uprawnienia użytkownika "+document.getElementById("userlogin"+pomid).innerHTML+"?<br><button onclick='wykonaj()'>Tak</button><button onclick='confirmclose()'>Nie</button>";
	action="deletePrevs";
	userId=id;
}
function confirmclose()
{
	confirm.style.display = "none";
}
async function wykonaj(){
	  let formData = new FormData();
	  formData.append("ssidd","${ssidd}");
	  formData.append("level",inputprevs.value);
	  let response = await fetch('${pageContext.request.contextPath}/'+action+'/'+userId, {
	    method: "POST", 
	    body: formData
	  });
	  loading.style.display="grid";
	  if (response.status == 200) {
		  loading.style.display="none";
		  document.location.reload(true);
	  }	
	  else{
		  loading.style.display="none";
	  	  window.alert("Błąd połączenia z serwerem");
	  }
}
async function searchUsers(type){
	  let formData = new FormData();
	  var select=document.getElementById("userscon");
	  formData.append("ssidd","${ssidd}");
	  let response = await fetch('${pageContext.request.contextPath}/getUsers/'+type, {
	    method: "POST", 
	    body: formData
	  }); 
	  let responseText = await getTextFromStream(response.body);
	  loading.style.display="grid";
	  if (response.status == 200) {
		  loading.style.display="none";
		  select.innerHTML=responseText;
	  }	
	  else{
		  loading.style.display="none";
	  	  window.alert("Błąd połączenia z serwerem");
	  }
}
function searchuser(){
	var pom=document.getElementById("searchUser");
	searchUsers(pom.value);
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
function checkValue(){
	if(inputprevs.value>254)
		inputprevs.value=254;
	if(inputprevs.value<1)
		inputprevs.value=1;
}
</script>
</head>
<body>
	<div id="loading"><h2>Przetwarzanie żądania.</h2><div id="loadanim"></div></div>
	<jsp:include page="UserModule.jsp"></jsp:include>
	<div id="settingscontainer" style='display: grid;'>
	<div id="container">
		<div style="display: flex;">
			<input type="text" id="searchUser" style="width: 50%" placeholder="Wpisz login szukanego użytkownika">
			<button onclick="searchuser()">Szukaj</button>
		</div>
		<div id="prevsform" style="display: none;">
			<div id="selecteduser">  </div>
			<input type="number" id="inputprevs" min="1" max="254" onchange="checkValue()"><button onclick="wykonaj()">Zatwierdź</button>
		</div>
		<div id="confirm" style="display:none; width: 40%; margin: 0 auto; position: fixed; top: 200px; background-color: white; border: 1px #777 solid; left: 25%;padding: 5px; text-align:center;"><div id="confirmtext"></div></div>
		<div id="userscon">
			
		</div>
	</div>
	</div>
	<script>
	searchUsers("all");
	var prevsform = document.getElementById("prevsform");
	var selecteduser = document.getElementById("selecteduser");
	var inputprevs = document.getElementById("inputprevs");
	var confirm = document.getElementById("confirm");
	var confirmtext = document.getElementById("confirmtext");
	</script>
</body>
</html>