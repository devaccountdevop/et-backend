package com.es.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.response.DownloadTemplateResponse;
import com.es.service.DownloadTemplateService;

@RestController
@RequestMapping("/estimation-tool")

public class DownloadTemplateController {


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
