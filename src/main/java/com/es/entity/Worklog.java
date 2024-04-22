package com.es.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Worklog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String taskid;
	private String createdDate;
	private String updatedDate;
	private String startedDate;
	private String timeSpent;
	private int timeSpentSeconds;

	@ManyToOne
    @JoinColumn(name = "import_task_id")
	private ImportTask importTask;

	public ImportTask getImportTask() {
		return importTask;
	}

	public void setImportTask(ImportTask importTask) {
		this.importTask = importTask;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(String startedDate) {
		this.startedDate = startedDate;
	}

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}

	public int getTimeSpentSeconds() {
		return timeSpentSeconds;
	}

	public void setTimeSpentSeconds(int timeSpentSeconds) {
		this.timeSpentSeconds = timeSpentSeconds;
	}

	public Worklog(String taskid, String createdDate, String updatedDate, String startedDate, String timeSpent,
			int timeSpentSeconds) {
		super();
		this.taskid = taskid;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.startedDate = startedDate;
		this.timeSpent = timeSpent;
		this.timeSpentSeconds = timeSpentSeconds;
	}
	
	

	public Worklog(String taskid, String createdDate, String updatedDate, String startedDate, String timeSpent,
			int timeSpentSeconds, ImportTask importTask) {
		super();
		this.taskid = taskid;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.startedDate = startedDate;
		this.timeSpent = timeSpent;
		this.timeSpentSeconds = timeSpentSeconds;
		this.importTask = importTask;
	}

	public Worklog() {
		super();
	}

}
