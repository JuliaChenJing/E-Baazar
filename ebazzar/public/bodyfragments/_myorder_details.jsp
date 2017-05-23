
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<br>

<h3>Order Details</h3>
<hr>
<table id="table">
	<thead>
		<tr>
			<th>Product Name</th>
			<th>Quantity</th>
			<th>Unit Price</th>
			<th>Total Price</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${orderItems}" var="item">
			<tr>
				<td>${item.productName}</td>
				<td>${item.quantity}</td>
				<td>${item.unitPrice}</td>
				<td>${item.totalPrice}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<a href="javascript:history.back()" class="btn btn-default" style="margin-left:50px;">
	<span class="glyphicon-hand-left glyphicon"></span> back
</a>