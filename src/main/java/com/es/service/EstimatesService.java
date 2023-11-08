package com.es.service;

import java.util.List;

import com.es.entity.Estimates;
import com.es.entity.Signup;

public interface EstimatesService {

	Estimates getEstimatesById(int id);

	Estimates getEstimatesByTaskId(String taskId);

	Estimates saveEstimates(Estimates signup);

	List<Estimates> estimateslist(int id);

}
