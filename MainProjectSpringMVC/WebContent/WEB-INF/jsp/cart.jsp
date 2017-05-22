<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>EBazaar - Cart Items</title>
</head>
<body>
	Cart Items

	<table border="1">
		<c:forEach var="cartItemPres" items="${cartItems}">
			<tr>
				<td>${cartItemPres.itemName }</td>
				<td>${cartItemPres.quantity }</td>
				<td>${cartItemPres.price }</td>
				<td>${cartItemPres.totalPrice }</td>
			</tr>
		</c:forEach>
	</table>

	<p class="buttonRow">
		<input type="submit" value="Proceed to Checkout">
		<input type="submit" value="Continue Shopping" onclick="window.location='${pageContext.servletContext.contextPath}'">
		<input type="submit" value="Save Cart">
		<input type="submit" value="Delete Selected">
	</p>

</body>
</html>