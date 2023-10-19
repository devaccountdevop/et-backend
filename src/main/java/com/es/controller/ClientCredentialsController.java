package com.es.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.entity.ClientCredentials;
import com.es.response.ClientCredentialsResponse;
import com.es.response.ExceptionEnum;
import com.es.response.SuccessEnum;
import com.es.service.ClientCredentialsService;
import com.es.service.SignupService;


@RestController
@RequestMapping("/")
public class ClientCredentialsController {
	
	@Autowired
	ClientCredentialsService clientCredentialsService;
	@Autowired
	SignupService signupService;
	
	@GetMapping("/getclient/{Id}")
	public ClientCredentialsResponse getClientCredential(@PathVariable String Id) {
		
		ClientCredentialsResponse response = new ClientCredentialsResponse();
		
	    ClientCredentials clientCred = new ClientCredentials();
	    
	    clientCred = clientCredentialsService.getClientCredentials(Integer.parseInt(Id));
	    if(clientCred.getId() != 0) {
	    	response.setData(clientCred);
	   	 response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
	   	 response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
	   	 return response;
	   	 }else {
	   		 
	   		 response.setCode(ExceptionEnum.INVALID_AUTH_USER.getErrorCode());
	   		 response.setMessage(ExceptionEnum.INVALID_AUTH_USER.getMessage());
	   		 return response;
	   	 }
		
	}
	
	@GetMapping("/getclients/{userId}")
	public ClientCredentialsResponse getClientCredentialByUserId(@PathVariable String userId) {
		
		ClientCredentialsResponse response = new ClientCredentialsResponse();
		
	    List<ClientCredentials> clientCred = new ArrayList<>();
	    
	    clientCred.addAll(clientCredentialsService.getUsersByUserId(Integer.parseInt(userId)));
	    if(clientCred != null) {
	    	response.setData(clientCred);
	   	 response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
	   	 response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
	   	 return response;
	   	 }else {
	   		 
	   		 response.setCode(ExceptionEnum.INVALID_AUTH_USER.getErrorCode());
	   		 response.setMessage(ExceptionEnum.INVALID_AUTH_USER.getMessage());
	   		 return response;
	   	 }
		
	}
	
	@PostMapping("/saveclient")
	public ClientCredentialsResponse saveClientCredential(HttpServletRequest request, Model model) {
		
		ClientCredentialsResponse response = new ClientCredentialsResponse();
		String userId = request.getParameter("userId");
	    String userName = request.getParameter("userName");
	    String token = request.getParameter("token"); 
	    String clientName = request.getParameter("clientName"); 
	    
	    ClientCredentials clientCred = new ClientCredentials();
	    clientCred.setClientName(clientName);
	    clientCred.setUserId(Integer.parseInt(userId));
	    clientCred.setJiraUserName(userName);
	    clientCred.setToken(token);
	    clientCred = clientCredentialsService.saveClientCredentials(clientCred);
	    if(clientCred != null) {
	    	response.setData(clientCred);
	   	    response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
	   	    response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
	   	 return response;
	   	 }else {
	   		 
	   		 response.setCode(ExceptionEnum.INVALID_AUTH_USER.getErrorCode());
	   		 response.setMessage(ExceptionEnum.INVALID_AUTH_USER.getMessage());
	   		 return response;
	   	 }
		
	}
	
	

	@PutMapping("/updateclient/{Id}")
	public ClientCredentialsResponse updateClientCredentialsById(@PathVariable String Id, HttpServletRequest request, Model model) {
		
		ClientCredentialsResponse response = new ClientCredentialsResponse();
		String userId = request.getParameter("userId");
	    String userName = request.getParameter("userName");
	    String token = request.getParameter("token"); 
	    String clientName = request.getParameter("clientName"); 
	    
	    ClientCredentials clientCred = new ClientCredentials();
	    clientCred =  clientCredentialsService.getClientCredentials(Integer.parseInt(Id));
	    if(clientCred.getId() != 0) {
	    	clientCred.setId(Integer.parseInt(Id));
	    	clientCred.setClientName(clientName);
	    	clientCred.setJiraUserName(userName);
	    	clientCred.setToken(token);
	    	clientCred.setUserId(Integer.parseInt(userId));
	    	clientCredentialsService.saveClientCredentials(clientCred);
	    	response.setData(clientCred);
	   	    response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
	   	    response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
	   	 return response;
	    }else {
	   		 
	   		 response.setCode(ExceptionEnum.INVALID_AUTH_USER.getErrorCode());
	   		 response.setMessage(ExceptionEnum.INVALID_AUTH_USER.getMessage());
	   		 return response;
	   	 }
		
	}
	
	@DeleteMapping("/deleteclient/{Id}")
	public ClientCredentialsResponse deleteClientCredential(@PathVariable String Id) {
		
		ClientCredentialsResponse response = new ClientCredentialsResponse();
		ClientCredentials deleteCredentials = new ClientCredentials();
		deleteCredentials  = clientCredentialsService.getClientCredentials(Integer.parseInt(Id));
		if(deleteCredentials.getId() !=0) {
			clientCredentialsService.deleteClientCredentials(deleteCredentials.getId());
			response.setCode(200);
			response.setMessage("Credentials has been deleted ");
			response.setData(deleteCredentials);
			return response;
		}else {
			
			response.setCode(404);
			response.setMessage("Credentials not found ");
			response.setError(true);
			return response;
			
		}
		
	}
	
	

}
