package com.es.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	@Column(length=3000)
	private String taskDescription;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "estimates_id") // Provide the appropriate column name
	private TaskEstimates estimates;
	private String threePointEstimate;
	private String aiEstimate;
	private int actual;
	@ElementCollection
	private List<String> labels;

	private String riskFactor;
	private int originalEstimate;
	private String storyPoints;
	
	private String assignee;
	private String creationDate;
	@OneToMany(mappedBy = "importTask", cascade = CascadeType.PERSIST)
	 @JsonIgnoreProperties("importTask")
	private List<Worklog> worklogs;
	
	

	public List<Worklog> getWorklogs() {
		return worklogs;
	}

	public void setWorklogs(List<Worklog> worklogs) {
		this.worklogs = worklogs;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		creationDate = creationDate;
	}


	public ImportTask(int id, int sprintId, String summary, String taskId, String taskType, String taskPriority,
			String taskStatus, String taskDescription, TaskEstimates estimates, String threePointEstimate, String aiEstimate,
			int actual, List<String> labels, String riskFactor, int originalEstimate, String storyPoints) {
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
		this.originalEstimate = originalEstimate;
		this.storyPoints = storyPoints;
	}

	public int getOriginalEstimate() {
		return originalEstimate;
	}

	public void setOriginalEstimate(int originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	public String getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(String storyPoints) {
		this.storyPoints = storyPoints;
	}

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

	// public List<TaskEstimates> getEstimates() {
//		return estimates;
//	}
//	public void setEstimates(List<TaskEstimates> estimates) {
//		this.estimates = estimates;
//	}
	public String getThreePointEstimate() {
		return threePointEstimate;
	}

	public void setThreePointEstimate(String threePointEstimate) {
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

	public String getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(String riskFactor) {
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
			String taskStatus, List<String> taskLabels, String taskDescription, TaskEstimates estimates,String aiEstimate) {
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

	public ImportTask(int sprintId, String summary, String taskId, String taskDescription, String threePointEstimate,
			String aiEstimate, int actual, List<String> labels, String riskFactor) {
		super();
		this.sprintId = sprintId;
		this.summary = summary;
		this.taskId = taskId;
		this.taskDescription = taskDescription;
		// this.estimates = estimates;
		this.threePointEstimate = threePointEstimate;
		this.aiEstimate = aiEstimate;
		this.actual = actual;
		this.labels = labels;
		this.riskFactor = riskFactor;
	}

	public ImportTask(int id, int sprintId, String summary, String taskId, String taskType, String taskPriority,
			String taskStatus, String taskDescription, TaskEstimates estimates, String threePointEstimate, String aiEstimate,
			int actual, List<String> labels, String riskFactor) {
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

	public ImportTask(int sprintId, String summary, String taskId, String taskDescription, 
			String aiEstimate, int actual, List<String> labels, TaskEstimates estimates, String storyPoints, int originalEstimate, String priority, String assignee, String creationDate, List<Worklog> worklogs) {
		super();
		this.sprintId = sprintId;
		this.summary = summary;
		this.taskId = taskId;
		this.taskDescription = taskDescription;
		this.estimates = estimates;
		
		this.aiEstimate = aiEstimate;
		this.actual = actual;
		this.labels = labels;
		
		this.storyPoints = storyPoints;
		this.originalEstimate = originalEstimate;
		this.taskPriority = priority;
		this.assignee = assignee;
		this.creationDate = creationDate;
		this.worklogs = worklogs;
	}

}
