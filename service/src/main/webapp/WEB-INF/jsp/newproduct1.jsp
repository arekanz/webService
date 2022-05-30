<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
async function fileUpload(){
		  let formData = new FormData();
		  for(var i=0;i<productimg.files.length;i++){
				  formData.append("file", productimg.files[i]);
				  formData.append("ssidd","${ssidd }");
		  
		  }
		  document.getElementById("loading").style.display="block";
		  let response = await fetch('/uploadimg', {
		    method: "POST", 
		    body: formData
		  }); 

		  if (response.status == 200) {
		    document.location.reload(true);
		  }		
}
function sendimg(){
	var images = document.getElementById("images");
	if(images.innerHTML==null)
		window.alert("Dodaj zdjęcia produktu");
	else
		document.location.replace("./2");
}
</script>
<meta charset="utf-8">
<title>Nowy produkt - sklep</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/newproductstyle.css">
</head>
<body>
<div id="loading"><h2>Trwa wgrywanie zdjęć na serwer.</h2><div id="loadanim"></div></div>
	<div id="container" style="display: grid;">
		<form enctype="multipart/form-data" id="productForm" method="post">
		<label for="img">Dodaj zdjęcie(a) produktu</label><input type="file" name="productimg" id="productimg" accept="image/*" multiple onchange="fileUpload()">
		<p id="imgalert"></p>
		</form>
		<div id="images">
			<c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/showIMGs"><c:param name="ssidd" value="${ssidd }"></c:param></c:import>
		</div>
		<a class="abutton" onclick="sendimg()">Dalej&gt</a>
	</div>
</body>
</html>