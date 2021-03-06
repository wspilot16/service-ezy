package com.wsclient.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceData {
	private String wsdlUrl;
	private String context;
	private String bindingName;
	private String selectedMethod;
	private String soapHeader;
	private Map<String, String> httpRequestHeader;
	private List<WebRequestElement> webRequestElementList;
	private Map<String, List<WebRequestElement>> webMethodMap;
	//private Map<String, String> responseMap;
	private Integer responseTime;
	private String errorDescription;
	private Integer errorCode;
	private String response;
	private String rawResponse;
	private String requestUri;
	private String requestType;
	private String requestBody;
	private String responseType;
	private Map<String, String> requestMap;
	private List<KeyValue> headers;
	private String protocol;
	private List<SoapOperation> soapOperations;
	private SoapOperation soapOperation;
	private List<KeyValue> errors;

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	public Map<String, List<WebRequestElement>> getWebMethodMap() {
		if(webMethodMap == null) {
			 webMethodMap = new HashMap<String, List<WebRequestElement>>();
		}
		return webMethodMap;
	}

	public void setWebMethodMap(Map<String, List<WebRequestElement>> webMethodMap) {
		this.webMethodMap = webMethodMap;
	}


	public List<WebRequestElement> getWebRequestElementList() {
		if (webRequestElementList == null) {
			webRequestElementList = new ArrayList<WebRequestElement>();
		}
		return webRequestElementList;
	}

	public void setWebRequestElementList(List<WebRequestElement> webRequestElementList) {
		this.webRequestElementList = webRequestElementList;
	}

	public String getSelectedMethod() {
		return selectedMethod;
	}
	public void setSelectedMethod(String selectedMethod) {
		this.selectedMethod = selectedMethod;
	}

	public String getContext() {
		return context;
	}
	public void setContext(String context) { this.context = context;	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}
	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}

	public String getBindingName() {
		return bindingName;
	}
	public void setBindingName(String bindingName) {
		this.bindingName = bindingName;
	}

	public String getSoapHeader() {
		return soapHeader;
	}
	public void setSoapHeader(String soapHeader) {
		this.soapHeader = soapHeader;
	}

	public Map<String, String> getHttpRequestHeader() {
		return httpRequestHeader;
	}
	public void setHttpRequestHeader(Map<String, String> httpRequestHeader) {
		this.httpRequestHeader = httpRequestHeader;
	}

	public Integer getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Integer responseTime) {	this.responseTime = responseTime; }

	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}

	public String getRequestUri() {
		return requestUri;
	}
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public Map<String, String> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(Map<String, String> requestMap) {
		this.requestMap = requestMap;
	}

	public List<KeyValue> getHeaders() {
		if (headers == null) {
			headers = new ArrayList<>();
		}

		return headers;
	}

	public void setHeaders(List<KeyValue> headers) {
		this.headers = headers;
	}

	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public List<SoapOperation> getSoapOperations() {
		soapOperations = soapOperations == null?new ArrayList<>():soapOperations;
		return soapOperations;
	}

	public void setSoapOperations(List<SoapOperation> soapOperations) {
		this.soapOperations = soapOperations;
	}

	public SoapOperation getSoapOperation() {
		return soapOperation;
	}
	public void setSoapOperation(SoapOperation soapOperation) {
		this.soapOperation = soapOperation;
	}

	public List<KeyValue> getErrors() { errors = errors==null?new ArrayList<>():errors; return this.errors;}
	public void setErrors(List<KeyValue> errors) { this.errors = errors; }
}
