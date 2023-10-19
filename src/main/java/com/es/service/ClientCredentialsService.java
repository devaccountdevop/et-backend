package com.es.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.es.entity.ClientCredentials;


public interface ClientCredentialsService {
	
	ClientCredentials getClientCredentials(int id);
	ClientCredentials saveClientCredentials(ClientCredentials clientCredentials);
	ClientCredentials updateClientCredentials(ClientCredentials clientCredentials);
	//ClientCredentials getClientCredentialsByUserId(int id);
	void deleteClientCredentials(int userId);
	List<ClientCredentials> getUsersByUserId(int userId);
	public ClientCredentials updateClientCredentialsByUserId(ClientCredentials clientCredentials);
	
	
//	ClientCredentials getUserById(int id);
//	ClientCredentials getUserByClientName(String clientName);
//	ClientCredentials getUserByJiraUserName(String jiraUserName);
//	ClientCredentials saveUser(ClientCredentials clientCredentials);
//	ClientCredentials getUserByToken(String token);
//	List<ClientCredentials> Userlist(int id);
//	void deleteUser(int id);

}
