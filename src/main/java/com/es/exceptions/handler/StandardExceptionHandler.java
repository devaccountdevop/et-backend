package com.es.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.es.exceptions.AiApiRequestException;
import com.es.exceptions.BadRequestException;
import com.es.exceptions.JiraApiBadRequestException;
import com.es.exceptions.JiraApiException;
import com.es.exceptions.JiraApiUnauthException;
import com.es.response.StandardResponse;

@ControllerAdvice
public class StandardExceptionHandler {
 
	@ExceptionHandler({ JiraApiException.class })
	public ResponseEntity<Object> handleJiraApiException(JiraApiException exception) {
		StandardResponse response = new StandardResponse();
		response.setCode(exception.getErrorCode());
		response.setError(true);
		response.setMessage(exception.getErrorDetails());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler({ JiraApiBadRequestException.class })
	public ResponseEntity<Object> handleJiraApiBadRequestException(JiraApiBadRequestException exception) {
		StandardResponse response = new StandardResponse();
		response.setCode(exception.getErrorCode());
		response.setError(true);
		response.setMessage(exception.getErrorDetails());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler({ JiraApiUnauthException.class })
	public ResponseEntity<Object> handleJiraApiUnauthException(JiraApiUnauthException exception) {
		StandardResponse response = new StandardResponse();
		response.setCode(exception.getErrorCode());
		response.setError(true);
		response.setMessage(exception.getErrorDetails());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	
	@ExceptionHandler({ BadRequestException.class })
	public ResponseEntity<Object> handleBadRequestException(BadRequestException exception) {
		StandardResponse response = new StandardResponse();
		response.setCode(exception.getErrorCode());
		response.setError(true);
		response.setMessage(exception.getErrorDetails());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	@ExceptionHandler({AiApiRequestException.class})
	public ResponseEntity<Object> handleApiException(AiApiRequestException exception) {
		StandardResponse response = new StandardResponse();
		response.setCode(exception.getErrorCode());
		response.setError(true);
		response.setMessage(exception.getErrorDetails());
		return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(response);
	}
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleApiException(Exception exception) {
		StandardResponse response = new StandardResponse();
		response.setCode(500);
		response.setError(true);
		response.setMessage(exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}