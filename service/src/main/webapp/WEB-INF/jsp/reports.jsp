<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<meta charset="utf-8">
<title>Zgłoszenia</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/style.css">
<script>

async function changeType(type){
	  let formData = new FormData();
	  var select=document.getElementById("reportscon");
	  formData.append("ssidd","${ssidd}");
	  let response = await fetch('${pageContext.request.contextPath}/getReports/'+type, {
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
	<div id="container">
		<div style="display: flex;">
			<button onclick="changeType('all')">Wszystkie</button>
			<button onclick="changeType('products')">Produkty</button>
			<button onclick="changeType('comments')">Komentarze</button>
			<button onclick="changeType('users')">Użytkownicy</button>
		</div>
		<div id="reportscon">
			
		</div>
	</div>
	</div>
	<script>
	changeType('"all"');
	</script>
</body>
</html>