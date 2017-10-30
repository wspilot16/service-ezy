package demo;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
			serviceData.setRequestBody("{\"name\": \"morpheus\","
					+"\"job\": \"leader\"}");
			serviceData.setRequestUri("https://reqres.in/api/users");
			ServiceData responseData = restClientService.post(serviceData);
			// https://od-api-demo.oxforddictionaries.com:443/api/v1/domains/en/es
			// String quote = restTemplate
			// .getForObject("http://services.groupkt.com/state/get/IND/all",
			// String.class);
			/*response = customFactory.getRestTemplate()()
					.getp("https://www.googleapis.com/customsearch/v1", String.class);
			*/// log.info(quote);
			log.info("RESPONSE ENTITY : " + responseData.getResponse());
		} catch (HttpClientErrorException e) {
			log.error(e.getResponseBodyAsString());
		} catch (RestClientException e) {
			// log.error(response.getBody());
			log.error(e.getLocalizedMessage());
		}
		
	}
}
