<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javaScript">

$(document).ready(function(){
	$("#addressSelection").hide();
	$("#selectShipAddress").click(function(){ 
		$("#shippingaddress-dialog" ).dialog({
		      resizable: false,
		      maxHeight:400,
		      width:600,
		      modal: true,
		      buttons: {
		        Cancel: function() {
		          $(this).dialog("close");
		        }
		      }
		    });
		return false;
	});
	
	$("#selectBillAddress").click(function(){
		$("#billingaddress-dialog" ).dialog({
		      resizable: false,
		      maxHeight:400,
		      width:600,
		      modal: true,
		      buttons: {
		        Cancel: function() {
		          $(this).dialog("close");
		        }
		      }
		    });
		return false;
	});
	
	
	$("#shippingaddress-dialog .address-selected").click(function(){
		$("#shippingaddress-dialog" ).dialog("close");
		var td = $(this).parent().parent().children();
		$("#streetShipping").val($(td[0]).text());
		$("#cityShipping").val($(td[1]).text());
		$("#stateShipping").val($(td[2]).text());
		$("#zipShipping").val($(td[3]).text());
	})
	
	$("#billingaddress-dialog .address-selected").click(function(){
		$("#billingaddress-dialog" ).dialog("close");
		var td = $(this).parent().parent().children();
		$("#streetBilling").val($(td[0]).text());
		$("#cityBilling").val($(td[1]).text());
		$("#stateBilling").val($(td[2]).text());
		$("#zipBilling").val($(td[3]).text());
	})
	
});


function onBillingSameAsShipping(){
	document.getElementById("stateBilling").value = document.getElementById("stateShipping").value;
	document.getElementById("cityBilling").value = document.getElementById("cityShipping").value;
	document.getElementById("zipBilling").value = document.getElementById("zipShipping").value;
	document.getElementById("streetBilling").value = document.getElementById("streetShipping").value;
}
</script>

<style>
div.hidden {
    display: none;
}

div.shown {
    display: none;
}

.table{
	width:100%;
	font-size:smaller;
}
</style>

<br>
<h3>Shipping Information</h3>
<hr/>

<c:if test="${fn:length(exception) gt 0}">
 	<div class="alert alert-danger">${exception}</div>
</c:if>
<form:form action="mypaymentinfo" modelAttribute="checkoutmodel" method="POST">
		<div class="row">
			<div class="col-md-4">
				<div class="row">				
					<h4>Shipping Address</h4>
				</div>				 
				<div class="input-group">
				  <span class="input-group-addon">Street</span>
				  <form:input id="streetShipping" path="shippingAddress.street" type="text"  placeholder="101 N COURT" class="form-control"/>			  
				</div><form:errors path="shippingAddress.street" cssClass="text-danger"/>
				<div class="input-group">
				  <span class="input-group-addon">City</span>
				  <form:input id="cityShipping" path="shippingAddress.city" type="text"  placeholder="FAIRFIELD" class="form-control"/>			  
				</div><form:errors path="shippingAddress.city" cssClass="text-danger"/>				 
				<div class="input-group">
				  <span class="input-group-addon">State</span>
				  <form:input id="stateShipping" path="shippingAddress.state" type="text"  placeholder="IA" class="form-control"/>			  
				</div><form:errors path="shippingAddress.state" cssClass="text-danger"/>				 
				<div class="input-group">
				  <span class="input-group-addon">Zip</span>
				  <form:input id="zipShipping" path="shippingAddress.zip" type="text"  placeholder="52556" class="form-control"/>			  
				</div><form:errors path="shippingAddress.zip" cssClass="text-danger"/>				
			</div> 
			<div class="col-md-4">
				<div class="row">				
					<h4>Billing Address</h4>
				</div>
				<div class="input-group">
				  <span class="input-group-addon" id="basic-addon1">Street</span>
				  <form:input  id="streetBilling" path="billingAddress.street" type="text"  placeholder="101 N COURT" class="form-control"/>			  
				</div><form:errors path="billingAddress.street" cssClass="text-danger"/>
				<div class="input-group">
				  <span class="input-group-addon" id="basic-addon2">City</span>
				  <form:input id="cityBilling" path="billingAddress.city" type="text"  placeholder="FAIRFIELD" class="form-control"/>			  
				</div><form:errors path="billingAddress.city" cssClass="text-danger"/>				 
				<div class="input-group">
				  <span class="input-group-addon">State</span>
				  <form:input id="stateBilling" path="billingAddress.state" type="text"  placeholder="IA" class="form-control"/>			  
				</div><form:errors path="billingAddress.state" cssClass="text-danger"/>				 
				<div class="input-group">
				  <span class="input-group-addon">Zip</span>
				  <form:input id="zipBilling" path="billingAddress.zip" type="text"  placeholder="52556" class="form-control"/>			  
				</div><form:errors path="billingAddress.zip" cssClass="text-danger"/>				
			</div>
		</div>
		<div class="row">
		    	 <div class="checkbox">
		      		<label><input type="checkbox" value="" onclick="if(this.checked){onBillingSameAsShipping()}" />Billing Same as shipping</label>
		    	
		     		 <label><form:checkbox path="saveShippingAddress"  id="chkBoxSaveShipping" onClick="onSaveShipping()" />Save Shipping address</label>
		     		 <input type="hidden" id="saveShipping" value="${saveShipping }"></input>
		    	
		     		 <label><form:checkbox path="saveBillingAddress"  onClick="onSaveBilling()" />Save Billing Address</label>
		     		 <input type="hidden" id="saveBilling" value="${saveBilling }"></input>
		    	</div>
		</div>
		<div class="row">
		<div class="col-md-2 text-center">
			<button  class="btn btn-primary" role="button" id = "selectShipAddress">Select Ship Address</button>		
		</div>
		<div class="col-md-2 text-center">
			<button  class="btn btn-primary" role="button" id = "selectBillAddress">Select Bill Address</button>
		</div>
		<div class="col-md-2 text-center">		
			<input  class="btn btn-primary" role="button" id = "checkoutBtn" type="submit" value="Checkout" />		
		</div>
		<div class="col-md-2 text-center">
			<a  class="btn btn-primary" role="button"  href="shoppingcart" id = "checkoutBtn">Back to Cart</a>		
		</div>
	</div>
	</form:form>
	
<div id="billingaddress-dialog" title="Select billing address" style="display:none;">
	<table class="table">
		<c:forEach items="${allBillAddresses}" var="add" varStatus="vs">
 			<tr><td>${add.street}</td><td>${add.city}</td><td>${add.state}</td><td>${add.zip}</td><td><button type="button" class="btn btn-warning address-selected">Bill to this address</button></td></tr> 
		</c:forEach>
	</table>
</div>	
	
<div id="shippingaddress-dialog" title="Select shipping address" style="display:none;">
	<table class="table">
		<c:forEach items="${allShipAddresses}" var="add" varStatus="vs">
 			<tr><td>${add.street}</td><td>${add.city}</td><td>${add.state}</td><td>${add.zip}</td><td><button type="button" class="btn btn-warning address-selected">Ship to this address</button></td></tr>
		</c:forEach>
	</table>
</div>