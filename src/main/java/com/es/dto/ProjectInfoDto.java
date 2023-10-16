package com.es.dto;

public class ProjectInfoDto {
	
	    private int id;
	    private String name;

	 
	 

	    // Getters and setters for id and name

	    public ProjectInfoDto(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }
	}

