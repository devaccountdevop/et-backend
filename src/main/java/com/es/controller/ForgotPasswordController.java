
package com.es.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.es.entity.EmailDetails;
import com.es.entity.ForgotPassword;
import com.es.service.EmailDetailsService;
import com.es.service.ForgotPasswordService;

@RestController 
  public class ForgotPasswordController {
  
  @Autowired 
  private ForgotPasswordService forgotPasswordService;
  
  @Autowired
  EmailDetailsService detailsService;
  
  @PostMapping("/forgot-password") 
  public String  generatePasswordResetToken(@RequestBody EmailDetails emailDetails)
  {
	  detailsService.sendSimpleMail( emailDetails  );
	  
      return "Email sent successfully!";
  }
  
   }
   
   
	/*
	 * { Optional<ForgotPassword> userOptional =
	 * forgotPasswordService.findByEmail(email);
	 * 
	 * if (userOptional.isPresent()) { ForgotPassword forgotPassword =
	 * userOptional.get();
	 * forgotPassword.generatePasswordResetToken(forgotPassword); return
	 * "Password reset token generated successfully."; } else { return
	 * "No user found with the provided email address."; } }
	 * 
	 * 
	 * @PostMapping("/reset-password") public String resetPassword(@RequestParam
	 * String resetToken, @RequestParam String newPassword) { if
	 * (forgotPasswordService.isValidResetToken(resetToken)) {
	 * Optional<ForgotPassword> userOptional =
	 * forgotPasswordService.findByResetToken(resetToken);
	 * 
	 * if (Optional.isPresent()) { ForgotPassword forgotPassword =
	 * userOptional.get(); ForgotPasswordService.resetPassword(user, newPassword);
	 * return "Password reset successfully."; } else { return
	 * "User not found for the provided reset token."; } } else { return
	 * "Invalid reset token."; } } }
	 */
