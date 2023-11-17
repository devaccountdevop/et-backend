package com.es.dto;

public class AiEstimatesDto {
	
	private int id;
	private String Aiestimates;
	private int test;
	private String taskId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAiestimates() {
		return Aiestimates;
	}
	public void setAiestimates(String aiestimates) {
		Aiestimates = aiestimates;
	}
	public int getTest() {
		return test;
	}
	public void setTest(int test) {
		this.test = test;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public AiEstimatesDto(int id, String aiestimates, int test, String taskId) {
		super();
		this.id = id;
		Aiestimates = aiestimates;
		this.test = test;
		this.taskId = taskId;
	}
	public AiEstimatesDto() {
		// TODO Auto-generated constructor stub
	}
      
}
