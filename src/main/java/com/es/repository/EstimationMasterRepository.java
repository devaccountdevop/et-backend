package com.es.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es.entity.EstimationMaster;


@Repository
public interface EstimationMasterRepository extends JpaRepository<EstimationMaster, Integer> {

	List<EstimationMaster> findAllByIdIn(List<Integer> ids);

}
