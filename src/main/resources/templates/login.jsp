<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!-- Set actual locale -->
<!-- end Set actual locale -->

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" href="css/bootstrap.min.css"/>         
	<script src="js/bootstrap.min.js"></script>       

<title><fmt:message key="pageLogin.titleLogin"/></title>
</head>
<body>
<div class="container">
<!-- menu -->

<div>
<h1><fmt:message key="pageLogin.yourLogin"/></h1>
<c:if test="${not empty errorDescription }">
<p><fmt:message key="${errorDescription}"/></p>
</c:if>
<form name="login" action="Controller" method="post">
<input type="text" name="login" placeholder="John123" required class="form-control" />
<br>
<input type="password" name="pass" placeholder="yourPassword123" required class="form-control" />
<br>
<input type="submit" class="btn btn-primary gtn-lg" name="send" value="<fmt:message key="pageLogin.buttonSubmit"/>"/>
<br>
<input type="hidden" name="cmd" value="Login"/>
</form>

<c:url value="register.jsp" var="urlRegister"/>
<br>
<p><a href="${urlRegister}"><fmt:message key="pageLogin.registerLink"/></a>
<c:url value="index.jsp" var="urlIndex"/>
<p><a href="${urlIndex}"><fmt:message key="pageLogin.goHomePage"/></a>
</div>


</div>
</body>
</html>