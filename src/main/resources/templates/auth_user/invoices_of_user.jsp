<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!-- Set actual locale 2 -->
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="resources.user"/>
<!-- End Set locale 2 -->

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" href="css/bootstrap.min.css"/>         
	<script src="js/bootstrap.min.js"></script>       

<title><fmt:message key="invoicesUser.title"/></title>
</head>
<body>
<div class="container">
		<!-- Language switcher begin -->
		<form name="locales" action="/delivery/Controller" method="post">
			<select name="lang" onchange="this.form.submit()"
				class="btn btn-default dropdown-toggle">
				<option selected disabled><fmt:message
						key="fragLang.chooseLang" /></option>
				<option value="ru"><fmt:message key="fragLang.ru" /></option>
				<option value="en"><fmt:message key="fragLang.en" /></option>
			</select>
			<input type="hidden" name="cmd" value="SetLocale" />
			<input type="hidden" name="goTo" value="auth_user/invoices_of_user.jsp">
		</form>
		<!-- end Language switcher -->

<c:import url="auth_user_menu_inner.jsp" charEncoding="utf-8"  />


<h1><fmt:message key="invoicesUser.header"/></h1>
<c:if test="${empty invoiceList}">
	<c:set var="invoiceList" value="${pageInvoicesOfUserToPay.nextPage()}"/>
</c:if>	
<form name="invoicesUserForm" action="/delivery/Controller" method="post">
<table class="table">
<thead>
<tr>
<th><fmt:message key="invoicesUser.date"/></th>
<th><fmt:message key="invoicesUser.person"/></th>
<th><fmt:message key="invoicesUser.type"/></th>
<th><fmt:message key="invoicesUser.sum"/></th>
<th><fmt:message key="invoicesUser.mark"/></th>
</tr>
</thead>

<c:if test="${invoiceList.size() > 0}">
<c:forEach var="inv" items="${invoiceList}">
	<tr>
		<td>${inv.getCreationDateTime()}</td>
		<td>${inv.getPerson().getSurname()} ${st.getPerson().getName()}</td>
		<td>${inv.getInvoiceStatus().getName()}</td>
		<td>${inv.getSum()}</td>
		<td><input type="radio" name="invoiceId" value="${inv.getId()}" required /></td>
	</tr>
</c:forEach>
</c:if>
<c:if test="${invoiceList.size() == 0}">
	<fmt:message key="invoicesUser.noInvoices"/>
</c:if>
</table>

<p>
<button type="submit" name="cmd" value="InvoiceUserPay"><fmt:message key="invoicesUser.Pay"/></button>
</p>
</form>
<form name="nextPrevForm" action="/delivery/Controller" method="post">
<button type="submit" name="cmd" value="InvoiceUserPrev"><fmt:message key="common.prevPage"/></button>
<button type="submit" name="cmd" value="InvoiceUserNext"><fmt:message key="common.nextPage"/></button>
</form>
</div>
</body>
</html>