<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
 

<script>
	function checkCatalogName(){
		if($("#catalogName").val() == ""){
			$("#catalogNameError").show();
			return false;
		}
		else{
			$("#catalogNameError").hide();
			return true;
		}
	}
	
</script> 
<br>
<h3>Catalogs List</h3>
<hr>
	<form action="admin/catalog" method="post">        
        <div class="row">
		  <div class="col-lg-6"> 
		  	<div class="clo-lg-1">
		    	<span style="color:red; display:none; padding-left:10px;" id="catalogNameError">You must input catalog name</span>
		    </div>
		    <div class="input-group clo-lg-5">
		      <input type="text" id="catalogName" name="name" style="width:100%" class="form-control" placeholder="Catalog name here...">
		      <span class="input-group-btn">
		        <input class="btn btn-primary" type="submit" onclick="return checkCatalogName();" value="Add">
		      </span>		    
		    </div>		   
		  </div>
		</div>
	</form> 
    <table id="table" class="table">
    	<thead><td>Id</td><td>Name</td><td></td></thead>
    	<tbody>
    		<c:forEach items = "${catalogs}" var="catalog">
    		<tr>
    			<td>${catalog.id}</td>
    			<td>${catalog.name}</td>
    			<td><a href="admin/catalog/${catalog.id}" class="action" >Delete</a></td>
    		</tr>
    		</c:forEach>       	
    	</tbody>
    </table> 
 