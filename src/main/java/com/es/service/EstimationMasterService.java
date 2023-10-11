package com.es.service;

import java.util.List;

import com.es.dto.EmployeeMasterDto;
 import com.es.entity.EstimationMaster;


public interface EstimationMasterService {

	public EstimationMaster getEmployee(int id);

	public List<EstimationMaster> getEmployeeByList(String idListSepByComma);

	public EstimationMaster saveEmployeeMaster(EmployeeMasterDto employeeMasterDto);

	public List<EstimationMaster> getAllEmployee();

	public void EmployeedeleteById(Integer id);

	public void updateEmployeeMaster(EstimationMaster employeeMaster);

	
}
