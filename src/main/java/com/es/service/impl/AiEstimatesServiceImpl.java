package com.es.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.es.dto.AiResponseDto;
import com.es.dto.AiTaskEstimateRequestDto;
import com.es.entity.ImportTask;
import com.es.entity.TaskEstimates;
import com.es.exceptions.AiApiRequestException;
import com.es.repository.ImportTaskRepository;
import com.es.service.AiEstimatesService;
import com.es.service.EstimatesService;
import com.es.service.ImportTaskService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class AiEstimatesServiceImpl implements AiEstimatesService {

	@Value("${ai.url}")
	private String aiUrl;

	@Value("${AI.response.field.aiEstimate}")
	private String aiEstimate;

	@Value("${AI.response.field.threePointEstimate}")
	private String threePointEstimate;

	@Value("${AI.response.field.riskFactor}")
	private String riskFactor;
	
	@Value("${AI.response.field.replaced}")
	private String replaced;
	
	

	@Autowired
	private EstimatesService estimateService;
	
	@Autowired
	ImportTaskRepository importTaskRepository;

	@Override
	public List<AiResponseDto> getAiEstimates(List<AiTaskEstimateRequestDto> request) throws Exception {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(request), headers);

	    ResponseEntity<String> responseEntity = restTemplate.exchange(aiUrl, HttpMethod.POST, entity, String.class);
	    if (responseEntity.getStatusCode() != HttpStatus.OK) {
	        throw new AiApiRequestException(
	            Arrays.asList(responseEntity.getBody()).toArray(),
	            responseEntity.getStatusCodeValue(),
	            "Error Calling AI Api"
	        );
	    }

	    String jsonResponse = responseEntity.getBody();
	    JsonParser jsonParser = new JsonParser();
	    JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();

	    List<AiResponseDto> responseFromAi = new ArrayList<>();
	    for (JsonElement element : jsonArray) {
	        JsonObject jsonObject = element.getAsJsonObject();
	        
	        String taskId = jsonObject.has("task_id") && !jsonObject.get("task_id").isJsonNull() ? 
		            jsonObject.get("task_id").getAsString() : null;
	        String aiEstimate = jsonObject.has(this.aiEstimate) && !jsonObject.get(this.aiEstimate).isJsonNull() ? 
	            jsonObject.get(this.aiEstimate).getAsString() : null;
	        String threePointEstimate = jsonObject.has(this.threePointEstimate) && !jsonObject.get(this.threePointEstimate).isJsonNull() ? 
	            jsonObject.get(this.threePointEstimate).getAsString() : null;
	        String riskFactor = jsonObject.has(this.riskFactor) && !jsonObject.get(this.riskFactor).isJsonNull() ? 
	            jsonObject.get(this.riskFactor).getAsString() : null;
	        String replaced = jsonObject.has(this.replaced) && !jsonObject.get(this.replaced).isJsonNull() ? 
	            jsonObject.get(this.replaced).getAsString() : null;

	        AiResponseDto responseDto = new AiResponseDto();
	        responseDto.setTaskId(taskId);
	        responseDto.setAiEstimate(aiEstimate);
	        responseDto.setRiskFactor(riskFactor);
	        responseDto.setThreePointEstimate(threePointEstimate);
	        responseDto.setReplaced(replaced);
	        responseFromAi.add(responseDto);
	    }

	    return responseFromAi;
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveCustomFields(List<ImportTask> request) throws Exception {

		 for (ImportTask taskEstimates : request) {
		        TaskEstimates dbTaskEstimates = estimateService.getEstimatesById(taskEstimates.getEstimates().getId());
		        
		        if (dbTaskEstimates != null) {
		            dbTaskEstimates.setHigh(taskEstimates.getEstimates().getHigh());
		            dbTaskEstimates.setLow(taskEstimates.getEstimates().getLow());
		            dbTaskEstimates.setRealistic(taskEstimates.getEstimates().getRealistic());
		            estimateService.saveTaskEstimates(dbTaskEstimates);
		        }
		    }

	}

	public List<AiTaskEstimateRequestDto> dataForAiEstimates(List<Map<String, Object>> objects) {
	    List<AiTaskEstimateRequestDto> estimateRequestDtoList = new ArrayList<>();

	    for (Map<String, Object> object : objects) {
	        AiTaskEstimateRequestDto aiTaskEstimateRequestDto = new AiTaskEstimateRequestDto();
	        aiTaskEstimateRequestDto.setTask_id((String) object.get("taskId"));
	        aiTaskEstimateRequestDto.setTask_descrption((String) object.get("taskDescription"));
	        aiTaskEstimateRequestDto.setTask_name((String) object.get("summary"));
	        aiTaskEstimateRequestDto.setSprint_number(String.valueOf(object.get("sprintId")));

	        Object labelsObj = object.get("labels");
	        if (labelsObj != null && labelsObj.getClass().isArray()) {
	            String[] labelsArray = (String[]) labelsObj;
	            String labelsString = String.join(",", labelsArray);
	            aiTaskEstimateRequestDto.setTask_label(labelsString);
	        } else if (labelsObj instanceof List) {
	            List<String> labelsList = (List<String>) labelsObj;
	            String labelsString = String.join(",", labelsList);
	            aiTaskEstimateRequestDto.setTask_label(labelsString);
	        } else if (labelsObj instanceof String) {
	            aiTaskEstimateRequestDto.setTask_label((String) labelsObj);
	        } else {
	            aiTaskEstimateRequestDto.setTask_label("");
	        }

	        aiTaskEstimateRequestDto.setPriority((String) object.get("taskPriority"));
	        aiTaskEstimateRequestDto.setPlanned_estimate(String.valueOf(object.get("originalEstimate")));
	        Map<String, Object> estimatesObj = (Map<String, Object>) object.get("estimates");
	        aiTaskEstimateRequestDto.setMost_likely_estimate(
	                estimatesObj.get("realistic") != null ? String.valueOf(estimatesObj.get("realistic")) : "0");
	        aiTaskEstimateRequestDto.setOptimistic_estimate(
	                estimatesObj.get("low") != null ? String.valueOf(estimatesObj.get("low")) : "0");
	        aiTaskEstimateRequestDto.setPessimistic_estimate(
	                estimatesObj.get("high") != null ? String.valueOf(estimatesObj.get("high")) : "0");

	        estimateRequestDtoList.add(aiTaskEstimateRequestDto);
	    }

	    return estimateRequestDtoList;
	}


	public List<ImportTask> saveAiResponse(List<AiResponseDto> aiResponseDtos, int sprintId) {
	    List<ImportTask> updatedTasks = new ArrayList<>();

	    if (!aiResponseDtos.isEmpty()) {
	        // Get all task IDs from the response DTOs
	        List<String> taskIds = aiResponseDtos.stream()
	                .map(AiResponseDto::getTaskId)
	                .collect(Collectors.toList());

	        // Fetch all tasks in bulk
	        List<ImportTask> tasks = importTaskRepository.findByTaskIdInAndSprintId(taskIds, sprintId);

	        // Map tasks by taskId for easy access
	        Map<String, ImportTask> taskMap = tasks.stream()
	                .collect(Collectors.toMap(ImportTask::getTaskId, task -> task));

	        for (AiResponseDto response : aiResponseDtos) {
	            ImportTask importTask = taskMap.get(response.getTaskId());

	            if (importTask != null) {
	                // Update task fields
	                importTask.setAiEstimate(response.getAiEstimate());
	                importTask.setRiskFactor(response.getRiskFactor());
	                importTask.setThreePointEstimate(response.getThreePointEstimate());
	                importTask.setReplaced(response.getReplaced());

	                updatedTasks.add(importTask);
	            }
	        }

	        // Save all updated tasks in bulk
	        importTaskRepository.saveAll(updatedTasks);
	    }

	    return updatedTasks;
	}


}
