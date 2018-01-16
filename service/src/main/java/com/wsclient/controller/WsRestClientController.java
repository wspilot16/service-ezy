package com.wsclient.controller;

import com.wsclient.model.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wsclient.model.ServiceData;
import com.wsclient.service.RestClientService;

@RestController
@RequestMapping(value = "/rest")
public class WsRestClientController {
	private final Logger logger = LoggerFactory.getLogger(WsRestClientController.class);

	@Autowired
	RestClientService restClientService;

	@RequestMapping(value = "/post", method = {RequestMethod.GET, RequestMethod.POST}, produces = { "application/json" })
	public ServiceData processPostRequest(@RequestBody ServiceData serviceData) {
		long start = System.currentTimeMillis();
		try {
			serviceData = restClientService.post(serviceData);
		} catch (Exception e) {
			serviceData.getErrors().add(new KeyValue("system_error", e.getMessage()));
		}
		long end = System.currentTimeMillis();
		long duration = end-start;
		if (serviceData.getResponseTime() != null) {
			serviceData.setResponseTime((int) duration);
		}
		return serviceData;
	}
}
