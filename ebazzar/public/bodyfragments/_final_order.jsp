<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<br />
<h3>Order Summary</h3>
<hr>

<c:if test="${fn:length(exception) gt 0}">
 	<div class="alert alert-danger">${exception}</div>
</c:if>
<b>Order Item :</b>
<c:forEach var="cdi" items="${cartDataItems}" varStatus="rowCounter">
	<div class="row">
		<div class="col-md-1">
			<div class="small-thumbnail">
				<a href="product/${cdi.productId}"><img src="static/images/${cdi.catalogid}${cdi.productId}.jpg" /></a>
			</div>
		</div>
		<div class="col-md-4">
			<div style="float:left;">
				<p class="text-primary">
					<a href="product/${cdi.productId}"><b>${cdi.itemName }</b></a>
					<input type="hidden" id="cdiId${rowCounter.index }" value="${cdi.price }"></input>
				</p>
				<p>Price: ${cdi.price }</p>
			</div>					
		</div>				
		<div class="col-md-1">
			<p>
				<b>$${cdi.price }</b>
			</p>
			<p class="text-muted">Each</p>
		</div>
		<div class="col-md-2">
			<p><b>${cdi.quantity}</b><p>
			<p>Quantity</p>
		</div>

		<div class="col-md-2">
			<p>
				<label class="subTotal" id="subTotal${rowCounter.index}">
					<fmt:formatNumber value="${cdi.price*cdi.quantity}" maxFractionDigits="2" />
				</label>
			</p>
			<p class="text-muted">Total</p>
		</div>
	</div>
	<hr
		style="background: #454545; color: #ddd; clear: both; float: none; width: 100%; height: .1em; margin: 0 0 1.45em; border: none;" />
</c:forEach>
<div class="col-md-2">
		<p class="text-primary">
			Total: $ <label id="total">${total}</label>
		</p>
</div>
<br/>
<hr>
<div class="col-md-10">
	<b>Ship To:</b> ${checkoutmodel.shippingAddress.street} ${checkoutmodel.shippingAddress.city} ${checkoutmodel.shippingAddress.state}, ${checkoutmodel.shippingAddress.zip}
</div>
<br/>
<hr>
<div class="col-md-10">
	<b>Payment :</b> ${checkoutmodel.creditCard.cardType} - ${fn:substring(checkoutmodel.creditCard.cardNum, fn:length(checkoutmodel.creditCard.cardNum) - 4, fn:length(checkoutmodel.creditCard.cardNum))}
</div>  
<br/>
<hr>
<div class="row">
	<div class="col-md-3"></div>
	<div class="col-md-2  text-center">
		<form action="myplaceorder">
			<button class="btn btn-primary" type="submit" id="checkoutBtn">Submit Order</button>
		</form>
	</div>
	<div class="col-md-2  text-center">
		<form action="${pageContext.request.contextPath}/" method="GET">
			<button class="btn btn-primary" type="submit">Continue Shopping</button>
		</form>
	</div>	
</div> 