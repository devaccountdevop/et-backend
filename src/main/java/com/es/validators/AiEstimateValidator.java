package com.es.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.es.dto.AiTaskEstimateRequestDto;

public class AiEstimateValidator {

	public static List<String> validateTaskEstimate(AiTaskEstimateRequestDto request) {
		List<String> errorMessage = new ArrayList<>();
		if (null == request) {
			errorMessage.add("Invalid Request");
		}

		if (null == request.getMostLikely()) {
			errorMessage.add("Invalid Request: missing request field MostLikely");
		}

		if (null == request.getOptimistic()) {
			errorMessage.add("Invalid Request: missing request field Optimistic");
		}

		if (null == request.getPessimistic()) {
			errorMessage.add("Invalid Request: missing request field Pessimistic");
		}
		
		if (!StringUtils.hasLength(request.getOriginalEstimates())) {
			errorMessage.add("Invalid Request: missing request field Original Estimates");
		}

		if (request.getSprintNumber() == null || request.getSprintNumber() <= 0) {
		    errorMessage.add("Invalid Request: Sprint Number must be a positive integer");
		}

		if (!StringUtils.hasLength(request.getStoryPoints())) {
			errorMessage.add("Invalid Request: missing request field Story Points");
		}

		if (!StringUtils.hasLength(request.getTaskDescription())) {
			errorMessage.add("Invalid Request: missing request field Task Description");
		}

		if (!StringUtils.hasLength(request.getTaskName())) {
			errorMessage.add("Invalid Request: missing request field Task Lable");
		}

		if (!StringUtils.hasLength(request.getTaskPriority())) {
			errorMessage.add("Invalid Request: missing request field Task Priority");
		}
		
		if (!StringUtils.hasLength(request.getTaskId())) {
			errorMessage.add("Invalid Request: missing request field Task Id");
		}

		return errorMessage;
	}

}
