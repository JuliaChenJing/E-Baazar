<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">
	function confirmDelete(delForm) {		
		$( "#dialog-confirm-delete" ).dialog({
		      resizable: false,
		      width:500,
		      modal: true,
		      buttons: {
		        "Confirm": function() {
		        	delForm.submit();
		          $( this ).dialog( "close" );
		        },
		        Cancel: function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		return false;
	}
	
	function confirmSave(saveForm){
		$( "#dialog-confirm-save" ).dialog({
		      resizable: false,
		      height:140,
		      width:500,
		      modal: true,
		      buttons: {
		        "Confirm": function() {
		        	saveForm.submit();
		          	$( this ).dialog( "close" );
		        },
		        Cancel: function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		
		return false;
	}
	
	
	function calculateTotal(){
		var total = 0;
		$(".subTotal").each(function(){
			total += parseFloat($(this).html().replace(/,/g, ''));
		});			
		
		$("#total").html(total.toFixed(2));
	}
	
	function calculateCurrentItem(){

		var id = currentQuantityInput.attr('id');
		var idVal = id.replace("quantityId", "");
		var priceVal = $("#cdiId" + idVal).val();
	
		var newVal = currentQuantityInput.val();						
		var newTotal = parseInt(newVal)  * parseFloat(priceVal);

		$("#subTotal" + idVal).text(newTotal.toFixed(2));
	}
	
	
	$(document).ready(function() {
		calculateTotal();
	 
		if($("#dialog-confirm-save-completed") != null){
			$( "#dialog-confirm-save-completed" ).dialog({
			      resizable: false,
			      height:140,
			      width:300,
			      modal: true,
			      buttons: {
			        "OK": function() {
			          	$(this).dialog( "close" );
			        }
			      }
			    });
		}
		
		
	$('.cdiQuantity').on('change',function(index, old) {
						currentQuantityInput = $(this); 
						calculateCurrentItem();
						$.ajax({
					        url : "${home}shoppingcart/" + $(this).attr('id').replace("quantityId", "") + "/" + $(this).val(),
					        type : 'GET',
					        success : function(response) {
					        	if(response != "true"){
					        		currentQuantityInput.val(response);
					        		calculateCurrentItem();
					        		$( "#dialog-confirm" ).dialog({
						      		      resizable: false,
						      		      height:150,
						      		      width:650,
						      		      modal: true,
						      		      buttons: {
						      		        "Thanks": function() {
						      		          $( this ).dialog( "close" );
						      		        }
						      		      }
						      		    });
					        	}
					        	
					        	calculateTotal();
					        }
						});						
					});
	});
	
	
	var currentQuantityInput;
</script>

<br />
<h3>Shopping Cart</h3>
<hr>
<c:if test="${fn:length(exception) gt 0}">
 	<div class="alert alert-danger">${exception}</div>
</c:if>
<div id="dialog-confirm-delete" title="Remove cart item?" style="display:none;">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to remove this item from cart?</p>
</div>

<div id="dialog-confirm-save" title="Save cart item?" style="display:none;">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to save the whole cart?</p>
</div>

<c:if test="${savecompleted}">
	<div id="dialog-confirm-save-completed" title="OK" style="display:none;">
	  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Save completed.</p>
	</div>
</c:if>

<div id="dialog-confirm" title="Warning" style="display:none;">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>The quantity of an item in your cart is over the ones we have. Sorry about this. :-)</p>
</div>


<c:choose>
	<c:when test="${cartDataItems.size() > 0}">
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
					<p>
						<input type="number" id="quantityId${rowCounter.index }"  min="1" value="${cdi.quantity}" class="cdiQuantity" />
					<p>
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

				<div class="col-md-2">
					<form
						action="${pageContext.request.contextPath}/shoppingcart/${rowCounter.index}"
						method="POST"
						onsubmit="return confirmDelete(this);">
						<button class="btn btn-danger" type="submit">Remove from Cart</button>
					</form>
				</div>
			</div>
			<hr
				style="background: #454545; color: #ddd; clear: both; float: none; width: 100%; height: .1em; margin: 0 0 1.45em; border: none;" />
		</c:forEach>
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-2  text-center">
				<form action="mycheckout">
					<button class="btn btn-primary" type="submit" id="checkoutBtn">Checkout</button>
				</form>
			</div>
			<div class="col-md-2  text-center">
				<form action="${pageContext.request.contextPath}/" method="GET">
					<button class="btn btn-primary" type="submit">Continue Shopping</button>
				</form>
			</div>
			<div class="col-md-2 text-center">			
				<form action="myshoppingcart/save" method="GET" onsubmit="return confirmSave(this);">	
					<button id="saveCart" class="btn btn-primary" type="submit">Save Cart</button>
				</form>
			</div>
			<div class="col-md-2">
				<p class="text-primary">
					Total: $ <label id="total">0</label>
				</p>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<p>No data found in your shopping cart, try to add some first!
		<div class="row">
			<div class="col-md-2  text-center">
				<form action="${pageContext.request.contextPath}/" method="GET">
					<button class="btn btn-primary" type="submit">Continue Shopping</button>
				</form>
			</div>
		</div>	
	</c:otherwise>
</c:choose>
