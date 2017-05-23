<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>

$(document).ready(function() {
	$('#quantity').on('change',function(index, old) {
					$.ajax({
				        url : "shoppingcart/check/" + $("#productId").text() + "/" + $(this).val(),
				        type : 'GET',
				        success : function(response) {
				        	if(response == "false"){
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
				        		$('#quantity').val($("#quantityAvail").text());
				        	}			        	
				        }
					});						
				});
});
</script>
<br/>


<div id="dialog-confirm" title="Warning" style="display:none;">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>We really like you but we are almost out of stock, Sorry about this. :-)</p>
</div>

<section class="container">
	<form action="addToCart/${product.id}" method="POST" >
		<div class="row">
			<div class="col-md-10">
				<img class="product_details" src="static/images/${product.catalogid}${product.id}.jpg" alt="image" />
				<div class="col-md-8">
					<h3>${product.name}</h3>
					<p>${product.description}</p>
					<p>
						<strong>Id : </strong><span id="productId" class="label label-info">${product.id}</span>
					</p> 
					<p>
						<strong>Catalog :</strong> ${product.catalog.name}
					</p>	
					<p>
						<strong>Mfg Date :</strong> ${product.mfg}
					</p>			 
					<p>
						<strong>Available units in stock :</strong><span id="quantityAvail">${product.quantityAvail}</span> 
					</p>
					<h4><span class="label label-warning">${product.unitPrice} USD</span></h4>
					
					<p>
						<strong> Quantity :</strong>	
						<input type="number" id="quantity" name="quantity" value="1"  min="1" max="${product.quantityAvail}" style="width:40px;"/>					
					</p>
					
					<p>
						<button  type="submit" class="btn btn-primary btn-large">
							<span class="glyphicon-shopping-cart glyphicon"></span> Order Now
						</button> 			
						 
						<a href="javascript:history.back()" class="btn btn-default" style="margin-left:50px;">
							<span class="glyphicon-hand-left glyphicon"></span> back
						</a>
		
					</p>
		
				</div>
			</div>
		</div>
	</form>
</section>

