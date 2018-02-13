package demo;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.wsclient.model.KeyValue;
import com.wsclient.model.ServiceData;
import com.wsclient.service.RestClientService;
import com.wsclient.util.CustomFactory;

public class WebServiceTester {
	private static final Logger log = LoggerFactory.getLogger(WebServiceTester.class);

	public static void main(String[] args) {
		try {
			testRestWebService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testRestWebService() throws JsonParseException, JsonMappingException, IOException,
	KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		ResponseEntity<String> response = null;
		try{
			RestClientService restClientService = new RestClientService();
			CustomFactory customFactory = new CustomFactory();
			ServiceData serviceData = new ServiceData();
			serviceData.setProtocol("REST");
			serviceData.setRequestType(HttpMethod.GET.name());
			// serviceData.setRequestBody("{\"name\": \"morpheus\","
			// +"\"job\": \"leader\"}");
			serviceData.setRequestUri("https://10.14.226.153:9002/rest/v2/tf/products/HPT28U?fields=FULL");

			HttpMethod httpMethod = HttpMethod.valueOf(HttpMethod.class, serviceData.getRequestType());
			final HttpHeaders headers = new HttpHeaders();
			ResponseEntity<String> responseEntity = null;
			headers.setContentType(MediaType.APPLICATION_JSON);
			serviceData.getHeaders().forEach(header -> headers.add(header.getKey(), header.getValue()));
			HttpEntity<String> httpEntity = new HttpEntity<>(serviceData.getRequestBody(), headers);
			responseEntity = customFactory.getRestTemplate().exchange(serviceData.getRequestUri(), httpMethod,
					httpEntity, String.class);
			// serviceData.setResponseTime(responseEntity.getHeaders().get("response-time"));
			serviceData.setResponse(responseEntity.getBody());
			serviceData.setRawResponse(responseEntity.getBody());
			List<KeyValue> respHeaders = new ArrayList<>();
			responseEntity.getHeaders().forEach((key, values) -> respHeaders
					.add(new KeyValue(key, values != null && !values.isEmpty() ? values.get(0) : null)));
			serviceData.setResponseHeaders(respHeaders);
			serviceData.setResponseType("json");

			log.info("RESPONSE ENTITY : " + responseEntity.getBody());
		} catch (HttpClientErrorException e) {
			log.error(e.getResponseBodyAsString());
		} catch (RestClientException e) {
			// log.error(response.getBody());
			log.error(e.getLocalizedMessage());
		}
		
	}
}
