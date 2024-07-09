package com.es.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.transaction.annotation.Transactional;

import com.es.dto.GraphData;
import com.es.dto.GraphDataDto;
import com.es.dto.SprintInfoDto;
import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;
import com.es.entity.Worklog;
import com.es.repository.ImportSprintRepository;
import com.es.repository.ImportTaskRepository;
import com.es.service.ImportSprintService;

@Service
public class ImportSprintServiceImpl implements ImportSprintService {

	
	 private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");
	    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	@Autowired
	ImportSprintRepository importSprintRepository;

	@Autowired
	ImportTaskRepository importTaskRepository;
	Workbook workbook;

	@Override
	public List<ImportSprint> getSprintDataAsList(InputStream inputStream , int userId) {
		List<ImportSprint> invList = new ArrayList<>();
		DataFormatter dataFormatter = new DataFormatter();
		try {
			workbook = WorkbookFactory.create(inputStream);
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);

		List<String> headerRowData = getHeaderRowData(sheet);

		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext()) {
			iterator.next();
			iterator.next();
			iterator.next();
		}

		while (iterator.hasNext()) {
			Row row = iterator.next();
			List<String> rowData = new ArrayList<>();

			for (int columnIndex = 0; columnIndex < headerRowData.size(); columnIndex++) {
				Cell cell = row.getCell(columnIndex);
				String cellValue = (cell != null) ? dataFormatter.formatCellValue(cell) : null;
				rowData.add(cellValue);
			}

			if (!rowData.isEmpty() && rowData.stream().anyMatch(value -> value != null)) {
				ImportSprint credentials = createSprintList(rowData ,userId);
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


private ImportSprint createSprintList(List<String> rowData, int userId) {
    int value3 = 0;
    int value4 = 0;

    String value5 = rowData.get(7); // Assumed this is not a date field
    String sprintCreatedDate = rowData.get(8);
    String sprintStartDate = rowData.get(9);
    String sprintCompletedDate = rowData.get(10);
    String sprintEndDate = rowData.get(11);

    // Parse and format the date strings
    String formattedCreatedDate = parseAndFormatDate(sprintCreatedDate);
    String formattedStartDate = parseAndFormatDate(sprintStartDate);
    String formattedCompletedDate = parseAndFormatDate(sprintCompletedDate);
    String formattedEndDate = parseAndFormatDate(sprintEndDate);

    if (rowData.get(6) != null && !rowData.get(6).isEmpty()) {
        try {
            value3 = Integer.parseInt(rowData.get(6));
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Add appropriate handling
        }
    }

    if (rowData.get(4) != null && !rowData.get(4).isEmpty()) {
        try {
            value4 = Integer.parseInt(rowData.get(4));
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Add appropriate handling
        }
    }

    return new ImportSprint(value4, value3, value5, formattedStartDate, formattedEndDate, formattedCompletedDate, formattedCreatedDate, userId);
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
	public int saveSprintData(List<ImportSprint> importSprints, int userId) {
		if (importSprints == null || importSprints.isEmpty()) {

			return 0;
		}
		List<ImportSprint> uniqueSprints = removeDuplicateProjects(importSprints);

		Set<Integer> sprintIds = uniqueSprints.stream().filter(sprint -> sprint != null && sprint.getSprintId() > 0)
				.map(ImportSprint::getSprintId).collect(Collectors.toSet());

		Map<Integer, ImportSprint> existingSprintsMap = importSprintRepository.findBySprintIdInAndUserId(sprintIds, userId).stream()
				.collect(Collectors.toMap(ImportSprint::getSprintId, Function.identity()));

		List<ImportSprint> sprintsToSave = new ArrayList<>();

		for (ImportSprint importSprint : uniqueSprints) {
			int sprintId = importSprint.getSprintId();

			if (importSprint != null && importSprint.getSprintName() != null && !importSprint.getSprintName().isEmpty()
					&& importSprint.getProjectId() > 0 && importSprint.getSprintId() > 0) {

				ImportSprint existingSprint = existingSprintsMap.get(sprintId);

				if (existingSprint != null) {

					existingSprint.setProjectId(importSprint.getProjectId());
					existingSprint.setCreatedDate(importSprint.getCreatedDate());
					existingSprint.setStartDate(importSprint.getStartDate());
					existingSprint.setCompleteDate(importSprint.getCompleteDate());
					existingSprint.setEndDate(importSprint.getEndDate());
					existingSprint.setSprintName(importSprint.getSprintName());
					existingSprint.setStartDate(importSprint.getStartDate());
					existingSprint.setEndDate(importSprint.getEndDate());
					existingSprint.setCompleteDate(importSprint.getCompleteDate());
			
					sprintsToSave.add(existingSprint);
				} else {

					sprintsToSave.add(importSprint);
				}
			}
		}

		if (!sprintsToSave.isEmpty()) {

			importSprintRepository.saveAll(sprintsToSave);
		}

		return sprintsToSave.size();
	}

	@Override
	public ImportSprint getProjects(int id) {
		Optional<ImportSprint> list = importSprintRepository.findById(id);
		return !list.isPresent() ? null : list.get();
	}

	@Override
	public ImportSprint saveProjects(ImportSprint importSprint) {

		return importSprintRepository.save(importSprint);
	}

	@Override
	public ImportSprint updateClientCredentials(ImportSprint importSprint) {

		return importSprintRepository.save(importSprint);
	}

	@Override
	public List<SprintInfoDto> getAllSprintByProjectId(int projectId, int UserId) {
	    // Retrieve all sprints for the given project
	    List<ImportSprint> sprintList = importSprintRepository.findAllSprintByProjectIdAndUserId(projectId, UserId);
 
	    if (sprintList.isEmpty()) {
	        // If there are no sprints, return an empty list
	        return new ArrayList<>();
	    }
 
	    // Lists to store sprint dates, sprint IDs, and a map of sprint IDs to their dates
	    List<LocalDate> sprintDates = new ArrayList<>();
	    List<Integer> sprintIds = new ArrayList<>();
	    Map<Integer, List<String>> sprintDatesById = new HashMap<>();
 
	    // Iterate through sprintList to gather IDs and dates
	    for (ImportSprint sprint : sprintList) {
	        sprintIds.add(sprint.getSprintId());
	        List<String> datesBetween = getDatesBetween(sprint.getStartDate(), sprint.getEndDate());
	        sprintDatesById.put(sprint.getSprintId(), datesBetween);
	    }
 
	    // List to store SprintInfoDto objects
	    List<SprintInfoDto> sprintDTOList = new ArrayList<>();
 
	    // Iterate through sprintList to create SprintInfoDto objects
	    for (ImportSprint sprint : sprintList) {
	        SprintInfoDto sprintDTO = new SprintInfoDto(sprint.getId(), sprint.getProjectId(), sprint.getSprintId(),
	                sprint.getSprintName(), "0", 0);
	        sprintDTO.setSprintId(sprint.getSprintId());
	        sprintDTO.setSprintName(sprint.getSprintName());
	        sprintDTO.setStartDate(sprint.getStartDate());
	        sprintDTO.setEndDate(sprint.getEndDate());
 
	        int sumOriginalEstimate = 0;
	        double sumAiEstimate = 0.0;
 
	        // Retrieve tasks only for this sprint
	        List<ImportTask> tasksForSprint = importTaskRepository.findAllTaskBySprintId(sprint.getSprintId());
 
	        // Calculate sum of original estimate and AI estimate for tasks in this sprint
	        for (ImportTask task : tasksForSprint) {
	            sumOriginalEstimate += task.getOriginalEstimate();
 
	            String aiEstimateString = task.getAiEstimate();
	            if (aiEstimateString != null && !aiEstimateString.isEmpty()) {
	                if (aiEstimateString.contains(".")) {
	                    double aiEstimateDouble = Double.parseDouble(aiEstimateString);
	                    sumAiEstimate += aiEstimateDouble;
	                } else {
	                    int aiEstimateInt = Integer.parseInt(aiEstimateString);
	                    sumAiEstimate += aiEstimateInt;
	                }
	            } else {
	                sumAiEstimate += 0;
	            }
	        }
 
//	        // Convert estimates to days
//	        sumOriginalEstimate /= 3600;
//	        sumOriginalEstimate /= 8;
//	        sumAiEstimate /= 8;
 
	        sprintDTO.setSumOfOriginalEstimate(sumOriginalEstimate);
 
	        String sumAiEstimateString = String.valueOf(sumAiEstimate);
	        sprintDTO.setSumOfAiEstimate(sumAiEstimateString);
 
	        // Check if there are sprint dates available
	        if (sprintDatesById != null) {
	            Map<String, List<GraphDataDto>> tasksBySprintDate = new HashMap<>();
 
	            // Iterate through each sprint date
	            for (String sprintDate : sprintDatesById.get(sprint.getSprintId())) {
	                // Filter tasks for the current sprint and date
	            	List<ImportTask> tasksForSprintAndDate = tasksForSprint.stream()
	            		    .filter(task -> {
	            		        // Ensure that actualTimedate is not null
	            		        if (task.getActualTimeDate() == null) {
	            		            return false;
	            		        }
	            		        
	            		        // Parse the actualTimedate and sprintDate
	            		        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	            		        LocalDate actualTimeDate = LocalDate.parse(task.getActualTimeDate(), inputFormatter);
	            		        LocalDate parsedSprintDate = LocalDate.parse(sprintDate, inputFormatter);

	            		        // Check if the actualTimedate matches the sprint date
	            		        return actualTimeDate.equals(parsedSprintDate);
	            		    })
	            		    .collect(Collectors.toList());

 
	                // Create GraphDataDto objects for tasks on this sprint and date
	                List<GraphDataDto> taskDto = new ArrayList<>();
 
	                // Initialize variables to calculate aggregated values
	                int totalOriginalEstimateInSeconds = 0;
	                int totalActualTime = 0;
	                double totalAiEstimate = 0.0;
	                int totalRemaining = 0;
	                double totalVelocity = 0.0;
	                double totalRiskFactor = 0.0 ;
	                double totalThreePointEstimate = 0.0;																				
 
	                for (ImportTask task : tasksForSprintAndDate) {
	                	
	                	 String threePointString = task.getThreePointEstimate();
	                        if (threePointString != null && !threePointString.isEmpty()) {
	                            if (threePointString.contains(".")) {
	                                double doubleThreePoint = Double.parseDouble(threePointString);
	                                totalThreePointEstimate += doubleThreePoint;
	                            } else {
	                                int intThreePoint = Integer.parseInt(threePointString);
	                                totalThreePointEstimate += intThreePoint;
	                            }
	                        }
	                        
	                        totalOriginalEstimateInSeconds += task.getOriginalEstimate();
	                        totalActualTime += task.getActual();
	                        String aiEstimateString = task.getAiEstimate();
	                        if (aiEstimateString != null && !aiEstimateString.isEmpty()) {
	                            if (aiEstimateString.contains(".")) {
	                                double aiEstimateDouble = Double.parseDouble(aiEstimateString);
	                                totalAiEstimate += aiEstimateDouble;
	                            } else {
	                                int aiEstimateInt = Integer.parseInt(aiEstimateString);
	                                totalAiEstimate += aiEstimateInt;
	                            }
	                        }
	                       
	                        String velocity = task.getStoryPoints();
	                        if (velocity != null && !velocity.isEmpty()) {
	                            if (velocity.contains(".")) {
	                                double doubleVelocity = Double.parseDouble(velocity);
	                                totalVelocity += doubleVelocity;
	                            } else {
	                                int intVelocity = Integer.parseInt(velocity);
	                                totalVelocity += intVelocity;
	                            }
	                        }
	                        String riskFactorString = task.getRiskFactor();
	                        if (riskFactorString != null && !riskFactorString.isEmpty()) {
	                            if (riskFactorString.contains(".")) {
	                                double doubleRiskFactor = Double.parseDouble(riskFactorString);
	                                totalRiskFactor += doubleRiskFactor;
	                            } else {
	                                int intRiskFactor = Integer.parseInt(riskFactorString);
	                                totalRiskFactor += intRiskFactor;
	                            }
	                        }

	                   
	                }
 
	                // Convert totalActualEstimate from seconds to hours
	                double totalActualEstimateInHours = totalActualTime;
	                
	                double totalRemainingInHours = totalOriginalEstimateInSeconds - totalActualTime;
	                totalRemainingInHours = totalRemainingInHours < 0 ? 0 : totalRemainingInHours;
//	                // Convert totalRemaining from seconds to hours
//	                double totalRemainingInHours = totalRemaining / 3600.0;

 
	                // Create a single GraphDataDto object with aggregated values for this date
	                GraphDataDto graphDataDto = new GraphDataDto();
	                graphDataDto.setActualEstimate((int) totalActualEstimateInHours);
	                graphDataDto.setAiEstimate(String.valueOf(totalAiEstimate));
	                graphDataDto.setRemaining((int) totalRemainingInHours);
	                graphDataDto.setThreePointEstimate(String.valueOf(totalThreePointEstimate));
	                graphDataDto.setRiskFactor(String.valueOf(totalRiskFactor));
	                // Convert totalVelocity to String before setting
	                graphDataDto.setVelocity(String.valueOf(totalVelocity));
	                
	                taskDto.add(graphDataDto);
 
	                // Put the taskDto list into the tasksBySprintDate map for this sprint date
	                tasksBySprintDate.put(sprintDate, taskDto);
	            }
 
 
	            // Set the tasksBySprintDate map to the sprintDTO
	            sprintDTO.setGraphData(tasksBySprintDate);
	        }
 
	        sprintDTOList.add(sprintDTO);
	    }
 
	    return sprintDTOList;
	}



	public List<ImportSprint> removeDuplicateProjects(List<ImportSprint> sprints) {
		if (sprints == null || sprints.isEmpty()) {
			// No projects to check, return an empty list
			return Collections.emptyList();
		}

		Set<String> uniqueSprintsKeys = new HashSet<>();
		List<ImportSprint> uniqueSprints = new ArrayList<>();

		for (ImportSprint sprint : sprints) {
			if (isValidProject(sprint)) {
				String projectKey = generateProjectKey(sprint);

				if (uniqueSprintsKeys.add(projectKey)) {
					// Unique project, add it to the new list
					uniqueSprints.add(sprint);
				} else {
					// Duplicate project key found, handle it as needed
					System.out.println("Warning: Duplicate sprints key found - " + projectKey);
				}
			}
		}

		return uniqueSprints;
	}

	private boolean isValidProject(ImportSprint sprints) {
		return sprints != null && sprints.getProjectId() > 0 && sprints.getSprintName() != null
				&& !sprints.getSprintName().isEmpty() && sprints.getSprintId() > 0;
	}

	private String generateProjectKey(ImportSprint sprints) {

		return sprints.getProjectId() + "-" + sprints.getSprintId() + "-" + sprints.getSprintName();
	}

	  

	    private List<String> getDatesBetween(String startDate, String endDate) {
	        if (startDate == null || endDate == null) {
	            return Collections.emptyList();
	        }
	        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	        LocalDate start;
	        LocalDate end;
	        
	        try {
	            start = LocalDate.parse(startDate, DATE_FORMATTER);
	            end = LocalDate.parse(endDate, DATE_FORMATTER);
	        } catch (DateTimeParseException e) {
	            e.printStackTrace(); // Add appropriate handling
	            return Collections.emptyList();
	        }

	        List<String> dates = new ArrayList<>();
	        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
	            // Skip weekends (Saturday and Sunday)
	            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
	                dates.add(date.format(DATE_FORMATTER));
	            }
	        }
	        return dates;
	    }


}
