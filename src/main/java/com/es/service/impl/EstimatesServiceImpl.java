package com.es.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.entity.Estimates;
import com.es.repository.EstimatesRepository;
import com.es.service.EstimatesService;

@Service
public class EstimatesServiceImpl implements EstimatesService {

	@Autowired
	EstimatesRepository estimatesRepository;

	@Override
	public Estimates getEstimatesById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Estimates getEstimatesByTaskId(String taskId) {
		Estimates estimates = new Estimates();
		estimates = estimatesRepository.findByTaskId(taskId);
		return estimates;
	}

	@Override
	public Estimates saveEstimates(Estimates signup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Estimates> estimateslist(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
