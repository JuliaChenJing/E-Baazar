   <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
   <div class="navbar-default sidebar" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav" id="side-menu">
            	<c:if test="${not LoggedIn.admin}">
				 	<li><a href=""><i class="fa fa-shopping-cart fa-fw"></i>Products</a>
	                <c:forEach items="${catalogs}" var="ct">
	                 <li>
	                     <a href="catalog/${ct.id}"><i class="fa fa-fw"></i> ${ct.name}</a>
	                 </li>
	                 </c:forEach>
	                </li>
<!--
   					<li><a href="shoppingcart"><i class="fa fa-shopping-cart fa-fw"></i>Shopping Cart</a></li>
   					<li><a href="myshoppingcart"><i class="fa fa-fw"></i>Retrieve Saved Cart</a></li>
 -->
	            </c:if>
                
                <c:if test="${LoggedIn.admin}">
	                <li>
	                    <a href="#"><i class="fa fa-wrench fa-fw"></i>Maintain Products<span class="fa arrow"></span></a>
	                    <ul class="nav nav-second-level">
	                        <li>
	                            <a href="admin/product">New Product</a>
	                        </li>
	                        <li>
	                            <a href="admin/products">View Products</a>
	                        </li>
	                    </ul>                    
	                </li>
	                <li>
                    	<a href="admin/catalog"><i class="fa fa-edit fa-fw"></i>Maintain Catalog</a>
                	</li>
                </c:if>
            </ul>
        </div>
   </div>