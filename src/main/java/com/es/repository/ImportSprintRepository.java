package com.es.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es.entity.ImportSprint;
@Repository
public interface ImportSprintRepository extends JpaRepository<ImportSprint, Integer> {
	
	ImportSprint findBySprintId(int sprintId);
	List<ImportSprint> findBySprintIdIn(Set<Integer> sprintIds);
	List<ImportSprint> findAllSprintByProjectId(int projectId);
}
