package com.wsclient.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.wsclient.model.ServiceData;
import com.wsclient.service.RestClientService;

@RestController(value = "/rest")
public class WsRestClientController {
	private final Logger logger = LoggerFactory.getLogger(WsRestClientController.class);

	@Autowired
	RestClientService restClientService;

	@RequestMapping(value = "/get", method = RequestMethod.POST, produces = { "application/json" })
	public ServiceData processGetRequest(@RequestBody String requestUri) {
		ServiceData serviceData = new ServiceData();
		try {
			serviceData = restClientService.get(serviceData);
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return serviceData;
	}
}
