package com.es.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "estimation_master")
public class EstimationMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String role;
	private char active;
	private String type;
	private String password;
	private String email_id;
	private String First_Name;
	private String Last_Name;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_on;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_on;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public char getActive() {
		return active;
	}
	public void setActive(char active) {
		this.active = active;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getFirst_Name() {
		return First_Name;
	}
	public void setFirst_Name(String first_Name) {
		this.First_Name = first_Name;
	}
	public String getLast_Name() {
		return Last_Name;
	}
	public void setLast_Name(String last_Name) {
		this.Last_Name = last_Name;
	}
	public Date getCreated_on() {
		return created_on;
	}
	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}
	public Date getUpdated_on() {
		return updated_on;
	}
	public void setUpdated_on(Date updated_on) {
		this.updated_on = updated_on;
	}

	@Override
	public String toString() {
		return "EmployeeMaster [id=" + id + ", role=" + role + ", active=" + active + ", type=" + type + ", password="
				+ password + ", email_id=" + email_id + ", First_Name=" + First_Name + ", Last_Name=" + Last_Name
				+ ", created_on=" + created_on + ", updated_on=" + updated_on + "]";
	}
}
