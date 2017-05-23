<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
	$(function(){
		 $("#table").DataTable();
	})	
</script>

<div class="navbar-header" style="min-width:25%">
	<a class="navbar-brand" href=""><img class="logo" src="static/images/ebazaar-logo.jpg" /></a>
</div>

<div  style="min-width:75%;float: right;">
	<div style="position:top; margin-top: 4px; margin-bottom: 4px;">
		<ul class="nav navbar-top-links">
			<!-- Products -->
			<li class="dropdown">
				<a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="fa fa-wrench fa-fw"></i>Products <i class="fa fa-caret-down"></i>
				</a>
				<ul class="dropdown-menu">
	                <c:forEach items="${catalogs}" var="ct">
	                 <li>
	                     <a href="catalog/${ct.id}"><i class="fa fa-fw"></i> ${ct.name}</a>
	                 </li>
	                 </c:forEach>
				</ul>
			</li>
			<!-- Customer -->
			<li class="dropdown">
				<a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="fa fa-user fa-fw"></i>Customer <i class="fa fa-caret-down"></i>
				</a>
				<ul class="dropdown-menu">
					<li><a href="shoppingcart">Shopping Cart</a></li>
					<c:choose>
						<c:when test="${LoggedIn.loggedIn}">
							<li><a href="myshoppingcart">Retrieve Saved Cart</a></li>
							<li><a href="myorder">Review Orders</a></li>
						</c:when>
					</c:choose>
				</ul>
			</li>
			<!-- Administrator -->
            <c:if test="${LoggedIn.admin}">
			<li class="dropdown">
				<a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="fa fa-gear fa-fw"></i>Administrator <i class="fa fa-caret-down"></i>
				</a>
				<ul class="dropdown-menu">
	                <li><a href="admin/product">New Product</a></li>
	                <li><a href="admin/products">View Products</a></li>
	                <li><a href="admin/catalog">Maintain Catalogs</a></li>
				</ul>
			</li>
            </c:if>			
		</ul>
	</div>
	<div style="position:bottom;">
		<div class="arrange" style="float:left">
		    <div class="js-searchbar-typeahead-input searchbar-typeahead arrange-fill searchbar-typeahead-input empty">
		      <span class="twitter-typeahead typeahead" style="position: relative; display: inline-block; direction: ltr;">
		        <input id="search" style="padding-left:10px;" class="js-searchbar-input js-header-instant-placeholder searchbar-input tt-input" type="text" name="query" placeholder="Search" value="" autocomplete="off" autocapitalize="off" autocorrect="off" spellcheck="false" accesskey="s" role="combobox" aria-haspopup="true" dir="auto" style="position: relative; vertical-align: top;">
		      	<span class="tt-dropdown-menu" style="position: absolute; top: 100%; left: 0px; z-index: 100; display: none; right: auto;"></span>
		      </span>
		      <button class="js-searchbar-clear searchbar-clear btn-link absolute-center-v" type="reset">
		        <i class="wmicon wmicon-remove"></i>
		        <span class="visuallyhidden">Clear search field</span>
		      </button>
		    </div>
		
		    <div class="arrange-fit">
		      <button type="submit" class="searchbar-submit js-searchbar-submit" style="margin-top: 1px;">
		        
		        <i class="wmicon wmicon-search glyphicon glyphicon-search"></i><span class="visuallyhidden">Search</span>
		      </button>
		    </div>
		  </div>
		    
		<div class="userLogin">
			<c:choose>
				<c:when test="${LoggedIn.loggedIn}">
					Welcome ${LoggedIn.loginId} (<a href="logout">Sign Out</a>)
				</c:when>
				<c:otherwise>
					<a href="login">Hello, Sign In</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
			
</div>
