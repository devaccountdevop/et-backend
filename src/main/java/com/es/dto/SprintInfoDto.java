package com.es.dto;

import java.util.List;

public class SprintInfoDto {

	private Integer id;
	private int projectId;
	private int sprintId;
	private String sprintName;
	private String sumOfAiEstimate;
	private int sumOfOriginalEstimate;
	private List<GraphData> graphData;
	private String startDate;
	private String endDate;

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

	public List<GraphData> getGraphData() {
		return graphData;
	}

	public void setGraphData(List<GraphData> graphData) {
		this.graphData = graphData;
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

	public String getSumOfAiEstimate() {
		return sumOfAiEstimate;
	}

	public void setSumOfAiEstimate(String sumOfAiEstimate) {
		this.sumOfAiEstimate = sumOfAiEstimate;
	}

	public int getSumOfOriginalEstimate() {
		return sumOfOriginalEstimate;
	}

	public void setSumOfOriginalEstimate(int sumOfOriginalEstimate) {
		this.sumOfOriginalEstimate = sumOfOriginalEstimate;
	}

	public SprintInfoDto(Integer id, int projectId, int sprintId, String sprintName, String sumOfAiEstimate,
			int sumOfOriginalEstimate) {
		super();
		this.id = id;
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintName = sprintName;
		this.sumOfAiEstimate = sumOfAiEstimate;
		this.sumOfOriginalEstimate = sumOfOriginalEstimate;
	}

	public SprintInfoDto() {
		super();
	}

}