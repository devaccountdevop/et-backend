package com.es.exceptions;

public class JiraApiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String errors;
	private int errorCode;
	private String errorDetails;

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public JiraApiException(String errors, int errorCode, String errorDetails) {
		super();
		this.errors = errors;
		this.errorCode = errorCode;
		this.errorDetails = errorDetails;
	}

	public JiraApiException() {
		super();
	}


	

}
