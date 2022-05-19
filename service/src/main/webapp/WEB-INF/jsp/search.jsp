<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<meta charset="utf-8">
<title>Szukaj - ${searchedtext} - Sklep</title>
<link rel="stylesheet"  href="http://localhost:8080/css/style.css">
</head>
<body>
<div id="inset_form" style="display:none"></div>
<script>
	var searchedText="${searchedtext}";
	var currentCat=${currentcat};
	var searchedbool=${searchedBool};
	if(searchedbool==false && searchedText!=null)
		{
		$('#inset_form').html('<form action="' + window.location.origin + '/searching/1" name="searchFormV" method="post" style="display:none;"><input type="text" id="category" value="'+currentCat+'" name="category"></input><input id="search" type="text" value="${searchedtext}" name="search"/></form>');

	    document.forms['searchFormV'].submit();
		}
</script>
<jsp:include page="UserModule.jsp"></jsp:include>
<div id="container">
<c:if test="${searchlist!=null}">
	${searchlist}
	</c:if>
<c:if test="${searchlist==null}">
<p>Nie udało się znalezć pasujących wyników</p>
</c:if>
<c:if test="${searchedpages!=null}">
	${searchedpages}
	</c:if>
</div>
</body>
</html>