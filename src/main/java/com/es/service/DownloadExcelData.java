package com.es.service;

import java.io.IOException;

public interface DownloadExcelData {
	byte[] generateProjectData (int projectId, int clientId, int userId) throws IOException;
	byte[] generateSprintData (int projectId, int clientId, int sprintId, int userId) throws IOException;
	byte[] generateBacklogData (int projectId, int clientId, int userId) throws IOException;
	
	
	

}
