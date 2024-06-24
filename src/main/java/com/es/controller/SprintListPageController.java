package com.es.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.SprintListPageDto;
import com.es.entity.ImportTask;
import com.es.response.ExceptionEnum;
import com.es.response.GetSprintResponse;
import com.es.response.SprintListPageResponse;
import com.es.response.SuccessEnum;
import com.es.service.ImportTaskService;
import com.es.service.JIRARestService;

@RestController
@RequestMapping("/estimation-tool/")
public class SprintListPageController {
	@Autowired
	ImportTaskService importTaskService;

	@GetMapping("getAllTasks/{sprintId}/{projectId}")
	public SprintListPageResponse getAllSprints(@PathVariable String sprintId, @PathVariable String projectId) {
		SprintListPageResponse response = new SprintListPageResponse();
		ArrayList<ImportTask> taskList = new ArrayList<>();
		taskList.addAll(importTaskService.getAllTaskBySprintId(Integer.parseInt(sprintId)));

		if (taskList != null) {
			response.setCode(200);
			response.setMessage("success");
			response.setData(taskList);
			return response;
		} else {
			response.setCode(404);
			response.setMessage("invalid");
			return response;
		}

	}
	
	@GetMapping("getprojectbacklog/{projectId}")
	public SprintListPageResponse getAllBacklogTask(@PathVariable String projectId) {
		SprintListPageResponse  response = new SprintListPageResponse();
		if(projectId == null ) {
			return response;
		}
		List<ImportTask> backlogTask = this.importTaskService.getAllBacklogTask(Integer.parseInt(projectId));
		if(backlogTask.isEmpty()) {
			response.setCode(ExceptionEnum.DATA_NOT_FOUND.getErrorCode());
			response.setMessage(ExceptionEnum.DATA_NOT_FOUND.getMessage());
			response.setData(backlogTask);
			return response;
		}
		response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
		response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
		response.setData(backlogTask);
		return response;
		
	}

}
