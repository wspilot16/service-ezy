package demo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WebServiceTester {
	private static final Logger log = LoggerFactory.getLogger(WebServiceTester.class);

	public static void main(String[] args) {
		try {
			testRestWebService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testRestWebService() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		// https://od-api-demo.oxforddictionaries.com:443/api/v1/domains/en/es
		// String quote = restTemplate
		// .getForObject("http://services.groupkt.com/state/get/IND/all",
		// String.class);
		ResponseEntity<String> response = restTemplate.getForEntity("http://services.groupkt.com/state/get/IND/all",
				String.class);
		// log.info(quote);
		log.info("RESPONSE ENTITY : " + response.getBody());
		Map<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		map = mapper.readValue(response.getBody(), HashMap.class);
		log.info(String.valueOf(map.size()));
	}

}
