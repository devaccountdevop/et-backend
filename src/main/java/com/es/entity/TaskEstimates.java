package com.es.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="task_estimates")
public class TaskEstimates {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
		private int low;
		private int realistic;
		private int high;
		private String taskId;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public int getLow() {
			return low;
		}
		public void setLow(int low) {
			this.low = low;
		}
		public int getRealistic() {
			return realistic;
		}
		public void setRealistic(int realistic) {
			this.realistic = realistic;
		}
		public int getHigh() {
			return high;
		}
		public void setHigh(int high) {
			this.high = high;
		}
		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}
		public TaskEstimates(Integer id, int low, int realistic, int high, String taskId) {
			super();
			this.id = id;
			this.low = low;
			this.realistic = realistic;
			this.high = high;
			this.taskId = taskId;
		}
	
		public TaskEstimates() {
			
		}
		
		
		
		
	}
