package com.es.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.dto.ProjectGraphDto;
import com.es.dto.ProjectInfoDto;
import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.entity.ImportTask;
import com.es.service.ClientCredentialsService;
import com.es.service.DownloadExcelData;
import com.es.service.ImportProjectsService;
import com.es.service.ImportSprintService;
import com.es.service.ImportTaskService;
 
@Service
public class DownloadExcelDataServiceImpl implements DownloadExcelData {

    @Autowired
    ImportProjectsService importProjectsService;
    @Autowired
    ImportSprintService importSprintService;
    @Autowired
    ImportTaskService importTaskService;
    
    @Autowired
    ClientCredentialsService clientCredentialsService;


	@Override
	public byte[] generateProjectData(int projectId, int clientId, int userId) throws IOException {
		ClientCredentials clientData = new ClientCredentials();
		if(clientId != 0) {
			
			 clientData = clientCredentialsService.getClientCredentials(clientId);
			
		}
		
		 if (projectId == 0) {
	            throw new IllegalArgumentException("Project ID cannot be zero");
	        }

	        ProjectInfoDto downloadProject = importProjectsService.getProjectByProjectId(projectId, userId);

	        try (Workbook workbook = new XSSFWorkbook()) {
	            Sheet sheet = workbook.createSheet("Project Data");

	         // Create the information row
	            Row infoRow = sheet.createRow(0);

	            // Merge the first row
	            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 20));

	            // Set the value for the merged cell
	            Cell infoCell = infoRow.createCell(0);
	            infoCell.setCellValue("Information about the Columns:\nIssue Type - e.g: \"Bug,\" \"Story,\" or \"Task,\". Status - e.g: \"To Do,\" \"In Progress,\" or \"Done,\". Priority - e.g: \"High,\" \"Medium,\" or \"Low,\".");

	            
	            // Wrap text for the merged cells
	            CellStyle wrapStyle = workbook.createCellStyle();
	            wrapStyle.setWrapText(true);
	            infoCell.setCellStyle(wrapStyle);
	            
	            // Create header row
	            
	            Row headerRow = sheet.createRow(2);
	            String[] headers = {
	                "clientName", "clientUserName", "clientToken", "clientUserName For project",
	                "projectId", "ProjectName", "sprintId", "sprintName", "Summary", "Issue key",
	                "Issue Type", "Status", "Priority", "Original estimate", "Updated", "Labels",
	                "Description", "Custom field (Ai Estimate)", "Optimistic Estimate",
	                "Pessimistic Estimate", "Realistic Estimate"
	            };
	            for (int i = 0; i < headers.length; i++) {
	                Cell cell = headerRow.createCell(i);
	                cell.setCellValue(headers[i]);
	            }

	            // Populate project and sprint info
	            int rowIndex = 3;
	            boolean clientDetailsWritten = clientData == null;
	            
	            // Write sprint tasks
	            for (ProjectGraphDto sprint : downloadProject.getSprintInfoDtos()) {
	                for (ImportTask task : sprint.getTaskDetails()) {
	                    Row row = sheet.createRow(rowIndex++);

	                    if (!clientDetailsWritten) {
	                        row.createCell(0).setCellValue(clientData.getClientName()); // clientName
	                        row.createCell(1).setCellValue(clientData.getJiraUserName()); // clientUserName
	                        row.createCell(2).setCellValue(clientData.getToken()); // clientToken
	                        clientDetailsWritten = true;
	                    } else {
	                        row.createCell(0).setCellValue(""); // clientName
	                        row.createCell(1).setCellValue(""); // clientUserName
	                        row.createCell(2).setCellValue(""); // clientToken
	                    }

	                    row.createCell(3).setCellValue(downloadProject.getJiraUserName()); // clientUserName For project
	                    row.createCell(4).setCellValue(downloadProject.getProjectId()); // projectId
	                    row.createCell(5).setCellValue(downloadProject.getProjectName()); // ProjectName
	                    row.createCell(6).setCellValue(sprint.getSprintId()); // sprintId
	                    row.createCell(7).setCellValue(sprint.getSprintName()); // sprintName
	                    row.createCell(8).setCellValue(task.getSummary()); // Summary
	                    row.createCell(9).setCellValue(task.getTaskId()); // Issue key
	                    row.createCell(10).setCellValue(task.getTaskType()); // Issue Type
	                    row.createCell(11).setCellValue(task.getTaskStatus()); // Status
	                    row.createCell(12).setCellValue(task.getTaskPriority()); // Priority
	                    row.createCell(13).setCellValue(task.getOriginalEstimate()); // Original estimate
	                    row.createCell(14).setCellValue(task.getCreationDate()); // Updated
	                    row.createCell(15).setCellValue(task.getAssignee()); // Labels
	                    row.createCell(16).setCellValue(task.getTaskDescription()); // Description
	                    row.createCell(17).setCellValue(task.getAiEstimate()); // Custom field (Ai Estimate)
	                    row.createCell(18).setCellValue(task.getEstimates().getLow()); // Optimistic Estimate
	                    row.createCell(19).setCellValue(task.getEstimates().getHigh()); // Pessimistic Estimate
	                    row.createCell(20).setCellValue(task.getEstimates().getRealistic()); // Realistic Estimate
	                }
	            }

