package com.wsclient.model;

import java.util.ArrayList;
import java.util.List;

public class SoapOperation {

	private String name;
	private String requestTemplate;
	private List<WebRequestElement> requestElements;
	
	public SoapOperation() {
		// TODO Auto-generated constructor stub
	}
	
	public SoapOperation(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRequestTemplate() {
		return requestTemplate;
	}
	public void setRequestTemplate(String requestTemplate) {
		this.requestTemplate = requestTemplate;
	}

	public List<WebRequestElement> getRequestElements() {
		if (requestElements == null) {
			requestElements = new ArrayList<>();
		}
		return requestElements;
	}

	public void setRequestElements(List<WebRequestElement> requestElements) {
		this.requestElements = requestElements;
	}
}
