package com.auzmor.restapi.dto;

import com.auzmor.restapi.common.NullToEmptyStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * @author KVK
 * Pojo for generic response from rest api
 */
public class ApiResponse {

	private String message;
	private String error;	
	
	/**
	 * 
	 */
	public ApiResponse() {
		super();
	}
	
	/**
	 * @param message
	 * @param error
	 */
	public ApiResponse(String message, String error) {
		super();
		this.message = message;
		this.error = error;
	}
	public String getMessage() {
		return message == null ? "" : message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error == null ? "" : error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
