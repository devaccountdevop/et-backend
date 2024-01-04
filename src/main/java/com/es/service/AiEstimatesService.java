package com.es.service;

import java.util.Map;

import com.es.dto.AiTaskEstimateRequestDto;
import com.es.response.AiEstimatesResponse;

public interface AiEstimatesService {

	AiEstimatesResponse getAiEstimates(AiTaskEstimateRequestDto request) throws Exception;

	void saveCustomFields(Map<String,Object> object) throws Exception;
	 AiTaskEstimateRequestDto dataForAiEstimates(Map<String,Object> object);
}
