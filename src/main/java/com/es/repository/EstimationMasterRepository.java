package com.es.repository;

import java.util.List;

import com.es.entity.EstimationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EstimationMasterRepository extends JpaRepository<EstimationMaster, Integer> {

	List<EstimationMaster> findAllByIdIn(List<Integer> ids);

}
