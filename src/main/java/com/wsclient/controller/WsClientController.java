package com.wsclient.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.wsclient.exception.CustomException;
import com.wsclient.model.ServiceData;
import com.wsclient.model.WebRequestElement;
import com.wsclient.service.SoapRequestGenerator;
import com.wsclient.service.SoapResponseGenerator;
import com.wsclient.type.ResponseErrorCodeType;
import com.wsclient.util.XmlToJsonConverter;

@RestController
//@Controller
public class WsClientController {
	private final Logger logger = LoggerFactory.getLogger(WsClientController.class);

	@Autowired SoapRequestGenerator soapRequestGenerator;
	@Autowired SoapResponseGenerator soapResponseGenerator;
	@Autowired XmlToJsonConverter xmlToJsonConverter;

	@RequestMapping(value = "/webMethodList", method = RequestMethod.POST, produces={"application/json"})
	public ServiceData getWebMethodList (@RequestBody String wsdlUrl) throws UnsupportedEncodingException, XPathExpressionException, SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		ServiceData serviceData = new ServiceData();
		try {
			wsdlUrl = java.net.URLDecoder.decode(wsdlUrl, "UTF-8");
			wsdlUrl = org.apache.commons.lang3.StringUtils.substringAfter(wsdlUrl, "formURL=");
			serviceData.setWsdlUrl(wsdlUrl);
			serviceData.setWebMethodMap(soapRequestGenerator.getWebMethodMapWithElements(serviceData));
		} catch (Exception e) {
			if (e instanceof CustomException) {
				serviceData.setErrorCode(((CustomException) e).getCode());
				serviceData.setErrorDescription(((CustomException) e).getDescription());
				serviceData.setResponseTime(((CustomException) e).getResponseTime() + " ms");
			} else {
				serviceData.setErrorCode(ResponseErrorCodeType.UNKNOWN.getCode());
				serviceData.setErrorDescription(e.getMessage());
			}
			logger.error(e.getMessage());
		}
		return serviceData;
	}

	@RequestMapping(value = "/webMethodElements", method = RequestMethod.POST, produces={"application/json"})
	public ServiceData getWebMethodElements (@RequestBody ServiceData serviceData) throws UnsupportedEncodingException, XPathExpressionException, SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		String selectedMethod = StringUtils.capitalize(serviceData.getSelectedMethod());
		serviceData.setSelectedMethod(selectedMethod);
		List<WebRequestElement> methodElements = serviceData.getWebMethodMap().get(selectedMethod);
		serviceData.setWebRequestElementList(methodElements);
		return serviceData;
	}

	@RequestMapping(value = "/webResponse", method = RequestMethod.POST)
	public ServiceData getWebResponse (@RequestBody ServiceData serviceData) throws UnsupportedEncodingException, XPathExpressionException, SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		String soapResponseXml = null;
		try {
			WsdlRequest wsdlRequest = soapRequestGenerator.getSoapRquestXML(serviceData);
			soapResponseXml = soapResponseGenerator.invokeWebService(wsdlRequest,serviceData);
		}catch (Exception e) {
			if (e instanceof CustomException) {
				serviceData.setErrorCode(((CustomException) e).getCode());
				serviceData.setErrorDescription(((CustomException) e).getDescription());
				serviceData.setResponseTime(((CustomException) e).getResponseTime() + " ms");
			} else {
				serviceData.setErrorCode(ResponseErrorCodeType.UNKNOWN.getCode());
				serviceData.setErrorDescription(e.getMessage());
			}
			logger.error(e.getMessage());
		}
		if (serviceData.getErrorCode() == null) {
			serviceData.setResponseXml(soapResponseXml);
			serviceData.setResponseJson(xmlToJsonConverter.convert(soapResponseXml));
			Map<String, String> htmlResponse = soapResponseGenerator.getHtmlResponse(soapResponseXml);
			serviceData.setResponseMap(htmlResponse);
		}
		return serviceData;
	}
}
