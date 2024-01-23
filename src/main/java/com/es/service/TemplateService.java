package com.es.service;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.entity.TemplateEntity;
import com.es.repository.TemplateRepository;

@Service
public class TemplateService {
	
	@Autowired
	TemplateRepository repository;
	public Optional<TemplateEntity> findByFilename(String filename) {
		
		return repository.findByFilename(filename);
		
	}

}
