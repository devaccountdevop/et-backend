package com.es.controller;

import java.time.LocalDateTime;
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

import com.es.dto.AiTaskEstimateRequestDto;
import com.es.response.AiEstimatesResponse;
import com.es.service.AiEstimatesService;
import com.es.validators.AiEstimateValidator;

@RestController
@RequestMapping("/estimation-tool")
public class AiEstimatesController {

	@Autowired
	AiEstimatesService aiEstimatesService;

	@PostMapping("/aiestimates")
	public ResponseEntity<AiEstimatesResponse> aiEstimates(@RequestBody Map<String, Object> requestBody)  {
		AiEstimatesResponse response = new AiEstimatesResponse();
		try {
	        // Extract "updateTask" object from the request body
	        Map<String, Object> updateTaskObj = (Map<String, Object>) requestBody.get("value");
	        AiTaskEstimateRequestDto estimateRequestDto = new AiTaskEstimateRequestDto();
	        estimateRequestDto = aiEstimatesService.dataForAiEstimates(updateTaskObj);
	        // Validate the updateTask object
//	        List<String> errors = AiEstimateValidator.validateTaskEstimate(estimateRequestDto);
//	        if (!CollectionUtils.isEmpty(errors)) {
//	        	
//	            response.setCode(400);
//	            response.setError(true);
//	            response.setMessage(String.join(",", errors));
//	            response.setTimestamp(LocalDateTime.now());
//	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//	        }
//	       
	        // TODO: Call AI API
	        AiEstimatesResponse aiResponse = aiEstimatesService.getAiEstimates(estimateRequestDto);

	        // TODO: SAVE request In DB
	       aiEstimatesService.saveCustomFields(updateTaskObj);

	        return new ResponseEntity<>(aiResponse, HttpStatus.OK);

	    } catch (Exception e) {
	        // Handle exceptions
	        response.setCode(500);
	        response.setError(true);
	        response.setMessage("Internal Server Error");
	        response.setTimestamp(LocalDateTime.now());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PostMapping("/mockApi")
	public ResponseEntity<Map> mockAiAPI(@RequestBody AiTaskEstimateRequestDto request) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("aiEstimate", 1.55);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("data", data);
		response.put("code", 200);
		response.put("error", false);
		//response.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<Map>(response, HttpStatus.OK);
	}
}
