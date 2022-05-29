<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<meta charset="utf-8">
<title>Kategorie</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/style.css">
<script>
async function getCategories(){
	  let formData = new FormData();
	  var select=document.getElementById("categoriescon");
	  formData.append("ssidd","${ssidd}");
	  let response = await fetch('${pageContext.request.contextPath}/serveroptions/getCategories', {
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
var action;
function setCat(id){
	action="setCategories";
	wykonaj(id,action);
}
function edit(id){
	var gjnm=document.getElementById("category"+id);
	gjnm.readOnly=false;
	document.getElementById("addbutt"+id).style.display = "block";
	document.getElementById("concategorybutt"+id).style.display="none";
	action="changeCategories";
}
function deleteC(id){
	confirm.style.display = "flex";
	confirmtext.innerHTML = "Czy na pewno chcesz usunąć kategorię "+document.getElementById("category"+id).value+"?<br>Spowoduje to również usunięcie wszystkich podkategorii!<br><button onclick='wykonaj("+id+",\"deleteCategories\")'>Tak</button><button onclick='confirmclose()'>Nie</button>";
	action="deleteCategories";
}
function confirmclose()
{
	confirm.style.display = "none";
}
async function wykonaj(id,act){
		if(act!=null && act!="")
			action = act;
	  let formData = new FormData();
	  formData.append("ssidd","${ssidd}");
	  formData.append("id",id);
	  formData.append("text",document.getElementById("category"+id).value);
	  let response = await fetch('${pageContext.request.contextPath}/'+action+'/', {
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
	<div id="confirm" style="display:none; width: 40%; margin: 0 auto; position: fixed; top: 200px; background-color: white; border: 1px #777 solid; left: 25%;padding: 5px; text-align:center;"><div id="confirmtext"></div></div>
		
		<div id="categoriescon">
			
		</div>
	</div>
	</div>
	<script>
	getCategories();
	var confirm = document.getElementById("confirm");
	</script>
</body>
</html>