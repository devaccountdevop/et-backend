package com.es.service;

import java.util.List;

import com.es.entity.TaskEstimates;
import com.es.entity.Signup;

public interface EstimatesService {
	
	TaskEstimates getEstimatesById(int id);
	TaskEstimates getEstimatesByTaskId(String taskId);
	TaskEstimates saveEstimates(TaskEstimates taskEstimates);
	List<TaskEstimates> getEstimatesListByTaskId(String taskId);

}
