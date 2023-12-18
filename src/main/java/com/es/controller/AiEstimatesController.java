package com.es.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.TaskAiEstimatesDto;
import com.es.entity.ImportTask;
import com.es.entity.TaskEstimates;
import com.es.response.AiEstimatesResponse;
import com.es.response.TaskEstimatesResponse;
import com.es.service.AiEstimatesService;

@RestController
@RequestMapping("/estimation-tool")
public class AiEstimatesController {
	
	@Autowired
	AiEstimatesService aiEstimatesService;
	
	@PostMapping("/aiestimates")
	public AiEstimatesResponse aiEstimates(@RequestBody Map<String, Object> requestBody) {
	    AiEstimatesResponse response = new AiEstimatesResponse();

	    try {
	        // Extract "updateTask" object from the request body
	        Map<String, Object> updateTaskObj = (Map<String, Object>) requestBody.get("value");
	        Map<String, Object> estimatesObj = (Map<String, Object>) updateTaskObj.get("estimates");
	        if (updateTaskObj != null) {
	            // Create a TaskAiEstimatesDto object and set values
	            ImportTask tasks = new ImportTask();
	            TaskEstimates taskEstimates = new TaskEstimates();

	            // Extract values from the map and handle null values appropriately
	            tasks.setTaskId(updateTaskObj.get("taskId") != null ? updateTaskObj.get("taskId").toString() : "");
	            taskEstimates.setLow(estimatesObj.get("low") != null ? Integer.parseInt(estimatesObj.get("low").toString()) : 0);
	            tasks.setTaskDescription(updateTaskObj.get("taskDescription") != null ? updateTaskObj.get("taskDescription").toString() : "");
	            tasks.setSummary(updateTaskObj.get("summary") != null ? updateTaskObj.get("summary").toString() : "");
	            taskEstimates.setHigh(estimatesObj.get("high") != null ? Integer.parseInt(estimatesObj.get("high").toString()) : 0);
	            taskEstimates.setRealistic(estimatesObj.get("realistic") != null ? Integer.parseInt(estimatesObj.get("realistic").toString()) : 0);
	            tasks.setThreePointEstimate(updateTaskObj.get("threePointEstimate") != null ? Integer.parseInt(updateTaskObj.get("threePointEstimate").toString()) : 0);
	            
	            tasks.setEstimates(taskEstimates);

	            // Check if taskEstimates is not null
	            if (tasks != null) {
	                double weightedAverage = aiEstimatesService.weightedAverage(tasks.getEstimates().getLow(), tasks.getEstimates().getHigh(), tasks.getEstimates().getRealistic());
	                double standardDeviation = aiEstimatesService.standardDeviation(tasks.getEstimates().getLow(), tasks.getEstimates().getHigh());

	                // Validate the values and perform further checks
	                if (weightedAverage != 0 && standardDeviation != 0) {
	                    double randomValue;
	                    
	                    // Generate random values until a non-zero value is obtained
	                    do {
	                        randomValue = Math.random() * 10;
	                    } while (randomValue == 0);

	                    // Convert to int if needed
	                    int intValue = (int) randomValue;
	                    // Create a response data object if needed
	                   
	                    tasks.setAiEstimate(intValue);
	                    response.setCode(200);
	                    response.setData(tasks);
	                    return response;
	                } else {
	                    response.setCode(404);
	                    response.setMessage("weightedAverage or standardDeviation is null");
	                    return response;
	                }
	            } else {
	                response.setCode(404);
	                response.setMessage("all fields required");
	                return response;
	            }
	        } else {
	            response.setCode(404);
	            response.setMessage("updateTask is null");
	            return response;
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // Log the exception details for debugging
	        response.setCode(500);
	        response.setMessage("something went wrong: " + e.getMessage());
	        return response;
	    }
	}



}

