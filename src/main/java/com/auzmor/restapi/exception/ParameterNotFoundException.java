package com.auzmor.restapi.exception;

public class ParameterNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String parameter;

	public ParameterNotFoundException(String parameter) {
		super();
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public ParameterNotFoundException() {
		super();
	}

	public ParameterNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParameterNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterNotFoundException(Throwable cause) {
		super(cause);
	}	
}
