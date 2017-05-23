<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<br>
<h3>Payment Information</h3>
<hr />
<c:if test="${fn:length(exception) gt 0}">
 	<div class="alert alert-danger">${exception}</div>
</c:if>
<form:form action="myagreement" modelAttribute="checkoutmodel">
	<div class="row">
		<div class="col-md-6">		 
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">Name on Card</span>
			  <form:input id="name" path="creditCard.nameOnCard" type="text"  placeholder="Michael Jordan" class="form-control" aria-describedby="basic-addon1"/>			  
			</div><form:errors path="creditCard.nameOnCard" cssClass="text-danger"/>
			<br>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon3">Card No.</span>
			  <form:input id="cardNum" path="creditCard.cardNum" type="text"  placeholder="xxxx-xxxx-xxxx-xxxx" class="form-control" aria-describedby="basic-addon3"/>			  
			</div><form:errors path="creditCard.cardNum" cssClass="text-danger"/>
			<br>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon4">Card Type</span>
			  <form:input id="cardType" path="creditCard.cardType" type="text"  placeholder="Visa,MasterCard,Discover..." class="form-control" aria-describedby="basic-addon4"/>			  
			</div><form:errors path="creditCard.cardType" cssClass="text-danger"/>
			<br>
							
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon2">Expiration Date</span>
			  <form:input id="expireationDate" path="creditCard.expirationDate" type="text"  placeholder="MM/DD/YYYY" class="form-control" aria-describedby="basic-addon2"/>			  
			</div><form:errors path="creditCard.expirationDate" cssClass="text-danger"/>
			<br>	
		</div>
	</div>
	<div class="row">
		<div class="col-md-2 text-center">
			<input class="btn btn-primary " type="submit" role="button"	id="checkoutBtn" value="Checkout">
		</div>
		<div class="col-md-3 text-center">
			<a class="btn btn-primary" href="shoppingcart">Back to Shopping	Cart </a>
		</div>
		<div class="col-md-2 text-center">
			<button class="btn btn-primary" onclick="javascript:history.back();">Previous Screen</button>
		</div>
	</div>
</form:form>