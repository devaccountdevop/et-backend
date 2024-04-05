package com.es.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.es.entity.ImportProjects;
@Repository
public interface ImportProjectsRepository extends JpaRepository<ImportProjects, Integer> {
	
	 public List<ImportProjects> findByJiraUserName(String jiraUserName);
	 
	 ImportProjects findByProjectId(int projectId);
	 List<ImportProjects> findByProjectIdIn(Set<Integer> projectIds);
	//public  List<ImportProjects> findAllProjectByUserName(String jiraUserName);
	 @Query("SELECT projectName FROM ImportProjects ip WHERE ip.jiraUserName = :jiraUserName")
	    String findProjectNameByJiraUserName(@Param("jiraUserName") String jiraUserName);
}