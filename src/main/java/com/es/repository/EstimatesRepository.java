package com.es.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es.entity.TaskEstimates;

@Repository
public interface EstimatesRepository extends JpaRepository<TaskEstimates, Integer> {
	TaskEstimates findByTaskId(String taskId);
	
	
}
