package com.es.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
 
import com.es.entity.EmailDetails;
import com.es.service.EmailDetailsService;
 
@RestController
public class EmailDetailsController {
 
	    @Autowired private EmailDetailsService emailDetailsService;
	
	    // Sending a simple Email
	    @GetMapping("/sendMail")
	    public String
	    sendMail(@RequestBody EmailDetails details)
	    {
	        String status
	            = emailDetailsService.sendSimpleMail(details);
	
	        return status;
	    }}