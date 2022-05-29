<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<meta charset="utf-8">
<title>Wersje Robocze - sklep</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/newproductstyle.css">
</head>
<body>
<div id="loading"><h2>Przetwarzanie żądania.</h2><div id="loadanim"></div></div>
	<div id="container" style="display: grid;">
		<div id="newproducts" style="display: flex;">
			<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/showNewProductsMiniatures"><c:param name="ssidd" value="${ssidd }"></c:param></c:import>
		</div>
		<a class="abutton" onclick="createNewPage()">Utwórz nowy produkt</a>
	</div>
</body>
</html>