<%@page import="java.util.List"%>
<%@page import="sun.swing.StringUIClientPropertyKey"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Locale"%>


<%-- <%@ page contentType="text/html; charset=UTF-8" %> --%>
<%
String userName="akumar";
String clientName="ZIFF_DAVIS,BNZSA_CALLING,TALEND";
boolean showPopup=false;

/* LdapUserDetailService userDetailService = (LdapUserDetailService) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
List<String> lClient=userDetailService.getClientList();

System.out.println("lClient" + lClient);
if (StringUtils.isBlank(clientName)) {
	if (lClient != null && lClient.size() > 0) {
		int iLclient = lClient.size();
		if (iLclient > 1) {
			showPopup = true;
		}
		clientName = lClient.get(0).trim();
	}
} */
if (StringUtils.isBlank(clientName)) {
	clientName = "SSG";
}
String internalKey = "internal";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<c:set var="url">${pageContext.request.requestURL}</c:set>
<meta charset="utf-8">

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>LeadCREATR Report</title>
<link rel="apple-touch-icon" sizes="180x180" href="<c:url value="resources/images/fav/apple-touch-icon.png" />">
<link rel="icon" type="image/png" sizes="32x32" href="<c:url value="resources/images/fav/favicon-32x32.png" />">
<link rel="icon" type="image/png" sizes="16x16" href="<c:url value="resources/images/fav/favicon-16x16.png" />">
<link rel="manifest" href="<c:url value="resources/images/fav/site.webmanifest" />">
<meta name="msapplication-TileColor" content="#da532c">
<meta name="theme-color" content="#ffffff">
<!-- Stylesheets -->

<link rel="stylesheet" href="<c:url value="resources/css/bootstrap4.min.css" />">
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="<c:url value="resources/css/hxv7cob.css" />">
<link rel="stylesheet" href="<c:url value="resources/css/bootstrap-multiselect.css" />">
<link rel="stylesheet" href="<c:url value="resources/css/daterangepicker.css" />">
<link rel="stylesheet" href="<c:url value="resources/css/dc_main.css" />">
<style>

</style>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script> <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>  <![endif]-->
<!-- jQuery and JavaScripts -->

<script src="<%=request.getContextPath()%>/resources/js/jquery-3.3.1.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/jquery-migrate-3.0.1.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/moment.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/dataTables.bootstrap4.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/daterangepicker.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/dataTables.select.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/bootstrap-multiselect.js"></script>
<script src="<c:url value="resources/js/dataTables.buttons.min.js" />"></script>
<script src="<c:url value="resources/js/vfs_fonts.js" />"></script>
<script src="<c:url value="/resources/js/jquery.confirm.js" />"></script> 
<script src="<%=request.getContextPath()%>/resources/js/lc-script.js"></script>
<style>
#dialogbox{ position:fixed}
</style>
<script>
function CustomAlert(){
		this.render = function(dialog){
				var winW = window.innerWidth;
				var winH = window.innerHeight;
				var dialogoverlay = document.getElementById('dialogoverlay');
				var dialogbox = document.getElementById('dialogbox');
				dialogoverlay.style.display = "block";
				//dialogoverlay.style.height = winH+"10px";
				dialogoverlay.style.height = "1150px";
				dialogbox.style.left = (winW/2) - (550 * .5)+"px";
				dialogbox.style.top = "100px";
				dialogbox.style.display = "block";
				document.getElementById('dialogboxhead').innerHTML = "Alert!";
				document.getElementById('dialogboxbody').innerHTML = dialog;
				document.getElementById('dialogboxfoot').innerHTML = '<button class="btn btn-info" onclick="Alert.ok()">OK</button>';
		}
		this.ok = function(){
			document.getElementById('dialogbox').style.display = "none";
			document.getElementById('dialogoverlay').style.display = "none";
		}
	}


var Alert = new CustomAlert();

function noBack() {
		window.history.forward();
}

	var start=moment();
	var end=moment();
	var restDownloadurl='';
	var restCounturl='';
	
function reloaddatatable(){
		var table = $('#example').DataTable();
		//alert('table1'+table);
		if($('#selectedCompanyName').val()=="" && $('#industrySelect').val()=="" && $('#countrySelect').val()=="" && $('#jobFunctionSelect').val()=="" && $('#seniorityLevelSelect').val()==""&& $('#dateRangeSelect span').text()=="Date Range Ignored" && $('#ignoreDate').html()=="" && $('#Records_With_Company_id').text()=="" && $('#Records_With_Email').text()=="" && $('#notDownloaded').text()==""){
			Alert.render(" You Have Not Selected Any Filter! \n Please Select One To Proceed!");
			return;
		}
		
		disableButtons();
		var restloadurl="searchFilter.do?clientName=<%=clientName%>"+"&companyName="+encodeURIComponent($('#selectedCompanyName').val())+"&industryName="+encodeURIComponent($('#industrySelect').val().join("!!|"))+"&countryName="+$('#countrySelect').val().join("!!|")+"&jobFunction="+encodeURIComponent($('#jobFunctionSelect').val().join("!!|"))+"&seniorityLevel="+$('#seniorityLevelSelect').val().join("!!|")+"&dateRange="	+$('#dateRangeSelect span').text()+"&ignoreDate="+$('#ignoreDate').html()+"&Records_With_Company_id="+$('#Records_With_Company_id').text()+"&Records_With_Email="+$('#Records_With_Email').text()+"&notDownloaded="+$('#notDownloaded').text();
		
    	restDownloadurl="downloadAll.do?clientName=<%=clientName%>"+"&companyName="+encodeURIComponent($('#selectedCompanyName').val())+"&industryName="+encodeURIComponent($('#industrySelect').val().join("!!|"))+"&countryName="+$('#countrySelect').val().join("!!|")+"&jobFunction="+$('#jobFunctionSelect').val().join("!!|")+"&seniorityLevel="+$('#seniorityLevelSelect').val().join("!!|")+"&dateRange="	+$('#dateRangeSelect span').text()+"&ignoreDate="+$('#ignoreDate').html()+"&Records_With_Company_id="+$('#Records_With_Company_id').text()+"&Records_With_Email="+$('#Records_With_Email').text()+"&notDownloaded="+$('#notDownloaded').text();
		restCounturl="downloadCount.do?clientName=<%=clientName%>"+"&companyName="+encodeURIComponent($('#selectedCompanyName').val())+"&industryName="+encodeURIComponent($('#industrySelect').val().join("!!|"))+"&countryName="+$('#countrySelect').val().join("!!|")+"&jobFunction="+$('#jobFunctionSelect').val().join("!!|")+"&seniorityLevel="+$('#seniorityLevelSelect').val().join("!!|")+"&dateRange="	+$('#dateRangeSelect span').text()+"&ignoreDate="+$('#ignoreDate').html()+"&Records_With_Company_id="+$('#Records_With_Company_id').text()+"&Records_With_Email="+$('#Records_With_Email').text()+"&notDownloaded="+$('#notDownloaded').text();
		popupCount(restCounturl,restloadurl);
		
		//setTimeout(function() { returnCount(); }, 5000);
}

