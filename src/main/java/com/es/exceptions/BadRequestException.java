package com.es.exceptions;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Object[] errors;
	private int errorCode;
	private String errorDetails;

	public BadRequestException(Object[] errors, int errorCode, String errorDetails) {
		super();
		this.errors = errors;
		this.errorCode = errorCode;
		this.errorDetails = errorDetails;
	}

	public Object[] getErrors() {
		return errors;
	}

	public void setErrors(Object[] errors) {
		this.errors= errors;
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

}
