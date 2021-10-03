<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="rozaryonov.delivery.repository.PageableFactory"%>


<!-- Set actual locale 2 -->
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="resources.manager"/>
<!-- End Set locale 2 -->

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" href="css/bootstrap.min.css"/>         
	<script src="js/bootstrap.min.js"></script>       

<title><fmt:message key="reportDay.title"/></title>
</head>
<body>
<div class="container">
<!-- Language switcher begin -->
<form name="locales" action="/delivery/Controller" method="post">
	<select class="btn btn-default navbar-btn" name="lang" onchange="this.form.submit()">
		<option selected disabled><fmt:message key="pageLogin.chooseLang"/></option>
		<option value="ru"><fmt:message key="pageLogin.ru"/></option>
		<option value="en"><fmt:message key="pageLogin.en"/></option>
	</select>
	<input type="hidden" name="cmd" value="SetLocale"/>
	<input type="hidden" name="goTo" value="manager/report_day.jsp">
</form>
<!-- end Language switcher -->

<nav>
	<a class="btn btn-default navbar-btn" href="/delivery/manager/view/cabinet.jsp"><fmt:message key="common.goCabinet" /></a>
	<a class="btn btn-default navbar-btn" href="/delivery/Controller?cmd=Logout"><fmt:message key="common.linkLogout" /></a>
</nav>


<h1><fmt:message key="reportDay.header"/></h1>
<form name="shippingForm2" action="/delivery/Controller" method="post">
<table class="table">
<thead>
<tr>
<th><fmt:message key="reportDay.Date"/></th>
<th><fmt:message key="reportDay.Turnover"/></th>
</tr>
</thead>	
<c:if test="${reportDayList.size() > 0}">
<c:forEach var="s" items="${reportDayList}">
	<tr>
		<td>${s.getIndex()}</td>
		<td>${s.getValue()}</td>
	</tr>
</c:forEach>
</c:if>
<c:if test="${shippingListFinish.size() == 0}">
	<fmt:message key="reportDay.noData"/>
</c:if>
</table>
</form>

</div>
</body>
</html>