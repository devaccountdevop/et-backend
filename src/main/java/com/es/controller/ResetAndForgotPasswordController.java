package com.es.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.entity.Signup;
import com.es.response.EmailResponse;
import com.es.response.ExceptionEnum;
import com.es.response.ResetPasswordResponse;
import com.es.response.SuccessEnum;
import com.es.service.SignupService;
import com.es.service.impl.ForgotPasswordService;
import com.es.service.impl.ResetPasswordService;

@RestController
@RequestMapping("/estimation-tool")
public class ResetAndForgotPasswordController {

	@Autowired
	ResetPasswordService resetPasswordService;

	@Autowired
	private ForgotPasswordService emailService;
	
	@Autowired
	SignupService signupService;


	@PostMapping("/forgotpassword")
	public EmailResponse sendEmail(HttpServletRequest request, Model model) throws MessagingException {

		EmailResponse response = new EmailResponse();

		String email = request.getParameter("email");
		//String username = request.getParameter("username");
		Signup userDetails = signupService.getUserByEmail(email);
		
		if(userDetails != null) {
			String message = emailService.sendEmail(userDetails);
			if (message != null) {
				response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
				response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
				response.setData(message);
				return response;
			}
			else {
				response.setCode(ExceptionEnum.INVALID_USER.getErrorCode());
				response.setMessage(ExceptionEnum.INVALID_USER.getMessage());
				return response;
			}
		} else {
			response.setCode(ExceptionEnum.INVALID_USER.getErrorCode());
			response.setMessage(ExceptionEnum.INVALID_USER.getMessage());
			return response;
		}
	}

	@PostMapping("/resetpassword")
	public ResetPasswordResponse resetPassword(HttpServletRequest request, Model model) {

		ResetPasswordResponse response = new ResetPasswordResponse();

		String newPassword = request.getParameter("newPassword");
		String userName = request.getParameter("userName");
		String oldPassword = request.getParameter("oldPassword");
		Signup signup = new Signup();
		signup = resetPasswordService.resetPassword(userName, oldPassword, newPassword);
		if (signup != null) {
			response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
			response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
			response.setData(signup);
			return response;
		} else {
			response.setCode(ExceptionEnum.INVALID_USER.getErrorCode());
			response.setMessage(ExceptionEnum.INVALID_USER.getMessage());
			return response;
		}
	}
}
