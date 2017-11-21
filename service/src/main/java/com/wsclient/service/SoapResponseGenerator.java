package com.wsclient.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xerces.dom.DeferredElementImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.impl.wsdl.submit.transports.http.support.attachments.WsdlSinglePartHttpResponse;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.wsclient.exception.CustomException;
import com.wsclient.model.ServiceData;
import com.wsclient.type.ResponseErrorCodeType;

@Component
public class SoapResponseGenerator {
	private final Logger logger = LoggerFactory.getLogger(SoapResponseGenerator.class);
	/**
	 * Here we generate the SOAP message using SoapBuilder and post it using SoapClient
	 */
	public String invokeWebService(WsdlRequest request, ServiceData serviceData)
			throws SubmitException, CustomException {
		// submit the request
		WsdlSubmit<WsdlRequest> submit = null;
		submit = request.submit(new WsdlSubmitContext(request), false);
		logger.debug(":::Request (before hitting webservice):::\n"+request.getRequestContent());
		
		// wait for the response
		Response response = submit.getResponse();
		WsdlSinglePartHttpResponse resp = (WsdlSinglePartHttpResponse)response;
		if (ResponseErrorCodeType.containsCode(resp.getStatusCode())) {
			throw new CustomException(resp.getStatusCode(), response.getContentAsString(), response.getTimeTaken());
		}
		// print the response
		String content = response.getContentAsString();
		serviceData.setResponseTime(response.getTimeTaken()+" ms");
		logger.debug(":::Response:::\n"+content);
		return content;
	}

	public Map<String, String> getHtmlResponse(String soapResponseXMl) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setIgnoringElementContentWhitespace(true);
		Document doc = builderFactory.newDocumentBuilder().parse(
				new InputSource(new ByteArrayInputStream(soapResponseXMl.getBytes())));
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodes =  (NodeList)xpath.evaluate("/Envelope/Body", doc, XPathConstants.NODESET);
		Map<String, String> responseMap = new LinkedHashMap<String, String>();
		loopChildNodes(nodes, responseMap);
		return responseMap;
	}

	private Map<String, String> loopChildNodes(NodeList nodes, Map<String, String> elementMap) {
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			if (nodes.item(idx).getNodeType() == Node.ELEMENT_NODE) {
				DeferredElementImpl childNode = (DeferredElementImpl) nodes.item(idx).getChildNodes();
				 String elementName = WebserviceUtil.removeNameSpace(childNode.getNodeName());
				if(childNode.getLength()>1){
					loopChildNodes(childNode, elementMap);
				}else {
					elementMap.put(elementName, childNode.getTextContent());
				} 
			}
		}
		return elementMap;
	}
}
