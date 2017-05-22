<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>EBazaar - Products</title>
</head>
<body>
	Product List for ${catalogName } Catalog

	<table border="1">
		<c:forEach var="productPres" items="${products}">
			<tr>
				<td><a href="${pageContext.servletContext.contextPath}/viewProductDetail?id=${productPres.id }">${productPres.name }</a></td>
			</tr>
		</c:forEach>
	</table>

	<p class="buttonRow">
		<input type="submit" value="Back" onclick="window.location='${pageContext.servletContext.contextPath}'">
	</p>

</body>
</html>