<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.ss.session.SSSessionManager"%>
<%SSSessionManager ss_session=new SSSessionManager(request); 
String message=request.getParameter("error");
/* System.out.println("Error parameter "+request.getParameter("error"));
System.out.println("Error "+request.getAttribute("error2")); */
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>LeadCREATR - Login</title>
<link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/resources/images/fav/apple-touch-icon.png" />"> 
<link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/resources/images/fav/favicon-32x32.png" />"> 
<link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/resources/images/fav/favicon-16x16.png" />"> 
<link rel="manifest" href="<c:url value="/resources/images/fav/site.webmanifest" />"> 
<meta name="msapplication-TileColor" content="#da532c">
<meta name="theme-color" content="#ffffff">

<!-- Stylesheets --> 
<link href="<c:url value="/resources/css/bootstrap4.min.css" />"rel="stylesheet">  
<link href="<c:url value="/resources/css/hxv7cob.css" />"rel="stylesheet">  
<link href="<c:url value="/resources/css/dc_main.css" />"rel="stylesheet">  

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body class="bgHome">
<div class="container">
	<div class="row">
		<div class="col col-md-8">
			<div class="logoDiv">
				<object type="image/svg+xml" data="<c:url value="/resources/images/dc_logo_full.svg"/>" class="logoHome">DemandCentr Logo</object>
				<h1 class="h1_head">welcome to <span>demandcentr</span></h1>
				<p class="pdetails">the industry's first instant-on, always-on, do-it-for-me Marketing Platform as a Service. Connected to one of the largest global audiences of B2B buyers, it's the only demand generation platform tuned to build the ultimate audience engine of engagement-ready buyers.</p>
			</div>
		</div>
		<div class="col col-md-4">
			<div class="loginForm">
				<form method="post" action="login" id="login_form">
					<div class="form-group" id="errMsg"></div>
					<div class="form-group">
						<label for="userEmail" class="sr-only">email</label>
						<input type="email" class="form-control" id="j_username" name="j_username" aria-describedby="emailHelp" placeholder="email">
					<%-- <c:if test="${UorP_Wrong==UorP_Wrong}">
					<%System.out.print("Hello from Abhi"); %>
				`		<p>Hello from abhinandan[poiuytr]</p>
					</c:if> --%>
					</div>
					<div id="errMsg" class="col-md-12 error_msg text-center"></div>
					<div id="msg" class="Msg"></div>
					<div class="form-group">
						<label for="userPass" class="sr-only">password</label>
						<input type="password" class="form-control" id="j_password" name="j_password" aria-describedby="emailHelp" placeholder="password">
					</div>
					<div class="form-group">
						<button type="submit" class="btn btn-login" id="sbmt_btn" title="Log In">Log In</button>
					</div>
					<!-- <div class="form-group margB0 loglinks">
						<a href="#" title="Forgot your Password?">Forgot Password?</a>
						<a href="#" title="Create New Account">Create Account</a>
					</div> -->
				</form>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<footer>
				<nav class="navbar fixed-bottom navbar-light">
					&copy; Selling Simplified Group, Inc. 2019  |  All Rights Reserved  |  <a href="#" title="Privacy Policy">Privacy Policy</a>  |  <a href="https://www.sellingsimplified.com" title="sellingsimplified.com">sellingsimplified.com</a>
				</nav>
			</footer>
		</div>
	</div>
</div>
<%-- <% if(message=="true"){ System.out.println("Calling.......");%>
	<script>alert("calling.........");
	if(message=="UorP_Wrong"){
		$("#errMsg").html("<i class='fa fa-check-circle'></i> Error occured during getting count of downloadable data.").removeClass('callout-danger').addClass('callout-success').show();
		setTimeout(function() {$('#errMsg').hide();}, 5000);
		</script>
		<%}%>
		<% if(message!="true"){ System.out.println("Calling.false......");%>
	<script>alert("calling.........");
	if(message=="UorP_Wrong"){
		$("#errMsg").html("<i class='fa fa-check-circle'></i> Error occured during getting count of downloadable data.").removeClass('callout-danger').addClass('callout-success').show();
		setTimeout(function() {$('#errMsg').hide();}, 5000);
		</script>
		<%}%> --%>
<!-- jQuery and JavaScripts -->
<script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery-migrate-3.0.0.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap.bundle.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.validate.min.js" />"></script>
<!-- script src="<c:url value="/resources/js/jquery-validate.bootstrap-tooltip.js" />"></script-->
<script src="<c:url value="/resources/js/dc-script.js" />"></script>
<script>
function msgDisplay(){
	var message=<%=message%>;
	alert("calling.........");
	if(message=="UorP_Wrong"){
		$("#errMsg").html("<i class='fa fa-check-circle'></i> Error occured during getting count of downloadable data.").removeClass('callout-danger').addClass('callout-success').show();
		setTimeout(function() {$('#errMsg').hide();}, 5000);
		return false;
	}
}


				</script>
</body>
</html>
