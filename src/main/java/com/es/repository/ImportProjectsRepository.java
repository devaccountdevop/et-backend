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
	
	  @Query("SELECT p FROM ImportProjects p WHERE p.jiraUserName = :jiraUserName AND p.userId = :userId")
	    List<ImportProjects> findByJiraUserNameAndUserId(@Param("jiraUserName") String jiraUserName, @Param("userId") int userId);
	 List< ImportProjects> findByUserId(Integer userId);
	  @Query("SELECT p FROM ImportProjects p WHERE p.projectId = :projectId AND p.userId = :userId")
	    ImportProjects findByProjectIdAndUserId(@Param("projectId") int projectId, @Param("userId") int userId);
	 @Query("SELECT p FROM ImportProjects p WHERE p.projectId IN :projectIds AND p.userId = :userId")
	    List<ImportProjects> findByProjectIdInAndUserId(@Param("projectIds") Set<Integer> projectIds, @Param("userId") int userId);
	//public  List<ImportProjects> findAllProjectByUserName(String jiraUserName);
	 @Query("SELECT projectName FROM ImportProjects ip WHERE ip.jiraUserName = :jiraUserName")
	    String findProjectNameByJiraUserName(@Param("jiraUserName") String jiraUserName);
}