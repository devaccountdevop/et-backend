package com.es.service.impl;
 
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.es.entity.EmailDetails;
import com.es.service.EmailDetailsService;
 
 
 
@Service
 
	public class EmailDetailsServiceImpl implements EmailDetailsService {
		
	   
	   private final JavaMailSender mailSender;
	 public EmailDetailsServiceImpl(JavaMailSender mailSender) {
		 this.mailSender = mailSender;
	}
	    
	
		@Override
		public String sendSimpleMail(EmailDetails details) {
		    try {
		   	
		 
	            // Creating a simple mail message
	          SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
	            // Setting up necessary details
	          simpleMailMessage.setFrom("ayush.tyagiims@gmail.com");
	          simpleMailMessage.setTo("ayush.tyagiims@gmail.com");
	          simpleMailMessage.setText("Please add html link");
	          simpleMailMessage.setSubject("test");
	          
	          
	            
	            this.mailSender.send(simpleMailMessage);
	            return "Password reset instruction have been sent to the e-mail address provided...";
	        }
	
	        // Catch block to handle the exceptions
	        catch (Exception e) {
	            e.printStackTrace();
	        	return "Error while Sending Mail"+ e.getMessage();
	            
	        }}}


 


		