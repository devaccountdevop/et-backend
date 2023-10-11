package com.es.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.SignupDto;
import com.es.entity.Signup;
import com.es.response.ExceptionEnum;
import com.es.response.SignupResponse;
import com.es.response.SuccessEnum;
import com.es.service.SignupService;

@RestController
@RequestMapping("/signup")
public class SignupController {
	
	
	@Autowired
	SignupService signupService;
	
 @PostMapping("")
 	public SignupResponse saveUser(HttpServletRequest request, Model model) {
	    SignupResponse response = new SignupResponse();

	    
	    String email = request.getParameter("email"); 
	    String userName = request.getParameter("userName");
	    String password = request.getParameter("password"); 

//	    SignupDto signupDto = new SignupDto();
//	    signupDto.setEmailId(email);
//	    signupDto.setPassword(password);
//	    signupDto.setUserName(userName);
 if(!email.isEmpty()&& !userName.isEmpty()&&!password.isEmpty()) {
	 
	 Signup saveUser = new Signup();
	 
	 saveUser.setEmail(email);
	 saveUser.setUserName(userName);
	 saveUser.setPassword(password);
	 this.signupService.saveUser(saveUser);
	 SignupDto userDto = new SignupDto();
	 userDto.setEmailId(email);
	 userDto.setPassword(password);
	 userDto.setUserName(userName);
	 response.setData(userDto);
	 response.setCode(SuccessEnum.SUCCESS_REGISTER.getCode());
	 response.setMessage(SuccessEnum.SUCCESS_REGISTER.getMessage());
	 return response;
	 }else {
		 
		 response.setCode(ExceptionEnum.INVALID_USER.getErrorCode());
		 response.setMessage(ExceptionEnum.INVALID_USER.getMessage());
		 return response;
	 }
 }
	  
}
