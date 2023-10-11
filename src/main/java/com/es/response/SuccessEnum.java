package com.es.response;

public enum SuccessEnum {

	/*
	 * Universal Group
	 */
	SUCCESS_LOGIN(200, "Succefull login"), SUCCESS_REGISTER(200, "Successfully Register"),
	SUCCESS_UPDATE(200, "Successfully Update"), SUCCESS_TYPE(200, "success"),
	ALREADY_REGISTER(200, "Email Id already register");

	private Integer code;
	private String message;

	SuccessEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
