<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

 <script type="text/javascript">
     $(function () {
         $('#mfg').datepicker();
     });
 </script>
<br>
<c:choose>
	<c:when test="${empty isEdit}">
		<h3>Add new product</h3>
	</c:when>
	<c:otherwise>
		<h3>Edit product</h3>
	</c:otherwise>
</c:choose>
<hr>
<form:form action="admin/product"  modelAttribute="newProduct" class="form-horizontal" enctype="multipart/form-data">
		<fieldset>		

			<form:errors path="*" cssClass="alert alert-danger" element="div"/>
			 
			<form:input id="productId" path="productId" type="hidden"  />
			 
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">Name</span>
			  <form:input id="name" path="name" type="text"  placeholder="Product name" class="form-control" aria-describedby="basic-addon1"/>			  
			</div><form:errors path="name" cssClass="text-danger"/>
			<br>			 
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon7">Category</span>
			  <form:select path="category" class="form-control" aria-describedby="basic-addon7">
			  		<form:options items="${catalogs}" itemLabel="name" itemValue="id" />
			  </form:select>
			</div>
			<br>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon2">UnitPrice</span>
			  <form:input id="unitPrice" path="unitPrice" type="text"  placeholder="0.0" class="form-control" aria-describedby="basic-addon2"/>
			  <form:errors path="unitPrice" cssClass="text-danger"/>
			</div>
			<br>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon7">Mfg Date</span>
			  <form:input id="mfg" path="mfg" type="text"  placeholder="MM/DD/YYYY" class="form-control" aria-describedby="basic-addon7"/>			  			 
			  <form:errors path="mfg" cssClass="text-danger"/>
			</div>
			<br>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon4">UnitsInStock</span>
			  <form:input id="unitsInStock" path="unitsInStock" type="text"  placeholder="0" class="form-control" aria-describedby="basic-addon4"/>
			  <form:errors path="unitsInStock" cssClass="text-danger"/>
			</div>
			<br> 
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon6">Description</span>			  
			  <form:textarea id="description" path="description" rows = "2"  class="form-control" aria-describedby="basic-addon6"/>
			</div>	


			<br>
			<div class="form-group">
				<div class="col-lg-offset-2 col-lg-10">
					<c:choose>
						<c:when test="${empty isEdit}">
							<input type="submit" id="btnAdd" class="btn btn-primary btn-large" value ="Add Product"/>
						</c:when>
						<c:otherwise>
							<input type="submit" id="btnAdd" class="btn btn-primary btn-large" value ="Update Product"/>
						</c:otherwise>
					</c:choose>				
				</div>
			</div>
			
		</fieldset>
	</form:form>


