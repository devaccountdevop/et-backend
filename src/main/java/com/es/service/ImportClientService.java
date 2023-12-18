package com.es.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.es.entity.ClientCredentials;



public interface ImportClientService {

	List<ClientCredentials> getClientDataAsList(InputStream inputStream, int UserId);
	
	int saveClientData(List<ClientCredentials> client);
}
