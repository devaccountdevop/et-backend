package com.es.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.entity.Estimates;
import com.es.response.EstimatesResponse;
import com.es.response.ExceptionEnum;
import com.es.response.SuccessEnum;
import com.es.service.EstimatesService;
import com.es.service.JIRARestService;

@RestController
@RequestMapping("/estimation-tool")
public class EstimatesController {
	@Autowired
	EstimatesService estimatesService;
	@Autowired
	JIRARestService jiraRestService;

	@PostMapping("/saveestimates")
	public EstimatesResponse saveEstimates(@RequestBody Map<String, Object> requestBody, Model model) {
		EstimatesResponse response = new EstimatesResponse();

		Object updateTaskObj = requestBody.get("updateTask");

		if (updateTaskObj instanceof List) {
			Estimates estimates = new Estimates();
			List<Map<String, Object>> updateTaskList = (List<Map<String, Object>>) updateTaskObj;
			for (Map<String, Object> task : updateTaskList) {
				String taskId = (String) task.get("id");
				String aiEstimates = (String) task.get("aiEstimate");
				estimates.setTaskId(taskId);
				estimates.setTaskId(aiEstimates);

				estimatesService.saveEstimates(estimates);
				this.jiraRestService.updateToJIRA(taskId, aiEstimates);

				if (estimates == null) {
					response.setCode(ExceptionEnum.INVALID_AUTH_USER.getErrorCode());
					response.setMessage(ExceptionEnum.INVALID_AUTH_USER.getMessage());
					return response;
				}
			}
			response.setData(estimates);
			response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
			response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
			return response;
		} else {
			// Handle if it's an unexpected type
			response.setCode(ExceptionEnum.FAILED_TYPE.getErrorCode());
			response.setMessage("Invalid updateTask format in the request");
			return response;
		}
	}

}
