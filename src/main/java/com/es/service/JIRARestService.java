package com.es.service;

import com.es.dto.EmployeeMasterDto;
import com.es.dto.ProjectInfoDto;
import com.es.entity.EstimationMaster;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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


	public List<ProjectInfoDto> getAllProjects() {
	    RestTemplate restTemplate = new RestTemplate();

 

	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    headers.setBasicAuth(jira_username, jira_token);
	    HttpEntity<String> entity = new HttpEntity<>(headers);

 

	    ResponseEntity<String> responseEntity = restTemplate.exchange(jira_base_url + jira_get_project, HttpMethod.GET, entity, String.class);
	    String jsonResponse = responseEntity.getBody();
	    JsonParser jsonParser = new JsonParser();
	    JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

 

	    JsonArray values = jsonObject.getAsJsonArray("values");

 

	    List<ProjectInfoDto> projectInfoList = new ArrayList<>();

 

	    for (int i = 0; i < values.size(); i++) {
	        JsonObject projectObject = values.get(i).getAsJsonObject();
	        int projectId = projectObject.get("id").getAsInt();
	        JsonObject location = projectObject.getAsJsonObject("location");
	        String projectName = location.get("projectName").getAsString();

 

	        ProjectInfoDto projectInfo = new ProjectInfoDto(projectId, projectName);
	        projectInfoList.add(projectInfo);
	    }

 

	    return projectInfoList;
	}
	
 	
// 	public String getAllProjectsInJSON(){
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//		headers.setBasicAuth(jira_username, jira_token);
//		HttpEntity<String> entity = new HttpEntity<>(headers);
//		ResponseEntity<String> response = restTemplate.exchange(jira_base_url + jira_get_project, HttpMethod.GET, entity, String.class);
//		//Gson gson = new Gson();
//		//String abc = gson.toJson(str); //your list of Master_City
//		//System.out.println("abc::: " + abc);
//		return response.getBody();
//	}



	
}
