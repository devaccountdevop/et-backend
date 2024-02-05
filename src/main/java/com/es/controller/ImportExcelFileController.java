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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;
import com.es.response.ExceptionEnum;
import com.es.response.ImportExcelFileResponse;
import com.es.response.SuccessEnum;
import com.es.service.ClientCredentialsService;
import com.es.service.ImportClientService;
import com.es.service.ImportProjectsService;
import com.es.service.ImportSprintService;
import com.es.service.ImportTaskService;

@RestController
@RequestMapping("/estimation-tool")
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

	@PostMapping(value = "/uploadFile/{Id}", consumes = "multipart/form-data")
	@Transactional
	public ImportExcelFileResponse fileUpload(@RequestParam("file") MultipartFile file, @PathVariable String Id) {
		ImportExcelFileResponse response = new ImportExcelFileResponse();
		if (!isExcelFile(file)) {
			// String message = "Invalid file format. Only .xlsx files are allowed";
			response.setCode(ExceptionEnum.DATA_NOT_FOUND.getErrorCode());
			response.setMessage("Invalid file format. Only .xlsx files are allowed.");
			return response;
		}

		try {
			List<ClientCredentials> clientList = excelservice.getClientDataAsList(file.getInputStream(),
					Integer.parseInt(Id));

			if (clientList.isEmpty()) {
				response.setCode(ExceptionEnum.DATA_NOT_FOUND.getErrorCode());
				response.setMessage("No client data to save.");
				return response;
			}

			int noOfClientRecords = excelservice.saveClientData(clientList);

			if (noOfClientRecords == 0) {
				response.setCode(ExceptionEnum.UNIVERSAL_ERROR.getErrorCode());
				response.setMessage("Error saving client records.");
				return response;
			}

			List<ImportProjects> projectList = importProjectsService.getProjectDataAsList(file.getInputStream());
			List<ImportSprint> sprintList = importSprintService.getSprintDataAsList(file.getInputStream());
			List<ImportTask> taskList = importTaskService.getTaskDataAsList(file.getInputStream());

			int noOfTaskRecords = importTaskService.saveTaskData(taskList);

			if (noOfTaskRecords == 0) {
				response.setCode(ExceptionEnum.UNIVERSAL_ERROR.getErrorCode());
				response.setMessage("Error saving task records.");
				return response;
			}

			if (projectList.isEmpty()) {
				response.setCode(ExceptionEnum.UNIVERSAL_ERROR.getErrorCode());
				response.setMessage("No project data to save.");
				return response;
			}

			List<ImportProjects> noOfProjectRecords = importProjectsService.saveProjectData(projectList);
			int noOfSprintRecords = importSprintService.saveSprintData(sprintList);
// 
//	        if (noOfProjectRecords == 0) {
//                response.setCode(ExceptionEnum.UNIVERSAL_ERROR.getErrorCode());
//                response.setMessage("Error saving project records.");
//                return response;
//            }
// 
			response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
			response.setMessage("File uploaded successfully.");

			return response;
		} catch (IOException e) {
			e.printStackTrace();
			response.setCode(ExceptionEnum.UNIVERSAL_ERROR.getErrorCode());
			response.setMessage("Error processing the file.");
			return response;
		}
	}

	private boolean isExcelFile(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();

		return originalFilename != null && originalFilename.toLowerCase().endsWith(".xlsx");
	}

}