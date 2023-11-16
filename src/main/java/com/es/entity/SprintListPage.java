package com.es.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SprintListPage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private String description;
	private String label;
	private String estimates;
	private int threePointEstimate;
	private int aiEstimate;
	private int actual;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getEstimates() {
		return estimates;
	}

	public void setEstimates(String estimates) {
		this.estimates = estimates;
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

	public int getActual() {
		return actual;
	}

	public void setActual(int actual) {
		this.actual = actual;
	}

}
