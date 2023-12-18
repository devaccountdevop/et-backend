package com.es.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;
import com.es.service.ClientCredentialsService;
import com.es.service.ImportClientService;
import com.es.service.ImportProjectsService;
import com.es.service.ImportSprintService;
import com.es.service.ImportTaskService;

@RestController

public class ImportExcelFileController {

	@Autowired
	ImportClientService excelservice;

	@Autowired
	ImportProjectsService importProjectsService;
	
	@Autowired
	ImportSprintService importSprintService;
	
	@Autowired
	ImportTaskService importTaskService;

	@Value("${file.upload-dir}")
	String FILE_DIRECTORY;

	@Autowired
	ClientCredentialsService clientCredentialsService;

//	@PostMapping(value = "/uploadFile/{Id}", consumes = "multipart/form-data")
//	public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file, @PathVariable String Id) {
//		try {
//		    List<ClientCredentials> clientList = excelservice.getExcelDataAsList(file.getInputStream(),
//		            Integer.parseInt(Id));
//
//		    if (!clientList.isEmpty()) {
//		        int noOfClientRecords = excelservice.saveClientData(clientList);
//
//		        // Check if client records were successfully saved
//		        if (noOfClientRecords > 0) {
//		            List<ImportProjects> projectList = importProjectsService
//		                    .getProjectDataAsList(file.getInputStream());
//
//		            List<ImportSprint> sprintList = importSprintService.getSprintDataAsList(file.getInputStream());
//
//		            // Parse Task List Data
//		            List<ImportTask> taskList = importTaskService.getTaskDataAsList(file.getInputStream());
//		            int noOfTaskRecords = importTaskService.saveTaskData(taskList);
//
//		            // Check if task records were successfully saved
//		            if (noOfTaskRecords > 0) {
//		                // Continue with project and sprint logic...
//
//		                if (!projectList.isEmpty()) {
//		                    int noOfProjectRecords = importProjectsService.saveProjectData(projectList);
//
//		                    int noOfSprintRecords = importSprintService.saveSprintData(sprintList);
//
//		                    // Check if project records were successfully saved
//		                    if (noOfProjectRecords > 0) {
//		                        return new ResponseEntity<>(
//		                                "File uploaded successfully. Number of client records saved: " + noOfClientRecords
//		                                        + ". Number of project records saved: " + noOfProjectRecords
//		                                        + ". Number of sprint records saved: " + noOfSprintRecords
//		                                        + ". Number of task records saved: " + noOfTaskRecords,
//		                                HttpStatus.OK);
//		                    } else {
//		                        return new ResponseEntity<>("Error saving project records.",
//		                                HttpStatus.INTERNAL_SERVER_ERROR);
//		                    }
//		                } else {
//		                    return new ResponseEntity<>("No project data to save.", HttpStatus.BAD_REQUEST);
//		                }
//		            } else {
//		                return new ResponseEntity<>("Error saving task records.", HttpStatus.INTERNAL_SERVER_ERROR);
//		            }
//		        } else {
//		            return new ResponseEntity<>("Error saving client records.", HttpStatus.INTERNAL_SERVER_ERROR);
//		        }
//		    } else {
//		        return new ResponseEntity<>("No client data to save.", HttpStatus.BAD_REQUEST);
//		    }
//		} catch (IOException e) {
//		    e.printStackTrace();
//		    return new ResponseEntity<>("Error processing the file.", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	@PostMapping(value = "/uploadFile/{Id}", consumes = "multipart/form-data")
	@Transactional
	public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file, @PathVariable String Id) {
	    // Check if the file is an Excel file
	    if (!isExcelFile(file)) {
	        return new ResponseEntity<>("Invalid file format. Only .xlsx files are allowed.", HttpStatus.BAD_REQUEST);
	    }

	    try {
	        List<ClientCredentials> clientList = excelservice.getClientDataAsList(file.getInputStream(), Integer.parseInt(Id));

	        if (clientList.isEmpty()) {
	            return new ResponseEntity<>("No client data to save.", HttpStatus.BAD_REQUEST);
	        }

	        int noOfClientRecords = excelservice.saveClientData(clientList);

	        if (noOfClientRecords == 0) {
	            return new ResponseEntity<>("Error saving client records.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        List<ImportProjects> projectList = importProjectsService.getProjectDataAsList(file.getInputStream());
	        List<ImportSprint> sprintList = importSprintService.getSprintDataAsList(file.getInputStream());
	        List<ImportTask> taskList = importTaskService.getTaskDataAsList(file.getInputStream());

	        int noOfTaskRecords = importTaskService.saveTaskData(taskList);

	        if (noOfTaskRecords == 0) {
	            return new ResponseEntity<>("Error saving task records.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        if (projectList.isEmpty()) {
	            return new ResponseEntity<>("No project data to save.", HttpStatus.BAD_REQUEST);
	        }

	        int noOfProjectRecords = importProjectsService.saveProjectData(projectList);
	        int noOfSprintRecords = importSprintService.saveSprintData(sprintList);

	        if (noOfProjectRecords == 0) {
	            return new ResponseEntity<>("Error saving project records.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        return new ResponseEntity<>("File uploaded successfully. Number of client records saved: " + noOfClientRecords +
	                ". Number of project records saved: " + noOfProjectRecords +
	                ". Number of sprint records saved: " + noOfSprintRecords +
	                ". Number of task records saved: " + noOfTaskRecords, HttpStatus.OK);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Error processing the file.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	private boolean isExcelFile(MultipartFile file) {
	    
	    String originalFilename = file.getOriginalFilename();

	    return originalFilename != null && originalFilename.toLowerCase().endsWith(".xlsx");
	}


}
