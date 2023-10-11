/**
 * 
 */
package com.es.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Parveen
 *
 */
public class StandardResponse {

	private int code;
	private String message;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime timestamp = LocalDateTime.now();
	private boolean error = false;
	private Object data;
	
	

	public StandardResponse() {
	}

	public StandardResponse(int code, String message, LocalDateTime timestamp, boolean error, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.timestamp = timestamp;
		this.error = error;
		this.data = data;
	}



	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
