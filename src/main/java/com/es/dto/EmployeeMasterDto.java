package com.es.dto;


import java.util.Date;

public class EmployeeMasterDto {
	
	private Integer id;
	private String role;
	private Character active;
	private String type;
	private String password;
	private Date created_on;
	private Date updated_on;
	private String email_id;
	private String First_Name;
	private String Last_Name;
	
	
	public EmployeeMasterDto() {}
	
	public EmployeeMasterDto(Integer id, String role, Character active, String type, String password, Date created_on,
			Date updated_on, String Last_Name, String First_Name, String email_id) {
		super();
		this.id = id;
		this.role = role;
		this.active = active;
		this.type = type;
		this.password = password;
		this.created_on = created_on;
		this.updated_on = updated_on;
		this.email_id = email_id;
		this.First_Name = First_Name;
		this.Last_Name = Last_Name;
	}
	
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
	public Character getActive() {
		return active;
	}
	public void setActive(Character active) {
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
		First_Name = first_Name;
	}

	public String getLast_Name() {
		return Last_Name;
	}

	public void setLast_Name(String last_Name) {
		Last_Name = last_Name;
	}

	@Override
	public String toString() {
		return "EmployeeMasterDto [id=" + id + ", role=" + role + ", active=" + active + ", type=" + type
				+ ", password=" + password + ", created_on=" + created_on + ", updated_on=" + updated_on + ", email_id="
				+ email_id + ", First_Name=" + First_Name + ", Last_Name=" + Last_Name + "]";
	}


	
}
