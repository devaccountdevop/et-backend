package com.es.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.es.entity.ClientCredentials;
import com.es.entity.ImportTask;
import com.es.repository.ClientCredentialsRepository;
import com.es.service.ImportClientService;

@Service
public class ImportClientServiceImpl implements ImportClientService {

	@Autowired
	ClientCredentialsRepository clientCredentialsRepository;
	Workbook workbook;

	@Value("${file.upload-dir}")
	public String EXCEL_FILE_PATH;

	public List<ClientCredentials> getClientDataAsList(InputStream inputStream, int userId) {
	    List<ClientCredentials> invList = new ArrayList<>();
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
	            ClientCredentials credentials = createClientCredentials(rowData, userId);
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


	private ClientCredentials createClientCredentials(List<String> rowData, int userId) {
	    return new ClientCredentials(rowData.get(0), rowData.get(1), rowData.get(2), userId);
	}


	@Override
	public int saveClientData(List<ClientCredentials> clients) {
	    if (clients == null || clients.isEmpty()) {
	        // No clients to save, return 0
	        return 0;
	    }

	    // Extract user IDs from the list
	    Set<Integer> userIds = clients.stream()
	            .filter(client -> client != null && client.getUserId() != 0)
	            .map(ClientCredentials::getUserId)
	            .collect(Collectors.toSet());

	    // Create a set of Jira tokens
	    Set<String> userJiraTokens = clients.stream()
	            .filter(client -> client != null && client.getToken() != null )
	            .map(ClientCredentials::getToken)
	            .collect(Collectors.toSet());

	    // Retrieve existing clients from the database based on user IDs and Jira tokens
	    List<ClientCredentials> existingClients = clientCredentialsRepository.findByClientInAndUserIdIn(userJiraTokens, userIds);

	    Map<String, ClientCredentials> existingClientsMap = existingClients.stream()
	            .collect(Collectors.toMap(client -> client.getUserId() + "-" + client.getToken(), Function.identity()));

	    List<ClientCredentials> clientsToSave = new ArrayList<>();

	    // Iterate over the clients
	    for (ClientCredentials clientCredentials : clients) {
	        String token = clientCredentials.getToken();
	        int userId = clientCredentials.getUserId();

	        if (clientCredentials != null
	                && clientCredentials.getToken() != null && !clientCredentials.getToken().isEmpty()
	                && clientCredentials.getUserId() > 0 && userId > 0
	                && clientCredentials.getClientName() != null && !clientCredentials.getClientName().isEmpty()) {

	            // Check for duplicate user ID and Jira token
	            String key = userId + "-" + token;
	            ClientCredentials existingClient = existingClientsMap.get(key);

	            if (existingClient != null) {
	                // Update existing client data
	                existingClient.setClientName(clientCredentials.getClientName());
	                existingClient.setToken(clientCredentials.getToken());
	                existingClient.setUserId(clientCredentials.getUserId());
	                clientsToSave.add(existingClient);
	            } else {
	                // Save new client data to the list
	                clientsToSave.add(clientCredentials);
	            }
	        }
	        // If any required field is blank or null, skip saving this client
	    }

	    if (!clientsToSave.isEmpty()) {
	        // Save all clients in one request
	        clientCredentialsRepository.saveAll(clientsToSave);
	    }

	    return clientsToSave.size();
	}




	private List<ClientCredentials> createList(List<String> excelData, int noOfColumns, int UserId) {
	    ArrayList<ClientCredentials> clientList = new ArrayList<>();

	    int i = noOfColumns; // Assuming you want to skip the first row

	    do {
	        ClientCredentials client = new ClientCredentials();

	        // Check if the indices are within the bounds of excelData
	        if (i < excelData.size() && i + 1 < excelData.size() && i + 2 < excelData.size()) {
	            client.setClientName(excelData.get(i));
	            client.setJiraUserName(excelData.get(i + 1));
	            client.setToken(excelData.get(i + 2));
	            // Add more columns as needed

	            client.setUserId(UserId);
	            clientList.add(client);
	        } else {
	            // Handle the case where there is no data for the expected columns
	            System.err.println("No data for columns " + i + ", " + (i + 1) + ", " + (i + 2) + " in row " + (i / noOfColumns));
	        }

	        i = i + noOfColumns; // Move to the next set of values in excelData
	    } while (i < excelData.size());

	    return clientList;
	}
//	private List<ClientCredentials> createList(List<String> excelData, int noOfColumns, int userId) {
//	    ArrayList<ClientCredentials> clientList = new ArrayList<>();
//
//	    int i = noOfColumns; // Assuming you want to skip the first row
//
//	    do {
//	        ClientCredentials client = new ClientCredentials();
//
//	        for (int j = 0; j < noOfColumns && i + j < excelData.size(); j++) {
//	            // Check for valid index before accessing to avoid exceeding list size
//	            if (i + j < excelData.size()) {
//	                String cellValue = excelData.get(i + j);
//
//	                // Set values based on column index
//	                switch (j) {
//	                    case 0:
//	                        client.setClientName(cellValue);
//	                        break;
//	                    case 1:
//	                        client.setJiraUserName(cellValue);
//	                        break;
//	                    case 2:
//	                        client.setToken(cellValue);
//	                        break;
//	                    // Add more cases for additional columns
//	                    default:
//	                        // Handle additional columns as needed
//	                }
//	            } else {
//	                // Handle missing value for this specific column
//	                // Adjust based on your desired behavior
//	                // Example:
//	                System.err.println("Missing value for column " + j + " in row " + (i / noOfColumns));
//	            }
//	        }
//
//	        // Set common properties
//	        client.setUserId(userId);
//	        clientList.add(client);
//
//	        i = i + noOfColumns; // Move to the next set of values in excelData
//	    } while (i < excelData.size());
//
//	    return clientList;
//	}


}
