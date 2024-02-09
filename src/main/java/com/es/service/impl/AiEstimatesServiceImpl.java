package com.es.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import com.es.service.AiEstimatesService;
import com.es.service.EstimatesService;
import com.google.gson.Gson;
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
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(request), headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(aiUrl, HttpMethod.POST, entity, String.class);
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new AiApiRequestException(Arrays.asList(responseEntity.getBody()).toArray(),
					responseEntity.getStatusCodeValue(), "Error Calling AI Api");
		}

		String jsonResponse = responseEntity.getBody();
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

		JsonElement aiEstimateValue = jsonObject.get(aiEstimate);
		String aiEstimate = (aiEstimateValue != null && !aiEstimateValue.isJsonNull()) ? aiEstimateValue.getAsString()
				: null;
		JsonElement threePointEstimateValue = jsonObject.get(threePointEstimate);
		String threePointEstimate = (threePointEstimateValue != null && !threePointEstimateValue.isJsonNull())
				? threePointEstimateValue.getAsString()
				: null;
		JsonElement riskFactorValue = jsonObject.get(riskFactor);
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

		return aiTaskEstimateRequestDto;
	}

	public ImportTask saveAiResponse(ImportTask importTask, AiResponseDto aiResponseDto) {

		importTask.setAiEstimate(aiResponseDto.getAiEstimate());

		importTask.setRiskFactor(aiResponseDto.getRiskFactor());
		importTask.setThreePointEstimate(aiResponseDto.getThreePointEstimate());
		return importTask;

	}

}
