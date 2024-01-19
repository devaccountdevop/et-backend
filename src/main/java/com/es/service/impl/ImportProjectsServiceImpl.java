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

import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.repository.ImportProjectsRepository;
import com.es.service.ImportProjectsService;

@Service
public class ImportProjectsServiceImpl implements ImportProjectsService {

	@Autowired
	ImportProjectsRepository importProjectsRepository;

	Workbook workbook;

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
	public List<ImportProjects> getProjectDataAsList(InputStream inputStream) {
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
				ImportProjects credentials = createProjectList(rowData);
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

	private ImportProjects createProjectList(List<String> rowData) {
		String value3 = rowData.get(3);

		// Check for null before using as int
		Integer value4 = null;
		if (rowData.get(4) != null && !rowData.get(4).isEmpty()) {
			try {
				value4 = Integer.parseInt(rowData.get(4));
			} catch (NumberFormatException e) {
				// Handle the exception if the value cannot be parsed as an integer
				e.printStackTrace(); // Log the exception or handle it based on your requirements
			}
		}

		String value5 = rowData.get(5);

		// Use value4 as int only if it is not null
		int intValue4 = (value4 != null) ? value4.intValue() : 0;

		return new ImportProjects(intValue4, value5, value3);
	}

	@Override
	public int saveProjectData(List<ImportProjects> projects) {
		if (projects == null || projects.isEmpty()) {
			// No projects to save, return 0
			return 0;
		}

		List<ImportProjects> uniqueProjects = removeDuplicateProjects(projects);

		Set<Integer> projectIds = uniqueProjects.stream()
				.filter(project -> project != null && project.getProjectId() > 0).map(ImportProjects::getProjectId)
				.collect(Collectors.toSet());

		Map<Integer, ImportProjects> existingProjectsMap = importProjectsRepository.findByProjectIdIn(projectIds)
				.stream().collect(Collectors.toMap(ImportProjects::getProjectId, Function.identity()));

		List<ImportProjects> projectsToSave = new ArrayList<>();

		for (ImportProjects project : uniqueProjects) {
			if (project != null && project.getJiraUserName() != null && !project.getJiraUserName().isEmpty()
					&& project.getProjectId() > 0 && project.getProjectName() != null
					&& !project.getProjectName().isEmpty()) {

				ImportProjects existingProject = existingProjectsMap.get(project.getProjectId());

				if (existingProject != null) {
					// Update existing project data
					existingProject.setJiraUserName(project.getJiraUserName());
					existingProject.setProjectName(project.getProjectName());
					projectsToSave.add(existingProject);
				} else {
					// Save new project data to the list
					projectsToSave.add(project);
				}
			}
		}

		if (!projectsToSave.isEmpty()) {
			// Save all projects in one request
			importProjectsRepository.saveAll(projectsToSave);
		}

		return projectsToSave.size();
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
	public List<ImportProjects> getProjectsByJiraUserName(String jiraUserName) {
		return importProjectsRepository.findByJiraUserName(jiraUserName);
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
		return project != null && project.getProjectId() > 0 && project.getJiraUserName() != null
				&& !project.getJiraUserName().isEmpty() && project.getProjectName() != null
				&& !project.getProjectName().isEmpty();
	}

	private String generateProjectKey(ImportProjects project) {
		// Create a composite key using projectId, projectName, and jiraUserName
		return project.getProjectId() + "-" + project.getProjectName() + "-" + project.getJiraUserName();
	}

}
