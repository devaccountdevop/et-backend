package com.es.controller;

import com.es.entity.EstimationMaster;
import com.es.entity.Signup;
import com.es.response.ExceptionEnum;
import com.es.response.LoginResponse;
import com.es.response.SuccessEnum;
import com.es.service.EstimationMasterService;
import com.es.service.SignupService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("estimation-tool/login")
public class LoginController {
    @Autowired
    EstimationMasterService eser;
    @Autowired
    SignupService signupService;

    @GetMapping("/get_employee")
    public String employee(@RequestParam Integer id) {
        EstimationMaster employeeMasterEntity = eser.getEmployee(id);
        return employeeMasterEntity.toString();
    }
    
    @PostMapping("")
    public LoginResponse userLogin(HttpServletRequest request, Model model) {
        LoginResponse response = new LoginResponse();

        // Retrieve form data using HttpServletRequest
        String email = request.getParameter("email"); // Assuming "email" is the name attribute of the email input field in your form
        String password = request.getParameter("password"); // Assuming "password" is the name attribute of the password input field in your form

        Signup signup = new Signup();
        signup.setUserName(email);
        signup.setPassword(password);

        Signup loginDetails = signupService.getUserByUserName(email);

        if (loginDetails != null) {
            if (signup.getUserName().equals(loginDetails.getUserName()) && signup.getPassword().equals(loginDetails.getPassword())) {
                response.setCode(SuccessEnum.SUCCESS_LOGIN.getCode());
                response.setMessage(SuccessEnum.SUCCESS_LOGIN.getMessage());
                response.setData(loginDetails);
            } else {
                response.setCode(ExceptionEnum.INVALID_USER.getErrorCode());
                response.setMessage(ExceptionEnum.INVALID_USER.getMessage());
                response.setError(true);
            }
        } else {
            response.setCode(ExceptionEnum.INVALID_USER.getErrorCode());
            response.setMessage(ExceptionEnum.INVALID_USER.getMessage());
            response.setError(true);
        }

        return response;
    }

}
