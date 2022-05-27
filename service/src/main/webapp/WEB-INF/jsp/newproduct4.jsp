<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
function back(){
	document.location.replace("./3");
}
async function createpage(){
		  let formData = new FormData();
		  formData.append("ssidd", "${ssidd}");
		  let response = await fetch('/createnewproduct', {
		    method: "POST", 
		    body: formData
		  }); 

		  if (response.status == 200) {
		    document.location.replace("${pageContext.request.contextPath}/main");
		  }		
}

</script>
<meta charset="utf-8">
<title>Podgląd - sklep</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/newproductstyle.css">
<style type="text/css">
.closebutton{
	display: none;
}
</style>
</head>
<body>
<div id="loading"><h2>Tworzenie strony.</h2><div id="loadanim"></div></div>
	<div id="container" style="display: grid;">
	<a class="abutton" onclick="back()">&ltWstecz</a>
		<div id="images">
			<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/showIMGs">
		<c:param name="ssidd" value="${ssidd}"></c:param></c:import>
		</div>
		<div id="info">
			<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/showINFO">
		<c:param name="ssidd" value="${ssidd}"></c:param></c:import>
		</div>
		<script>
	function iResize() {
	    document.getElementById('podglad').style.height = (document.getElementById('podglad').contentWindow.document.body.offsetHeight + 20) + 'px';
	}
	</script>
		<form id="pdgpom" style="display: none" target="podglad" action="${pageContext.request.contextPath}/getContentText" method="get">
    <input type="text" name="ssidd" value="${ssidd }" />
    <input type="submit">
	</form>
	<script type="text/javascript">document.getElementById("pdgpom").submit();document.getElementById("pdgpom").innerHTML = "";</script>
			<iframe onload="iResize()" name="podglad" id="podglad" src="#"></iframe>
		<a class="abutton" onclick="createpage()">Utwórz Produkt</a>
	</div>
</body>
</html>