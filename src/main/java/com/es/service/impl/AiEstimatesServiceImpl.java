package com.es.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import javax.transaction.InvalidTransactionException;

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

import com.es.dto.AiTaskEstimateRequestDto;
import com.es.entity.TaskEstimates;
import com.es.exceptions.AiApiRequestException;
import com.es.response.AiEstimatesResponse;
import com.es.service.AiEstimatesService;
import com.es.service.EstimatesService;

@Service
public class AiEstimatesServiceImpl implements AiEstimatesService {
	
	@Value("${ai.url}")
	private String aiUrl;
	
	@Autowired
	private EstimatesService estimateService;

	@Override
	public AiEstimatesResponse getAiEstimates(AiTaskEstimateRequestDto request) throws Exception {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<AiTaskEstimateRequestDto> entity = new HttpEntity<>(request, headers);
		ResponseEntity<AiEstimatesResponse> responseEntity = restTemplate.exchange(aiUrl, HttpMethod.POST, entity,
				AiEstimatesResponse.class);
		if(responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new AiApiRequestException(Arrays.asList(responseEntity.getBody()).toArray(), responseEntity.getStatusCodeValue(), "Error Calling AI Api");
		}
		
		return responseEntity.getBody();
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveCustomFields(Map<String,Object> object) throws Exception {
		
		TaskEstimates taskEstimates = new TaskEstimates();
		
		 Map<String, Object> estimatesObj = (Map<String, Object>) object.get("estimates");
		 taskEstimates.setTaskId((String) object.get("taskId"));
		 taskEstimates.setId(estimatesObj.get("id") != null ? Integer.parseInt(estimatesObj.get("id").toString()) : 0); 
		 taskEstimates.setRealistic (estimatesObj.get("realistic") != null ? Integer.parseInt(estimatesObj.get("realistic").toString()) : 0); 
		 taskEstimates.setLow(estimatesObj.get("low") != null ? Integer.parseInt(estimatesObj.get("low").toString()) : 0); 
		 taskEstimates.setHigh(estimatesObj.get("high") != null ? Integer.parseInt(estimatesObj.get("high").toString()) : 0); 
		TaskEstimates dbTaskEstimates = estimateService.getEstimatesById(taskEstimates.getId());
		if( dbTaskEstimates !=null) {
			dbTaskEstimates.setHigh(taskEstimates.getHigh());
			dbTaskEstimates.setLow(taskEstimates.getLow());
			dbTaskEstimates.setRealistic(taskEstimates.getRealistic());
			TaskEstimates saveTaskEstimates =estimateService.saveTaskEstimates(dbTaskEstimates);
		}
		
	}
	
	public AiTaskEstimateRequestDto dataForAiEstimates(Map<String,Object> object) {
		AiTaskEstimateRequestDto aiTaskEstimateRequestDto = new AiTaskEstimateRequestDto();
		aiTaskEstimateRequestDto.setTaskId((String) object.get("taskId"));
		aiTaskEstimateRequestDto.setTaskDescription((String) object.get("taskDescription"));
		aiTaskEstimateRequestDto.setTaskName((String) object.get("summary"));  
		aiTaskEstimateRequestDto.setSprintNumber((Integer) object.get("sprintId")); 
		aiTaskEstimateRequestDto.setTaskLabel((String) object.get("lables"));
		aiTaskEstimateRequestDto.setTaskPriority((String) object.get("taskPriority"));
		aiTaskEstimateRequestDto.setStoryPoints((String) object.get("taskPriority"));
		aiTaskEstimateRequestDto.setOriginalEstimates((String) object.get("taskPriority"));
		 Map<String, Object> estimatesObj = (Map<String, Object>) object.get("estimates");
		 aiTaskEstimateRequestDto.setTaskId((String) object.get("taskId"));
		 aiTaskEstimateRequestDto.setMostLikely(estimatesObj.get("realistic") != null ? Integer.parseInt(estimatesObj.get("realistic").toString()) : 0); 
		 aiTaskEstimateRequestDto.setOptimistic(estimatesObj.get("low") != null ? Integer.parseInt(estimatesObj.get("low").toString()) : 0); 
		 aiTaskEstimateRequestDto.setPessimistic(estimatesObj.get("high") != null ? Integer.parseInt(estimatesObj.get("high").toString()) : 0); 
		
		
		return aiTaskEstimateRequestDto;
		
	}
}
