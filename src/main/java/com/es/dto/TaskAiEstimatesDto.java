package com.es.dto;

public class TaskAiEstimatesDto {
	
	private String title;
	private String description;
	private int threePointEstimate;
	private int aiEstimate;
	private int lowEstimate;
	private int realisticEstimate;
	private int highEstimate;
	private String taskId;
	
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getThreePointEstimate() {
		return threePointEstimate;
	}
	public void setThreePointEstimate(int threePointEstimate) {
		this.threePointEstimate = threePointEstimate;
	}
	public int getAiEstimate() {
		return aiEstimate;
	}
	public void setAiEstimate(int aiEstimate) {
		this.aiEstimate = aiEstimate;
	}
	public int getLowEstimate() {
		return lowEstimate;
	}
	public void setLowEstimate(int lowEstimate) {
		this.lowEstimate = lowEstimate;
	}
	public int getRealisticEstimate() {
		return realisticEstimate;
	}
	public void setRealisticEstimate(int realisticEstimate) {
		this.realisticEstimate = realisticEstimate;
	}
	public int getHighEstimate() {
		return highEstimate;
	}
	public void setHighEstimate(int highEstimate) {
		this.highEstimate = highEstimate;
	}
	
	

}
