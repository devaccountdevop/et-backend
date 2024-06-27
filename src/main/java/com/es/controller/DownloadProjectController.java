package com.es.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.service.DownloadExcelData;
import com.es.service.JIRARestService;
 
@RestController
@RequestMapping("/estimation-tool")

public class DownloadProjectController {
	
	@Autowired
	JIRARestService jiraRestService;
	  @Autowired
	    private DownloadExcelData downloadExcelData;
 
	    @PostMapping("/download")
	    public ResponseEntity<byte[]> downloadExcel(HttpServletRequest request, Model model) throws IOException {
	    	int projectId = Integer.parseInt(request.getParameter("projectId"));
	    	int userId = Integer.parseInt(request.getParameter("userId"));
	    	int clientId = Integer.parseInt(request.getParameter("clientId"));
	    	String sprintId = request.getParameter("sprintId");
	    	byte[] excelData = new byte[0];

	    	if (projectId !=0 && sprintId.isEmpty()) { 
	    		
	    		 excelData = downloadExcelData.generateProjectData(projectId, clientId);
	    	}else {
	    		if(projectId !=0 && sprintId.equals("0")){
	    			excelData = downloadExcelData.generateBacklogData(projectId, clientId);
	    		}else {
	    			excelData = downloadExcelData.generateSprintData(projectId, clientId, Integer.parseInt(sprintId));
	    		}
	    		
	    	}
	    	
	    		
	    	
	  
 
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDispositionFormData("attachment", "data.xlsx");
 
	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(excelData);
	    }
}
