package com.es.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AiTaskEstimateRequestDto {

	@JsonProperty("summary")
	private String taskName;
	private String taskDescription;
	private String taskLabel;
	private String originalEstimates;
	private Integer sprintNumber;
	private String taskPriority;
	private String storyPoints;
	private int optimistic;
	private int mostLikely;
	private int pessimistic;
	private String taskId;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTaskLabel() {
		return taskLabel;
	}

	public void setTaskLabel(String taskLabel) {
		this.taskLabel = taskLabel;
	}

	public String getOriginalEstimates() {
		return originalEstimates;
	}

	public void setOriginalEstimates(String originalEstimates) {
		this.originalEstimates = originalEstimates;
	}

	public Integer getSprintNumber() {
		return sprintNumber;
	}

	public void setSprintNumber(Integer sprintNumber) {
		this.sprintNumber = sprintNumber;
	}

	public String getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}

	public String getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(String storyPoints) {
		this.storyPoints = storyPoints;
	}

	public Integer getOptimistic() {
		return optimistic;
	}

	public void setOptimistic(Integer optimistic) {
		this.optimistic = optimistic;
	}

	public Integer getMostLikely() {
		return mostLikely;
	}

	public void setMostLikely(Integer mostLikely) {
		this.mostLikely = mostLikely;
	}

	public Integer getPessimistic() {
		return pessimistic;
	}

	public void setPessimistic(Integer pessimistic) {
		this.pessimistic = pessimistic;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
