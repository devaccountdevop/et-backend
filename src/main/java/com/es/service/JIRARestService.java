package com.es.service;

import com.es.dto.EmployeeMasterDto;
import com.es.entity.EstimationMaster;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;


@Service
public class JIRARestService {

	@Value("${jira.username}")
	private String jira_username;

	@Value("${jira.token}")
	private String 	jira_token;

	@Value("${jira.base.url}")
	private String 	jira_base_url;

	@Value("${jira.get.project.endpoint}")
	private String jira_get_project;


 	public String getAllProjects(){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBasicAuth(jira_username, jira_token);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		String str = restTemplate.exchange(jira_base_url+jira_get_project, HttpMethod.GET, entity, String.class).getBody();
		Gson gson = new Gson();
		String abc = gson.toJson(str); //your list of Master_City
		//System.out.println("abc::: " + abc);
		return abc;
	}



	
}
