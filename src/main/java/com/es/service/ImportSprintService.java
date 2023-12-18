package com.es.service;

import java.io.InputStream;
import java.util.List;

import com.es.entity.ImportProjects;
import com.es.entity.ImportSprint;

public interface ImportSprintService {
	
List<ImportSprint> getSprintDataAsList(InputStream inputStream);
	
	int saveSprintData(List<ImportSprint> importSprint);
    
	ImportSprint getProjects(int id);
	ImportSprint saveProjects(ImportSprint importSprint);
	ImportSprint updateClientCredentials(ImportSprint importSprint);
	List<ImportSprint> getAllSprintByProjectId(int projectId);
}
