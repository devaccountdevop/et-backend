package com.es.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.es.entity.ClientCredentials;

public interface ClientCredentialsService {

	ClientCredentials getClientCredentials(int id);

	ClientCredentials saveClientCredentials(ClientCredentials clientCredentials);

	ClientCredentials updateClientCredentials(ClientCredentials clientCredentials);

	void deleteClientCredentials(int userId);

	List<ClientCredentials> getUsersByUserId(int userId);

	public ClientCredentials updateClientCredentialsByUserId(ClientCredentials clientCredentials);

}
