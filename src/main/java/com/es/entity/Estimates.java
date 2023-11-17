package com.es.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="task_estimates")
public class Estimates {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
		private String low;
		private String realistic;
		private String high;
		private String taskId;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getLow() {
			return low;
		}
		public void setLow(String low) {
			this.low = low;
		}
		public String getRealistic() {
			return realistic;
		}
		public void setRealistic(String realistic) {
			this.realistic = realistic;
		}
		public String getHigh() {
			return high;
		}
		public void setHigh(String high) {
			this.high = high;
		}
		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}
		
	}
