<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<br>

<h3>Products List</h3>
<hr>
<table id="table">
	<thead>
		<tr>
			<th>Id</th>
			<th>Catalog</th>
			<th>Name</th>
			<th>Quantity</th>
			<th>Price</th>
			<th>MFG Date</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${products}" var="product">
			<tr>
				<td>${product.id}</td>
				<td>${product.catalog.name}</td>
				<td><a href="product/${product.id}" class="action" style="font-weight:bold;font-size: inherit;">${product.name}</a></td>
				<td>${product.quantityAvail}</td>
				<td>${product.unitPrice}</td>
				<td>${product.mfg}</td>
				<td>
					<a href="admin/products/edit/${product.id}"class="action">Edit</a>
					<a href="admin/products/delete/${product.id}" class="action">Delete</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
