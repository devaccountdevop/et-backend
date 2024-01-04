package com.es.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.es.dto.AiEstimatesDto;
import com.es.dto.SprintListPageDto;
import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;
import com.es.entity.TaskEstimates;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class JIRARestService {

	@Autowired
	EstimatesService estimatesService;

	@Value("${jira.username}")
	private String jira_username;

	@Value("${jira.token}")
	private String jira_token;

	@Value("${jira.base.url}")
	private String jira_base_url;

	@Value("${jira.get.project.endpoint}")
	private String jira_get_project;

	@Value("${ai.url}")
	private String apiUrl;

	@Value("${jira.update.property}")
	private String jira_update_task;

	@Autowired
	ClientCredentialsService clientCredentialsService;

	public List<ImportProjects> getAllProjects(int clientId) {
		try {
			ClientCredentials clientCredentials = clientCredentialsService.getClientCredentials(clientId);

			if (clientCredentials != null) {
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				headers.setBasicAuth(clientCredentials.getJiraUserName(), clientCredentials.getToken());
				HttpEntity<String> entity = new HttpEntity<>(headers);

				ResponseEntity<String> responseEntity = restTemplate.exchange(jira_base_url + jira_get_project,
						HttpMethod.GET, entity, String.class);

				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					String jsonResponse = responseEntity.getBody();
					JsonParser jsonParser = new JsonParser();
					JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

					JsonArray values = jsonObject.getAsJsonArray("values");

					List<ImportProjects> projectInfoList = new ArrayList<>();

					for (int i = 0; i < values.size(); i++) {
						JsonObject projectObject = values.get(i).getAsJsonObject();
						int projectId = projectObject.get("id").getAsInt();
						JsonObject location = projectObject.getAsJsonObject("location");
						String projectName = location.get("projectName").getAsString();

						ImportProjects projectInfo = new ImportProjects(projectId, projectName,
								clientCredentials.getJiraUserName());
						projectInfoList.add(projectInfo);
					}

					return projectInfoList;
				} else {
					// Handle non-successful HTTP response
					// You can log the error, throw an exception, or handle it based on your
					// requirements.
					return null;
				}
			} else {
				// Handle the case where client credentials are not found
				// You can log the error, throw an exception, or handle it based on your
				// requirements.
				return null;
			}
		} catch (HttpClientErrorException.Unauthorized unauthorizedException) {
			// Handle unauthorized (401) error, e.g., incorrect username or token
			// You can log the error, throw a custom exception, or handle it based on your
			// requirements.
			unauthorizedException.printStackTrace(); // Log the error or handle it based on your requirements.
			return null;
		} catch (Exception e) {
			// Handle other exceptions
			e.printStackTrace(); // Log the exception or handle it based on your requirements.
			return null;
		}
	}

	public List<ImportSprint> getAllSprintsByProjectId(int projectId, int clientId) {
		try {
			ClientCredentials clientCredentials = clientCredentialsService.getClientCredentials(clientId);

			if (clientCredentials == null) {
				// Handle the case where client credentials are not found
				// You can log the error, throw an exception, or handle it based on your
				// requirements.
				System.err.println("Client credentials not found for client ID: " + clientId);
				return Collections.emptyList();
			}

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBasicAuth(clientCredentials.getJiraUserName(), clientCredentials.getToken());
			HttpEntity<String> entity = new HttpEntity<>(headers);

			String jiraSprintEndpoint = jira_base_url + jira_get_project + projectId + "/sprint";

			ResponseEntity<String> responseEntity = restTemplate.exchange(jiraSprintEndpoint, HttpMethod.GET, entity,
					String.class);

			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				String jsonResponse = responseEntity.getBody();
				JsonParser jsonParser = new JsonParser();
				JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

				JsonArray values = jsonObject.getAsJsonArray("values");

				List<ImportSprint> sprintInfoList = new ArrayList<>();

				if (values != null && values.size() > 0) {
					for (JsonElement value : values) {
						JsonObject sprintObject = value.getAsJsonObject();
						int sprintId = sprintObject.get("id").getAsInt();
						String sprintName = sprintObject.get("name").getAsString();

						ImportSprint sprintInfo = new ImportSprint(projectId, sprintId, sprintName);
						sprintInfoList.add(sprintInfo);
					}
				}

				return sprintInfoList;
			} else {
				// Handle non-successful HTTP response
				// You can log the error, throw an exception, or handle it based on your
				// requirements.
				System.err.println("Error in getAllSprintsByProjectId - Non-successful HTTP response: "
						+ responseEntity.getStatusCode());
				return Collections.emptyList();
			}
		} catch (HttpClientErrorException.Unauthorized unauthorizedException) {
			// Handle unauthorized (401) error, e.g., incorrect username or token
			// You can log the error, throw a custom exception, or handle it based on your
			// requirements.
			unauthorizedException.printStackTrace(); // Log the error or handle it based on your requirements.
			return Collections.emptyList();
		} catch (Exception e) {
			// Handle other exceptions
			e.printStackTrace(); // Log the exception or handle it based on your requirements.
			return Collections.emptyList();
		}
	}

	public List<ImportTask> getAllTasksBySprintId(int SprintId, int projectId, int clientId) {

		ClientCredentials clientCredentials = clientCredentialsService.getClientCredentials(clientId);
		if (clientCredentials != null) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBasicAuth(clientCredentials.getJiraUserName(), clientCredentials.getToken());
			HttpEntity<String> entity = new HttpEntity<>(headers);
			// String jiraTasksEndpoint = jira_base_url + jira_get_project + "/sprint/" +
			// SprintId + "/issue" ;
			String jiraTasksEndpoint = jira_base_url + jira_get_project + "/" + projectId + "/sprint/" + SprintId
					+ "/issue";
			ResponseEntity<String> responseEntity = restTemplate.exchange(jiraTasksEndpoint, HttpMethod.GET, entity,
					String.class);
			String jsonResponse = responseEntity.getBody();
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

			JsonArray issues = jsonObject.getAsJsonArray("issues");

			List<ImportTask> taskInfoList = new ArrayList<>();

			for (int i = 0; i < issues.size(); i++) {
				JsonObject issueObject = issues.get(i).getAsJsonObject();
				String issueId = issueObject.get("key").getAsString();

				JsonObject fields = issueObject.getAsJsonObject("fields");
				String issueName = fields.get("summary").getAsString();
				String issuePriority = fields.getAsJsonObject("priority").get("name").getAsString();
				JsonElement customFieldElement1 = fields.get("customfield_10033");
				JsonElement customFieldElement2 = fields.get("aggregatetimeoriginalestimate");
				JsonElement descriptionElement = fields.get("description");
				String issueDescription = (descriptionElement != null && !descriptionElement.isJsonNull())
						? descriptionElement.getAsString()
						: null;
				JsonElement customFieldElement = fields.get("customfield_10036");
				
				String aiEstimate = (customFieldElement != null && !customFieldElement.isJsonNull())
				        ? customFieldElement.getAsString()
				        : "0";
				JsonArray labelArray = fields.getAsJsonArray("labels");
				List<String> labelsList = new ArrayList<>();

				if (labelArray != null) {
					for (int j = 0; j < labelArray.size(); j++) {
						labelsList.add(labelArray.get(j).getAsString());
					}
				}

				TaskEstimates  taskEstimates  = new TaskEstimates();
				taskEstimates.setHigh(0);
				taskEstimates.setLow(0);
				taskEstimates.setRealistic(0);
				taskEstimates.setTaskId(issueId);
				ImportTask taskInfo = new ImportTask(SprintId, issueName, issueId, issueDescription, 5, aiEstimate, 0,
						labelsList, 3,taskEstimates);
				taskInfoList.add(taskInfo);
			}

			return taskInfoList;
		} else {
			return null;
		}
	}

	public AiEstimatesDto getAiEstimates(String taskId) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		// Set your headers if needed, for example, basic authentication.

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
		String jsonResponse = responseEntity.getBody();

		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();

		AiEstimatesDto aiEstimatesDto = new AiEstimatesDto();

		for (JsonElement element : jsonArray) {
			JsonObject issueObject = element.getAsJsonObject();

			if (taskId == issueObject.get("key").getAsString()) {
				int issueId = issueObject.get("id").getAsInt();
				String issueName = issueObject.get("Aiestimates").getAsString();

				aiEstimatesDto.setId(issueId);
				// aiEstimatesDto.setAiestimates(issueName);
				aiEstimatesDto.setTaskId(taskId);

				return aiEstimatesDto;
			}
		}

		return aiEstimatesDto;
	}

	public void updateToJIRA(String taskId, String newAiestimates) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(jira_username, jira_token);

		String jiraUpdateEndpoint = jira_update_task + taskId;
		JsonObject aiestimatesObject = new JsonObject();
		//String newAiestimatesAsString = String.valueOf(newAiestimates);
		aiestimatesObject.addProperty("customfield_10036", newAiestimates);
		JsonObject fieldsObject = new JsonObject();
		fieldsObject.add("fields", aiestimatesObject);
		HttpEntity<String> entity = new HttpEntity<>(fieldsObject.toString(), headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(jiraUpdateEndpoint, HttpMethod.PUT, entity,
				String.class);
		if (responseEntity.getStatusCode().is2xxSuccessful()) {

			System.out.println("Aiestimates updated successfully for issue ID " + taskId);
		} else {

			System.err.println("Failed to update Aiestimates for issue ID " + taskId);
		}

	}

	public List<SprintListPageDto> getAllBacklogTasks(int projectId) {

		{
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBasicAuth(jira_username, jira_token);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			// String jiraTasksEndpoint = jira_base_url + jira_get_project + "/sprint/" +
			// SprintId + "/issue" ;
			String jiraTasksEndpoint = jira_base_url + jira_get_project + "/" + projectId + "/backlog";
			ResponseEntity<String> responseEntity = restTemplate.exchange(jiraTasksEndpoint, HttpMethod.GET, entity,
					String.class);
			String jsonResponse = responseEntity.getBody();
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

			JsonArray issues = jsonObject.getAsJsonArray("issues");

			List<SprintListPageDto> taskInfoList = new ArrayList<>();

			for (int i = 0; i < issues.size(); i++) {
				JsonObject issueObject = issues.get(i).getAsJsonObject();
				String issueId = issueObject.get("key").getAsString();

				JsonObject fields = issueObject.getAsJsonObject("fields");
				String issueName = fields.get("summary").getAsString();
				JsonElement descriptionElement = fields.get("description");
				String issueDescription = (descriptionElement != null && !descriptionElement.isJsonNull())
						? descriptionElement.getAsString()
						: null;
				JsonElement customFieldElement = fields.get("customfield_10036");
				int aiEstimate = (customFieldElement != null && !customFieldElement.isJsonNull())
						? Integer.parseInt(customFieldElement.getAsString())
						: 0;

				JsonArray labelArray = fields.getAsJsonArray("labels");
				List<String> labels = new ArrayList<>();
				if (labelArray != null) {
					for (JsonElement labelElement : labelArray) {
						labels.add(labelElement.getAsString());
					}
				}
				TaskEstimates estimates = new TaskEstimates();
				List<TaskEstimates> labels1 = new ArrayList<>();
				// estimates = estimatesService.getEstimatesByTaskId(issueId);
				labels1.add(estimates);
				AiEstimatesDto aiEstimatesDto = new AiEstimatesDto();
				// aiEstimatesDto = this.getAiEstimates(issueId);
				// aiEstimatesDto.setAiestimates();
				SprintListPageDto taskInfo = new SprintListPageDto(issueId, issueName, issueDescription, labels1,
						labels, aiEstimate, 4);
				taskInfoList.add(taskInfo);
			}

			return taskInfoList;
		}
	}

}
