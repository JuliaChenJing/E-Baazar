<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<head>
    <!-- Specify default URL and default target for all links on a page -->
    <base href="${pageContext.request.contextPath}/" />
     
    <!-- Bootstrap Core CSS -->
    <link href="static/css/bootstrap.css" rel="stylesheet">

	<link href="static/css/metisMenu.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="static/css/sb-admin-2.css" rel="stylesheet">
 
    <!-- Custom Fonts -->
    <link href="static/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    
    <!-- Data Tables -->
    <link rel="stylesheet" type="text/css" href="http://cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css">
    
    <!-- jQuery UI -->
    <link rel="stylesheet" type="text/css" href="static/css/jquery-ui-1.9.2.custom.css">
    
    <!-- Main css -->
    <link href="static/css/style.css" rel="stylesheet" type="text/css">

    <!-- jQuery -->
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>

    <script src="static/js/metisMenu.js"></script>

    <!-- jQuery UI -->
    <script src="static/js/jquery-ui-1.9.2.custom.js"></script>
    
    <!-- Bootstrap Core JavaScript -->
    <script src="static/js/bootstrap.js"></script>
    
    <script src="static/js/sb-admin-2.js"></script>
 
    <!-- Data Tables -->
    <script src="http://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script> 

	<title><tiles:getAsString name="title" /></title>
	
	<style>
		#page-wrapper{
			margin:0;
		}	
	</style>
</head>
 
<body>     
    <div id="wrapper">
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
           <tiles:insertAttribute name="header" />
        </nav>
        <div id="page-wrapper">            
            <tiles:insertAttribute name="body" />
        </div>
        <div>
            <tiles:insertAttribute name="footer" />            
        </div>            
    </div> 
        
</body>
</html>