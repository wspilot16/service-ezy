package com.wsclient.exception;

import com.wsclient.type.ResponseErrorCodeType;

public class CustomException extends Exception{
	private String description;
	private Integer code;
	private Long responseTime;
	
	public CustomException() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public Integer getCode() {
		return code;
	}

	public Long getResponseTime() {
		return responseTime;
	}

	public CustomException(Integer message, String description, Long responseTime) {
		super(ResponseErrorCodeType.getByCode(message).getDescription());
		this.code = message;
		this.description = description;
		this.responseTime = responseTime;
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomException(Throwable cause) {
		super(cause);
	}

	
}
