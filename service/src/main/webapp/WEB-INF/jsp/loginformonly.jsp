<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Zaloguj się, aby uzyskać dostęp do zawartości</title>
<style>
	#logincontainer{
		width: 60%;
		background-color: grey;
		margin: 20px auto;
		color: white;
		padding: 8%;
	}
	.loginform{
		width: 90%;
		margin: 10px auto;
		background-color: #777;
		color: white;
		height: 34px;
		padding: 3px;
		font-size: 22px;
		border: 1px solid black;
		border-radius: 2px;
	}
	#loginformB{
		width: 90%;
		margin: 10px auto;
		background-color: #777;
		color: white;
		height: 34px;
		padding: 3px;
		font-size: 22px;
		border: 1px solid black;
		border-radius: 2px;
		text-align: center;
	}
</style>
</head>
<body style="display: grid">
	<div id="logincontainer">
	<h2>Aby uzyskać dostęp do wybranej zawartości muszisz się zalogować</h2>
	<c:if test="${wrongl==true}"><p class="warning">Błędny login i/lub hasło</p></c:if>
	<form id="loginForm" method="post" action="/loginonly">
		<input class="loginform" type="text" name="login" placeholder="login"></input>
		<input class="loginform" type="password" name="password" placeholder="password"></input>
		<input id="loginformB" type="submit" value="Zaloguj się">
	</form>
	</div>
</body>
</html>