<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="pl-PL">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">

function back(){
	document.location.replace("./2");
}
async function fileUpload(){
		  let formData = new FormData();
				  formData.append("file", content_url.files[0]);
				  formData.append("ssidd", "${ssidd}");
		  document.getElementById("loading").style.display="block";
		  let response = await fetch('/uploadhtml', {
		    method: "POST", 
		    body: formData
		  }); 

		  if (response.status == 200) {
		    document.location.reload(true);
		  }		
}
async function addImgM(){
	  let formData = new FormData();
			  formData.append("file", productimg.files[0]);
			  formData.append("ssidd", "${ssidd}");
	  document.getElementById("loading").style.display="block";
	  let response = await fetch('/uploadcontentimg', {
	    method: "POST", 
	    body: formData
	  }); 

	  if (response.status == 200) {
		  document.getElementById("loading").style.display="none";
		  if(document.getElementById("contenttextid").value==null)
			  document.location.reload(true);
		  else
			  textUpload();
	  }		
}
async function textUpload(){
	  let formData = new FormData();
	  var text=document.getElementById("contenttextid").value;
			  formData.append("file", text);
			  formData.append("ssidd", "${ssidd}");
	  document.getElementById("loading").style.display="block";
	  let response = await fetch('/uploadhtmltext', {
	    method: "POST", 
	    body: formData
	  }); 

	  if (response.status == 200) {
	    document.location.reload();
	  }		
}
function addImg(){
	var imgadd=document.getElementById("imgadd");
	imgadd.style.display="block";
}
function pageChange(){
	var select=document.getElementById("pagecontent").value;
	var content=document.getElementById("contentcreator");
	if(select==1)
		{
			content.innerHTML = "<label for='content_url'>Wgraj plik .html</label><input type='file' accept='text/html' name='content_url' id='content_url' onchange='fileUpload()'>";
		}
	else{
		content.innerHTML = "<button onclick='showImg()'>Dodaj zdjęcie</button><textarea name='contenttextid' id='contenttextid' placeholder='Napisz zawartość strony' onchange='textUpload()'>"+cont+"</textarea>"
	}
}
function addImg(){
	var imgbox=document.getElementById("imgadd");
	imgbox.style.display="block";
}
function showImg(){
	var imgbox=document.getElementById("imgbox");
	imgbox.style.display="block";
}
function close(name){
	if(document.getElementById('imgadd').style.display=="none")
	document.getElementById('imgbox').style.display="none";
	document.getElementById('imgadd').style.display="none";
}
function equals(main,searched){
	var csize = searched.length;
	for(var i=0;i<(main.length-csize);i++)
		{
		if(main.substr(i,i+csize-1)==searched)
			return true;
		}
	return false;
}
async function dalej(){
	if(document.getElementById("contenttextid").value!=null){
		let formData = new FormData();
		  var text=document.getElementById("contenttextid").value;
				  formData.append("file", text);
				  formData.append("ssidd", "${ssidd}");
		  document.getElementById("loading").style.display="block";
		  let response = await fetch('/uploadhtmltext', {
		    method: "POST", 
		    body: formData
		  }); 

		  if (response.status == 200) {
		    document.location.replace("./4");
		  }		
	}
	else
		window.alert("Plik nie ma zawartości lub nie został wgrany na serwer");
}
window.onbeforeunload = function () {
	var pppp=window.open('${pageContext.request.contextPath}/savechanges?ssidd=${ssidd}', '_blank');
	pppp.close();
};
</script>
<meta charset="utf-8">
<title>Nowy produkt - sklep</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/newproductstyle.css">
</head>
<body>
<div id="loading"><h2>Trwa wgrywanie zawartości.</h2><div id="loadanim"></div></div>
<div id="imgbox" style="position:relative"><a onclick="close()">Zamknij</a><c:import charEncoding="UTF-8" url="${pageContext.request.contextPath}/getContentImgs"><c:param name="ssidd" value="${ssidd}"></c:param></c:import><p>Aby dodać zdjęcie złap i przeciągnij je: &lt img src="(tu pomiędzy cudzysłowia)" /&gt</p></div>
<div id="imgadd" style="position:relative">
	<a onclick="close()">Zamknij</a>
	<form enctype="multipart/form-data" id="productForm" method="post">
		<label for="img">Dodaj zdjęcie produktu</label><input type="file" name="productimg" id="productimg" accept="image/*" onchange="addImgM()">
	</form>
</div>
<div style="break-after: always;"></div>
<div style="break-before: always;"></div>
	<div id="container" style="display: grid;">
	<a class="abutton" onclick="back()">&ltWstecz</a>
		<label for="contenttype">Wybierz jak chcesz dodać zawartość:</label>
		<select name="contenttype" id="pagecontent" onchange="pageChange()">
			<option value="1" selected>Wgraj plik .html na serwer</option>
			<option value="2">Utwórz zawartość w tym formularzu</option>
		</select>
		<div id="contentcreator">
		<div id="validator"></div>
			<label for="content_url">Wgraj plik .html</label><input type='file' accept="text/html" name='content_url' id='content_url' onchange='fileUpload()'>
		</div>
		<h4>Tu zostanie wyświetlony podgląd strony:</h4>
		<div style="break-after: always;"></div>
		<script>
		var cont;
	function iResize() {
	    document.getElementById('podglad').style.height = (document.getElementById('podglad').contentWindow.document.body.offsetHeight + 20) + 'px';
	    if(window.frames[0].document.getElementsByTagName("html")[0].innerHTML!=null && window.frames[0].document.getElementsByTagName("html")[0].innerHTML!="")
		{
	    	
	    	cont=window.frames[0].document.getElementsByTagName("html")[0].innerHTML;
		document.getElementById("pagecontent").value = 2;
		pageChange();
		
		}
	}
	</script>
		<form id="pdgpom" style="display: none" target="podglad" action="${pageContext.request.contextPath}/getContentText" method="get">
    <input type="text" name="ssidd" value="${ssidd }" />
    <input type="submit">
	</form>
	<script type="text/javascript">document.getElementById("pdgpom").submit();document.getElementById("pdgpom").innerHTML = "";
	</script>
			<iframe onload="iResize()" name="podglad" id="podglad" src="#"></iframe>
		<script>
		
		</script>
		<div style="break-before: always;"></div>
		<a class="abutton" onclick="dalej()">Dalej&gt</a>
	</div>
	<div onload="iResize()"></div>
</body>
</html>