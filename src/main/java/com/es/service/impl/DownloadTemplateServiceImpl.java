package com.es.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.es.response.DownloadTemplateResponse;
import com.es.service.DownloadTemplateService;

@Service
public class DownloadTemplateServiceImpl implements DownloadTemplateService {

	Workbook workbook;
	@Override
	public DownloadTemplateResponse getExcelFile(InputStream inputStream) {
		
		DownloadTemplateResponse response = new DownloadTemplateResponse();
	        List<List<String>> excelData = new ArrayList<>(); // List to store both header and row data
	        DataFormatter dataFormatter = new DataFormatter();

	        try {
	            workbook = WorkbookFactory.create(inputStream);
	        } catch (EncryptedDocumentException | IOException e) {
	            e.printStackTrace();
	        }

	        Sheet sheet = workbook.getSheetAt(0);

	        List<String> headerRowData = getHeaderRowData(sheet); // Retrieve header row data
	        excelData.add(headerRowData); // Add header data to the list

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

	            // Add row data to the list
	            excelData.add(rowData);
	        }

	        response.setData(excelData);

	        try {
	            workbook.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return response;
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
	
		
	}
	


