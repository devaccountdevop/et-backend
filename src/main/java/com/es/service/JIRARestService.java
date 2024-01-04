package com.es.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
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
import com.google.gson.Gson;
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

	@Value("${Ai.url}")
	private String apiUrl;

	@Value("${jira.update.property}")
	private String jira_update_task;

	@Value("${create.custom.field.Jira.url}")
	private String customField_creation_url;

	@Value("${AiEstimate.field.name}")
	private String field_Name;

	@Value("${StoryPoints.field.name}")
	private String storyPoints_field_Name;

	@Value("${originalEstimates.field.name}")
	private String originalEstimates_field_Name;

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

		String customFieldInJira;
		ClientCredentials clientCredentials = clientCredentialsService.getClientCredentials(clientId);
		if (clientCredentials != null) {
			customFieldInJira = getFieldId(field_Name);
			if (customFieldInJira == null) {
				customFieldInJira = createField(field_Name);
			}

			String storyPointsFieldInJira;
			if (clientCredentials != null) {
				storyPointsFieldInJira = getFieldId(storyPoints_field_Name);
//	            if (storyPointsFieldInJira == null) {
//	                storyPointsFieldInJira = createField(storyPoints_field_Name);
//	            }

				String originalEstimateFieldInJira;
				if (clientCredentials != null) {
					originalEstimateFieldInJira = getFieldId(originalEstimates_field_Name);
//	                if (originalEstimateFieldInJira == null) {
//	                    originalEstimateFieldInJira = createField(originalEstimates_field_Name);
//	                }
//	            

//	            List<String> assignScreens = new ArrayList<String>();
//	            assignScreens = assignScreensToCustomFields();

					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
					headers.setBasicAuth(clientCredentials.getJiraUserName(), clientCredentials.getToken());
					HttpEntity<String> entity = new HttpEntity<>(headers);
					String jiraTasksEndpoint = jira_base_url + jira_get_project + "/" + projectId + "/sprint/"
							+ SprintId + "/issue";
					ResponseEntity<String> responseEntity = restTemplate.exchange(jiraTasksEndpoint, HttpMethod.GET,
							entity, String.class);
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
						JsonElement descriptionElement = fields.get("description");
						String issueDescription = (descriptionElement != null && !descriptionElement.isJsonNull())
								? descriptionElement.getAsString()
								: null;
						JsonElement customFieldElement = fields.get(customFieldInJira);
						String aiEstimate = (customFieldElement != null && !customFieldElement.isJsonNull())
						        ? customFieldElement.getAsString()
						        : "0";

						JsonElement storyPointsElement = fields.get(storyPointsFieldInJira);
						String storyPoints = (storyPointsElement != null && !storyPointsElement.isJsonNull())
								? storyPointsElement.getAsString()
								: null;

						JsonElement originalEstimateElement = fields.get(originalEstimateFieldInJira);
						int originalEstimate = (originalEstimateElement != null
								&& !originalEstimateElement.isJsonNull())
										? Integer.parseInt(originalEstimateElement.getAsString())
										: 0;

						JsonObject priorityObject = fields.getAsJsonObject("priority");
						String priority = (priorityObject != null && !priorityObject.isJsonNull())
								? priorityObject.get("name").getAsString()
								: null;

						JsonArray labelArray = fields.getAsJsonArray("labels");
						List<String> labelsList = new ArrayList<>();

						if (labelArray != null) {
							for (int j = 0; j < labelArray.size(); j++) {
								labelsList.add(labelArray.get(j).getAsString());
							}
						}

						TaskEstimates taskEstimates = new TaskEstimates();
						taskEstimates.setHigh(0);
						taskEstimates.setLow(0);
						taskEstimates.setRealistic(0);
						taskEstimates.setTaskId(issueId);
						ImportTask taskInfo = new ImportTask(SprintId, issueName, issueId, issueDescription, 5,
								aiEstimate, 0, labelsList, 3, taskEstimates, storyPoints, originalEstimate, priority);
						taskInfoList.add(taskInfo);
					}

					return taskInfoList;
				}
			}
		}
		return null;
	}

	public AiEstimatesDto getAiEstimates(String taskId) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		// Set your headers if needed, for example, basic authentication.

		String customFieldInJira;

		customFieldInJira = getFieldId(field_Name);
		if (customFieldInJira == null) {
			customFieldInJira = createField(field_Name);
		}

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

		String customFieldInJira;

		customFieldInJira = getFieldId(field_Name);
		if (customFieldInJira == null) {
			customFieldInJira = createField(field_Name);
		}

		String jiraUpdateEndpoint = jira_update_task + taskId;
		JsonObject aiestimatesObject = new JsonObject();
		// String newAiestimatesAsString = String.valueOf(newAiestimates);
		aiestimatesObject.addProperty(customFieldInJira, newAiestimates);
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

			String customFieldInJira;

			customFieldInJira = getFieldId(field_Name);
			if (customFieldInJira == null) {
				customFieldInJira = createField(field_Name);
			}
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
				JsonElement customFieldElement = fields.get(customFieldInJira);
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

	public String getFieldId(String fieldName) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBasicAuth(jira_username, jira_token);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		// String jiraTasksEndpoint = jira_base_url + jira_get_project + "/sprint/" +
		// SprintId + "/issue" ;
		String jiraTasksEndpoint = customField_creation_url + "/field";
		ResponseEntity<String> responseEntity = restTemplate.exchange(jiraTasksEndpoint, HttpMethod.GET, entity,
				String.class);
		String jsonResponse = responseEntity.getBody();
		// Check if the field exists in the response
		if (jsonResponse != null && jsonResponse.contains("\"name\":\"" + fieldName + "\"")) {
			// Parse the response to get the field ID
			int index = jsonResponse.indexOf("\"name\":\"" + fieldName + "\",");
			if (index != -1) {
				int idStartIndex = jsonResponse.lastIndexOf("\"id\":\"", index);
				int idEndIndex = jsonResponse.indexOf("\",", idStartIndex);
				if (idStartIndex != -1 && idEndIndex != -1) {
					return jsonResponse.substring(idStartIndex + "\"id\":\"".length(), idEndIndex);
				}
			}
		}

		// Return null if the field does not exist
		return null;
	}

	public String createField(String fieldName) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBasicAuth(jira_username, jira_token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		String jiraTasksEndpoint = customField_creation_url + "/field";
		String fieldJson = String.format("{\"name\":\"%s\", \"description\":\"%s\", \"type\":\"%s\"}", fieldName,
				"A custom field for Ai Estimate", "com.atlassian.jira.plugin.system.customfieldtypes:textfield");
		ResponseEntity<String> createResponseEntity = restTemplate.exchange(jiraTasksEndpoint, HttpMethod.POST,
				new HttpEntity<>(fieldJson, headers), String.class);
		String jsonResponse = createResponseEntity.getBody();
		int idStartIndex = jsonResponse.indexOf("\"id\":\"");
		int idEndIndex = jsonResponse.indexOf("\",", idStartIndex);
		if (idStartIndex != -1 && idEndIndex != -1) {
			return jsonResponse.substring(idStartIndex + "\"id\":\"".length(), idEndIndex);
		}

		// Return null if the field does not exist, and creation failed
		return null;
	}

	public List<String> assignScreensToCustomFields() {

		try {
			// Step 1: Get custom field ID
			String customFieldInJira = getFieldId(field_Name);
			if (customFieldInJira == null) {
				customFieldInJira = createField(field_Name);
			}
			String numericCustomId = extractNumericId(customFieldInJira);
			// Step 2: Get all screens IDs
			List<String> screenIds = getAllScreenIds();

			// Step 3: Assign custom field to screens
			assignCustomFieldToScreens(numericCustomId, screenIds);

		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();
			System.err.println("Exception: " + e.getMessage());
		}

		return null;
	}

	private List<String> getAllScreenIds() {
		// Implement the logic to get all screen IDs

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(jira_username, jira_token);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		String screen_endpoint = customField_creation_url + "/screens";
		ResponseEntity<String> responseEntity = restTemplate.exchange(screen_endpoint, HttpMethod.GET, entity,
				String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
//	            String jsonResponse = responseEntity.getBody();
//	            JsonParser jsonParser = new JsonParser();
//	            JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();
			String jsonResponse = responseEntity.getBody();
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();
			JsonArray values = jsonObject.getAsJsonArray("values");

			List<String> screenIds = new ArrayList<>();

			for (int i = 0; i < values.size(); i++) {
				JsonObject projectObject = values.get(i).getAsJsonObject();
				int screenId = projectObject.get("id").getAsInt();
				String screenIdAsString = String.valueOf(screenId);
				screenIds.add(screenIdAsString);
			}

			return screenIds;
		}
		return null;
	}

	private void assignCustomFieldToScreens(String customFieldId, List<String> screenIds) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(jira_username, jira_token);

		for (String screenId : screenIds) {
			String jiraUpdateEndpoint = customField_creation_url + "/" + customFieldId + "/screens/add";
			String requestBody = "{\"screens\": [{\"id\": \"" + screenId + "\"}]}";

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

			ResponseEntity<String> responseEntity = restTemplate.exchange(jiraUpdateEndpoint, HttpMethod.PUT, entity,
					String.class);

			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				System.err.println("Failed to assign custom field to screen with ID " + screenId + ". Response: "
						+ responseEntity.getBody());
			}
		}
	}

	public void updateLabelsToJira(String taskId, List<String> newLabels) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBasicAuth(jira_username, jira_token);

			// Fetch the current labels of the issue
			List<String> existingLabels = getCurrentLabels(taskId);

			// Combine existing and new labels
			List<String> combinedLabels = combineLabels(existingLabels, newLabels);

			String jiraUpdateEndpoint = jira_update_task + taskId;

			// Create JSON payload with combined labels
			JsonObject labelsObject = new JsonObject();
			labelsObject.add("labels", new Gson().toJsonTree(combinedLabels));

			JsonObject fieldsObject = new JsonObject();
			fieldsObject.add("fields", labelsObject);

			HttpEntity<String> entity = new HttpEntity<>(fieldsObject.toString(), headers);

			// Send the request to update labels
			ResponseEntity<String> responseEntity = restTemplate.exchange(jiraUpdateEndpoint, HttpMethod.PUT, entity,
					String.class);

			// Check the response status
			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				System.out.println("Labels updated successfully for issue ID " + taskId);
			} else {
				System.err.println(
						"Failed to update labels for issue ID " + taskId + ". Response: " + responseEntity.getBody());
			}
		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();
			System.err.println("Exception while updating labels for issue ID " + taskId + ": " + e.getMessage());
		}
	}

	// Fetches the current labels of the JIRA issue
	private List<String> getCurrentLabels(String taskId) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(jira_username, jira_token);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		String jiraGetIssueEndpoint = jira_update_task + taskId;

		ResponseEntity<String> responseEntity = restTemplate.exchange(jiraGetIssueEndpoint, HttpMethod.GET, entity,
				String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			String responseBody = responseEntity.getBody();
			if (responseBody != null) {
				// Parse the JSON string
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(responseBody);

				// Check if the "fields" key exists
				if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("fields")) {
					JsonObject fields = jsonElement.getAsJsonObject().getAsJsonObject("fields");

					// Check if the "labels" key exists within "fields"
					if (fields.has("labels")) {
						JsonArray labelsArray = fields.getAsJsonArray("labels");
						List<String> currentLabels = new ArrayList<>();
						for (JsonElement labelElement : labelsArray) {
							currentLabels.add(labelElement.getAsString());
						}
						return currentLabels;
					} else {
						System.err.println("No 'labels' key in the 'fields' object.");
					}
				} else {
					System.err.println("No 'fields' key in the response.");
				}
			} else {
				System.err.println("Response body is null.");
			}
		} else {
			System.err.println("Non-successful response: " + responseEntity.getStatusCodeValue());
		}

		return Collections.emptyList();
	}

	// Combines existing and new labels without duplicates
	private List<String> combineLabels(List<String> existingLabels, List<String> newLabels) {
		List<String> combinedLabels = new ArrayList<>(existingLabels);
		combinedLabels.addAll(newLabels);
		return new ArrayList<>(new LinkedHashSet<>(combinedLabels));
	}

	private String extractNumericId(String customFieldInJira) {
		// Extract numeric part from custom field ID
		// Assuming the custom field ID format is "customfield_<numeric_id>"
		String[] parts = customFieldInJira.split("_");
		if (parts.length == 2) {
			return parts[1];
		} else {
			// Handle invalid format gracefully
			throw new IllegalArgumentException("Invalid custom field ID format: " + customFieldInJira);
		}
	}

}
