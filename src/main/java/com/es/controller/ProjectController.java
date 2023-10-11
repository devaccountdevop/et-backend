package com.es.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.es.service.JIRARestService;

@Controller
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

    @GetMapping("/viewproject")
    public String viewProject(Model model) {
        model.addAttribute("getAllProjects",jIRARestService.getAllProjects());
        String response = jIRARestService.getAllProjects();
        System.out.println("hittttt ::: "+ response);
        return "index";
    }
    
    @GetMapping("/viewprojectL")
    public String viewProjectL(Model model) {
        model.addAttribute("getAllProjects",jIRARestService.getAllProjects());
        String response = jIRARestService.getAllProjects();
        System.out.println("LLLL ::: "+ response);
        return "index3";
    }

    @GetMapping("getAllProjects")
    public String getAllProjects() {
        return "Response :: " + jIRARestService.getAllProjects();
    }
}