	            // Retrieve and write backlog tasks
	            List<ImportTask> backlogTasks = importTaskService.getAllBacklogTask(projectId);

	            for (ImportTask backlogTask : backlogTasks) {
	                Row row = sheet.createRow(rowIndex++);
	                row.createCell(0).setCellValue(""); // Empty clientName
	                row.createCell(1).setCellValue(""); // Empty clientUserName
	                row.createCell(2).setCellValue(""); // Empty clientToken
	                row.createCell(3).setCellValue(downloadProject.getJiraUserName()); // clientUserName For project
	                row.createCell(4).setCellValue(downloadProject.getProjectId()); // projectId
	                row.createCell(5).setCellValue(downloadProject.getProjectName()); // ProjectName
	                row.createCell(6).setCellValue(0); // Empty sprintId for backlog
	                row.createCell(7).setCellValue(""); // Empty sprintName for backlog
	                row.createCell(8).setCellValue(backlogTask.getSummary()); // Summary
	                row.createCell(9).setCellValue(backlogTask.getTaskId()); // Issue key
	                row.createCell(10).setCellValue(backlogTask.getTaskType()); // Issue Type
	                row.createCell(11).setCellValue(backlogTask.getTaskStatus()); // Status
	                row.createCell(12).setCellValue(backlogTask.getTaskPriority()); // Priority
	                row.createCell(13).setCellValue(backlogTask.getOriginalEstimate()); // Original estimate
	                row.createCell(14).setCellValue(backlogTask.getCreationDate()); // Updated
	                row.createCell(15).setCellValue(backlogTask.getAssignee()); // Labels
	                row.createCell(16).setCellValue(backlogTask.getTaskDescription()); // Description
	                row.createCell(17).setCellValue(backlogTask.getAiEstimate()); // Custom field (Ai Estimate)
	                row.createCell(18).setCellValue(backlogTask.getEstimates().getLow()); // Optimistic Estimate
	                row.createCell(19).setCellValue(backlogTask.getEstimates().getHigh()); // Pessimistic Estimate
	                row.createCell(20).setCellValue(backlogTask.getEstimates().getRealistic()); // Realistic Estimate
	            }

	            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	                workbook.write(outputStream);
	                return outputStream.toByteArray();
	            }
	        }
	}


	@Override
	public byte[] generateSprintData(int projectId, int clientId, int sprintId, int userId) throws IOException {
	    ClientCredentials clientData = new ClientCredentials();
	    if (clientId != 0) {
	        clientData = clientCredentialsService.getClientCredentials(clientId);
	    }

	    if (projectId == 0) {
	        throw new IllegalArgumentException("Project ID cannot be zero");
	    }

	    ProjectInfoDto downloadProject = importProjectsService.getProjectByProjectId(projectId, userId);

	    try (Workbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("Project Data");

	        // Create the information row
	        Row infoRow = sheet.createRow(0);
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 20));
	        Cell infoCell = infoRow.createCell(0);
	        infoCell.setCellValue("Information about the Columns:\nIssue Type - e.g: \"Bug,\" \"Story,\" or \"Task,\". Status - e.g: \"To Do,\" \"In Progress,\" or \"Done,\". Priority - e.g: \"High,\" \"Medium,\" or \"Low,\".");
	        CellStyle wrapStyle = workbook.createCellStyle();
	        wrapStyle.setWrapText(true);
	        infoCell.setCellStyle(wrapStyle);

	        // Create header row
	        Row headerRow = sheet.createRow(2);
	        String[] headers = {
	            "clientName", "clientUserName", "clientToken", "clientUserName For project",
	            "projectId", "ProjectName", "sprintId", "sprintName", "Summary", "Issue key",
	            "Issue Type", "Status", "Priority", "Original estimate", "Updated", "Labels",
	            "Description", "Custom field (Ai Estimate)", "Optimistic Estimate",
	            "Pessimistic Estimate", "Realistic Estimate"
	        };
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	        }

	        // Populate project and sprint info
	        int rowIndex = 3;
	        boolean clientDetailsWritten = false;

	        // Write sprint tasks
	        for (ProjectGraphDto sprint : downloadProject.getSprintInfoDtos()) {
	            if (sprint.getSprintId() == sprintId) {
	                for (ImportTask task : sprint.getTaskDetails()) {
	                    Row row = sheet.createRow(rowIndex++);

	                    if (!clientDetailsWritten) {
	                        row.createCell(0).setCellValue(clientData.getClientName()); // clientName
	                        row.createCell(1).setCellValue(clientData.getJiraUserName()); // clientUserName
	                        row.createCell(2).setCellValue(clientData.getToken()); // clientToken
	                        clientDetailsWritten = true;
	                    } else {
	                        row.createCell(0).setCellValue(""); // clientName
	                        row.createCell(1).setCellValue(""); // clientUserName
	                        row.createCell(2).setCellValue(""); // clientToken
	                    }

	                    row.createCell(3).setCellValue(downloadProject.getJiraUserName()); // clientUserName For project
	                    row.createCell(4).setCellValue(downloadProject.getProjectId()); // projectId
	                    row.createCell(5).setCellValue(downloadProject.getProjectName()); // ProjectName
	                    row.createCell(6).setCellValue(sprint.getSprintId()); // sprintId
	                    row.createCell(7).setCellValue(sprint.getSprintName()); // sprintName
	                    row.createCell(8).setCellValue(task.getSummary()); // Summary
	                    row.createCell(9).setCellValue(task.getTaskId()); // Issue key
	                    row.createCell(10).setCellValue(task.getTaskType()); // Issue Type
	                    row.createCell(11).setCellValue(task.getTaskStatus()); // Status
	                    row.createCell(12).setCellValue(task.getTaskPriority()); // Priority
	                    row.createCell(13).setCellValue(task.getOriginalEstimate()); // Original estimate
	                    row.createCell(14).setCellValue(task.getCreationDate()); // Updated
	                    row.createCell(15).setCellValue(task.getAssignee()); // Labels
	                    row.createCell(16).setCellValue(task.getTaskDescription()); // Description
	                    row.createCell(17).setCellValue(task.getAiEstimate()); // Custom field (Ai Estimate)
	                    row.createCell(18).setCellValue(task.getEstimates().getLow()); // Optimistic Estimate
	                    row.createCell(19).setCellValue(task.getEstimates().getHigh()); // Pessimistic Estimate
	                    row.createCell(20).setCellValue(task.getEstimates().getRealistic()); // Realistic Estimate
	                }
	               
	            }
	            
	        }

	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            workbook.write(outputStream);
	            return outputStream.toByteArray();
	        }
	    }
	}


	@Override
	public byte[] generateBacklogData(int projectId, int clientId, int userId) throws IOException {
		 ClientCredentials clientData = new ClientCredentials();
		    if (clientId != 0) {
		        clientData = clientCredentialsService.getClientCredentials(clientId);
		    }
		 if (projectId == 0) {
		        throw new IllegalArgumentException("Project ID cannot be zero");
		    }

		    ProjectInfoDto downloadProject = importProjectsService.getProjectByProjectId(projectId, userId);

		    try (Workbook workbook = new XSSFWorkbook()) {
		        Sheet sheet = workbook.createSheet("Backlog Data");

		        // Create the information row
		        Row infoRow = sheet.createRow(0);

		        // Merge the first row
		        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 20));

		        // Set the value for the merged cell
		        Cell infoCell = infoRow.createCell(0);
		        infoCell.setCellValue("Information about the Columns:\nIssue Type - e.g: \"Bug,\" \"Story,\" or \"Task,\". Status - e.g: \"To Do,\" \"In Progress,\" or \"Done,\". Priority - e.g: \"High,\" \"Medium,\" or \"Low,\".");

		        // Wrap text for the merged cells
		        CellStyle wrapStyle = workbook.createCellStyle();
		        wrapStyle.setWrapText(true);
		        infoCell.setCellStyle(wrapStyle);

		        // Create header row
		        Row headerRow = sheet.createRow(2);
		        String[] headers = {
		            "clientName", "clientUserName", "clientToken", "clientUserName For project",
		            "projectId", "ProjectName", "sprintId", "sprintName", "Summary", "Issue key",
		            "Issue Type", "Status", "Priority", "Original estimate", "Updated", "Labels",
		            "Description", "Custom field (Ai Estimate)", "Optimistic Estimate",
		            "Pessimistic Estimate", "Realistic Estimate"
		        };
		        for (int i = 0; i < headers.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(headers[i]);
		        }

		        // Retrieve and write backlog tasks
		        List<ImportTask> backlogTasks = importTaskService.getAllBacklogTask(projectId);
		        int rowIndex = 3;
		        
		        boolean clientDetailsWritten = false;

		        for (ImportTask backlogTask : backlogTasks) {
		            Row row = sheet.createRow(rowIndex++);
		            
		            if (!clientDetailsWritten) {
                        row.createCell(0).setCellValue(clientData.getClientName()); // clientName
                        row.createCell(1).setCellValue(clientData.getJiraUserName()); // clientUserName
                        row.createCell(2).setCellValue(clientData.getToken()); // clientToken
                        clientDetailsWritten = true;
                    } else {
                        row.createCell(0).setCellValue(""); // clientName
                        row.createCell(1).setCellValue(""); // clientUserName
                        row.createCell(2).setCellValue(""); // clientToken
                    }
		        
		            row.createCell(3).setCellValue(downloadProject.getJiraUserName()); // clientUserName For project
		            row.createCell(4).setCellValue(downloadProject.getProjectId()); // projectId
		            row.createCell(5).setCellValue(downloadProject.getProjectName()); // ProjectName
		            row.createCell(6).setCellValue(0); // Empty sprintId for backlog
		            row.createCell(7).setCellValue(""); // Empty sprintName for backlog
		            row.createCell(8).setCellValue(backlogTask.getSummary()); // Summary
		            row.createCell(9).setCellValue(backlogTask.getTaskId()); // Issue key
		            row.createCell(10).setCellValue(backlogTask.getTaskType()); // Issue Type
		            row.createCell(11).setCellValue(backlogTask.getTaskStatus()); // Status
		            row.createCell(12).setCellValue(backlogTask.getTaskPriority()); // Priority
		            row.createCell(13).setCellValue(backlogTask.getOriginalEstimate()); // Original estimate
		            row.createCell(14).setCellValue(backlogTask.getCreationDate()); // Updated
		            row.createCell(15).setCellValue(backlogTask.getAssignee()); // Labels
		            row.createCell(16).setCellValue(backlogTask.getTaskDescription()); // Description
		            row.createCell(17).setCellValue(backlogTask.getAiEstimate()); // Custom field (Ai Estimate)
		            row.createCell(18).setCellValue(backlogTask.getEstimates().getLow()); // Optimistic Estimate
		            row.createCell(19).setCellValue(backlogTask.getEstimates().getHigh()); // Pessimistic Estimate
		            row.createCell(20).setCellValue(backlogTask.getEstimates().getRealistic()); // Realistic Estimate
		        }

		        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
		            workbook.write(outputStream);
		            return outputStream.toByteArray();
		        }
		    }
}}
