package com.es.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;

import com.es.dto.ProjectGraphDto;
import com.es.dto.ProjectInfoDto;
import com.es.dto.SprintInfoDto;
import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;
import com.es.repository.ImportProjectsRepository;
import com.es.repository.ImportSprintRepository;
import com.es.service.ImportProjectsService;
import com.es.service.ImportSprintService;
import com.es.service.ImportTaskService;

@Service
public class ImportProjectsServiceImpl implements ImportProjectsService {

	@Autowired
	ImportProjectsRepository importProjectsRepository;
	
	@Autowired
	ImportSprintRepository importSprintRepository;
	
	@Autowired
	ImportTaskService importTaskService;

	Workbook workbook;
	
	@Autowired
	ImportSprintService importSprintService;

//	@Override
//	public List<ImportProjects> getProjectDataAsList(InputStream inputStream) {
//		List<String> list = new ArrayList<String>();
//		DataFormatter dataFormatter = new DataFormatter();
//		try {
//			workbook = WorkbookFactory.create(inputStream);
//		} catch (EncryptedDocumentException | IOException e) {
//			e.printStackTrace();
//		}
//		Sheet sheet = workbook.getSheetAt(0);
//		
//		int noOfColumns = sheet.getRow(0).getLastCellNum();
//		for (Row row : sheet) {
//			for (Cell cell : row) {
//				String cellValue = dataFormatter.formatCellValue(cell);
//				list.add(cellValue);
//			}
//		}
//		List<ImportProjects> invList = createList(list, noOfColumns );
//
//		try {
//			workbook.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return invList;
//	
//	}
	public List<ImportProjects> getProjectDataAsList(InputStream inputStream, int userId) {
		List<ImportProjects> invList = new ArrayList<>();
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
				ImportProjects credentials = createProjectList(rowData, userId);
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

	private ImportProjects createProjectList(List<String> rowData, int userId) {
		String value3 = rowData.get(3);

		Integer value4 = null;
		if (rowData.get(4) != null && !rowData.get(4).isEmpty()) {
			try {
				value4 = Integer.parseInt(rowData.get(4));
			} catch (NumberFormatException e) {

				e.printStackTrace();
			}
		}

		String value5 = rowData.get(5);

		int intValue4 = (value4 != null) ? value4.intValue() : 0;

		return new ImportProjects(intValue4, value5, value3, userId);
	}

	@Override
	public List<ImportProjects> saveProjectData(List<ImportProjects> projects, int userId) {
		if (projects == null || projects.isEmpty()) {

			return projects;
		}

		List<ImportProjects> uniqueProjects = removeDuplicateProjects(projects);

		Set<Integer> projectIds = uniqueProjects.stream()
				.filter(project -> project != null && project.getProjectId() > 0).map(ImportProjects::getProjectId)
				.collect(Collectors.toSet());

		Map<Integer, ImportProjects> existingProjectsMap = importProjectsRepository.findByProjectIdInAndUserId(projectIds, userId)
				.stream().collect(Collectors.toMap(ImportProjects::getProjectId, Function.identity()));

		List<ImportProjects> projectsToSave = new ArrayList<>();
		List<ImportProjects> updatedProject = new ArrayList<>();
		for (ImportProjects project : uniqueProjects) {
			if (project != null && project.getProjectId() > 0 && project.getProjectName() != null
					&& !project.getProjectName().isEmpty()) {

				ImportProjects existingProject = existingProjectsMap.get(project.getProjectId());

				if (existingProject != null) {
					if (!existingProject.getProjectName().equals(project.getProjectName())) {
						ImportProjects updatedInstance = new ImportProjects();
						updatedInstance.setProjectId(existingProject.getProjectId());
						updatedInstance.setJiraUserName(existingProject.getJiraUserName());
						updatedInstance.setProjectName(existingProject.getProjectName());

						updatedProject.add(updatedInstance);
					}
					existingProject.setJiraUserName(project.getJiraUserName());
					existingProject.setProjectName(project.getProjectName());
					projectsToSave.add(existingProject);
				} else {

					projectsToSave.add(project);
				}
			}
		}

		if (!projectsToSave.isEmpty()) {

			importProjectsRepository.saveAll(projectsToSave);
		}

		return updatedProject;
	}

	public List<String> modifiedProjectsName(List<String> projectList) {

		return projectList;

	}

	@Override
	public ImportProjects getProjects(int id) {
		Optional<ImportProjects> list = importProjectsRepository.findById(id);
		return !list.isPresent() ? null : list.get();
	}

	@Override
	public ImportProjects saveProjects(ImportProjects importProjects) {
		return importProjectsRepository.save(importProjects);
	}

	@Override
	public ImportProjects updateClientCredentials(ImportProjects importProjects) {
		return importProjectsRepository.save(importProjects);
	}

	@Override
	public List<ProjectInfoDto> getProjectsByJiraUserName(String jiraUserName, int userId) {
	    List<ImportProjects> importProjects = importProjectsRepository.findByJiraUserNameAndUserId(jiraUserName, userId);
	    List<ProjectInfoDto> projectInfoDtoList = new ArrayList<>();

	    for (ImportProjects project : importProjects) {
//	        List<ImportSprint> importSprints = importSprintRepository.findAllSprintByProjectId(project.getProjectId());
	        List<SprintInfoDto> importSprints = importSprintService.getAllSprintByProjectId(project.getProjectId(), userId);
	        String projectStartDate = null;
	        String projectEndDate = null;
	        List<ProjectGraphDto> projectGraphDto = new ArrayList();
	         int newScope = 0;
	        int sumOriginalEstimate = 0;
	        double sumAiEstimate = 0.0;
	        if (importSprints != null && !importSprints.isEmpty()) {
	            // If there are sprints for this project, find project start and end dates
	            projectStartDate = importSprints.get(0).getStartDate();
	            projectEndDate = importSprints.get(0).getEndDate();
	            
	           
		        
	            for (SprintInfoDto sprint : importSprints) {
	            	if(projectStartDate == null && projectEndDate == null) {
	            		projectStartDate = sprint.getStartDate();
	            		 projectEndDate = sprint.getEndDate();
	            	}
	            	ProjectGraphDto graphDto = new ProjectGraphDto();
	            	  newScope =importTaskService.projectScope(project.getProjectId(), sprint.getSprintId(), sprint.getEndDate(),project.getUserId() );
	            	graphDto.setTaskDetails(importTaskService.getAllTaskBySprintId(sprint.getSprintId()));
	            	 sumOriginalEstimate += sprint.getSumOfOriginalEstimate();
	            	String aiEstimateString = sprint.getSumOfAiEstimate();
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
		        
	 
		       
		            graphDto.setProjectScope(newScope);
	            	graphDto.setEndDate(sprint.getEndDate());
	            	graphDto.setProjectId(sprint.getProjectId());
	            	graphDto.setSprintId(sprint.getSprintId());
	            	graphDto.setSprintName(sprint.getSprintName());
	            	graphDto.setStartDate(sprint.getStartDate());
	                String sprintStartDate = sprint.getStartDate();
	                String sprintEndDate = sprint.getEndDate();
	                projectGraphDto.add(graphDto);
	                // Update project start date if needed
	                if (sprintStartDate != null && projectStartDate != null &&
	                        sprintStartDate.compareTo(projectStartDate) < 0) {
	                    projectStartDate = sprintStartDate;
	                }

	                // Update project end date if needed
	                if (sprintEndDate != null && projectEndDate != null &&
	                        sprintEndDate.compareTo(projectEndDate) > 0) {
	                    projectEndDate = sprintEndDate;
	                }
	            }
	            
	        }

	        // Create ProjectInfoDto regardless of whether sprints exist or not
	        ProjectInfoDto projectInfoDto = new ProjectInfoDto(project.getProjectId(), project.getProjectName(), project.getJiraUserName(), projectStartDate, projectEndDate, projectGraphDto, String.valueOf(sumAiEstimate), sumOriginalEstimate);
	        projectInfoDtoList.add(projectInfoDto);
	    }

	    return projectInfoDtoList;
	}

	@Override
	public List<ImportProjects> saveProjectList(List<ImportProjects> importProjects) {
		return importProjectsRepository.saveAll(importProjects);
	}

	public List<ImportProjects> removeDuplicateProjects(List<ImportProjects> projects) {
		if (projects == null || projects.isEmpty()) {
			// No projects to check, return an empty list
			return Collections.emptyList();
		}

		Set<String> uniqueProjectKeys = new HashSet<>();
		List<ImportProjects> uniqueProjects = new ArrayList<>();

		for (ImportProjects project : projects) {
			if (isValidProject(project)) {
				String projectKey = generateProjectKey(project);

				if (uniqueProjectKeys.add(projectKey)) {
					// Unique project, add it to the new list
					uniqueProjects.add(project);
				} else {
					// Duplicate project key found, handle it as needed
					System.out.println("Warning: Duplicate project key found - " + projectKey);
				}
			}
		}

		return uniqueProjects;
	}

	private boolean isValidProject(ImportProjects project) {
		return project != null && project.getProjectId() > 0  && project.getProjectName() != null
				&& !project.getProjectName().isEmpty();
	}

	private String generateProjectKey(ImportProjects project) {
		// Create a composite key using projectId, projectName, and jiraUserName
		return project.getProjectId() + "-" + project.getProjectName() ;
	}

	@Override
	public List<ProjectInfoDto> getProjectsByUserId(Integer userId) {
		
		 List<ImportProjects> importProjects = importProjectsRepository.findByUserId(userId);
		 
		 
		 List<ImportProjects> projectsWithOutClient = importProjects.stream()
			        .filter(project -> project.getJiraUserName() == null || project.getJiraUserName().isEmpty())
			        .collect(Collectors.toList());
		    List<ProjectInfoDto> projectInfoDtoList = new ArrayList<>();

		    for (ImportProjects project : projectsWithOutClient) {
		    	
//		        List<ImportSprint> importSprints = importSprintRepository.findAllSprintByProjectId(project.getProjectId());
		        List<SprintInfoDto> importSprints = importSprintService.getAllSprintByProjectId(project.getProjectId(), project.getUserId());
		        String projectStartDate = null;
		        String projectEndDate = null;
		        List<ProjectGraphDto> projectGraphDto = new ArrayList();
		        int newScope = 0;
		        int sumOriginalEstimate = 0;
		        double sumAiEstimate = 0.0;
		        if (importSprints != null && !importSprints.isEmpty()) {
		            // If there are sprints for this project, find project start and end dates
		            projectStartDate = importSprints.get(0).getStartDate();
		            projectEndDate = importSprints.get(0).getEndDate();
		            
		           
			        
		            for (SprintInfoDto sprint : importSprints) {
		            	if(projectStartDate == null && projectEndDate == null) {
		            		projectStartDate = sprint.getStartDate();
		            		 projectEndDate = sprint.getEndDate();
		            	}
		            	ProjectGraphDto graphDto = new ProjectGraphDto();
		            	 newScope =importTaskService.projectScope(project.getProjectId(), sprint.getSprintId(), sprint.getEndDate(),project.getUserId() );
		            	graphDto.setTaskDetails(importTaskService.getAllTaskBySprintId(sprint.getSprintId()));
		            	 sumOriginalEstimate += sprint.getSumOfOriginalEstimate();
		            	String aiEstimateString = sprint.getSumOfAiEstimate();
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
			        
		 
			       
			            graphDto.setProjectScope(newScope);
		            	graphDto.setEndDate(sprint.getEndDate());
		            	graphDto.setProjectId(sprint.getProjectId());
		            	graphDto.setSprintId(sprint.getSprintId());
		            	graphDto.setSprintName(sprint.getSprintName());
		            	graphDto.setStartDate(sprint.getStartDate());
		                String sprintStartDate = sprint.getStartDate();
		                String sprintEndDate = sprint.getEndDate();
		                projectGraphDto.add(graphDto);
		                // Update project start date if needed
		                if (sprintStartDate != null && projectStartDate != null &&
		                        sprintStartDate.compareTo(projectStartDate) < 0) {
		                    projectStartDate = sprintStartDate;
		                }

		                // Update project end date if needed
		                if (sprintEndDate != null && projectEndDate != null &&
		                        sprintEndDate.compareTo(projectEndDate) > 0) {
		                    projectEndDate = sprintEndDate;
		                }
		            }
		            
		        }

		        // Create ProjectInfoDto regardless of whether sprints exist or not
		        ProjectInfoDto projectInfoDto = new ProjectInfoDto(project.getProjectId(), project.getProjectName(), project.getJiraUserName(), projectStartDate, projectEndDate, projectGraphDto, String.valueOf(sumAiEstimate), sumOriginalEstimate);
		        projectInfoDtoList.add(projectInfoDto);
		    }

		    return projectInfoDtoList;
		}

	@Override
	public ProjectInfoDto getProjectByProjectId(int projectId, int userId) {
	    // Find the project by its ID
	    ImportProjects downloadProject = importProjectsRepository.findByProjectIdAndUserId(projectId, userId);
 
	   
 
	    // Retrieve sprints for the project
	    List<SprintInfoDto> importSprints = importSprintService.getAllSprintByProjectId(downloadProject.getProjectId(), userId);
 
	    String projectStartDate = null;
	    String projectEndDate = null;
	    List<ProjectGraphDto> projectGraphDto = new ArrayList<>();
 
	    int sumOriginalEstimate = 0;
	    double sumAiEstimate = 0.0;
 
	    if (importSprints != null && !importSprints.isEmpty()) {
	        projectStartDate = importSprints.get(0).getStartDate();
	        projectEndDate = importSprints.get(0).getEndDate();
 
	        for (SprintInfoDto sprint : importSprints) {
	            if (projectStartDate == null && projectEndDate == null) {
	                projectStartDate = sprint.getStartDate();
	                projectEndDate = sprint.getEndDate();
	            }
 
	            ProjectGraphDto graphDto = new ProjectGraphDto();
	            graphDto.setTaskDetails(importTaskService.getAllTaskBySprintId(sprint.getSprintId()));
	            sumOriginalEstimate += sprint.getSumOfOriginalEstimate();
 
	            String aiEstimateString = sprint.getSumOfAiEstimate();
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
 
	            graphDto.setEndDate(sprint.getEndDate());
	            graphDto.setProjectId(sprint.getProjectId());
	            graphDto.setSprintId(sprint.getSprintId());
	            graphDto.setSprintName(sprint.getSprintName());
	            graphDto.setStartDate(sprint.getStartDate());
	            String sprintStartDate = sprint.getStartDate();
	            String sprintEndDate = sprint.getEndDate();
	            projectGraphDto.add(graphDto);
 
	            if (sprintStartDate != null && projectStartDate != null &&
	                sprintStartDate.compareTo(projectStartDate) < 0) {
	                projectStartDate = sprintStartDate;
	            }
 
	            if (sprintEndDate != null && projectEndDate != null &&
	                sprintEndDate.compareTo(projectEndDate) > 0) {
	                projectEndDate = sprintEndDate;
	            }
	        }
	    }
 
	    ProjectInfoDto projectInfoDto = new ProjectInfoDto(
	    		downloadProject.getProjectId(),
	    		downloadProject.getProjectName(),
	    		downloadProject.getJiraUserName(),
	        projectStartDate,
	        projectEndDate,
	        projectGraphDto,
	        String.valueOf(sumAiEstimate),
	        sumOriginalEstimate
	    );
 
	    return projectInfoDto;
	}	


	

}
