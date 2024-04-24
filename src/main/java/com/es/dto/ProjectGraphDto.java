package com.es.dto;

import java.util.List;

import com.es.entity.ImportTask;

public class ProjectGraphDto {
	
	private int projectId;
	private int sprintId;
	private String sprintName;
	private String startDate;
	private String endDate;
	private List<ImportTask> taskDetails;
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
	public List<ImportTask> getTaskDetails() {
		return taskDetails;
	}
	public void setTaskDetails(List<ImportTask> taskDetails) {
		this.taskDetails = taskDetails;
	}
	public ProjectGraphDto(int projectId, int sprintId, String sprintName, String startDate, String endDate,
			List<ImportTask> taskDetails) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintName = sprintName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskDetails = taskDetails;
	}
	public ProjectGraphDto() {
		super();
	}
	
	

}
