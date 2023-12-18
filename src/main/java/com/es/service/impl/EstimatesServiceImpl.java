package com.es.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.entity.TaskEstimates;
import com.es.repository.EstimatesRepository;
import com.es.service.EstimatesService;

@Service
public class EstimatesServiceImpl implements EstimatesService {

	
	@Autowired
	EstimatesRepository estimatesRepository;
	
	@Override
	public TaskEstimates getEstimatesById(int id) {
		Optional<TaskEstimates> list = estimatesRepository.findById(id);
		return ! list.isPresent() ? null: list.get();
	}

	@Override
	public TaskEstimates getEstimatesByTaskId(String taskId) {
		TaskEstimates estimates = new TaskEstimates();
		estimates = estimatesRepository.findByTaskId(taskId);
		return estimates;
	}
	@Override
	public TaskEstimates saveEstimates(TaskEstimates taskEstimates) {
	    if (taskEstimates != null) {
	        int id = taskEstimates.getId();
	        if (id != 0) {
	            Optional<TaskEstimates> existingEstimatesOptional = estimatesRepository.findById(id);

	            if (existingEstimatesOptional.isPresent()) {
	                // Update existing estimates with the new values
	                TaskEstimates existingEstimates = existingEstimatesOptional.get();
	                existingEstimates.setLow(taskEstimates.getLow());
	                existingEstimates.setRealistic(taskEstimates.getRealistic());
	                existingEstimates.setHigh(taskEstimates.getHigh());
	                // Set other properties as needed

	                // Save the updated estimates
	                return estimatesRepository.save(existingEstimates);
	            } else {
	                // If no existing estimates found or taskId doesn't match, save the new estimates
	                return estimatesRepository.save(taskEstimates);
	            }
	        }
	    }
	    return null;
	}


	@Override
	public List<TaskEstimates> getEstimatesListByTaskId(String taskId) {
		return null;
	}

	

}
