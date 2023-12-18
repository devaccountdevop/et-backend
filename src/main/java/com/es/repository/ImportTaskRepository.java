package com.es.repository;

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
	
	@Query("SELECT t FROM ImportTask t WHERE t.taskId IN :taskIds AND t.sprintId IN :sprintIds")
    List<ImportTask> findByTaskIdInAndSprintIdIn(@Param("taskIds") Collection<String> taskIds, @Param("sprintIds") Collection<Integer> sprintIds);
}
