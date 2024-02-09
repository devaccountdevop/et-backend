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
 
        if (!StringUtils.hasText(request.getMost_likely_estimate())) {
            errorMessage.add("Invalid Request: missing request field MostLikely");
        }
 
        if (!StringUtils.hasText(request.getOptimistic_estimate())) {
            errorMessage.add("Invalid Request: missing request field Optimistic");
        }
 
        if (!StringUtils.hasText(request.getPessimistic_estimate())) {
            errorMessage.add("Invalid Request: missing request field Pessimistic");
        }
 
        if (!StringUtils.hasText(request.getPlanned_estimate())) {
            errorMessage.add("Invalid Request: missing request field Original Estimates");
        }
 
        if (!StringUtils.hasText(request.getSprint_number())) {
            errorMessage.add("Invalid Request: missing request field Sprint Number");
        }
 
        if (!StringUtils.hasText(request.getTask_descrption())) {
            errorMessage.add("Invalid Request: missing request field Task Description");
        }
 
        if (!StringUtils.hasText(request.getTask_name())) {
            errorMessage.add("Invalid Request: missing request field Task Lable");
        }
 
        if (!StringUtils.hasText(request.getPriority())) {
            errorMessage.add("Invalid Request: missing request field Task Priority");
        }
        if (!StringUtils.hasText(request.getTask_label())) {
            errorMessage.add("Invalid Request: missing request field Task Label");
        }
 
     
 
        return errorMessage;
    }
 
}