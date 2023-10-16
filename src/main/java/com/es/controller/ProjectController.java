package com.es.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.ProjectInfoDto;
import com.es.response.GetProjectResponse;
import com.es.service.JIRARestService;

@RestController
@RequestMapping("/")
public class ProjectController {
    @Autowired
    JIRARestService jIRARestService;

    @GetMapping("/abc")
    public String getEmployee() {

        return "index";
    }
    
    @GetMapping("/")
    public String getLogin() {

        return "index3";
    }

//    @GetMapping("/viewproject")
//    public String viewProject(Model model) {
//        model.addAttribute("getAllProjects",jIRARestService.getAllProjects());
//        String response = jIRARestService.getAllProjects();
//        System.out.println("hittttt ::: "+ response);
//        return "index";
//    }
//    
//    @GetMapping("/viewprojectL")
//    public String viewProjectL(Model model) {
//        model.addAttribute("getAllProjects",jIRARestService.getAllProjects());
//        String response = jIRARestService.getAllProjects();
//        System.out.println("LLLL ::: "+ response);
//        return "index3";
//    }

    @GetMapping("getAllProjects")
    public GetProjectResponse getAllProjects() { 
    	GetProjectResponse response = new GetProjectResponse();


    	ArrayList<ProjectInfoDto > list = new ArrayList<>();

    	list.addAll( jIRARestService.getAllProjects());

    	if(list != null) {
    		response.setCode(200);
    		response.setMessage("success");
    		response.setData(list);
    		return response;
    	}else {
    		response.setCode(404);
    		response.setMessage("invalid");
    		return response;
    	}

    }

}
