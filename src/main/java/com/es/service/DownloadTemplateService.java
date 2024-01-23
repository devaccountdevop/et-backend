package com.es.service;

import java.io.InputStream;

import com.es.response.DownloadTemplateResponse;

public interface DownloadTemplateService {

	
	DownloadTemplateResponse getExcelFile(InputStream inputStream);

}
