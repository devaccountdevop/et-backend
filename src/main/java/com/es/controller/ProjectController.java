package com.es.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.dto.ProjectInfoDto;
import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.entity.Signup;
import com.es.response.ExceptionEnum;
import com.es.response.GetProjectResponse;
import com.es.response.SuccessEnum;
import com.es.service.ClientCredentialsService;
import com.es.service.ImportClientService;
import com.es.service.ImportProjectsService;
import com.es.service.JIRARestService;
import com.es.service.SignupService;

@RestController
@RequestMapping("/estimation-tool/")
public class ProjectController {
    @Autowired
    JIRARestService jIRARestService;
    
    @Autowired
    ClientCredentialsService clientCredentialsService;
    
    @Autowired
    ImportProjectsService importProjectsService; 
    
    @Autowired
    SignupService signupService;

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

    @GetMapping("getAllProjects/{clientId}")
    public GetProjectResponse getAllProjects(@PathVariable String clientId) {
        GetProjectResponse response = new GetProjectResponse();
        ArrayList<ProjectInfoDto> list = new ArrayList<>();

        try {
        	ClientCredentials clientCredentials = new ClientCredentials();
        	 clientCredentials = clientCredentialsService.getClientCredentials(Integer.parseInt(clientId));
        	if(clientCredentials != null) {
            list.addAll(importProjectsService.getProjectsByJiraUserName(clientCredentials.getJiraUserName(), clientCredentials.getUserId()));
        	}
            if (list != null && !list.isEmpty()) {
                response.setCode(200); // Use HttpStatus constants
                response.setMessage("success");
                response.setData(list);
            } else {
                response.setCode(404); // Use HttpStatus constants
                response.setMessage("invalid");
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., log the error)
            e.printStackTrace(); // Log the exception or handle it based on your requirements.
            response.setCode(500); // Use HttpStatus constants
            response.setMessage("error");
        }

        return response;
    }
    @GetMapping("getimportprojects/{userId}")
    public GetProjectResponse getAllProjectsByUserId(@PathVariable String userId) {
        GetProjectResponse response = new GetProjectResponse();
        List<ProjectInfoDto> projectList = new ArrayList<>();
        
        try {
            int userIdInt = Integer.parseInt(userId);
            Signup user = signupService.getUserById(userIdInt);
            
            if (user != null) {
                projectList.addAll(importProjectsService.getProjectsByUserId(user.getId()));
                response.setCode(SuccessEnum.SUCCESS_TYPE.getCode());
                response.setMessage(SuccessEnum.SUCCESS_TYPE.getMessage());
                response.setData(projectList);
            } else {
                response.setCode(ExceptionEnum.USER_NOT_EXIST.getErrorCode());
                response.setMessage(ExceptionEnum.USER_NOT_EXIST.getMessage());
            }
        } catch (NumberFormatException e) {
            response.setCode(ExceptionEnum.USER_NOT_EXIST.getErrorCode());
            response.setMessage("Invalid user ID format.");
        } catch (Exception e) {
            response.setCode(ExceptionEnum.UNIVERSAL_ERROR.getErrorCode());
            response.setMessage("An error occurred while fetching the projects.");
            e.printStackTrace();
        }

        return response;
    }


}