function popupCount(restCounturl,restloadurl){
	var downloadCount;
	var remainingCount;
	var downloadCountEmail;
	$.ajax({
		"type" : 'POST',
		url: restCounturl,
		success: function (data,textStatus, xhr) {
			        var table = $('#example').DataTable();
			        table.destroy();
			        $('#example').empty();
			        var remainingCount = data.substring( 0, data.indexOf("|||"));
			        var downloadCountEmail = data.substring(data.indexOf("|||")+3,data.indexOf('@@@') );
			        var downloadCount = data.substring((data.indexOf('@@@')+3),data.length);
			        remainingCount=Number(remainingCount);
			        downloadCountEmail=Number(downloadCountEmail);
			        downloadCount=Number(downloadCount);
					/* var countMap=jQuery.parseJSON(data); 
					downloadCount=countMap.data.downloadCount;
					remainingCount=countMap.data.remainingLimit;
					downloadCountEmail=countMap.data.downloadCountEmail; */
					var popupLimit;
					loaddatatable(restloadurl);
					//$("#downloadable").val(downloadCount);
					$("#downloadable").text(downloadCount);
					//$("#downloadable_with_email").val(downloadCountEmail);
					$("#downloadable_with_email").text(downloadCountEmail);
					if(downloadCount > 4000 && remainingCount >0){
							if(downloadCount>=remainingCount){
								popupLimit=remainingCount;
							}
							else if(downloadCount < remainingCount){
								popupLimit=downloadCount;
							}
							Alert.render(" We are showing Latest 4000 of "+downloadCount+"  Records.");
					}
					/* else{
							alert("YOUR LIMIT HAVE BEEN EXCEEDED.\nRENEW YOUR PACKAGE TO DOWNLOAD MORE");
					} */
			},
			error: function(xhr, textStatus, errorThrow){
				$("#errMsg").html("<i class='fa fa-check-circle'></i> Error occured during getting count of downloadable data.").removeClass('callout-danger').addClass('callout-success').show();
				setTimeout(function() {$('#errMsg').hide();}, 5000);
				return false;
			} 
		});
}

	
function loaddatatable(resturl) {
		var rows_selected = [];
		var table= $('#example').DataTable({
			 "fnInitComplete": function(oSettings, json) {
				  enableButtons();
				//alert( 'DataTables has finished its initialisation.' );
			    },
						select:{
									style: 'multi'
								},
						
						"scrollY" : "70vh",
						"scrollX" : true,
						scrollCollapse : true,
						processing : true,
						ajax:{
								url : resturl,
								headers: { "HeaderName": "<%=internalKey%>" },
								//dataType:'json',
								"type" : 'POST',
								contentType : 'charset=utf-8',
								"dataSrc": ""
							},
							
						"columns" : [
										{
											data : "modifiedon",
											title: "Modified On",
											"visible": false
										},
			 							{
											data : "company_id",
											title: "company_id_company",
											"visible": false
										},
										{
											data : "contact_id",
											title: "Contact ID",
											/*  "visible": false */ 
										},
										{
											data : "company_id",
											title: "Company ID"
											// "visible": false
										},
										{
											data : "companyname",
											title: "Company Name"
										},
										{
											data : "firstname",
											title: "First Name"
										},
										{
											data : "lastname",
											title: "Last Name"
										},
										{
											data : "corp_email",
											title: "Corp Email"
										},
										{
											data : "country",
											title: "Country"
										},
										{
											data : "jobtitle",
											title: "Job Title"
										},
										{
											data : "jobfunction",
											title: "Job Function"
										},
										{
											data : "seniority_level",
											title: "Seniority Level"
										},
										{
											data : "industry",
											title: "Industry"
										},
										{
											data : "revenue",
											title: "Revenue" 
										},
										{
											data : "employeetotal",
											title: "Employee Size"
										}
									],
							order: [2, 'asc']
					});
		//alert(data);
		table.on('deselect', function (e, dt, type, indexes) {
					var v=table.rows({selected: true});
					var x=table.rows('.selected');
				});
		table.on('select', function (e, dt, type, indexes) {
					var v=table.rows({selected: true});
					var x=table.rows('.selected');
				});
		
		
}

