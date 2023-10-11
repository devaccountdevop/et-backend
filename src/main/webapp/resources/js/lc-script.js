function enableButtons(){
	document.getElementById('searchButton').disabled = false;
	document.getElementById('downloadSelected').disabled = false;
	document.getElementById('downloadAll').disabled = false;
	document.getElementById('downloadWithEmail').disabled = false;
}

function disableButtons(){
	document.getElementById('searchButton').disabled = true;
	document.getElementById('downloadSelected').disabled = true;
	document.getElementById('downloadAll').disabled = true;
	document.getElementById('downloadWithEmail').disabled = true;
}
/*Custom alert MEthod
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

Count of downloadable data to show on UI
function popupCount(restCounturl){
	var downloadCount;
	var remainingCount;
	var downloadCountEmail;
	$.ajax({
		"type" : 'POST',
		url: restCounturl,
		success: function (data,textStatus, xhr) {
					var countMap=jQuery.parseJSON(data); 
					downloadCount=countMap.data.downloadCount;
					remainingCount=countMap.data.remainingLimit;
					downloadCountEmail=countMap.data.downloadCountEmail;
					var popupLimit;
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
					 else{
							alert("YOUR LIMIT HAVE BEEN EXCEEDED.\nRENEW YOUR PACKAGE TO DOWNLOAD MORE");
					} 
			},
			error: function(xhr, textStatus, errorThrow){
				$("#errMsg").html("<i class='fa fa-check-circle'></i> Error occured please try again after checking your connection.").removeClass('callout-danger').addClass('callout-success').show();
				setTimeout(function() {$('#errMsg').hide();}, 5000);
				return false;
			} 
		});
}*/
/*//for downloading all selected data 
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
										contentType : 'application/json; charset=utf-8',
										dataType : 'json',
										data :{contactIds},
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
								 confirm     area 
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
						success: function (data,textStatus, xhr) {
									var countMap=jQuery.parseJSON(data);
									var downloadCount=countMap.data.downloadCount;
									var remainingCount=countMap.data.remainingLimit;
									enableButtons();
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
															contentType : 'application/json; charset=utf-8',
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
		contentType : 'application/json; charset=utf-8',
		dataType : 'json',
		url: restDownloadurl,
		success: function (data,textStatus, xhr) {
						JSONToCSVConvertor(data,"_Leads",true);
				}
});
}*/