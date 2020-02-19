<!doctype html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
	<meta charset="UTF-8" />
	<title>Document</title>
</head>-
<body>
	<h1>Documentacion</h1>

<p>Endpoint: <b>${pageContext.request.contextPath }/api/pokemon</b></p>
<!-- Examen
montar bd 1 maestro detalle ( 1:N )
log 1 pt
busqueda y documentacion 2 pt
listar y buscar 
documentar en un word o en un index (con capturas)

 -->

<p>Listado pokemon GET <a target="_blank" href="${pageContext.request.contextPath}/api/pokemon/">/api/pokemon/</a>
<p>Detalle pokemon GET <a target="_blank" href="${pageContext.request.contextPath}/api/pokemon/1/">/api/pokemon/1/</a>
<p>Busqueda por nombre pokemon GET <a target="_blank" href="${pageContext.request.contextPath}/api/pokemon/?nombre=pikachu">/api/pokemon/?nombre=pi</a>
<p>Post: <a href="#" target="_blank"></a></p>
	
</body>
</html>