$(document).ready(function(){
	 var listItems;
	var clientName;
	var clientName = "SSG";
	clientName=clientName.replace("[","");
	clientName=clientName.replace("]","");
	clientName=clientName.split(",");
	for (var i =0; i < clientName.length; i++) {
			var client=clientName[i];
			listItems += '<option value="' + client + '">' + client + '</option>';
	}
	$("#clientSelect").html(listItems);

	<%
		if (showPopup){
	%>
				$(window).load(function(){
				$('#selectClientModal').modal('show');
				});
	<%	}
		else {%>
				$(window).load(function(){
					$('#selectClientModal').modal('hide');
					//$('#myModal').modal('show');
				}); 
		<%}%> 
		
		if($("#ignoreDate").is(':checked')){
			$('#ignoreDate').next("small").addClass("text-danger");
			$('#dateRangeSelect i').hide();
			$('#dateRangeSelect span').html('Date Range Ignored');
			$("#dateRangeSelect").off("click").addClass('disabled').css({"border-color":"#a94442","background-color":"#f2dede"});         
		}
		url="contactDetails.internal?clientName=<%=clientName%>" 
		var table=loaddatatable(url);
		limitUpdator();
		//setTimeout(function() { returnCount(); }, 10000);
		/*  Getting count of selected rows */
		$('#example tbody').on('click', 'tr', function(){
			$("#tableRowNumber").text($(this).index()+1);
		});
		/* Ajax calls for getting filter data */
		$.ajax({
				url: "countContact.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				type: "POST",
				success: function(data, textStatus, xhr){
							$("#totalContact").text(data);
							//$("#downloadable").val(data);
							$("#downloadable").text(data);
						}
			}); 
		$.ajax({
				url: "countEmail.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				type: "POST",
				success: function(data, textStatus, xhr){
							$("#emailContact").text(data);
							//$("#downloadable_with_email").val(data);
							$("#downloadable_with_email").text(data);
						}
			});
		$.ajax({
				url: "getDownloadReady.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				type: "POST",
				success: function(data, textStatus, xhr){
							$("#getDownloadReady").text(data);
						}
			});
		$.ajax({
				url: "getDownloadReadyToday.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				type: "POST",
				success: function(data, textStatus, xhr){
							$("#getDownloadReadyToday").text(data);
						}
			});
		$.ajax({
				url: "dailyLimitCount.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				type: "POST",
				success: function(data, textStatus, xhr){
							$("#dailyDownloadLimit").text(data);
						}
			});
		$.ajax({
				url: "contactCreditLimit.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				type: "POST",
				success: function(data, textStatus, xhr){
							$("#contactCreditLimit").text(data);
						}
			});
		$.ajax({
				url: "industry.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				success: function(data, textStatus, xhr){
							data = JSON.parse(data);
							var listItems;
							for (var i =0; i < data.length; i++) {
									var indus=data[i].industry;
									indus=indus.replace("'","\'");
									listItems += '<option value="' + indus+ '">' + data[i].industry+ '</option>';
								}
								$("#industrySelect").html(listItems);
								$("#industrySelect").multiselect('rebuild');
						}
			});
		$.ajax({
				url: "country.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				success: function(data, textStatus, xhr){
							data = JSON.parse(data);
							var listItems;
							for (var i = 0; i < data.length; i++) {
									listItems += "<option value='" + data[i].country+ "'>" + data[i].country + "</option>";
								}
								$("#countrySelect").html(listItems);
								$("#countrySelect").multiselect('rebuild');
						}
			});
		 $.ajax({
				url: "jobfunction.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				success: function(data, textStatus, xhr){
							data = JSON.parse(data);
							var listItems;
							for (var i = 0; i < data.length; i++) {
									listItems += "<option value='" +data[i].jobfunction+ "'>" + data[i].jobfunction + "</option>";
								}
								$("#jobFunctionSelect").html(listItems);
								$("#jobFunctionSelect").multiselect('rebuild');
						}
			}); 
		$.ajax({
				url: "senioritylevel.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				success: function(data, textStatus, xhr){
							data = JSON.parse(data);
							var listItems;
							for (var i = 0; i < data.length; i++) {
									listItems += "<option value='" +data[i].seniority_level+ "'>" + data[i].seniority_level + "</option>";
								}
								$("#seniorityLevelSelect").html(listItems);
								$("#seniorityLevelSelect").multiselect('rebuild');
						}
			});
		
		// Multi Select Code
		$("#industrySelect").multiselect({
				nonSelectedText: 'Select Industry',
				enableFiltering: true,
				enableCaseInsensitiveFiltering: true,
				enableHTML:true,
				//includeSelectAllOption:true,
				buttonClass:'btn btn-light btn-sm border border-info',
				buttonWidth: '100%',
				maxHeight: 200,
				templates: {
								li				: '<li><a href="javascript:void(0);"><label class="pl-2 col-form-label col-form-label-sm"></label></a></li>',
								filter			: '<li class="multiselect-item filter"><div class="input-group input-group-sm m-0 mb-1"><div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-search"></i></span></div><input class="form-control form-control-sm multiselect-search" type="text"></div></li>',
								filterClearBtn	: '<div class="input-group-append"><button class="btn btn-sm btn-outline-secondary multiselect-clear-filter" type="button"><i class="fa fa-close"></i></button></div>',
							},
				/* selectedClass: 'bg-secondary' */
		});
		$("#countrySelect").multiselect({
				nonSelectedText: 'Select Country',
				enableFiltering: true,
				includeSelectAllOption: true,
				enableCaseInsensitiveFiltering: true,
				buttonClass:'btn btn-light btn-sm border border-info',
				buttonWidth: '100%',
				maxHeight: 200,
				templates: {
								li				: '<li><a href="javascript:void(0);"><label class="pl-2 col-form-label col-form-label-sm"></label></a></li>',
								filter			: '<li class="multiselect-item filter"><div class="input-group input-group-sm m-0 mb-1"><div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-search"></i></span></div><input class="form-control form-control-sm multiselect-search" type="text"></div></li>',
								filterClearBtn	: '<div class="input-group-append"><button class="btn btn-sm btn-outline-secondary multiselect-clear-filter" type="button"><i class="fa fa-close"></i></button></div>',
							},
				/* selectedClass: 'bg-secondary' */
		});
		$("#jobFunctionSelect").multiselect({
				nonSelectedText: 'Select Job Function',
				selectedTop:true,
				enableFiltering: true,
				includeSelectAllOption: true,
				enableCaseInsensitiveFiltering: true,
				buttonClass:'btn btn-light btn-sm border border-info',
				buttonWidth: '100%',
				maxHeight: 200,
				templates: {
								li				: '<li><a href="javascript:void(0);"><label class="pl-2 col-form-label col-form-label-sm"></label></a></li>',
								filter			: '<li class="multiselect-item filter"><div class="input-group input-group-sm m-0 mb-1"><div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-search"></i></span></div><input class="form-control form-control-sm multiselect-search" type="text"></div></li>',
								filterClearBtn	: '<div class="input-group-append"><button class="btn btn-sm btn-outline-secondary multiselect-clear-filter" type="button"><i class="fa fa-close"></i></button></div>'
							},
				/* selectedClass: 'bg-secondary' */
		});
		$("#seniorityLevelSelect").multiselect({
				nonSelectedText: 'Select Seniority Level',
				enableFiltering: true,
				includeSelectAllOption: true,
				enableCaseInsensitiveFiltering: true,
				buttonClass:'btn btn-light btn-sm border border-info',
				buttonWidth: '100%',
				maxHeight: 200,
				templates: {
								li				: '<li><a href="javascript:void(0);"><label class="pl-2 col-form-label col-form-label-sm"></label></a></li>',
								filter			: '<li class="multiselect-item filter"><div class="input-group input-group-sm m-0 mb-1"><div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-search"></i></span></div><input class="form-control form-control-sm multiselect-search" type="text"></div></li>',
								filterClearBtn	: '<div class="input-group-append"><button class="btn btn-sm btn-outline-secondary multiselect-clear-filter" type="button"><i class="fa fa-close"></i></button></div>'
							},
				/* selectedClass: 'bg-secondary' */
		});
});

