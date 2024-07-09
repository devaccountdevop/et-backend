package com.es.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.es.entity.ImportSprint;
@Repository
public interface ImportSprintRepository extends JpaRepository<ImportSprint, Integer> {
	
	ImportSprint findBySprintId(int sprintId);
	  @Query("SELECT s FROM ImportSprint s WHERE s.sprintId IN :sprintIds AND s.userId = :userId")
	    List<ImportSprint> findBySprintIdInAndUserId(@Param("sprintIds") Set<Integer> sprintIds, @Param("userId") int userId);
	  @Query("SELECT s FROM ImportSprint s WHERE s.projectId = :projectId AND s.userId = :userId")
	    List<ImportSprint> findAllSprintByProjectIdAndUserId(@Param("projectId") int projectId, @Param("userId") int userId);
}
