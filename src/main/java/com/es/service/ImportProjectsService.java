package com.es.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;

public interface ImportProjectsService {
	
	List<ImportProjects> getProjectDataAsList(InputStream inputStream);
	
	List<ImportProjects> saveProjectData(List<ImportProjects> projects);
    
	ImportProjects getProjects(int id);
	ImportProjects saveProjects(ImportProjects importProjects);
	ImportProjects updateClientCredentials(ImportProjects importProjects);
	//ClientCredentials getClientCredentialsByUserId(int id);
//	void deleteProjects(int userId);
	List<ImportProjects> getProjectsByJiraUserName(String jiraUserName);
//	public ClientCredentials updateClientCredentialsByUserId(ClientCredentials clientCredentials);
	List<ImportProjects> saveProjectList(List<ImportProjects> importProjects);

	List<String> modifiedProjectsName(List<String> projectList);
		
}
