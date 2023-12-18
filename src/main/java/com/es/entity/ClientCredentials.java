package com.es.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name = "jira_credentials")
public class ClientCredentials {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String clientName;
	private String jiraUserName;
	private String token;
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
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getJiraUserName() {
		return jiraUserName;
	}
	public void setJiraUserName(String jiraUserName) {
		this.jiraUserName = jiraUserName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public ClientCredentials(String clientName, String jiraUserName, String token, int userId) {
		super();
		this.clientName = clientName;
		this.jiraUserName = jiraUserName;
		this.token = token;
		this.userId = userId;
	}
	public ClientCredentials() {
		// TODO Auto-generated constructor stub
	}
	
	

}
