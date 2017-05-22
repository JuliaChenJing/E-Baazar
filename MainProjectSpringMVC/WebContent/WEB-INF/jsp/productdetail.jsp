<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>EBazaar - Product Details</title>
</head>
<body>
	Product Details for ${product.name }

	<table border="1">
		<tr>
			<td>Item</td>
			<td>${product.name }</td>
		</tr>
		<tr>
			<td>Price</td>
			<td>${product.unitPrice }</td>
		</tr>
		<tr>
			<td>Quantity</td>
			<td>${product.quantityAvail }</td>
		</tr>
		<tr>
			<td>Review</td>
			<td>${product.description }</td>
		</tr>
	</table>

	<form method="post"
		action="${pageContext.servletContext.contextPath}/addToCart">
		<input type="submit" value="Add to Cart"> <input type="hidden"
			name="prodName" value="${product.name }"> <input
			type="hidden" name="unitPrice" value="${product.unitPrice }">
	</form>

	<button onclick="goBack()">Back</button>

	<script>
		function goBack() {
			window.history.back();
		}
	</script>
</body>

</html>