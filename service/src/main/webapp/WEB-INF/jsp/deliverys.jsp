<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<meta charset="utf-8">
<title>Usługi dostaw</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/style.css">
<script>

async function changeType(){
	  let formData = new FormData();
	  var select=document.getElementById("container");
	  formData.append("ssidd","${ssidd}");
	  let response = await fetch('${pageContext.request.contextPath}/serveroptions/getDeliverys/', {
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
async function wyslij(id,action){
	  let formData = new FormData();
	  var text=document.getElementById("deliveryname"+id);
	  var deliver=document.getElementById("deliveryC"+id);
	  var time=document.getElementById("deliverytime"+id);
	  var price=document.getElementById("deliveryprice"+id);
	  formData.append("ssidd","${ssidd}");
	  formData.append("id",id);
	  formData.append("name",text.value);
	  formData.append("deliver",deliver.value);
	  formData.append("time",time.value);
	  formData.append("price",price.value);
	  let response = await fetch('${pageContext.request.contextPath}/'+action+'/', {
	    method: "POST", 
	    body: formData
	  });
	  loading.style.display="grid";
	  if (response.status == 200) {
			confirm.style.display = "none";
		  changeType();
	  }	
	  else{
		  loading.style.display="none";
	  	  window.alert("Błąd połączenia z serwerem/lub niewypełnione pola: czas realizacji usługi i cena usługi");
	  }
}
var action;
function edit(id){
	var gjnm=document.getElementById("deliveryname"+id);
	gjnm.readOnly=false;
	document.getElementById("deliveryC"+id).readOnly=false;
	document.getElementById("deliverytime"+id).readOnly=false;
	document.getElementById("deliveryprice"+id).readOnly=false;
	document.getElementById("send"+id).style.display = "block";
	document.getElementById("butt"+id).style.display="none";
	action="changeDeliverys";
}
function deleteC(id){
	confirm.style.display = "flex";
	confirmtext.innerHTML = "Czy na pewno chcesz usunąć usługę "+document.getElementById("deliveryname"+id).value+" - "+document.getElementById("deliveryC"+id).value+"?<br><button onclick='wyslij("+id+",\"deleteDeliverys\")'>Tak</button><button onclick='confirmclose()'>Nie</button>";
	action="deleteDeliverys";
}
function confirmclose()
{
	confirm.style.display = "none";
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
</script>
</head>
<body>
	<div id="loading"><h2>Przetwarzanie żądania.</h2><div id="loadanim"></div></div>
	<jsp:include page="UserModule.jsp"></jsp:include>
	<div id="settingscontainer" style='display: grid;'>
	<div id="confirm" style="display:none; width: 40%; margin: 0 auto; position: fixed; top: 200px; background-color: white; border: 1px #777 solid; left: 25%;padding: 5px; text-align:center;"><div id="confirmtext"></div></div>
	
	<div id="container" style="display:grid;">
		
	</div>
	</div>
	<script>changeType();var confirm = document.getElementById("confirm");</script>
</body>
</html>