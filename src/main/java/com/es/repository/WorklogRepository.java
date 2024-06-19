package com.es.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.es.entity.Worklog;

public interface WorklogRepository extends JpaRepository<Worklog, Long> {
	void deleteAllByImportTaskId(int importTaskId);
}
