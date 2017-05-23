<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <!-- Page Content -->
    <div class="container-fluid">

        <div class="row-fluid">
 

            <div class="col-md-12">

                <div class="row carousel-holder">

                    <div class="col-md-12">
                        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                            <ol class="carousel-indicators">
                                <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                                <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                                <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                                <li data-target="#carousel-example-generic" data-slide-to="3"></li>
                            </ol>
                            <div class="carousel-inner">
                                <div class="item active">
                                    <img class="slide-image" src="static/images/h1.jpg" alt="">
                                </div>
                                <div class="item">
                                    <img class="slide-image" src="static/images/h2.jpg" alt="">
                                </div>
                                <div class="item">
                                    <img class="slide-image" src="static/images/h3.jpg" alt="">
                                </div>
                                <div class="item">
                                    <img class="slide-image" src="static/images/h4.jpg" alt="">
                                </div>
                            </div>
                            <a class="left carousel-control" href="#carousel-example-generic" data-slide="prev">
                                <span class="glyphicon glyphicon-chevron-left"></span>
                            </a>
                            <a class="right carousel-control" href="#carousel-example-generic" data-slide="next">
                                <span class="glyphicon glyphicon-chevron-right"></span>
                            </a>
                        </div>
                    </div>

                </div>

				<p class="hot">Featured products <img src="static/images/hot.gif" /></p>
                <div class="row">

                    <c:forEach items="${products}" var="product">
					    <div class="col-sm-2 col-lg-2 col-md-2">
	                        <div class="thumbnail">
	                            <a href="product/${product.id}"><img src="static/images/${product.catalogid}${product.id}.jpg" alt=""></a>
	                            <div class="caption">
	                                <h4 class="pull-right">$${product.unitPrice}</h4>
	                                <h5><a href="product/${product.id}">${product.name}</a>
	                                </h5>
	                              <%--   <p>${product.description}</p> --%>
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