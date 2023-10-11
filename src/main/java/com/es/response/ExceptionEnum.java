package com.es.response;

public enum ExceptionEnum {

	/*
	 * Universal Group
	 */
	UNIVERSAL_ERROR(901, "Something went wrong. Please try again"), FAILED_TYPE(400, "failed"),
	UNAUTHORIZED_ERROR(401, "Invalid credentials"), INVALID_USER(402, "Invalid AuthToken"),
	INVALID_SESSION(403, "Invalid User"), DATA_NOT_FOUND(404, "Data Not Found"),
	INVALID_PARAMETER(422, "Invalid Parameter"), INVALID_AUTH_USER(404, "Invalid auth user"),
	USER_NOT_EXIST(404, "user does not exist"), IS_REQUIRED(400, "is required"), INVALID_OLD_PASSWORD(400, "your old password is not match") ;

	private Integer errorCode;
	private String message;

	ExceptionEnum(Integer errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

}
