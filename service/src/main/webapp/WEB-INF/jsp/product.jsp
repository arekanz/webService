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

<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/style.css">
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
		<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/show/getProduct/"><c:param name="ssidd" value="${ssidd }"></c:param></c:import>
		<div id="productContent">
		<hr>
		<p style="page-break-after: always;"> </p>
		<div id="includedcontent">
		<script>
	function iResize() {
	    document.getElementById('podglad').style.height = (document.getElementById('podglad').contentWindow.document.body.offsetHeight + 20) + 'px';
	}
	</script>
		<form id="pdgpom" style="display: none" target="podglad" action="${pageContext.request.contextPath}/getContent" method="get">
    <input type="text" name="ssidd" value="${ssidd }" />
    <input type="submit">
	</form>
	<script type="text/javascript">document.getElementById("pdgpom").submit();document.getElementById("pdgpom").innerHTML = "";</script>
			<iframe onload="iResize()" name="podglad" id="podglad" src="#"></iframe>
		</div>
		<p style="page-break-before: always;"> </p>
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
			<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/show/comments/"><c:param name="ssidd" value="${ssidd }"></c:param></c:import>
			</div>
		</div>
	</div>
	</div>
</body>
</html>