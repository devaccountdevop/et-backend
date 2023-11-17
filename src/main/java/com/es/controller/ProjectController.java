package com.es.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.ProjectInfoDto;
import com.es.response.GetProjectResponse;
import com.es.service.ClientCredentialsService;
import com.es.service.JIRARestService;

@RestController
@RequestMapping("/estimation-tool/")
public class ProjectController {
	@Autowired
	JIRARestService jIRARestService;

	@Autowired
	ClientCredentialsService clientCredentialsService;

	@GetMapping("/abc")
	public String getEmployee() {

		return "index";
	}

	@GetMapping("/")
	public String getLogin() {

		return "index3";
	}

	@GetMapping("getAllProjects/{id}")
	public GetProjectResponse getAllProjects(@PathVariable String id) {

		GetProjectResponse response = new GetProjectResponse();

		ArrayList<ProjectInfoDto> list = new ArrayList<>();

		list.addAll(jIRARestService.getAllProjects(Integer.parseInt(id)));

		if (list != null) {
			response.setCode(200);
			response.setMessage("success");
			response.setData(list);
			return response;
		} else {
			response.setCode(404);
			response.setMessage("invalid");
			return response;
		}

	}

}
