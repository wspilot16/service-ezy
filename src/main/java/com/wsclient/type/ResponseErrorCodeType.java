package com.wsclient.type;

public enum ResponseErrorCodeType {
	RESPONSE500(500, "Internal Server Error"),
	RESPONSE404(404, "Not Found"),
	TIMEOUT(990, "Time Out"),
	UNKNOWN(999, "Unknown Reason");

	Integer code;
	String description;
	
	private ResponseErrorCodeType(Integer code, String description) {
		this.code = code;
		this.description = description;
	}

	public Integer getCode() {
		return code;
	}


	public String getDescription() {
		return description;
	}


	public static Boolean containsCode(Integer code) {
		for (ResponseErrorCodeType responseCodeType : ResponseErrorCodeType.values()) {
			if (responseCodeType.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static ResponseErrorCodeType getByCode(Integer code) {
		for (ResponseErrorCodeType responseCodeType : ResponseErrorCodeType.values()) {
			if (responseCodeType.getCode().equals(code)) {
				return responseCodeType;
			}
		}
		return null;
	}
}
