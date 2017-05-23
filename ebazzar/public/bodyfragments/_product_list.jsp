<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<br/>
<br/>
    <!-- Page Content -->
    <div class="container-fluid">

        <div class="row-fluid">
 

            <div class="col-md-12">          

                <div class="row">

					<c:forEach items="${products}" var="product">
					    <div class="col-sm-2 col-lg-2 col-md-2">
	                        <div class="thumbnail">
	                            <a href="product/${product.id}"><img src="static/images/${product.catalogid}${product.id}.jpg" alt=""></a>
	                            <div class="caption">
	                                <h4 class="pull-right">$${product.unitPrice}</h4>
	                                <h5><a href="product/${product.id}">${product.name}</a>
	                                </h5>
	                            </div>
	                            <div class="ratings">
	                                <p class="pull-right">
	                                	<span class="glyphicon glyphicon-hand-right"></span>
	                                	<span>${product.review}</span>
	                                </p>
	                                <p>
	                                	<c:set var = "rating" value="${product.rating}" />
	                                	<c:if test="${rating eq 0}">
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                	</c:if>
	                                	<c:if test="${rating eq 1}">
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                	</c:if>
	                                	<c:if test="${rating eq 2}">
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                	</c:if>
	                                	<c:if test="${rating eq 3}">
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                	</c:if>
	                                	<c:if test="${rating eq 4}">
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star-empty"></span>
	                                	</c:if>
	                                	<c:if test="${rating eq 5}">
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                		<span class="glyphicon glyphicon-star"></span>
	                                	</c:if>
	                                </p>
	                            </div>
	                        </div>
	                    </div>						
					</c:forEach>                
                </div>

            </div>

        </div>

    </div>
    <!-- /.container -->