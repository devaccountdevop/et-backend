package com.es.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.entity.ImportProjects;
import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;
import com.es.entity.Signup;
import com.es.exceptions.JiraApiBadRequestException;
import com.es.exceptions.JiraApiException;
import com.es.exceptions.JiraApiUnauthException;
import com.es.response.ExceptionEnum;
import com.es.response.GetProjectResponse;
import com.es.response.SuccessEnum;
import com.es.response.SyncResponse;
import com.es.service.ImportProjectsService;
import com.es.service.ImportSprintService;
import com.es.service.ImportTaskService;
import com.es.service.JIRARestService;
import com.es.service.SignupService;

@RestController
@RequestMapping("/estimation-tool")
public class SyncController {

	@Autowired
	JIRARestService jiraRestService;

	@Autowired
	ImportProjectsService importProjectsService;

	@Autowired
	ImportSprintService importSprintService;

	@Autowired
	ImportTaskService importTaskService;

	@Autowired
	SignupService signupService;

	@GetMapping("/sync/{userId}/{clientId}")
	public SyncResponse syncAllDataWithJira(@PathVariable String userId, @PathVariable String clientId) {
		SyncResponse response = new SyncResponse();

		try {
			Signup signup = signupService.getUserById(Integer.parseInt(userId));

			if (signup == null) {
				response.setCode(ExceptionEnum.INVALID_USER.getErrorCode());
				response.setMessage(ExceptionEnum.INVALID_USER.getMessage());
				return response;
			}

			GetProjectResponse projectResponse = jiraRestService.getAllProjects(Integer.parseInt(clientId));
			List<ImportProjects> projectInfoList = (List<ImportProjects>) projectResponse.getData();

			if (!projectInfoList.isEmpty()) {
				List<ImportSprint> allSprints = new ArrayList<>();
				List<ImportTask> allTasks = new ArrayList<>();

				for (ImportProjects project : projectInfoList) {
					List<ImportSprint> sprintInfoList = jiraRestService.getAllSprintsByProjectId(project.getProjectId(),
							Integer.parseInt(clientId));

					if (!sprintInfoList.isEmpty()) {
						allSprints.addAll(sprintInfoList);

						List<ImportTask> taskList = new ArrayList<>();
						for (ImportSprint sprint : sprintInfoList) {
							taskList.addAll(jiraRestService.getAllTasksBySprintId(sprint.getSprintId(),
									project.getProjectId(), Integer.parseInt(clientId)));
						}
						allTasks.addAll(taskList);
					}
				}

				if (!allSprints.isEmpty() && !allTasks.isEmpty()) {
					// Save all data to your database or perform any other necessary actions
					importProjectsService.saveProjectData(projectInfoList);
					importSprintService.saveSprintData(allSprints);
					importTaskService.saveTaskData(allTasks);

					List<Object> object = new ArrayList<>();
					object.addAll(projectInfoList);
					object.addAll(allSprints);
					object.addAll(allTasks);

					response.setCode(HttpStatus.OK.value());
					response.setData(object);
					return response;
				}
			} else {
				// Handle the case where no project data is retrieved
				response.setCode(projectResponse.getCode());
				response.setMessage("Failed to retrieve project data from Jira.");
				return response;
			}
		} catch (JiraApiUnauthException e) {
			// Handle unauthorized exception (e.g., incorrect Jira credentials)
			response.setCode(HttpStatus.UNAUTHORIZED.value());
			response.setMessage("Unauthorized: Invalid Jira credentials.");
			return response;
		} catch (JiraApiBadRequestException e) {
			// Handle bad request exception (e.g., invalid request to Jira API)
			response.setCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage("Bad Request: Invalid request to Jira API.");
			return response;
		} catch (JiraApiException e) {
			// Handle specific custom exception, if applicable
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage("Internal Server Error: " + e.getMessage());
			return response;
		} catch (Exception e) {
			// Handle other unexpected exceptions
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage("Internal Server Error: Something went wrong.");
			return response;
		}

		// Handle the case where no data is found or processed
		response.setCode(HttpStatus.NOT_FOUND.value());
		response.setMessage("No data found or processed.");
		return response;
	}

}
