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

import com.es.dto.SprintInfoDto;
import com.es.entity.ClientCredentials;
import com.es.entity.ImportProjects;
import com.es.entity.ImportSprint;
import com.es.entity.ImportTask;
import com.es.repository.ImportSprintRepository;
import com.es.repository.ImportTaskRepository;
import com.es.service.ImportSprintService;

@Service
public class ImportSprintServiceImpl implements ImportSprintService {

	@Autowired
	ImportSprintRepository importSprintRepository;

	@Autowired
	ImportTaskRepository importTaskRepository;
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

		List<String> headerRowData = getHeaderRowData(sheet);

		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext()) {
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
		int value3 = 0;
		int value4 = 0;
		String value5 = rowData.get(7);

		if (rowData.get(6) != null && !rowData.get(6).isEmpty()) {
			try {
				value3 = Integer.parseInt(rowData.get(6));
			} catch (NumberFormatException e) {

			}
		}

		if (rowData.get(4) != null && !rowData.get(4).isEmpty()) {
			try {
				value4 = Integer.parseInt(rowData.get(4));
			} catch (NumberFormatException e) {

			}
		}

		return new ImportSprint(value4, value3, value5);
	}

	@Override
	public int saveSprintData(List<ImportSprint> importSprints) {
		if (importSprints == null || importSprints.isEmpty()) {

			return 0;
		}
		List<ImportSprint> uniqueSprints = removeDuplicateProjects(importSprints);

		Set<Integer> sprintIds = uniqueSprints.stream().filter(sprint -> sprint != null && sprint.getSprintId() > 0)
				.map(ImportSprint::getSprintId).collect(Collectors.toSet());

		Map<Integer, ImportSprint> existingSprintsMap = importSprintRepository.findBySprintIdIn(sprintIds).stream()
				.collect(Collectors.toMap(ImportSprint::getSprintId, Function.identity()));

		List<ImportSprint> sprintsToSave = new ArrayList<>();

		for (ImportSprint importSprint : uniqueSprints) {
			int sprintId = importSprint.getSprintId();

			if (importSprint != null && importSprint.getSprintName() != null && !importSprint.getSprintName().isEmpty()
					&& importSprint.getProjectId() > 0 && importSprint.getSprintId() > 0) {

				ImportSprint existingSprint = existingSprintsMap.get(sprintId);

				if (existingSprint != null) {

					existingSprint.setProjectId(importSprint.getProjectId());
					existingSprint.setSprintName(importSprint.getSprintName());
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
	public List<SprintInfoDto> getAllSprintByProjectId(int projectId) {
		List<ImportSprint> sprintList = importSprintRepository.findAllSprintByProjectId(projectId);
		if (sprintList.isEmpty()) {
			return new ArrayList<>();
		}
		List<Integer> sprintIds = new ArrayList<>();
		for (ImportSprint sprint : sprintList) {
			sprintIds.add(sprint.getSprintId());
		}

		List<ImportTask> taskList = importTaskRepository.findAllBySprintIds(sprintIds);

		List<SprintInfoDto> sprintDTOList = new ArrayList<>();
		for (ImportSprint sprint : sprintList) {
			SprintInfoDto sprintDTO = new SprintInfoDto(sprint.getId(), sprint.getProjectId(), sprint.getSprintId(),
					sprint.getSprintName(), "0", 0);
			sprintDTO.setSprintId(sprint.getSprintId());
			sprintDTO.setSprintName(sprint.getSprintName());

			int sumOriginalEstimate = 0;
			double sumAiEstimate = 0.0;
			for (ImportTask task : taskList) {
				if (task.getSprintId() == sprint.getSprintId()) {
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
			}

			sumOriginalEstimate /= 3600;
			sumOriginalEstimate /= 8;
			sumAiEstimate /= 8;

			sprintDTO.setSumOfOriginalEstimate(sumOriginalEstimate);

			String sumAiEstimateString = String.valueOf(sumAiEstimate);
			sprintDTO.setSumOfAiEstimate(sumAiEstimateString);

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

}
