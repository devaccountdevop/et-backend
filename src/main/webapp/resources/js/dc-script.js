// JavaScript Document
$(document).ready(function(){
    "use strict";
    // Logo Animation
    /*	$('.objectC').delay(1000).queue(function(){
		$(this).addClass("move-right");
	});
	$('.objectD').delay(1000).queue(function(){
		$(this).addClass("move-left");
	});
*/	
	$(document).keypress(function(event){
		var keycode = event.keyCode;
		if(keycode == '13'){
			$("#sbmt_btn").click();
		}
	});
	
	$.get("https://ipinfo.io", function(response) {
		localStorage.setItem("CallerCountry", response.country);
	}, "jsonp");
	
	$("#login_form").validate({
		rules: {
			j_username: {required:true, email:true},
			j_password :{required:true},
			//employeetotal :{required:false, number:true}
		},
		messages: {
			j_username: {required:"Please enter your email", email:"Please enter a valid email"},
			j_password: {required:"Please enter password"}
		},
/*		tooltip_options: {
			j_username: {placement:'bottom'},
			j_password: {placement:'bottom'}
		} */
    });
	 /* $("#trialform").validate({
        rules: {
            companyname: "required",
            email: {required: true, email: true},
        },
        messages: {
            companyname: "Please enter your Company Name",
            email: "Please enter a valid Email Address"
        },
        tooltip_options: {
        	companyname: {placement:'top'},
        	email: {placement:'top'}
		}
    }); */
    
    $("#sbmt_btn").click(function(){
		//validateLogin();
		$("#login_form").valid();
	});
	
});