package com.es.controller;

	import java.util.ArrayList;
	 
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.RestController;
	 
	import com.es.dto.SprintListPageDto;
	import com.es.response.GetSprintResponse;
	import com.es.response.SprintListPageResponse;
	import com.es.service.JIRARestService;
	 
	@RestController
	public class SprintListPageController {
		    @Autowired
		    JIRARestService jIRARestService;
		    @GetMapping("getAllTasks/{sprintId}")
		    public SprintListPageResponse getAllSprints(@PathVariable String sprintId) { 
		    	SprintListPageResponse response = new SprintListPageResponse();
		    	 ArrayList<SprintListPageDto> taskList = new ArrayList<>();
		         taskList.addAll(jIRARestService.getAllTasksBySprintId1(Integer.parseInt(sprintId)));
	 
		    	if(taskList != null) {
		    		response.setCode(200);
		    		response.setMessage("success");
		    		response.setData(taskList);
		    		return response;
		    	}else {
		    		response.setCode(404);
		    		response.setMessage("invalid");
		    		return response;
		    	}
	 
		    }
		    
//		    @GetMapping("/getAllTasks/{id}")
//		    public String getTask(@PathVariable String id) {
//		    	
//		    	return jIRARestService.getAllTasksBySprintId(Integer.parseInt(id));
//		    }
	 
	}
