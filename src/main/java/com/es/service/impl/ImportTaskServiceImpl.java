package com.es.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.entity.ImportTask;
import com.es.entity.TaskEstimates;
import com.es.entity.Worklog;
import com.es.repository.ImportTaskRepository;
import com.es.repository.WorklogRepository;
import com.es.service.EstimatesService;
import com.es.service.ImportTaskService;

@Service
public class ImportTaskServiceImpl implements ImportTaskService {
	
	 private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");
	 private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	
	 @Autowired
	EstimatesService estimatesService;
	WorklogRepository worklogRepository;
	
	
	@Autowired
	ImportTaskRepository taskRepository;
	Workbook workbook;

	@Override
	public List<ImportTask> getTaskDataAsList(InputStream inputStream, int userId) {
		List<ImportTask> invList = new ArrayList<>();
		DataFormatter dataFormatter = new DataFormatter();
		try {
			workbook = WorkbookFactory.create(inputStream);
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);

		List<String> headerRowData = getHeaderRowData(sheet); // Retrieve header row data

		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext()) {
			iterator.next(); // Skip the first row
			iterator.next();
			iterator.next();
		}

		while (iterator.hasNext()) {
			Row row = iterator.next();
			List<String> rowData = new ArrayList<>();

			// Iterate through all cells in the row
			for (int columnIndex = 0; columnIndex < headerRowData.size(); columnIndex++) {
				Cell cell = row.getCell(columnIndex);
				String cellValue = (cell != null) ? dataFormatter.formatCellValue(cell) : null;
				rowData.add(cellValue);
			}

			// Check if all values in the current row are blank before adding to the list
			if (!rowData.isEmpty() && rowData.stream().anyMatch(value -> value != null)) {
				ImportTask credentials = createTaskList(rowData, userId);
				invList.add(credentials);
			}
		}

		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return invList;
	}

	private List<String> getHeaderRowData(Sheet sheet) {
		List<String> headerRowData = new ArrayList<>();
		Row headerRow = sheet.getRow(2);
		if (headerRow != null) {
			for (Cell cell : headerRow) {
				headerRowData.add(cell.getStringCellValue());
			}
		}

		return headerRowData;
	}

private ImportTask createTaskList(List<String> rowData, int userId) {
		
		// Initialize default values
//	    int originalEstimate = 0;
//	    int optimisticEstimate = 0;
//	    int pessimisticEstimate = 0;
//	    int realisticEstimate = 0;
	    
	   // int value3 = 0; // Default value if rowData.get(6) is null// sprintId
	    int projectId = 0;
	    int sprintId = 0;
	    if (rowData.get(6) != null && !rowData.get(6).isEmpty()) {
	        try {
	        	Integer.parseInt(rowData.get(4));
	        	sprintId = Integer.parseInt(rowData.get(6));
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Add appropriate handling
	        }
	    }
	    if (rowData.get(4) != null && !rowData.get(4).isEmpty()) {
	        try {
	        	projectId = Integer.parseInt(rowData.get(4));
	        	
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Add appropriate handling
	        }
	    }
	    
	    String summary = rowData.get(12); 
	    String taskId = rowData.get(13); 
	    String taskType = rowData.get(14); 
	    String taskstatus = rowData.get(15); 
	    String taskpriority = rowData.get(16); 
	    String storyPoint = rowData.get(17); 
	    List<String> lable = new ArrayList<>(); 
	    String description = rowData.get(22); 
	    
	    int originalEstimates = 0;
	    if (rowData.get(18) != null && !rowData.get(18).isEmpty()) {
	        try {
	        	 originalEstimates = Integer.parseInt(rowData.get(18));
	        	
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Add appropriate handling
	        }
	    }
	    int actualEstimate = 0;
	    if (rowData.get(19) != null && !rowData.get(19).isEmpty()) {
	        try {
	        	  actualEstimate = Integer.parseInt(rowData.get(19)); 
	        	
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Add appropriate handling
	        }
	    }
	    String actualTimeDate = rowData.get(20);
	    String createdDate = rowData.get(23);
	    String sprintAssignDate = rowData.get(24);
	    int optimisticEstimate = 0;
	    int pessimisticEstimate = 0;
	    int realisticEstimate = 0;
	    if (rowData.get(25) != null && !rowData.get(25).isEmpty()) {
	        try {
	        	 optimisticEstimate = Integer.parseInt(rowData.get(25)); 
	        	
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Add appropriate handling
	        }
	    }
	    
	    if (rowData.get(26) != null && !rowData.get(26).isEmpty()) {
	        try {
	        	  pessimisticEstimate = Integer.parseInt(rowData.get(26));
	        	
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Add appropriate handling
	        }
	    }
	    
	    if (rowData.get(27) != null && !rowData.get(27).isEmpty()) {
	        try {
	        	 realisticEstimate = Integer.parseInt(rowData.get(27));
	        	
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Add appropriate handling
	        }
	    }
	    String formattedActualTimeDate = parseAndFormatDate(actualTimeDate);
	    String formattedcreatedDate = parseAndFormatDate(createdDate);
        String formattedsprintAssignDate = parseAndFormatDate(sprintAssignDate);
   TaskEstimates estimates  = new TaskEstimates();
   estimates.setTaskId(taskId);
   estimates.setLow(optimisticEstimate);
   estimates.setHigh(pessimisticEstimate);
   estimates.setRealistic(realisticEstimate);
   
	    if (rowData.size() > 21) {
	        String labelsString = rowData.get(21);
	        if (labelsString != null && !labelsString.isEmpty()) {
	            // Assuming labels are comma-separated in the Excel cell
	        	lable.addAll(Arrays.asList(labelsString.split(",")));
	        }
	    }

	    return new ImportTask(sprintId, summary, taskId, taskType, taskpriority, taskstatus, lable, description, estimates, formattedcreatedDate, originalEstimates, projectId, formattedsprintAssignDate,formattedActualTimeDate,
	    	actualEstimate, storyPoint, userId);
	}
