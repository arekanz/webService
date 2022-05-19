<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
var validFilesTypes=["bmp","gif","png","jpg","jpeg"];

function ValidateFile(file)

{

  var label = document.getElementById("imgalert");

  var path = file.value;

  var ext=path.substring(path.lastIndexOf(".")+1,path.length).toLowerCase();

  var isValidFile = false;

  for (var i=0; i<validFilesTypes.length; i++)    
  {    
    if (ext==validFilesTypes[i])    
    {    
        isValidFile=true;    
        break;    
    }    
  }

  if (!isValidFile)    
  {
    label.innerHTML="Plik niepoprawny lub niedopuszczone rozszerzenie pliku" +    
     " rozszerzenia:\n\n"+validFilesTypes.join(", ");    
  }    
  return isValidFile;    
 }    
function fileUpload(){
		var file = document.getElementById("productimg");
		var boole = true;
	    if (!file.files) {
	        console.error("This browser doesn't seem to support the `files` property of file inputs.");
	    } else if (!file.files[0]) {
	    	document.getElementById("imgalert".innerHTML = "Musisz wybrać pliki");
	    } else {
	    	for(var i=0;i<file.files.length;i++){
	        var ffile = file.files[i];
	        if(ffile.size==0)
	        	boole=false;
	    	}
	    }
		if(boole==true)
		document.getElementById("productForm").submit();
		
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
<link rel="stylesheet"  href="http://localhost:8080/css/newproductstyle.css">
</head>
<body>
	<div id="container" style="display: grid;">
		<form enctype="multipart/form-data" id="productForm" action="../uploadimg" method="post">
		<label for="img">Dodaj zdjęcie(a) produktu</label><input type="file" name="img" id="productimg" accept="image/*" multiple onchange="fileUpload()">
		<p id="imgalert"></p>
		</form>
		<div id="images">
			<c:import url="http://localhost:8080/showIMGs"></c:import>
		</div>
		<a class="abutton" onclick="sendimg()">Dalej</a>
	</div>
</body>
</html>