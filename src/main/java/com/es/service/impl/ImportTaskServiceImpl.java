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

import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;
import com.es.entity.TaskEstimates;
import com.es.repository.ImportTaskRepository;
import com.es.service.EstimatesService;
import com.es.service.ImportTaskService;

@Service
public class ImportTaskServiceImpl implements ImportTaskService {

	@Autowired
	EstimatesService estimatesService;
	
	
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
		Row headerRow = sheet.getRow(0);
		if (headerRow != null) {
			for (Cell cell : headerRow) {
				headerRowData.add(cell.getStringCellValue());
			}
		}

		return headerRowData;
	}

	private ImportTask createTaskList(List<String> rowData) {
	    int value3 = 0; // Default value if rowData.get(6) is null// sprintId
	    String value5 = rowData.get(8); // summary
	    String value6 = rowData.get(9); // task id
	    String value7 = rowData.get(10); // task type
	    String value8 = rowData.get(12); // task priority
	    String value9 = rowData.get(11); // task status
	    List<String> value10 = new ArrayList<>(); // labels
	    String value11 = rowData.get(15); // task description
   TaskEstimates estimates  = new TaskEstimates();
   estimates.setTaskId(value6);
   
	    if (rowData.size() > 14) {
	        String labelsString = rowData.get(14);
	        if (labelsString != null && !labelsString.isBlank()) {
	            // Assuming labels are comma-separated in the Excel cell
	            value10.addAll(Arrays.asList(labelsString.split(",")));
	        }
	    }

	    if (rowData.get(6) != null && !rowData.get(6).isBlank()) {
	        try {
	            value3 = Integer.parseInt(rowData.get(6));
	        } catch (NumberFormatException e) {
	            // Handle the case where the string cannot be parsed to an integer
	            // You can log the error or take appropriate action based on your requirements
	        }
	    }

	    return new ImportTask(value3, value5, value6, value7, value8, value9, value10, value11, estimates);
	}


	@Override
	public int saveTaskData(List<ImportTask> importTasks) {
	    // Check if the list of tasks is null or empty
	    if (importTasks == null || importTasks.isEmpty()) {
	        // No tasks to save, return 0
	        return 0;
	    }
	    List<ImportTask> uniqueSprints = removeDuplicateProjects(importTasks);
	    // Extract task IDs from the list
	    Set<String> taskIds = uniqueSprints.stream()
	            .filter(task -> task != null && task.getTaskId() != null && !task.getTaskId().isBlank())
	            .map(ImportTask::getTaskId)
	            .collect(Collectors.toSet());

	    // Create a set of sprint IDs
	    Set<Integer> sprintIds = uniqueSprints.stream()
	            .filter(task -> task != null && task.getSprintId() != 0)
	            .map(ImportTask::getSprintId)
	            .collect(Collectors.toSet());

	    // Retrieve existing tasks from the database based on task IDs and sprint IDs
	    List<ImportTask> existingTasks = taskRepository.findByTaskIdInAndSprintIdIn(taskIds, sprintIds);

	    // Create a map of existing tasks for efficient lookup
	    Map<String, ImportTask> existingTasksMap = existingTasks.stream()
	            .collect(Collectors.toMap(task -> task.getTaskId() + "-" + task.getSprintId(), Function.identity()));
	    
	    
	    // Create a list to store tasks that need to be saved or updated
	    List<ImportTask> tasksToSave = new ArrayList<>();

	    // Iterate over the import tasks
	    for (ImportTask importTask : uniqueSprints) {
	        String taskId = importTask.getTaskId();
	        int sprintId = importTask.getSprintId();

	        if (importTask != null && importTask.getSummary() != null && sprintId > 0) {

	            // Check for duplicate task and sprint ID
	            String key = taskId + "-" + sprintId;
	            ImportTask existingTask = existingTasksMap.get(key);
	            TaskEstimates estimates = new TaskEstimates();

	            if (existingTask != null) {
	                // Update existing task data
	            	
	                existingTask.setSummary(importTask.getSummary());
	                existingTask.setTaskType(importTask.getTaskType());
	                existingTask.setTaskPriority(importTask.getTaskPriority());
	                existingTask.setTaskStatus(importTask.getTaskStatus());
	                existingTask.setLabels(importTask.getLabels());
	                existingTask.setTaskDescription(importTask.getTaskDescription());
	                existingTask.setThreePointEstimate(5);
	                existingTask.setRiskFactor(6);
	                tasksToSave.add(existingTask);
	                continue;
	            }

	            // Save new task data to the list
	            
       
	            importTask.setThreePointEstimate(5);
	            importTask.setRiskFactor(6);
	            tasksToSave.add(importTask);
	        }
	    }

	    if (!tasksToSave.isEmpty()) {
	        // Save all tasks in one request
	        taskRepository.saveAll(tasksToSave);
	    }

	    return tasksToSave.size();
	}



	@Override
	public ImportTask getTasks(int id) {
		Optional<ImportTask> list = taskRepository.findById(id);
		return !list.isPresent() ? null : list.get();
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
				&& !tasks.getTaskId().isBlank() && tasks.getSummary() !=null && !tasks.getSummary().isBlank() ;
	}

	private String generateProjectKey(ImportTask tasks) {
		
		return tasks.getSprintId() + "-" + tasks.getTaskId() ;
	}
	
	


}
