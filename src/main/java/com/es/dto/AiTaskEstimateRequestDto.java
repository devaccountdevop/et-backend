package com.es.dto;
 
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
 
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiTaskEstimateRequestDto {
 
	@JsonProperty("summary")
	private String task_id;
	private String task_name;
	private String task_description;
	private String task_label;
	private String planned_estimate;
	private String sprint_number;
	private String priority;
	private String Optimistic_estimate;
	private String Most_likely_estimate;
	private String Pessimistic_estimate;
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getTask_descrption() {
		return task_description;
	}
	public void setTask_descrption(String task_descrption) {
		this.task_description = task_descrption;
	}
	public String getTask_label() {
		return task_label;
	}
	public void setTask_label(String task_label) {
		this.task_label = task_label;
	}
	public String getPlanned_estimate() {
		return planned_estimate;
	}
	public void setPlanned_estimate(String planned_estimate) {
		this.planned_estimate = planned_estimate;
	}
	public String getSprint_number() {
		return sprint_number;
	}
	public void setSprint_number(String sprint_number) {
		this.sprint_number = sprint_number;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getOptimistic_estimate() {
		return Optimistic_estimate;
	}
	public void setOptimistic_estimate(String optimistic_estimate) {
		Optimistic_estimate = optimistic_estimate;
	}
	public String getMost_likely_estimate() {
		return Most_likely_estimate;
	}
	public void setMost_likely_estimate(String most_likely_estimate) {
		Most_likely_estimate = most_likely_estimate;
	}
	public String getPessimistic_estimate() {
		return Pessimistic_estimate;
	}
	public void setPessimistic_estimate(String pessimistic_estimate) {
		Pessimistic_estimate = pessimistic_estimate;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getTask_description() {
		return task_description;
	}
	public void setTask_description(String task_description) {
		this.task_description = task_description;
	}
 
	
}