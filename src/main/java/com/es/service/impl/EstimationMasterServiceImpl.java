package com.es.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.es.entity.EstimationMaster;
import com.es.repository.EstimationMasterRepository;
import com.es.service.EstimationMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.es.dto.EmployeeMasterDto;

 
@Service
public class EstimationMasterServiceImpl implements EstimationMasterService {

	@Autowired
	private EstimationMasterRepository eRepo;

	@Override
	public EstimationMaster getEmployee(int id) {
		Optional<EstimationMaster> EmployeeList = this.eRepo.findById(id);
		return ! EmployeeList.isPresent() ? null : EmployeeList.get();
	}

	@Override
	public List<EstimationMaster> getEmployeeByList(String idListSepByComma) {
		return null;
	}

	@Override
	public EstimationMaster saveEmployeeMaster(EmployeeMasterDto employeeMasterDto) {
		return null;
	}

	@Override
	public List<EstimationMaster> getAllEmployee() {
		return null;
	}

	@Override
	public void EmployeedeleteById(Integer id) {

	}

	@Override
	public void updateEmployeeMaster(EstimationMaster employeeMaster) {

	}


}
