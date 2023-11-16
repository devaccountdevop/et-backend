package com.es.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.entity.Estimates;
import com.es.response.EstimatesResponse;
import com.es.response.ExceptionEnum;
import com.es.response.SuccessEnum;
import com.es.service.EstimatesService;
import com.es.service.JIRARestService;

@RestController
public class EstimatesController {
	@Autowired
	EstimatesService estimatesService;
	@Autowired
	JIRARestService jiraRestService;

	@PostMapping("/saveestimates")
	public EstimatesResponse saveEstimates(HttpServletRequest request, Model model) {
		EstimatesResponse response = new EstimatesResponse();
		String low = request.getParameter("low");
		String realistic = request.getParameter("realistic");
		String high = request.getParameter("high");
		String taskId = request.getParameter("taskId");
		String aiEstimates = request.getParameter("aiEstimates");
		Estimates estimates = new Estimates();
		estimates.setLow(low);
		estimates.setRealistic(realistic);
		estimates.setHigh(high);
		estimates.setTaskId(taskId);
		estimates = estimatesService.saveEstimates(estimates);
		this.jiraRestService.updateToJIRA(taskId, aiEstimates);
		if (estimates != null) {
			response.setData(estimates);
			response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
			response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
			return response;
		} else {
			response.setCode(ExceptionEnum.INVALID_AUTH_USER.getErrorCode());
			response.setMessage(ExceptionEnum.INVALID_AUTH_USER.getMessage());
			return response;
		}

	}

}
