package com.es.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jira_sprint")
public class ImportSprint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private int projectId;
	private int sprintId;
	private String sprintName;
	private String startDate;
	private String endDate;
	private String completeDate;
	private String createdDate;
	private int userId;
	
	
	
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getSprintId() {
		return sprintId;
	}
	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}
	public String getSprintName() {
		return sprintName;
	}
	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}
	public ImportSprint(int projectId, int sprintId, String sprintName) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintName = sprintName;
	}
	

	
	
	
	public ImportSprint(int projectId, int sprintId, String sprintName, String startDate, String endDate,
			String completeDate, String createdDate, int userId) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintName = sprintName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.completeDate = completeDate;
		this.createdDate = createdDate;
		this.userId = userId;
	}
	public ImportSprint(int projectId, int sprintId, String sprintName, String startDate, String endDate,
			String completeDate, int userId) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintName = sprintName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.completeDate = completeDate;
		this.userId = userId;
	}
	public ImportSprint() {
		
	}
	
	
	
	
}

