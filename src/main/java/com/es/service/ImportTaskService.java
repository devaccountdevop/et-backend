package com.es.service;

import java.io.InputStream;
import java.util.List;

import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;

public interface ImportTaskService {
	
	List<ImportTask> getTaskDataAsList(InputStream inputStream);

	int saveTaskData(List<ImportTask> importtask);

	ImportTask getTasks(int id);

	ImportTask saveTasks(ImportTask importTask);

	ImportTask updateTasks(ImportTask importTask);
	List<ImportTask > getAllTaskBySprintId(int sprintId);
	
	 List<ImportTask> getAllBacklogTask(int projectId);

}
