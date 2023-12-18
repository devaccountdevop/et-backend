package com.es.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.SprintListPageDto;
import com.es.response.BackLogTaskResponse;
import com.es.response.ExceptionEnum;
import com.es.response.SuccessEnum;
import com.es.service.JIRARestService;

@RestController
@RequestMapping("/estimation-tool")
public class BacklogTasksController {

	@Autowired
	JIRARestService jiraRestService;
	
	@GetMapping("/backlogtasks/{projectId}")
	public BackLogTaskResponse getBacklogTasks(@PathVariable String projectId) {
		BackLogTaskResponse response = new BackLogTaskResponse();
		if(projectId != null) {
			
			List<SprintListPageDto> tasklist = new ArrayList<>();
			    tasklist.addAll(jiraRestService.getAllBacklogTasks(Integer.parseInt(projectId)));
			response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
			response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
			response.setData(tasklist);
			return response;
			
		}else {
			response.setCode(ExceptionEnum.INVALID_PARAMETER.getErrorCode());
			response.setMessage(ExceptionEnum.INVALID_PARAMETER.getMessage());
			return response;
		}
		
	}
	
}
