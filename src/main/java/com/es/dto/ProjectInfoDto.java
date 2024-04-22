package com.es.dto;

import java.util.List;

public class ProjectInfoDto {

	private int projectId;
	private String projectName;
	private String jiraUserName;
	private String projectStartDate;
	private String projectEndDate;
	private List<SprintInfoDto> sprintInfo;
	

	

	public List<SprintInfoDto> getSprintInfoDtos() {
		return sprintInfo;
	}

	public void setSprintInfoDtos(List<SprintInfoDto> sprintInfoDtos) {
		this.sprintInfo = sprintInfoDtos;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getJiraUserName() {
		return jiraUserName;
	}

	public void setJiraUserName(String jiraUserName) {
		this.jiraUserName = jiraUserName;
	}

	public String getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(String projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public String getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(String projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public ProjectInfoDto(int projectId, String projectName, String jiraUserName, String projectStartDate,
			String projectEndDate, List<SprintInfoDto> dtos) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.jiraUserName = jiraUserName;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.sprintInfo = dtos;
	}

	public ProjectInfoDto() {
		super();
	}

}
