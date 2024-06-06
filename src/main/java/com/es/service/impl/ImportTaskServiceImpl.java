package com.es.service.impl;

import java.io.IOException;
import java.io.InputStream;
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

	@Autowired
	EstimatesService estimatesService;
	WorklogRepository worklogRepository;
	
	
	@Autowired
	ImportTaskRepository taskRepository;
	Workbook workbook;

	@Override
	public List<ImportTask> getTaskDataAsList(InputStream inputStream) {
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
				ImportTask credentials = createTaskList(rowData);
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

private ImportTask createTaskList(List<String> rowData) {
		
		// Initialize default values
	    int originalEstimate = 0;
	    int optimisticEstimate = 0;
	    int pessimisticEstimate = 0;
	    int realisticEstimate = 0;
	    
	    int value3 = 0; // Default value if rowData.get(6) is null// sprintId
	    String value5 = rowData.get(8); // summary
	    String value6 = rowData.get(9); // task id
	    String value7 = rowData.get(10); // task type
	    String value8 = rowData.get(12); // task priority
	    String value9 = rowData.get(11); // task status
	    List<String> value10 = new ArrayList<>(); // labels
	    String value11 = rowData.get(16); // task description
	    String value12 = rowData.get(17);
	    String value13 = rowData.get(13);//original estimate
//	    String value18 = rowData.get(18);//optimistic
//	    optimisticEstimate = Integer.parseInt(value18);
//	    String value19 = rowData.get(19);//pessimistic
//	    pessimisticEstimate = Integer.parseInt(value19);
//	    String value20 = rowData.get(20);//realistic
//	    realisticEstimate = Integer.parseInt(value20);
//	    int originalEstimate = Integer.parseInt(value13);
	    
	    //String value13 = rowData.get(13);
	    if (value13 != null && !value13.isEmpty()) {
	        try {
	            originalEstimate = Integer.parseInt(value13);
	        } catch (NumberFormatException e) {
	            // Handle invalid number format
	            System.err.println("Invalid original estimate: " + value13);
	        }
	    }
	    
	    
	    String value18 = rowData.get(18);
	    if (value18 != null && !value18.isEmpty()) {
	        try {
	            optimisticEstimate = Integer.parseInt(value18);
	        } catch (NumberFormatException e) {
	            // Handle invalid number format
	            System.err.println("Invalid optimistic estimate: " + value18);
	        }
	    }

	    // Parse pessimistic estimate
	    String value19 = rowData.get(19);
	    if (value19 != null && !value19.isEmpty()) {
	        try {
	            pessimisticEstimate = Integer.parseInt(value19);
	        } catch (NumberFormatException e) {
	            // Handle invalid number format
	            System.err.println("Invalid pessimistic estimate: " + value19);
	        }
	    }

	    // Parse realistic estimate
	    String value20 = rowData.get(20);
	    if (value20 != null && !value20.isEmpty()) {
	        try {
	            realisticEstimate = Integer.parseInt(value20);
	        } catch (NumberFormatException e) {
	            // Handle invalid number format
	            System.err.println("Invalid realistic estimate: " + value20);
	        }
	    }
	    
	    
   TaskEstimates estimates  = new TaskEstimates();
   estimates.setTaskId(value6);
   estimates.setLow(optimisticEstimate);
   estimates.setHigh(pessimisticEstimate);
   estimates.setRealistic(realisticEstimate);
   
   
	    if (rowData.size() > 15) {
	        String labelsString = rowData.get(15);
	        if (labelsString != null && !labelsString.isEmpty()) {
	            // Assuming labels are comma-separated in the Excel cell
	            value10.addAll(Arrays.asList(labelsString.split(",")));
	        }
	    }

	    if (rowData.get(6) != null && !rowData.get(6).isEmpty()) {
	        try {
	            value3 = Integer.parseInt(rowData.get(6));
	        } catch (NumberFormatException e) {
	            // Handle the case where the string cannot be parsed to an integer
	            // You can log the error or take appropriate action based on your requirements
	        }
	    }

	    return new ImportTask(value3, value5, value6, value7, value8, value9, value10, value11, estimates, value12, originalEstimate);
	}


	@Override
	public int saveTaskData(List<ImportTask> importTasks) {
	    if (importTasks == null || importTasks.isEmpty()) {
	        return 0;
	    }

	    List<ImportTask> uniqueTasks = removeDuplicateProjects(importTasks);

	    // Fetch existing tasks from the database based on task IDs and sprint IDs
	    List<ImportTask> existingTasks = taskRepository.findByTaskIdInAndSprintIdIn(
	            uniqueTasks.stream()
	                    .filter(task -> task != null && task.getTaskId() != null && !task.getTaskId().isEmpty())
	                    .map(ImportTask::getTaskId)
	                    .collect(Collectors.toSet()),
	            uniqueTasks.stream()
	                    .filter(task -> task != null && task.getSprintId() != 0)
	                    .map(ImportTask::getSprintId)
	                    .collect(Collectors.toSet()));

	    Map<String, ImportTask> existingTasksMap = existingTasks.stream()
	            .collect(Collectors.toMap(task -> task.getTaskId() + "-" + task.getSprintId(), Function.identity()));

	    List<ImportTask> tasksToSave = new ArrayList<>();

	    for (ImportTask importTask : uniqueTasks) {
	        String taskId = importTask.getTaskId();
	        int sprintId = importTask.getSprintId();

	        if (importTask != null && importTask.getSummary() != null && sprintId > 0) {
	            String key = taskId + "-" + sprintId;
	            ImportTask existingTask = existingTasksMap.get(key);

	            if (existingTask != null) {
	                existingTask.setSummary(importTask.getSummary());
	                existingTask.setTaskType(importTask.getTaskType());
	                existingTask.setTaskPriority(importTask.getTaskPriority());
	                existingTask.setTaskStatus(importTask.getTaskStatus());
	                existingTask.setLabels(importTask.getLabels());
	                existingTask.setTaskDescription(importTask.getTaskDescription());
	                existingTask.setAiEstimate(importTask.getAiEstimate());
	                existingTask.setOriginalEstimate(importTask.getOriginalEstimate());
	                existingTask.setStoryPoints(importTask.getStoryPoints());
	                existingTask.setAssignee(importTask.getAssignee());
	                existingTask.setCreationDate(importTask.getCreationDate());
	                existingTask.setTaskStatus(importTask.getTaskStatus());
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
		return tasks != null && tasks.getSprintId() > 0 && tasks.getTaskId() != null
				&& !tasks.getTaskId().isEmpty() && tasks.getSummary() !=null && !tasks.getSummary().isEmpty() ;
	}

	private String generateProjectKey(ImportTask tasks) {
		
		return tasks.getSprintId() + "-" + tasks.getTaskId() ;
	}
	
	


}
