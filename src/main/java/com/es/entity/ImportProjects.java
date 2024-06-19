package com.es.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name ="jira_project")
public class ImportProjects {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
		private int projectId;
		private String projectName;
		private String jiraUserName;
		private int userId;
		
		
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public int getProjectId() {
			return projectId;
		}
		public void setProjectId(int projectId) {
			this.projectId = projectId;
		}
		public String getProjectName() {
			return projectName;
		}
		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		public String getJiraUserName() {
			return jiraUserName;
		}
		public void setJiraUserName(String jiraUserName) {
			this.jiraUserName = jiraUserName;
		}
		public ImportProjects(int projectId, String projectName, String jiraUserName) {
			super();
			this.projectId = projectId;
			this.projectName = projectName;
			this.jiraUserName = jiraUserName;
		}
		public ImportProjects() {
			
		}
		public ImportProjects(int projectId, String projectName, String jiraUserName, int userId) {
			super();
			this.projectId = projectId;
			this.projectName = projectName;
			this.jiraUserName = jiraUserName;
			this.userId = userId;
		}
		
		
		
}
