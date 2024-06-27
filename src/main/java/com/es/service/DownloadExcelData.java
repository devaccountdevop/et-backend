package com.es.service;

import java.io.IOException;

public interface DownloadExcelData {
	byte[] generateProjectData (int projectId, int clientId) throws IOException;
	byte[] generateSprintData (int projectId, int clientId, int sprintId) throws IOException;
	byte[] generateBacklogData (int projectId, int clientId) throws IOException;
	
	
	

}
