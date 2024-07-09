package com.es.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.AiResponseDto;
import com.es.dto.AiTaskEstimateRequestDto;
import com.es.entity.ImportTask;
import com.es.response.AiEstimatesResponse;
import com.es.service.AiEstimatesService;
import com.es.service.ImportTaskService;
import com.es.validators.AiEstimateValidator;

@RestController
@RequestMapping("/estimation-tool")
public class AiEstimatesController {

	@Autowired
	AiEstimatesService aiEstimatesService;
	
	@Autowired 
	ImportTaskService importTaskService;

	@PostMapping("/aiestimates")
	public AiEstimatesResponse aiEstimates(@RequestBody Map<String, Object> requestBody) {
	    AiEstimatesResponse response = new AiEstimatesResponse();
	    try {
	        // Extract "value" from the request body
	        Object value = requestBody.get("value");
	       
	        List<AiTaskEstimateRequestDto> estimateRequestDtoList = new ArrayList<>();

	        // Check if "value" is a list or a single object
	        if (value instanceof List) {
	            List<Map<String, Object>> updateTaskList = (List<Map<String, Object>>) value;
	                         
	            estimateRequestDtoList.addAll(aiEstimatesService.dataForAiEstimates(updateTaskList));
	        } else if (value instanceof Map) {
	            Map<String, Object> updateTaskObj = (Map<String, Object>) value;
	            estimateRequestDtoList.addAll(aiEstimatesService.dataForAiEstimates(Collections.singletonList(updateTaskObj)));
	        } else {
	            response.setCode(400);
	            response.setError(true);
	            response.setMessage("Invalid 'value' type");
	            response.setTimestamp(LocalDateTime.now());
	            return response;
	        }
              int sprintId = 0;
	        AiTaskEstimateRequestDto aiTaskEstimateRequestDto  = estimateRequestDtoList.get(0);
	        sprintId = Integer.parseInt( aiTaskEstimateRequestDto.getSprint_number());
	        
	        
	       

	        // Call AI API once for the entire list
	        List<AiResponseDto> aiResponseList = aiEstimatesService.getAiEstimates(estimateRequestDtoList);

	     
	        

	           List<ImportTask> updateTaskObj =  aiEstimatesService.saveAiResponse( aiResponseList,sprintId );

	            // Save custom fields (assuming updateTaskObj is available and corresponds to the task)
	            aiEstimatesService.saveCustomFields(updateTaskObj);  // This might need to be adjusted to match each task
	        
response.setData(updateTaskObj);
	        response.setCode(200);
	        response.setMessage("success");
	        return response;

	    } catch (Exception e) {
	        // Handle exceptions
	        response.setCode(500);
	        response.setError(true);
	        response.setMessage("Internal Server Error");
	        response.setTimestamp(LocalDateTime.now());
	        return response;
	    }
	}

		
	   
	@PostMapping("/mockApi")
	public ResponseEntity<Map> mockAiAPI(@RequestBody AiTaskEstimateRequestDto request) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("aifakeEstimate", 1.55);
		data.put("threePointEstimate", 4.3);
		data.put("riskFactor", .45);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("data", data);
		response.put("code", 200);
		response.put("error", false);
		//response.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<Map>(response, HttpStatus.OK);
	}
}
