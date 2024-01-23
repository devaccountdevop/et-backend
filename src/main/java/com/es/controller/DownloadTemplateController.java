package com.es.controller;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.es.entity.TemplateEntity;
import com.es.repository.TemplateRepository;
import com.es.response.DownloadTemplateResponse;
import com.es.service.DownloadTemplateService;
import com.es.service.TemplateService;

import org.springframework.util.StreamUtils;

@RestController
@RequestMapping("/estimation-tool")

public class DownloadTemplateController {

	@Autowired
	TemplateRepository repository;
	@Autowired
	TemplateService templateService;

	@Autowired
	DownloadTemplateService downloadTemplate;

	@GetMapping("/downloadtemplate")
	public DownloadTemplateResponse downloadExcelTemplate() throws IOException {

		InputStream inputStream = new ClassPathResource("templates/template.xlsx").getInputStream();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.xlsx");

		return downloadTemplate.getExcelFile(inputStream);

	}
}
