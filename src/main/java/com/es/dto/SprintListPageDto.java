package com.es.dto;

	import java.util.List;

import com.es.entity.TaskEstimates;
	 
	public class SprintListPageDto {
		private String id;
		private String title;
		private String description;
		private List<TaskEstimates>	estimates;
		private int threePointEstimate;
		private String aiEstimate;
		private int actual;
		private List<String> labels;
		private int riskFactor;
		
		
		
		

		public int getRiskFactor() {
			return riskFactor;
		}

		public void setRiskFactor(int riskFactor) {
			this.riskFactor = riskFactor;
		}

		public SprintListPageDto(String id, String title, String description, List<TaskEstimates> estimates,
				int threePointEstimate, String aiEstimate, int actual, List<String> labels) {
			super();
			this.id = id;
			this.title = title;
			this.description = description;
			this.estimates = estimates;
			this.threePointEstimate = threePointEstimate;
			this.aiEstimate = aiEstimate;
			this.actual = actual;
			this.labels = labels;
		}
		
		public SprintListPageDto(String id, String title, String description, List<TaskEstimates> estimates, List<String> labels, String ai, int riskFactor) {
			super();
			this.id = id;
			this.title = title;
			this.description = description;
			this.estimates = estimates;
			this.threePointEstimate = threePointEstimate;
			this.aiEstimate = aiEstimate;
			this.actual = actual;
			this.labels = labels;
			this.aiEstimate = ai;
			this.riskFactor = riskFactor;
		}


		public List<TaskEstimates> getEstimates() {
			return estimates;
		}


		public void setEstimates(List<TaskEstimates> estimates) {
			this.estimates = estimates;
		}


		public List<String> getLabels() {
			return labels;
		}


		public void setLabels(List<String> labels) {
			this.labels = labels;
		}


		

	 



		
	 
	 
		public String getId() {
			return id;
		}
		public void setId(String id) {
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
	}

