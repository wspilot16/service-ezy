package com.wsclient.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsclient.model.ServiceData;
import com.wsclient.util.CustomFactory;
import com.wsclient.util.XmlToJsonConverter;

@Service
public class RestClientService {
	private final Logger logger = LoggerFactory.getLogger(RestClientService.class);

	@Autowired CustomFactory customFactory;
	@Autowired SoapRequestGenerator generator;
	@Autowired SoapResponseGenerator soapResponseGenerator;
	@Autowired XmlToJsonConverter converter;

	public ServiceData post(ServiceData serviceData) throws JsonParseException, JsonMappingException, IOException,
			RestClientException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		if ("SOAP".equalsIgnoreCase(serviceData.getProtocol())) {
			if (serviceData.getRequestBody() == null) {
				try {
					serviceData.setSoapOperations(generator.getWebMethods(serviceData));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println(serviceData.getSoapOperation());
				WsdlRequest wsdlRequest;
				try {
					wsdlRequest = generator.getSoapRquestXML(serviceData);
					serviceData.setResponse(converter.convert(soapResponseGenerator.invokeWebService(wsdlRequest,serviceData)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		} else if ("REST".equalsIgnoreCase(serviceData.getProtocol())) {
			HttpMethod httpMethod = HttpMethod.valueOf(HttpMethod.class, serviceData.getRequestType());
			HttpHeaders headers = new HttpHeaders();
			ResponseEntity<String> responseEntity = null;
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> httpEntity = new HttpEntity<>(serviceData.getRequestBody(), headers);
			CustomFactory customFactory = new CustomFactory();
			responseEntity = customFactory.getRestTemplate().exchange(serviceData.getRequestUri(),
					httpMethod, httpEntity, String.class);
			
			serviceData.setResponse(responseEntity.getBody());
			serviceData.setResponseType("json");
		}
		
		//serviceData.setResponseMap(getMapFromJsonString(responseEntity.getBody()));
		return serviceData;
	}

	private Map<String, String> getMapFromJsonString(String responseBody)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		map = mapper.readValue(responseBody, HashMap.class);
		// NEED TO IMPLEMENT LOGIC TO ITERATE OVER THE GENERATED MAP AND GET
		// RESPONSE ELEMENTS.
		// PLESE EXECUTE WebServiceTester.java TO GET SAMPLE JSON RESPONSE
		// for (String key : map.keySet()) {
		// if(map.get(key) instance of Map)
		// }
		return map;
	}
}
