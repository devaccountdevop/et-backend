package com.es.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.entity.ClientCredentials;
import com.es.repository.ClientCredentialsRepository;
import com.es.service.ClientCredentialsService;


@Service
public class ClientCredentialsServiceImpl implements ClientCredentialsService{

	@Autowired 
	ClientCredentialsRepository clientCredentialsRepository;
	
	@Override
	public ClientCredentials getClientCredentials(int id) {
		
		Optional<ClientCredentials> list = clientCredentialsRepository.findById(id);
		return ! list.isPresent() ? null: list.get();
	}

	@Override
	public ClientCredentials saveClientCredentials(ClientCredentials clientCredentials) {
		
		return clientCredentialsRepository.save(clientCredentials);
	}

	@Override
	public ClientCredentials updateClientCredentials(ClientCredentials clientCredentials) {
		
		return clientCredentialsRepository.save(clientCredentials);
	}

	@Override
	public void deleteClientCredentials(int id) {
		
		clientCredentialsRepository.deleteById(id);
		
	}
	

	@Override
	public List<ClientCredentials> getUsersByUserId(int userId) {
		
		return clientCredentialsRepository.findByUserId(userId);
	}
	

	
}