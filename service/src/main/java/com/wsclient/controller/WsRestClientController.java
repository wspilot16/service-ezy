package com.wsclient.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.wsclient.model.ServiceData;
import com.wsclient.service.RestClientService;

@RestController
@RequestMapping(value = "/rest")
public class WsRestClientController {
	private final Logger logger = LoggerFactory.getLogger(WsRestClientController.class);

	@Autowired
	RestClientService restClientService;

	@RequestMapping(value = "/get", method = RequestMethod.POST, consumes={"application/json"}, produces = { "application/json" })
	public ServiceData processGetRequest(@RequestBody ServiceData serviceData) {
		try {
			serviceData = restClientService.get(serviceData);
		} catch (RestClientException e) {
			logger.error(e.getMessage());
		} catch (KeyManagementException e) {
			logger.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (KeyStoreException e) {
			logger.error(e.getMessage());
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return serviceData;
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST, produces = { "application/json" })
	public ServiceData processPostRequest(@RequestBody ServiceData serviceData) {
		try {
			serviceData = restClientService.post(serviceData);
		} catch (RestClientException e) {
			logger.error(e.getMessage());
		} catch (KeyManagementException e) {
			logger.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (KeyStoreException e) {
			logger.error(e.getMessage());
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
