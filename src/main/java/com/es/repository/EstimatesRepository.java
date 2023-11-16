package com.es.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es.entity.Estimates;
import com.es.entity.Signup;
@Repository
public interface EstimatesRepository extends JpaRepository<Estimates, Integer> {
	Estimates findByTaskId(String taskId);
}
