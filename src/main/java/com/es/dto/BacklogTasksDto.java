package com.es.dto;

import java.util.List;

public class BacklogTasksDto {
	
	private int id;
	private List<SprintListPageDto> taskList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<SprintListPageDto> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<SprintListPageDto> taskList) {
		this.taskList = taskList;
	}
	
	

}
