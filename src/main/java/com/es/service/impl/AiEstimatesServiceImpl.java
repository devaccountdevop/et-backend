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

import com.es.dto.AiResponseDto;
import com.es.dto.AiTaskEstimateRequestDto;
import com.es.entity.ImportTask;
import com.es.entity.TaskEstimates;
import com.es.exceptions.AiApiRequestException;
import com.es.response.AiEstimatesResponse;
import com.es.service.AiEstimatesService;
import com.es.service.EstimatesService;
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

	@Autowired
	private EstimatesService estimateService;

	@Override
	public AiResponseDto getAiEstimates(AiTaskEstimateRequestDto request) throws Exception {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<AiTaskEstimateRequestDto> entity = new HttpEntity<>(request, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(aiUrl, HttpMethod.POST, entity, String.class);
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new AiApiRequestException(Arrays.asList(responseEntity.getBody()).toArray(),
					responseEntity.getStatusCodeValue(), "Error Calling AI Api");
		}

		String jsonResponse = responseEntity.getBody();
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();
		JsonObject dataJsonObject = jsonObject.getAsJsonObject("data");
		JsonElement aiEstimateValue = dataJsonObject.get(aiEstimate);
		String aiEstimate = (aiEstimateValue != null && !aiEstimateValue.isJsonNull()) ? aiEstimateValue.getAsString()
				: null;
		JsonElement threePointEstimateValue = dataJsonObject.get(threePointEstimate);
		String threePointEstimate = (threePointEstimateValue != null && !threePointEstimateValue.isJsonNull())
				? threePointEstimateValue.getAsString()
				: null;
		JsonElement riskFactorValue = dataJsonObject.get(riskFactor);
		String riskFactor = (riskFactorValue != null && !riskFactorValue.isJsonNull()) ? riskFactorValue.getAsString()
				: null;
		AiResponseDto responseDto = new AiResponseDto();
		responseDto.setAiEstimate(aiEstimate);
		responseDto.setRiskFactor(riskFactor);
		responseDto.setThreePointEstimate(threePointEstimate);
		return responseDto;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveCustomFields(Map<String, Object> object) throws Exception {

		TaskEstimates taskEstimates = new TaskEstimates();

		Map<String, Object> estimatesObj = (Map<String, Object>) object.get("estimates");
		taskEstimates.setTaskId((String) object.get("taskId"));
		taskEstimates.setId(estimatesObj.get("id") != null ? Integer.parseInt(estimatesObj.get("id").toString()) : 0);
		taskEstimates.setRealistic(
				estimatesObj.get("realistic") != null ? Integer.parseInt(estimatesObj.get("realistic").toString()) : 0);
		taskEstimates
				.setLow(estimatesObj.get("low") != null ? Integer.parseInt(estimatesObj.get("low").toString()) : 0);
		taskEstimates
				.setHigh(estimatesObj.get("high") != null ? Integer.parseInt(estimatesObj.get("high").toString()) : 0);
		TaskEstimates dbTaskEstimates = estimateService.getEstimatesById(taskEstimates.getId());
		if (dbTaskEstimates != null) {
			dbTaskEstimates.setHigh(taskEstimates.getHigh());
			dbTaskEstimates.setLow(taskEstimates.getLow());
			dbTaskEstimates.setRealistic(taskEstimates.getRealistic());
			TaskEstimates saveTaskEstimates = estimateService.saveTaskEstimates(dbTaskEstimates);
		}

	}

	public AiTaskEstimateRequestDto dataForAiEstimates(Map<String, Object> object) {
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
		aiTaskEstimateRequestDto.setMostLikely(
				estimatesObj.get("realistic") != null ? Integer.parseInt(estimatesObj.get("realistic").toString()) : 0);
		aiTaskEstimateRequestDto.setOptimistic(
				estimatesObj.get("low") != null ? Integer.parseInt(estimatesObj.get("low").toString()) : 0);
		aiTaskEstimateRequestDto.setPessimistic(
				estimatesObj.get("high") != null ? Integer.parseInt(estimatesObj.get("high").toString()) : 0);

		return aiTaskEstimateRequestDto;

	}

	public ImportTask saveAiResponse(ImportTask importTask, AiResponseDto aiResponseDto) {

		importTask.setAiEstimate(aiResponseDto.getAiEstimate());

		importTask.setRiskFactor(aiResponseDto.getRiskFactor());
		importTask.setThreePointEstimate(aiResponseDto.getThreePointEstimate());
		return importTask;

	}

}
