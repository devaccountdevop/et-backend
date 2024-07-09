package com.es.dto;

public class AiResponseDto {
	
	private String taskId;
	private String aiEstimate;
	private String threePointEstimate;
	private String riskFactor;
	private String replaced;
	
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getReplaced() {
		return replaced;
	}
	public void setReplaced(String replaced) {
		this.replaced = replaced;
	}
	public String getAiEstimate() {
		return aiEstimate;
	}
	public void setAiEstimate(String aiEstimate) {
		this.aiEstimate = aiEstimate;
	}
	public String getThreePointEstimate() {
		return threePointEstimate;
	}
	public void setThreePointEstimate(String threePointEstimate) {
		this.threePointEstimate = threePointEstimate;
	}
	public String getRiskFactor() {
		return riskFactor;
	}
	public void setRiskFactor(String riskFactor) {
		this.riskFactor = riskFactor;
	}
	
	

}
