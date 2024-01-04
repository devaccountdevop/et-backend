package com.es.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table(name = "jira_task")
public class ImportTask {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int sprintId;
	private String summary;
	private String taskId;
	private String taskType;
	private String taskPriority;
	private String taskStatus;
	private String taskDescription;
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estimates_id") // Provide the appropriate column name
    private TaskEstimates estimates;
	private int threePointEstimate;
	private String aiEstimate;
	private int actual;
	@ElementCollection
	private List<String> labels;
	
	private int riskFactor;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSprintId() {
		return sprintId;
	}
	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskPriority() {
		return taskPriority;
	}
	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}


	public TaskEstimates getEstimates() {
		return estimates;
	}
	public void setEstimates(TaskEstimates estimates) {
		this.estimates = estimates;
	}
	//	public List<TaskEstimates> getEstimates() {
//		return estimates;
//	}
//	public void setEstimates(List<TaskEstimates> estimates) {
//		this.estimates = estimates;
//	}
	public int getThreePointEstimate() {
		return threePointEstimate;
	}
	public void setThreePointEstimate(int threePointEstimate) {
		this.threePointEstimate = threePointEstimate;
	}
	public String getAiEstimate() {
		return aiEstimate;
	}
	public void setAiEstimate(String aiEstimate) {
		this.aiEstimate = aiEstimate;
	}
	public int getActual() {
		return actual;
	}
	public void setActual(int actual) {
		this.actual = actual;
	}
	public int getRiskFactor() {
		return riskFactor;
	}
	public void setRiskFactor(int riskFactor) {
		this.riskFactor = riskFactor;
	}
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public ImportTask(int sprintId, String summary, String taskId, String taskType, String taskPriority,
			String taskStatus, List<String> taskLabels, String taskDescription, TaskEstimates estimates, String aiEstimate) {
		super();
		
		this.sprintId = sprintId;
		this.summary = summary;
		this.taskId = taskId;
		this.taskType = taskType;
		this.taskPriority = taskPriority;
		this.taskStatus = taskStatus;
		this.labels = taskLabels;
		this.taskDescription = taskDescription;
		this.estimates = estimates;
		this.aiEstimate = aiEstimate;
	}
	public ImportTask() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ImportTask(int sprintId, String summary, String taskId, String taskType, String taskPriority,
			String taskStatus, String taskDescription) {
		super();
		this.sprintId = sprintId;
		this.summary = summary;
		this.taskId = taskId;
		this.taskType = taskType;
		this.taskPriority = taskPriority;
		this.taskStatus = taskStatus;
		this.taskDescription = taskDescription;
	}
	public ImportTask(int sprintId, String summary, String taskId, String taskDescription, int threePointEstimate, String aiEstimate, int actual, List<String> labels,
			int riskFactor) {
		super();
		this.sprintId = sprintId;
		this.summary = summary;
		this.taskId = taskId;
		this.taskDescription = taskDescription;
		//this.estimates = estimates;
		this.threePointEstimate = threePointEstimate;
		this.aiEstimate = aiEstimate;
		this.actual = actual;
		this.labels = labels;
		this.riskFactor = riskFactor;
	}
	

	
	public ImportTask(int id, int sprintId, String summary, String taskId, String taskType, String taskPriority,
		String taskStatus, String taskDescription, TaskEstimates estimates, int threePointEstimate, String aiEstimate,
		int actual, List<String> labels, int riskFactor) {
	super();
	this.id = id;
	this.sprintId = sprintId;
	this.summary = summary;
	this.taskId = taskId;
	this.taskType = taskType;
	this.taskPriority = taskPriority;
	this.taskStatus = taskStatus;
	this.taskDescription = taskDescription;
	this.estimates = estimates;
	this.threePointEstimate = threePointEstimate;
	this.aiEstimate = aiEstimate;
	this.actual = actual;
	this.labels = labels;
	this.riskFactor = riskFactor;
}
public ImportTask(int sprintId, String summary, String taskId, String taskDescription, int threePointEstimate, String aiEstimate, int actual, List<String> labels,
		int riskFactor,TaskEstimates estimates) {
	super();
	this.sprintId = sprintId;
	this.summary = summary;
	this.taskId = taskId;
	this.taskDescription = taskDescription;
	this.estimates = estimates;
	this.threePointEstimate = threePointEstimate;
	this.aiEstimate = aiEstimate;
	this.actual = actual;
	this.labels = labels;
	this.riskFactor = riskFactor;
}
	
	

}
