<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<meta charset="utf-8">
<c:if test="${searchedtext!=null }">
<title>Szukaj - ${searchedtext} - Sklep</title>
</c:if>
<c:if test="${searchedtext==null }">
<title>Szukaj - Sklep</title>
</c:if>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="UserModule.jsp"></jsp:include>
	<div id="container">
	<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/show/searching/"><c:param name="ssidd" value="${ssidd }"></c:param></c:import>
	</div>
</body>
</html>