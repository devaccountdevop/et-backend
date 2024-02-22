package com.es.controller;

import com.es.dto.ProjectInfoDto;
import com.es.dto.SprintInfoDto;
import com.es.entity.EstimationMaster;
import com.es.entity.ImportSprint;
import com.es.response.GetProjectResponse;
import com.es.response.GetSprintResponse;
import com.es.service.EstimationMasterService;
import com.es.service.ImportSprintService;
import com.es.service.JIRARestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/estimation-tool/")
public class SprintController {

	@Autowired
	JIRARestService jIRARestService;

	@Autowired
	ImportSprintService importSprintService;

	@GetMapping("getAllSprints/{projectId}")
	public GetSprintResponse getAllSprints(@PathVariable String projectId) {
		GetSprintResponse response = new GetSprintResponse();

		List<SprintInfoDto> list = importSprintService.getAllSprintByProjectId(Integer.parseInt(projectId));

		if (!list.isEmpty()) {
			response.setCode(200);
			response.setMessage("success");
			response.setData(list);
		} else {
			response.setCode(404);
			response.setMessage("invalid");
		}

		return response;
	}

}
