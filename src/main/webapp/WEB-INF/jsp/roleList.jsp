<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Role List</title>
</head>
<body>
<table>
	<thead>
		<tr>
			<th>Id</th>
			<th>Name</th>
		</tr>
	</thead>
	<c:forEach var="role" items="${roleList}">
		<tr>
			<td>${role.getId()}</td>
			<td>${role.getName()}</td>
		</tr>
	</c:forEach>
</table>
</body>
</html>