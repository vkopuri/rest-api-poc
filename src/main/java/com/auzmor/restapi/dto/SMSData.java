package com.auzmor.restapi.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SMSData {

	@NotEmpty(message="{smsdata.from.empty}")
    @Size(min=6, max=16, message="{smsdata.from.invalid}")
	private String from;
	
	@NotEmpty(message="{smsdata.to.empty}")
	@Size(min=6, max=16, message="{smsdata.to.invalid}")
	private String to;
	
	@NotEmpty(message="{smsdata.text.empty}")
	@Size(min=1, max=120, message="{smsdata.text.invalid}")
	private String text;
	
	/**
	 * 
	 */
	public SMSData() {
		super();
	}

	/**
	 * @param from
	 * @param to
	 * @param text
	 */
	public SMSData(String from, String to, String text) {
		super();
		this.from = from;
		this.to = to;
		this.text = text;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}	
}