// Date Rangepicker Code
$(function() {
		//cb(start, end);
		$('#dateRangeSelect').on('cancel.daterangepicker', function(ev, picker) {
				$(this).val('');
				$('#dateRangeSelect span').html('No Date Selected');
		});
		$('#ignoreDate').click(function(){
				if($("#ignoreDate").is(':checked')){
						$('#ignoreDate').next("small").addClass("text-danger");
						$('#dateRangeSelect i').hide();
						$('#dateRangeSelect span').html('Date Range Ignored');
						$("#dateRangeSelect").off("click").addClass('disabled').css({"border-color":"#a94442","background-color":"#f2dede"});
				}
				else{
						$('#ignoreDate').next("small").removeClass("text-danger");
						$("#dateRangeSelect i").show();
						$('#dateRangeSelect span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
						$('#dateRangeSelect').removeClass('disabled').css({"border-color":"#ccc","background-color":"inherit"});
						dateRangeSelectFunction();
				}
		});
		/*   */
		$("form").on("reset", function(){
					setTimeout(function(){
						$("#industrySelect, #countrySelect, #jobFunctionSelect, #seniorityLevelSelect").multiselect("rebuild");
						$('#ignoreDate').next("small").removeClass("text-danger");
						$("#dateRangeSelect i").show();
						$('#dateRangeSelect span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
						$('#dateRangeSelect').removeClass('disabled').css({"border-color":"#ccc","background-color":"inherit"});
						$("#ignoreDate").removeAttr("checked");
						corp_emailCheckbbox();
						company_idCheckbbox();
						$("#Records_With_Company_id").removeAttr("checked");
						$("#Records_With_Company_id").removeAttr("checked");
						$('#notDownloaded').html('');
						dateRangeSelectFunction();
				});
		});    
});

function dateRangeSelectFunction(){
		function cb(start, end) {
				$('#dateRangeSelect span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
		}
		$('#dateRangeSelect').daterangepicker({
				startDate: start,
				endDate: end,
				ranges: {
							'Today': [moment(), moment()],
							'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
							'Last 7 Days': [moment().subtract(6, 'days'), moment()],
							'Last 30 Days': [moment().subtract(29, 'days'), moment()],
							'This Month': [moment().startOf('month'), moment().endOf('month')],
							'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
						},
				"showDropdowns": true,
				"applyButtonClasses": "dc-yellow"
		}, cb);
}

function corp_emailCheckbbox(){
		if($("#Records_With_Email").is(':checked')){
				$('#Records_With_Email').html('corp_email');
				//alert("if");
		}else{
				$('#Records_With_Email').html('');
				// alert("else");
		}
}

function company_idCheckbbox(){
		if($("#Records_With_Company_id").is(':checked')){
				$('#Records_With_Company_id').html('company_id');
		}else{
				$('#Records_With_Company_id').html('');
		}
}

function not_DownloadedCheckbbox(){
		if($("#notDownloaded").is(':checked')){
				$('#notDownloaded').html('notDownloaded');
		}else{
				$('#notDownloaded').html('');
		}
}

//for downloading data or sending error reports and information regarding download of data

function confirmOkDownloadSelected(contactIds,dailyDownloadLimit,downloadreadyToday,selectedIndecesLength,selectedIndices,downloadreadyTotal,contactCreditLimit,table) {
	var dubselectedIndecesLengthCreditLimit	;	
	if(dailyDownloadLimit >=downloadreadyToday +selectedIndecesLength){
					dubselectedIndecesLengthCreditLimit = (contactCreditLimit - downloadreadyTotal);
			}
			else if(dailyDownloadLimit <=downloadreadyToday +selectedIndecesLength){
					dubselectedIndecesLengthCreditLimit = (dailyDownloadLimit - downloadreadyToday);
			}
			var contactIds=[];
			//loop for iterating only top remaining contact
			for(var i = 0;i < (dubselectedIndecesLengthCreditLimit);i++){
					var index=selectedIndices[i];
					contactIds.push(table.data()[index].contact_id);
			}
			var contactIds=(contactIds.join(","));
			$.ajax({
					"type" : 'POST',
					//contentType : 'application/json; charset=utf-8',
					contentType : 'charset=utf-8',
					dataType : 'json',
					data :{},
					url: "downloadSelected.do?clientName=<%=clientName%>", 
					success: function (data,textStatus, xhr) {
									JSONToCSVConvertor(data,"_Leads",true);
							}
			});
	}

function limitUpdator(){
		$.ajax({
				url: "getDownloadReady.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				type: "POST",
				success: function(data, textStatus, xhr) {
								$("#getDownloadReady").text(data);
						}
		});
		$.ajax({
				url: "getDownloadReadyToday.internal?clientName=<%=clientName%>",
				headers: { "HeaderName": "<%=internalKey%>" },
				type: "POST",
				success: function(data, textStatus, xhr) {
								$("#getDownloadReadyToday").text(data);
						}
		});
}

/* <-- ****************************Confirm pop-up model *** Strat*******************  --> */
 
 $('#selectClientModal').modal() ;

function myFunction() {
	document.getElementById("myForm").submit();
   $('#selectClientModal').modal('hide');
} 
function confirmPopup(restDownloadurl){
$.confirm({
    'message' : "Your Download Limit is Exceeding !\nClick OK to Download Remaining Credits as per Limit \nOtherwise Press Cancel.",
    'buttons' : {
     '<span class="fa fa-check fa-2x" aria-hidden="true"></span> OK' : {
      'class' : 'yes',
      'action': function(){
       confirmOkDownloadAll(restDownloadurl);
      }
     },
     '<span class="fa fa-times fa-2x" aria-hidden="true"></span>  Cancel' : {
      'class' : 'no',
     
      'action': function(){ enableButtons();} // Nothing to do in this case. You can as well omit the action property.
     }
    }
   });
}
function confirmPopupDownloadSelected(contactIds,dailyDownloadLimit,downloadreadyToday,selectedIndecesLength,selectedIndices,downloadreadyTotal,contactCreditLimit,table){
	$.confirm({
	    'message' : "Your Download Limit is Exceeding !\nClick OK to Download Remaining Credits as per Limit \nOtherwise Press Cancel.",
	    'buttons' : {
	     '<span class="fa fa-check fa-2x" aria-hidden="true"></span> OK' : {
	      'class' : 'yes',
	      'action': function(){
	       confirmOkDownloadSelected(contactIds,dailyDownloadLimit,downloadreadyToday,selectedIndecesLength,selectedIndices,downloadreadyTotal,contactCreditLimit,table);

	      }
	     },
	     '<span class="fa fa-times fa-2x" aria-hidden="true"></span>  Cancel' : {
	      'class' : 'no',
	      'action': function(){enableButtons();} // Nothing to do in this case. You can as well omit the action property.
	     }
	    }
	   });
	}
//for downloading all selected data 
function downloadSelected(){
	disableButtons();
	debugger;
		var table=$('#example').DataTable();
		var dailyDownloadLimitString=$("#dailyDownloadLimit").html();
		var dailyDownloadLimit= parseInt(dailyDownloadLimitString, 10);
		var downloadreadyTodayString=$("#getDownloadReadyToday").html();
		var downloadreadyToday=parseInt(downloadreadyTodayString, 10);
		var downloadreadyTotalString=$("#getDownloadReady").html();
		var downloadreadyTotal=parseInt(downloadreadyTotalString, 10);
		var contactCreditLimitString=$("#contactCreditLimit").html();
		var contactCreditLimit=parseInt(contactCreditLimitString, 10);
		var selectedIndices=table.rows({selected: true})[0];
		var contactIds=[];
		var selectedIndecesLength=selectedIndices.length;
		for(var i=0;i<selectedIndecesLength;i++){
				var index=selectedIndices[i];
				contactIds.push(table.data()[index].contact_id);
		}
		if(contactCreditLimit <= (downloadreadyTotal) || (dailyDownloadLimit) <= (downloadreadyToday)){
				limitUpdator();
				Alert.render("Sorry Your Daily Download Limit or Contact Credit is Over!");
				enableButtons();
		}
		else{
				if(contactCreditLimit >= (downloadreadyTotal + selectedIndecesLength)){
						if(selectedIndecesLength === 0){
								limitUpdator();
								Alert.render("You Have Not Selected Any Data Please Select To Proceed!");
								enableButtons();
						}
						else if((dailyDownloadLimit) >= (downloadreadyToday + selectedIndecesLength)){
								var contactIds=(contactIds.join(","));
								$.ajax({
										"type" : 'POST',
										//contentType : 'application/json; charset=utf-8',
										contentType : 'charset=utf-8',
										dataType : 'json',
										data :{},
										url: "downloadSelected.do?clientName=<%=clientName%>",
										success: function ( data,textStatus, xhr) {
														JSONToCSVConvertor(data,"_Leads",true);
														enableButtons();
													},
										error: function(xhr, textStatus, errorThrow) {
											$("#errMsg").html("<i class='fa fa-check-circle'></i> Data not downloaded please try again after checking your connection.").removeClass('callout-danger').addClass('callout-success').show();
											setTimeout(function() {$('#errMsg').hide();}, 5000);
											enableButtons();
											return false;
									}
									});
						}
						else if(dailyDownloadLimit == downloadreadyToday){
								limitUpdator();
								Alert.render("Sorry! Your Daily Download Limit is Over");
								enableButtons();
						}
						else if((dailyDownloadLimit > downloadreadyToday) && (dailyDownloadLimit < downloadreadyToday+selectedIndecesLength)){
								/* confirm     area */
								confirmPopupDownloadSelected(contactIds,dailyDownloadLimit,downloadreadyToday,selectedIndecesLength,selectedIndices,downloadreadyTotal,contactCreditLimit,table);
						} 
					}
					else if(contactCreditLimit == downloadreadyTotal + selectedIndecesLength){
							limitUpdator();
							Alert.render("Sorry Your Contact Credit Limit is Over");
							enableButtons();
					}
					else if(( contactCreditLimit > downloadreadyTotal ) && (contactCreditLimit <= downloadreadyTotal + selectedIndecesLength)){
							
						confirmPopupDownloadSelected(contactIds,dailyDownloadLimit,downloadreadyToday,selectedIndecesLength,selectedIndices,downloadreadyTotal,contactCreditLimit,table);
					}
		}
}

function downloadAll(restDownloadurl,checkEmail,restCounturl) {
	
	disableButtons();
		var table=$('#example').DataTable();
		var rowCount=table.rows().count();
		if(rowCount<=0) {
				Alert.render("No Data Found, Please Try Again");
				enableButtons();
		}
		else {
				if(restDownloadurl == "") {
						restDownloadurl="downloadAll.do?clientName=<%=clientName%>"+"&companyName="+encodeURIComponent($('#selectedCompanyName').val())+"&industryName="+encodeURIComponent($('#industrySelect').val().join("!!|"))+"&countryName="+$('#countrySelect').val().join("!!|")+ "&jobFunction="+$('#jobFunctionSelect').val().join("!!|")+"&seniorityLevel="+$('#seniorityLevelSelect').val().join("!!|")+"&dateRange="	+$('#dateRangeSelect span').text()+"&ignoreDate="+$('#ignoreDate').html()+"&Records_With_Company_id="+$('#Records_With_Company_id').text()+"&Records_With_Email="+$('#Records_With_Email').text()+"&notDownloaded="+$('#notDownloaded').text();
						restCounturl="downloadCount.do?clientName=<%=clientName%>"+"&companyName="+encodeURIComponent($('#selectedCompanyName').val())+"&industryName="+encodeURIComponent($('#industrySelect').val().join("!!|"))+"&countryName="+$('#countrySelect').val().join("!!|")+ "&jobFunction="+$('#jobFunctionSelect').val().join("!!|")+"&seniorityLevel="+$('#seniorityLevelSelect').val().join("!!|")+"&dateRange="	+$('#dateRangeSelect span').text()+"&ignoreDate="+$('#ignoreDate').html()+"&Records_With_Company_id="+$('#Records_With_Company_id').text()+"&Records_With_Email="+$('#Records_With_Email').text()+"&notDownloaded="+$('#notDownloaded').text();
				}
				restDownloadurl= restDownloadurl+"&emailCheck="+checkEmail;
				restCounturl=restCounturl+"&emailCheck="+checkEmail;
				var dailyDownloadLimitString=$("#dailyDownloadLimit").html();
				var dailyDownloadLimit= parseInt(dailyDownloadLimitString, 10);
				var downloadreadyTodayString=$("#getDownloadReadyToday").html();
				var downloadreadyToday=parseInt(downloadreadyTodayString, 10);
				var downloadreadyTotalString=$("#getDownloadReady").html();
				var downloadreadyTotal=parseInt(downloadreadyTotalString, 10);
				var contactCreditLimitString=$("#contactCreditLimit").html();
				var contactCreditLimit=parseInt(contactCreditLimitString, 10);
				
				$.ajax({
						"type" : 'POST',
						url: restCounturl,
						contentType : 'charset=utf-8',
						success: function (data,textStatus, xhr) {
							 var remainingCount = data.substring( 0, data.indexOf("|||"));
						        var downloadCountEmail = data.substring(data.indexOf("|||")+3,data.indexOf('@@@') );
						        var downloadCount = data.substring((data.indexOf('@@@')+3),data.length);
						        remainingCount=Number(remainingCount);
						        downloadCountEmail=Number(downloadCountEmail);
						        downloadCount=Number(downloadCount);
									/* var countMap=jQuery.parseJSON(data);
									var downloadCount=countMap.data.downloadCount;
									var remainingCount=countMap.data.remainingLimit; */
									
									if(remainingCount <= 0){
											limitUpdator();
											Alert.render("Sorry Your Daily Download Limit or Contact Credit Limit is Over!");
									enableButtons();
									}
									else {
											if(contactCreditLimit >= (downloadreadyTotal + downloadCount)){
													if((dailyDownloadLimit) >= (downloadreadyToday + downloadCount)){
														$.ajax({
															"type" : 'POST',
															contentType : 'charset=utf-8',
															//contentType : 'application/json; charset=utf-8',
															dataType : 'json',
															url: restDownloadurl,
															success: function (data,textStatus, xhr) {
																			JSONToCSVConvertor(data,"_Leads",true);
																			enableButtons();		
															}
													});
													}
													else if(dailyDownloadLimit == downloadreadyToday){
															limitUpdator();
															Alert.render("Sorry! Your Daily Download Limit is Over");
															enableButtons();
													}
													else if((dailyDownloadLimit > downloadreadyToday) && (dailyDownloadLimit < (downloadreadyToday+downloadCount))){
														confirmPopup(restDownloadurl);
														
													}
											}
											else if(contactCreditLimit == downloadreadyTotal + downloadCount){
													limitUpdator();
													Alert.render("Sorry Your Contact Credit Limit is Over");
													enableButtons();
											}
											else  if((contactCreditLimit > downloadreadyTotal) && (contactCreditLimit <= downloadreadyTotal + downloadCount)){
												confirmPopup(restDownloadurl);
											}
									}
							},
							error: function(xhr, textStatus, errorThrow) {
								$("#errMsg").html("<i class='fa fa-check-circle'></i> Data not downloaded please try again after checking your connection.").removeClass('callout-danger').addClass('callout-success').show();
								setTimeout(function() {$('#errMsg').hide();}, 5000);
											enableButtons();
											return false;
									}
						});
		}
}
function confirmOkDownloadAll(restDownloadurl) {	
	$.ajax({
		"type" : 'POST',
		contentType : 'charset=utf-8',
		//contentType : 'application/json; charset=utf-8',
		dataType : 'json',
		url: restDownloadurl,
		success: function (data,textStatus, xhr) {
						JSONToCSVConvertor(data,"_Leads",true);
				}
});
}
function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    var CSV = '';    
    //This condition will generate the Label/Header
    if (ShowLabel) {
        var row = "";
        
        //This loop will extract the label from 1st index of on array
        for (var index in arrData[0]) {
              var data = arrData[0][index];
              //Now convert each value to string and comma-seprated
              row += index + ',';
        }
        row = row.slice(0, -1);
        //append Label row with line break
        CSV += row + '\r\n';
    }
    
    //1st loop is to extract each row
    for (var i = 0; i < arrData.length; i++) {
        var row = "";
        
        //2nd loop will extract each column and convert it in string comma-seprated
        for (var index in arrData[i]) {
            var data = arrData[i][index];
            if( arrData[i][index]==null){
                 row += '"' + '",' 
            }else
                row += '"' + arrData[i][index] + '",';
        }

        row.slice(0, row.length - 1);
        
        //add a line break after each row
        CSV += row + '\r\n';
    }

    if (CSV == '') {        
       Alert.render("Invalid data");
        return;
    }   
    
    //Generate a file name
    var fileName = '<%=clientName.trim()%>';
    //this will remove the blank-spaces from the title and replace it with an underscore
    fileName += ReportTitle.replace(/ /g,"_");   
    
    //Initialize file format you want csv or xls
    var blob = new Blob([CSV], { type: 'text/csv;charset=utf-8;' });
    //this trick will generate a temp <a /> tag
    if (window.navigator.msSaveOrOpenBlob)  // IE hack; see http://msdn.microsoft.com/en-us/library/ie/hh779016.aspx
        window.navigator.msSaveBlob(blob, fileName+ ".csv");
    else
    {
	    var link = document.createElement("a");    
	    var url = URL.createObjectURL(blob);
	    link.setAttribute("href", url);
	    link.setAttribute("download", fileName+ ".csv");
	    link.style.visibility = 'hidden';
	    document.body.appendChild(link);
	    link.click();
	    document.body.removeChild(link);
    }
    enableButtons();
    limitUpdator();
	}
//function to open pop-up window on load of page
function openForm() {
		document.getElementById("myForm").style.display = "block";
}

function closeForm() {
		document.getElementById("myForm").style.display = "none";
} 
 function returnCount(){
var table=$('#example').DataTable();
var rowCount=table.rows().count();
$("#rowcount").val(rowCount);

//alert(rowCount);
} 

</script>
</head>
<!-- body class="welcomeBg" onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload=""-->
<body>
<div id="dialogoverlay"></div>
		<div id="dialogbox">
			<div>
				<div id="dialogboxhead"></div>
				<div id="dialogboxbody"></div>
				<div id="dialogboxfoot"></div>
			</div>
		</div>
<!-- Header Starts -->
<header>
	<div class="container-fluid">
		<nav class="navbar navbar-expand-lg navbar-default fixed-top py-0">
		<!-- Logo and Mobile Toggle Icon Start -->
			<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 navbar-header px-0">
				<button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
					<span class="sr-only">Toggle navigation</span>
					<span class="navbar-toggler-icon"></span>
				</button>
				<a class="navbar-brand nav-link" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<object type="image/svg+xml" data="<%=request.getContextPath()%>/resources/images/dc_logo_header.svg" class="dcHdrLogo">
						LC Report Logo
					</object>
				</a>
			</div>
			<!-- Logo and Mobile Toggle Icon Ends -->
			<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-center pgTitle">LeadCREATR <span id="titleHeading">Report</span></div>
			<!-- Right Nav and other content for toggling -->
			<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 paddLR0">
				<div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
					<div class="logNameIn text-right">
	    				Welcome, <span id="userName"><%=userName%></span>&nbsp; | <a href="logout" class="signOut" title="logout">Log out</a><br><span class="dateTime"><%java.text.DateFormat df = new java.text.SimpleDateFormat("EEEEE, MMMMM dd yyyy | hh:mma z");%><%= df.format(new java.util.Date())%></span>
			    	</div>
				</div>
			</div>
		</nav>
	</div>
</header>
<!-- Header Ends -->

<div class="container-fluid">
	<!-- Content Starts-->
	<div class="row my-3">
		<div class="col-md-12">
			<fieldset class="fieldset shadow-sm">
				<div class="col-md-12">
					<!-- Section Row Starts -->
					<div class="row">
						<!-- Left Section Row Starts -->
						<div class="col-md-2 py-2 my-3 mb-3">
							<div class="col-md-12">
								<!-- Section Box -->
								<div class="left-box text-center">
									<p class="left-box-head">Total Contacts<br>Built To date</p>
									<p id="totalContact" class="left-box-count"></p>
								</div>
								<!-- /Section Box/ -->
								<!-- Section Box -->
								<div class="left-box text-center">
									<p class="left-box-head">Total Contacts<br>Built With Email</p>
									<p id="emailContact" class="left-box-count"></p>
								</div>
								<!-- /Section Box/ -->
								<!-- Section Box -->
								<div class="left-box text-center">
									<p class="left-box-head">Daily Download<br>Limit</p>
									<p id="dailyDownloadLimit" class="left-box-count"></p>
								</div>
								<!-- /Section Box/ -->
								<!-- Section Box -->
								<div class="left-box text-center">
									<p class="left-box-head"><%=clientName%><br>Credit Limit</p>
									<p id="contactCreditLimit" class="left-box-count"></p>
								</div>
								<!-- /Section Box/ -->
								<!-- Section Box -->
								<div class="left-box text-center">
									<p class="left-box-head">Total Downloaded<br>To date</p>
									<p id="getDownloadReady" class="left-box-count"></p>
								</div>
								<!-- /Section Box/ -->
								<!-- Section Box -->
								<div class="left-box text-center">
									<p class="left-box-head">Total Downloaded<br>Today</p>
									<p id="getDownloadReadyToday" class="left-box-count"></p>
								</div>
								<!-- /Section Box/ -->
							</div>
						</div>
						<!-- Left Section Row Ends -->
						<!-- Right Section Row Starts -->
						<div class="col-md-10 py-2 my-3 mb-3">
							<!--- Contact Search Filters Starts --->
							<div class="row mb-4">
								
								<div class="col-md-12 filterable">
									<div class="card border border-secondary">
										<form>
										<div class="card-header bg-secondary text-white">Contact Search Filters</div>
										<div class="card-body">
											<div class="row">
												<div class="col-md-3">
													<div class="form-group">
														<label for="industrySelect" class="col-form-label col-form-label-sm text-left">Industry</label>
														<select id="industrySelect" class="test_select form-control form-control-sm" multiple="">
														
														</select>
													</div>
												</div>
												<div class="col-md-2">
													<div class="form-group">
														<label for="countrySelect" class="col-form-label col-form-label-sm text-left">Country</label>
														<select id="countrySelect" class="form-control form-control-sm" multiple="multiple">
														</select>
													</div>
												</div>
												<div class="col-md-2">
													<div class="form-group">
														<label for="jobFunctionSelect" class="col-form-label col-form-label-sm">Job Function</label>
														<select id="jobFunctionSelect" class="form-control form-control-sm" multiple="multiple" >
														</select>
													</div>
												</div>
												<div class="col-md-2">
													<div class="form-group">
														<label for="seniorityLevelSelect" class="col-form-label col-form-label-sm">Seniority Level</label>
														<select id="seniorityLevelSelect" class="form-control form-control-sm" multiple="multiple">
														</select>
													</div>
												</div>
												<div class="col-md-3">
													<!-- h4 class="paddL15 text-primary margT0 sr-only">Explicit Search</h4-->
													<div class="form-group has-primary">
														<label for="selectedCompanyName" class="col-form-label col-form-label-sm text-color"><strong>Explicit Search</strong></label>
														<input type="text" id="selectedCompanyName" name="selectedCompanyName" class="form-control form-control-sm text-black" placeholder="Enter Company Name">
													</div>
												</div>
												
											</div>
											<div class="row">
												<div class="col-md-3">
													<div class="form-group">
														<label for="dateRangeSelect" class="col-form-label col-form-label-sm">Date Range</label>
														
														
														<div id="dateRangeSelect" class="form-control form-control-sm">
															<i class="fa fa-calendar" aria-hidden="true"></i>&nbsp;<span></span>&nbsp; <i class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></i>
														</div>
													</div>
												</div>
												
												
												<div class="col-md-7 px-0 d-flex justify-content-around">
												<div class="form-check form-check-inline">
															<input type="checkbox" class="form-check-input" id="ignoreDate" name="ignoreDate" checked>
																<label for="ignoreDate" class="form-check-label col-form-label-sm">Ignore Date</label>
															</div>
													<div class="form-check form-check-inline">
														<input type="checkbox" class="form-check-input" id="Records_With_Email" onclick="corp_emailCheckbbox()" name="Records_With_Email">
														<label for="Records_With_Email" class="form-check-label col-form-label-sm">Corp Email</label>
													</div>
													<div class="form-check form-check-inline">
														<input type="checkbox" class="form-check-input" id="Records_With_Company_id" onclick="company_idCheckbbox()" name="Records_With_Company_id">
														<label for="Records_With_Company_id" class="form-check-label col-form-label-sm">Company Id</label>
													</div>
													<div class="form-check form-check-inline">
														<input type="checkbox" class="form-check-input" id="notDownloaded" onclick="not_DownloadedCheckbbox()" name="notDownloaded">
														<label for="notDownloaded" class="form-check-label col-form-label-sm">Exclude Downloaded</label>
													</div>
												</div>
												
											</div>
										</div>
										<div class="card-footer text-muted text-left">
											<button type="button"id="searchButton" onclick="reloaddatatable()" 
											class="btn btn-primary btn-sm"><i class="fa fa-search" aria-hidden="true">
											</i> Search</button>
											<button type="reset" id="example-reset-text" class="btn btn-primary btn-sm"><i class="fa fa-refresh" aria-hidden="true"></i> Reset</button>
										</div>
										</form>
									</div>
								</div>
							</div>
							<!--- Contact Search Filters Ends --->
							
							<!--- Download Section Starts --->
							<div class="row mb-4">
								<div class="col-md-12">
									<div class="card border border-secondary">
										<div class="card-header bg-secondary text-white">Results</div>
										<div class="card-body">
							<div class="row">
							<div class="col-md-6 col-sm-12 d-flex">
							<div class="col-md-3 col-sm-12 d-flex">
							<p  id="downloadable"class="record-details text-left form-control-sm"></p> </div>
							<div class="col-md-2 col-sm-12 d-flex">
							<p class=" text-left bot-mar">Records</p></div>
							
							
							<div class="col-md-3 col-sm-12 d-flex">
							<p id="downloadable_with_email"class="record-details text-left form-control-sm"></p> </div>
							<div class="col-md-4 col-sm-12 d-flex">
							<p class=" text-left bot-mar">Records with Email</p></div>
							
							</div>
								<div class="col-md-6 col-sm-12 text-right text-muted">
								<button type="button" id="downloadSelected"onclick="downloadSelected()" title="Download Selected" class="btn btn-primary btn-sm">Download Selected</button>
								<button type="button"id="downloadAll" onclick="downloadAll(restDownloadurl,'N',restCounturl)" title="Download All" class="btn btn-primary btn-sm">Download All</button>
									
								<button type="button" id="downloadWithEmail" onclick="downloadAll(restDownloadurl,'Y',restCounturl)" title="Download All with Emails" class="btn btn-primary btn-sm">Download All with Emails</button>
								</div>
								
					<div id="errMsg" class="col-md-12 error_msg text-center"></div>
				
								
							</div>
							</div></div></div></div>
							<!--- Download Section Ends --->
							<!--- Contact Search View Table Starts --->
							<div class="row mb-4">
								<div class="col-md-12 filterable">
									<div class="card border border-secondary">
										<div class="card-header bg-secondary text-white">Contact Search View</div>
										<div class="card-body">
											<div class="row">
												<div class="col-md-12">
													<table id="example" class="display nowrap darkTable table table-striped table-bordered table-sm">
														
													</table>
												<div class="" id="shoe_row">1 row selected</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<!--- Contact Search View Table Ends --->
							
						</div>
						<!-- Right Section Row Ends -->
					</div>
					<!-- Section Row Ends -->
				</div>
			</fieldset>
		</div>
	</div>
	<!-- Content Ends-->
	<!-- Footer Starts-->
	<footer class="navbar">
		<div class="row">
			<div class="col-md-12">
				 <nav class="navbar fixed-bottom navbar-light">&copy; Selling Simplified Group, Inc. 2019  |  All Rights Reserved  |  <a href="#" title="Privacy Policy">Privacy Policy</a>  |  <a href="https://www.sellingsimplified.com" title="sellingsimplified.com">sellingsimplified.com</a></nav>
			</div>
		</div>
	</footer>
	<!-- Footer Ends-->
</div>

<!-- ******************************Modals and Popups Start******************************************* -->
<div id="selectClientModal" class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="selectClientModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header head-primary">
				<h5 class="modal-title" id="selectClientModalLabel">Select Client</h5>
			</div>
			<div class="modal-body">
				<form class="form-container" id="myForm" method="POST">
					<div class="form-group row">
						<label for="clientSelect" class="col-md-4 col-form-label">Select Client </label>
						<div class="col-sm-8">
							<select id="clientSelect" name="clientName" class="form-control form-control-sm">
							</select>
						</div>
					</div>
				</form>
     		 </div>
			<div class="modal-footer">
				<button type="button" onclick='myFunction()' class="btn btn-primary btn-sm submit">submit</button>
			</div>
		</div>
	</div>
</div>
<!--*************************************** Modals and Popups Ends *************************************-->

</body>

</html>
