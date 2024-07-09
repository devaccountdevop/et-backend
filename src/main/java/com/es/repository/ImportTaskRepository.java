package com.es.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.es.entity.ImportTask;

@Repository
public interface ImportTaskRepository extends JpaRepository<ImportTask, Integer> {
	
	ImportTask findByTaskId(String taskId);
	List<ImportTask> findByTaskIdIn(Set<String> taskIds);
	public List<ImportTask> findAllTaskBySprintId(int sprintId);
	
	@Query("SELECT t FROM ImportTask t WHERE t.sprintId IN :sprintIds")
    List<ImportTask> findAllBySprintIds(List<Integer> sprintIds);
	
	 @Query("SELECT t FROM ImportTask t WHERE t.taskId IN :taskIds AND t.projectId IN :projectIds AND t.userId = :userId")
	    List<ImportTask> findByTaskIdInAndProjectIdInAndUserId(
	        @Param("taskIds") Collection<String> taskIds, 
	        @Param("projectIds") Collection<Integer> projectIds, 
	        @Param("userId") int userId
	    );

	@Query("SELECT i, w FROM ImportTask i INNER JOIN i.worklogs w WHERE w.updatedDate IN :sprintDates")
	List<ImportTask> findAllTasksByUpdatedDate(@Param("sprintDates") Collection<LocalDate> sprintDates);


	 @Query("SELECT t FROM ImportTask t WHERE t.projectId = :projectId AND t.sprintId = 0")
	    List<ImportTask> findAllTasksByProjectId(@Param("projectId") int projectId);	

	 List<ImportTask> findByTaskIdInAndSprintId(List<String> taskIds, int sprintId);
	 
	 @Query("SELECT j FROM ImportTask j WHERE j.creationDate > :date AND j.projectId = :projectId")
	    List<ImportTask> findTasksBySprintEndDate(@Param("date") String date, @Param("projectId") int projectId);
}


