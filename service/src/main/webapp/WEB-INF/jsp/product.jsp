<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="com.webService.service.shopSection.*"%>
<%@page import="java.util.List"%>

<!DOCTYPE HTML>
<html lang="pl-PL">
<head>
<meta charset="utf-8">
<title id="tytulstrony"> - Sklep</title>

<link rel="stylesheet"  href="http://localhost:8080/css/style.css">
<script type="text/javascript">
	function addcomment(){
		document.getElementById("commentform").style.display = "block";
		document.getElementById("addcomment").style.display = "none";
	}
	function submitcomment(){
		var text=document.getElementById("comtext").value;
		if(text!=null)
		document.getElementById("commentform").submit();
		else
		document.getElementById("commentalert").style.display="block";
	}
	function editcomment(id){
	document.getElementById("commenttext"+id).readOnly=false;
	document.getElementById("editcomment"+id).style.display="none";
	document.getElementById("editcommentsubmit"+id).style.display="block";
	document.getElementById("deletecomment"+id).style.display="block";
	}
	function editcommentsubmit(id){
	if(document.getElementById("commenttext"+id).value!=null)
		document.getElementById("comment"+id).submit();
	else
	alert("Komentarz nie może być pusty");
	}
	function deletecommentsubmitmoderator(id){
		document.getElementById("commenttext"+id).value="";
		document.getElementById("comment"+id).submit();
	}
</script>

</head>
<body>
	<jsp:include page="UserModule.jsp"></jsp:include>
	
	<div id="container">
	<div id="product">
		<c:import url="http://localhost:8080/show/getProduct/"></c:import>
		<div id="productContent">
		<hr>
		<c:if test="${currentproductpath!=null}">
    			<c:if test="${currentproductpath!=null}"><c:import url="${currentproductpath}.html"></c:import></c:if>
    			</c:if>
    			<c:if test="${currentproductpath==null}">
    				<script>window.location.replace("../product/")</script>
    			</c:if>
			<div id="commentContainer">
			<hr>
			<h4>Komentarze</h4>
			<c:if test="${logged==true}" >
				<button id="addcomment" onclick="addcomment()">Dodaj komentarz</button>
				<form id="commentform" action="addcomment" method="post">
					<textarea id="comtext" name="commenttext" maxlength="200" placeholder="Wpisz treść swojego komentarza"></textarea>
					<button onclick="submitcomment()">Wstaw komentarz</button>
				</form>
				<p id="commentalert">Komentarz nie może być pusty</p>
			</c:if>
			<c:import url="http://localhost:8080/show/comments/"></c:import>
			</div>
		</div>
	</div>
	</div>
</body>
</html>