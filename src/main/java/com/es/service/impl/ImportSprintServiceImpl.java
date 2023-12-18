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
import com.es.entity.ImportSprint;
import com.es.repository.ImportSprintRepository;
import com.es.service.ImportSprintService;

@Service
public class ImportSprintServiceImpl implements ImportSprintService {

	@Autowired
	ImportSprintRepository importSprintRepository;
	Workbook workbook;

	@Override
	public List<ImportSprint> getSprintDataAsList(InputStream inputStream) {
		List<ImportSprint> invList = new ArrayList<>();
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
				ImportSprint credentials = createSprintList(rowData);
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
	private ImportSprint createSprintList(List<String> rowData) {
	    int value3 = 0; // Default value if rowData.get(6) is null
	    int value4 = 0; // Default value if rowData.get(4) is null
	    String value5 = rowData.get(7);

	    if (rowData.get(6) != null && !rowData.get(6).isBlank()) {
	        try {
	            value3 = Integer.parseInt(rowData.get(6));
	        } catch (NumberFormatException e) {
	            // Handle the case where the string cannot be parsed to an integer
	            // You can log the error or take appropriate action based on your requirements
	        }
	    }

	    if (rowData.get(4) != null && !rowData.get(4).isBlank()) {
	        try {
	            value4 = Integer.parseInt(rowData.get(4));
	        } catch (NumberFormatException e) {
	            // Handle the case where the string cannot be parsed to an integer
	            // You can log the error or take appropriate action based on your requirements
	        }
	    }

	    return new ImportSprint(value4, value3, value5);
	}

	@Override
	public int saveSprintData(List<ImportSprint> importSprints) {
	    if (importSprints == null || importSprints.isEmpty()) {
	        // No sprints to save, return 0
	        return 0;
	    }
	    List<ImportSprint> uniqueSprints = removeDuplicateProjects(importSprints);
	    // Extract sprint IDs from the list
	    Set<Integer> sprintIds = uniqueSprints.stream()
	            .filter(sprint -> sprint != null && sprint.getSprintId() > 0)
	            .map(ImportSprint::getSprintId)
	            .collect(Collectors.toSet());

	    // Retrieve existing sprints from the database based on sprint IDs
	    Map<Integer, ImportSprint> existingSprintsMap = importSprintRepository
	            .findBySprintIdIn(sprintIds)
	            .stream()
	            .collect(Collectors.toMap(ImportSprint::getSprintId, Function.identity()));

	    List<ImportSprint> sprintsToSave = new ArrayList<>();

	    // Iterate over the import sprints
	    for (ImportSprint importSprint : uniqueSprints) {
	        int sprintId = importSprint.getSprintId();

	        if (importSprint != null && importSprint.getSprintName() != null && !importSprint.getSprintName().isBlank()
	                && importSprint.getProjectId() > 0
	                && importSprint.getSprintId() > 0) {

	            // Find the sprint in the existing map
	            ImportSprint existingSprint = existingSprintsMap.get(sprintId);

	            if (existingSprint != null) {
	                // Update existing sprint data
	                existingSprint.setProjectId(importSprint.getProjectId());
	                existingSprint.setSprintName(importSprint.getSprintName());
	                sprintsToSave.add(existingSprint);
	            } else {
	                // Save new sprint data to the list
	                sprintsToSave.add(importSprint);
	            }
	        }
	    }

	    if (!sprintsToSave.isEmpty()) {
	        // Save all sprints in one request
	        importSprintRepository.saveAll(sprintsToSave);
	    }

	    return sprintsToSave.size();
	}


	

	@Override
	public ImportSprint getProjects(int id) {
		Optional<ImportSprint> list = importSprintRepository.findById(id);
		return ! list.isPresent() ? null: list.get();
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
	public List<ImportSprint> getAllSprintByProjectId(int projectId) {
		return importSprintRepository.findAllSprintByProjectId(projectId);
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
				&& !sprints.getSprintName().isBlank() && sprints.getSprintId()>0 ;
	}

	private String generateProjectKey(ImportSprint sprints) {
		
		return sprints.getProjectId() + "-" + sprints.getSprintId() + "-" + sprints.getSprintName();
	}
	
	
	
}
