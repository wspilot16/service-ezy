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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
			RestTemplate restTemplate = new RestTemplate();
			CustomFactory customFactory = new CustomFactory();
			// https://od-api-demo.oxforddictionaries.com:443/api/v1/domains/en/es
			// String quote = restTemplate
			// .getForObject("http://services.groupkt.com/state/get/IND/all",
			// String.class);
			response = customFactory.getRestTemplate()
					.getForEntity("https://www.googleapis.com/customsearch/v1", String.class);
			// log.info(quote);
			log.info("RESPONSE ENTITY : " + response.getBody());
			Map<String, String> map = new HashMap<String, String>();
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(response.getBody(), HashMap.class);
			log.info(String.valueOf(map.size()));
		} catch (HttpClientErrorException e) {
			log.error(e.getResponseBodyAsString());
		} catch (RestClientException e) {
			// log.error(response.getBody());
			log.error(e.getLocalizedMessage());
		}
		
	}
}
