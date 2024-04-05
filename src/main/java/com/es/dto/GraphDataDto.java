package com.es.dto;

public class GraphDataDto {

	
	private int actualEstimate;
	private int remaining;
	private String AiEstimate;
	private String velocity;


	public int getActualEstimate() {
		return actualEstimate;
	}

	public void setActualEstimate(int actualEstimate) {
		this.actualEstimate = actualEstimate;
	}

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public String getAiEstimate() {
		return AiEstimate;
	}

	public void setAiEstimate(String aiEstimate) {
		AiEstimate = aiEstimate;
	}

	public String getVelocity() {
		return velocity;
	}

	public void setVelocity(String velocity) {
		this.velocity = velocity;
	}

	public GraphDataDto(int actualEstimate, int remaining, String aiEstimate, String velocity) {
		super();
		this.actualEstimate = actualEstimate;
		this.remaining = remaining;
		AiEstimate = aiEstimate;
		this.velocity = velocity;
	}

	public GraphDataDto() {
		super();
	}

}