private String parseAndFormatDate(String dateStr) {
	 if (dateStr == null || dateStr.isEmpty()) {
	        return null;
	    }
    try {
        LocalDate date = LocalDate.parse(dateStr, INPUT_FORMATTER);
        return date.format(OUTPUT_FORMATTER);
    } catch (DateTimeParseException e) {
        e.printStackTrace(); // Add appropriate handling
        return null; // Or handle the error in another way
    }
}

	@Override
	public int saveTaskData(List<ImportTask> importTasks, int userId) {
	    if (importTasks == null || importTasks.isEmpty()) {
	        return 0;
	    }

	    List<ImportTask> uniqueTasks = removeDuplicateProjects(importTasks);

	    // Fetch existing tasks from the database based on task IDs and sprint IDs
	    List<ImportTask> existingTasks = taskRepository.findByTaskIdInAndProjectIdInAndUserId(
	            uniqueTasks.stream()
	                    .filter(task -> task != null && task.getTaskId() != null && !task.getTaskId().isEmpty())
	                    .map(ImportTask::getTaskId)
	                    .collect(Collectors.toSet()),
	            uniqueTasks.stream()
	                    .filter(task -> task != null && task.getProjectId() != 0)
	                    .map(ImportTask::getProjectId)
	                    .collect(Collectors.toSet()), userId);

	    Map<String, ImportTask> existingTasksMap = existingTasks.stream()
	            .collect(Collectors.toMap(task -> task.getTaskId() + "-" + task.getProjectId(), Function.identity()));

	    List<ImportTask> tasksToSave = new ArrayList<>();

	    for (ImportTask importTask : uniqueTasks) {
	        String taskId = importTask.getTaskId();
	        int projectId = importTask.getProjectId();

	        if (importTask != null && importTask.getSummary() != null && projectId > 0) {
	            String key = taskId + "-" + projectId;
	            ImportTask existingTask = existingTasksMap.get(key);

	            if (existingTask != null) {
	                existingTask.setSummary(importTask.getSummary());
	                existingTask.setTaskType(importTask.getTaskType());
	                existingTask.setTaskPriority(importTask.getTaskPriority());
	                existingTask.setTaskStatus(importTask.getTaskStatus());
	                existingTask.setLabels(importTask.getLabels());
	                existingTask.setTaskDescription(importTask.getTaskDescription());
	                existingTask.setAiEstimate(importTask.getAiEstimate());
	                existingTask.setEstimates(importTask.getEstimates());
	                existingTask.setOriginalEstimate(importTask.getOriginalEstimate());
	                existingTask.setStoryPoints(importTask.getStoryPoints());
	                existingTask.setAssignee(importTask.getAssignee());
	                existingTask.setCreationDate(importTask.getCreationDate());
	                existingTask.setSprintAssignDate(importTask.getSprintAssignDate());
	                existingTask.setActual(importTask.getActual());
	                existingTask.setActualTimeDate(importTask.getActualTimeDate());
	                existingTask.setTaskStatus(importTask.getTaskStatus());
	                existingTask.setProjectId(importTask.getProjectId());
	                existingTask.setSprintId(importTask.getSprintId());
	           
//	                
//	                List<Worklog> updatedWorklogs = new ArrayList<>();
//	                for (Worklog worklog : existingTask.getWorklogs()) {
//	                	Worklog existingWorklog  = new Worklog();
//	                    if (worklog.getId() != null) {
//	                    	existingWorklog = worklogRepository.findById(worklog.getId())
//	                                .orElseThrow(() -> new RuntimeException("worklog not found"));
//	                        existingWorklog.setCreatedDate(worklog.getCreatedDate());
//                            existingWorklog.setStartedDate(worklog.getStartedDate());
//                            existingWorklog.setTimeSpent(worklog.getTimeSpent());
//                            existingWorklog.setTimeSpentSeconds(worklog.getTimeSpentSeconds());
//                            existingWorklog.setUpdatedDate(worklog.getUpdatedDate());
//                            existingWorklog.setImportTask(existingTask); // Ensure task_id is set
//                            updatedWorklogs.add(existingWorklog);
//	                    } else {
//	                    	existingWorklog = worklog;
//	                    	existingWorklog.setImportTask(importTask);
//	                    }
//	                    updatedWorklogs.add(existingWorklog);
//	                }

//	                importTask.setWorklogs(updatedWorklogs);
	    
	                tasksToSave.add(existingTask);
	               
	            } else {
	             
	            	if(importTask.getWorklogs() != null && !importTask.getWorklogs().isEmpty()) {
	            		for (Worklog worklog : importTask.getWorklogs()) {
		                    worklog.setImportTask(importTask);
		                }
	            	}	                
	            	importTask.setWorklogs(importTask.getWorklogs());
	                tasksToSave.add(importTask);
	            }
	        }
	    }

	    if (!tasksToSave.isEmpty()) {
	        taskRepository.saveAll(tasksToSave);
	    }

	    return tasksToSave.size();
	}




	@Override
	public ImportTask getTasks(int id) {
		Optional<ImportTask> optionalTask = taskRepository.findById(id);
	    return optionalTask.orElse(null);
	}

	@Override
	public ImportTask saveTasks(ImportTask importTask) {

		return taskRepository.save(importTask);
	}

	@Override
	public ImportTask updateTasks(ImportTask importTask) {

		return null;
	}

	@Override
	public List<ImportTask> getAllTaskBySprintId(int sprintId) {

	    List<ImportTask> taskList = taskRepository.findAllTaskBySprintId(sprintId);
	    List<ImportTask> updatedTaskList = new ArrayList<>();

	    for (ImportTask importTask : taskList) {
	        TaskEstimates estimates = estimatesService.getEstimatesById(importTask.getEstimates().getId());
	        importTask.setEstimates(estimates);
	        updatedTaskList.add(importTask);
	    }

	    return updatedTaskList;
	}
	
	public List<ImportTask> removeDuplicateProjects(List<ImportTask> tasks) {
		if (tasks == null || tasks.isEmpty()) {
			// No projects to check, return an empty list
			return Collections.emptyList();
		}

		Set<String> uniqueSprintsKeys = new HashSet<>();
		List<ImportTask> uniqueSprints = new ArrayList<>();

		for (ImportTask task : tasks) {
			if (isValidProject(task)) {
				String projectKey = generateProjectKey(task);

				if (uniqueSprintsKeys.add(projectKey)) {
					// Unique project, add it to the new list
					uniqueSprints.add(task);
				} else {
					// Duplicate project key found, handle it as needed
					System.out.println("Warning: Duplicate sprints key found - " + projectKey);
				}
			}
		}

		return uniqueSprints;
	}

	private boolean isValidProject(ImportTask tasks) {
		return tasks != null  && tasks.getTaskId() != null
				&& !tasks.getTaskId().isEmpty() && tasks.getSummary() !=null && !tasks.getSummary().isEmpty() ;
	}

	private String generateProjectKey(ImportTask tasks) {
		
		return tasks.getProjectId() + "-" + tasks.getTaskId() ;
	}

	@Override
	public List<ImportTask> getAllBacklogTask(int projectId) {
		if(projectId == 0) {
			return null;
		}	
	List<ImportTask> backlogTask = this.taskRepository.findAllTasksByProjectId(projectId);
		return backlogTask;
	}
	

	@Override
	public int projectScope(int projectId, int sprintId,String sprintEndDate, int userId) {
		
		int totalscope = 0;
		List<ImportTask> importTasks = taskRepository.findTasksBySprintEndDate(sprintEndDate,projectId);
		if(!importTasks.isEmpty()) {
		
			for(ImportTask task: importTasks) {
				totalscope += task.getOriginalEstimate();
			}
		}
		return totalscope;
	}
	
	


}
