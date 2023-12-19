package com.es.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.entity.TemplateEntity;
import com.es.repository.TemplateRepository;
import com.es.service.TemplateService;

@RequestMapping("/estimation-tool")
@RestController
public class DownloadTemplateController {

	@Autowired
	TemplateRepository repository;
	@Autowired
	TemplateService templateService;

	@GetMapping("/downloadtemplate")
	public ResponseEntity<InputStreamResource> downloadExcelTemplate() throws IOException {
       
        InputStream inputStream = new ClassPathResource("templates/template.xlsx").getInputStream();

        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.xlsx");

       
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }


	}

