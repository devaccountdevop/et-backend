package com.es.service;

import java.util.List;
import java.util.Map;

import com.es.dto.AiResponseDto;
import com.es.dto.AiTaskEstimateRequestDto;
import com.es.entity.ImportTask;
import com.es.response.AiEstimatesResponse;

public interface AiEstimatesService {

	List<AiResponseDto> getAiEstimates(List<AiTaskEstimateRequestDto> request) throws Exception;

	void saveCustomFields(List<ImportTask> importtasks) throws Exception;
	 List<AiTaskEstimateRequestDto> dataForAiEstimates(List<Map<String,Object>> object);
	 List<ImportTask> saveAiResponse(List<AiResponseDto> aiResponseDto, int sprintId);
}
