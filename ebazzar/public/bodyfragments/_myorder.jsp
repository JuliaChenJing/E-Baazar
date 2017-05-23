
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<br>

<h3>Order List</h3>
<hr>
<table id="table">
	<thead>
		<tr>
			<th>Id</th>
			<th>Date of Order</th>
			<th>Total Price</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${orders}" var="order">
			<tr>
				<td>${order.orderId}</td>
				<td>${order.orderDate}</td>
				<td>${order.totalPrice}</td>
				<td><a href="myorder/${order.orderId}">Detail</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
