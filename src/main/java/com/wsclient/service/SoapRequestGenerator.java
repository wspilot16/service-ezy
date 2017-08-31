package com.wsclient.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.dom.DeferredElementImpl;
import org.apache.xmlbeans.XmlException;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.types.StringToStringMap;
import com.wsclient.exception.CustomException;
import com.wsclient.model.ServiceData;
import com.wsclient.model.WebRequestElement;
import com.wsclient.type.ResponseErrorCodeType;

@Component
public class SoapRequestGenerator {
	private static final Long TIME_OUT = 5L;
	private final Logger logger = LoggerFactory.getLogger(SoapRequestGenerator.class);
	
	public WsdlRequest getSoapRquestXML(ServiceData serviceData)
			throws XmlException, IOException, SoapUIException, InterruptedException,
			ExecutionException, CustomException, XPathExpressionException, SAXException, 
			ParserConfigurationException, TransformerFactoryConfigurationError, 
			TransformerException, XMLStreamException{
		WsdlProject project;
			project = new WsdlProject();
			WsdlInterface[] wsdls = doWsdlImport(project, serviceData);
			WsdlInterface wsdl = wsdls[0];
			WsdlOperation selectedOperation = getSelectedOperation(wsdl, serviceData.getSelectedMethod());
			WsdlRequest request = selectedOperation.addNewRequest("myRequest");
			String soapRequestXml = updateGeneratedXml(
					serviceData.getWebMethodMap().get(serviceData.getSelectedMethod()), selectedOperation.createRequest(true));
			if(StringUtils.isNotBlank(serviceData.getSoapHeader())) {
				soapRequestXml = setSoapHeader(soapRequestXml, serviceData.getSoapHeader());
			}
			if(serviceData.getHttpRequestHeader() != null) {
				request.setRequestHeaders((StringToStringMap) serviceData.getHttpRequestHeader());
			}
			request.setRequestContent(soapRequestXml);
			logger.debug("::: Request XML :::\n" + soapRequestXml);
			return request;
	}

	private String setSoapHeader(String soapRequestXml, String soapHeaderValue) throws XMLStreamException {
		StringBuilder sbr = new StringBuilder();
		String prefix = getPrefix(soapRequestXml);
		sbr.append("<"+prefix+":Header>");
		sbr.append(soapHeaderValue);
		sbr.append("</"+prefix+":Header>");
		String soapXmlwithHeader = soapRequestXml.replace("<soap:Header/>", sbr.toString());
		return soapXmlwithHeader;
	}

	private WsdlOperation getSelectedOperation(WsdlInterface wsdl, String opertaionName ) {
		for (Operation operation : wsdl.getAllOperations()) {
			if(StringUtils.containsIgnoreCase(operation.getName(), opertaionName)) {
				return  (WsdlOperation) wsdl.getOperationByName(operation.getName());
			}
		}
		return null;
	}


	private String updateGeneratedXml(List<WebRequestElement> requestElements, String soapRequestXMl) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new InputSource(new ByteArrayInputStream(soapRequestXMl.getBytes("utf-8"))));
		XPath xpath = XPathFactory.newInstance().newXPath();

		StringWriter writer = new StringWriter();
		for(WebRequestElement requestElement : requestElements) {
			NodeList nodes = (NodeList)xpath.evaluate(
					requestElement.getXpathElementName(), doc, XPathConstants.NODESET);
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				nodes.item(idx).setTextContent(requestElement.getElementValue());
			}
		}
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(new DOMSource(doc), new StreamResult(writer));
		//operationMap.replace(operationName, writer.getBuffer().toString());
		return writer.getBuffer().toString();
	}

	public Map<String, List<WebRequestElement>> getWebMethodMapWithElements(ServiceData serviceData) throws Exception {
		Map<String, List<WebRequestElement>> webMethodMap = new TreeMap<String, List<WebRequestElement>>(String.CASE_INSENSITIVE_ORDER);
		WsdlProject project;
		project = new WsdlProject();
		WsdlInterface[] wsdls = doWsdlImport(project, serviceData);
//				WsdlImporter.importWsdl(project, serviceData.getWsdlUrl());
		
		WsdlInterface wsdl = wsdls[0];
		for (Operation operation : wsdl.getAllOperations()) {
			String soapRequestXML = ((WsdlOperation) operation).createRequest(true);
			List<WebRequestElement> requestElements = getRequestElements(soapRequestXML);
			webMethodMap.put(operation.getName(), requestElements);
		}
		return webMethodMap;
	}

	private WsdlInterface[] doWsdlImport(final WsdlProject project, final ServiceData serviceData) throws InterruptedException, ExecutionException, CustomException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		WsdlInterface[] wsdlInterface = null;
		Future<WsdlInterface[]> future = executor.submit(new Callable() {

		    public WsdlInterface[] call() throws Exception {
		    	return WsdlImporter.importWsdl(project, serviceData.getWsdlUrl());
		    }
		});
		try {
			wsdlInterface = future.get(TIME_OUT, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
		    throw new CustomException(ResponseErrorCodeType.TIMEOUT.getCode(), e.getMessage(), TIME_OUT);
		} finally {
			executor.shutdownNow();
		}
		return wsdlInterface;
	}

	private List<WebRequestElement> getRequestElements(String soapRequestXml) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new ByteArrayInputStream((soapRequestXml).getBytes()));
		XPath xpath = XPathFactory.newInstance().newXPath();
		//NodeList nodes =  (NodeList)xpath.compile("/Envelope/Body").evaluate(doc, XPathConstants.NODESET);
		NodeList nodes =  (NodeList)xpath.evaluate("/Envelope/Body", document, XPathConstants.NODESET);
		//		loopChildnodes(nodes);
		//operationMap.replace(operationName, writer.getBuffer().toString());
		List<WebRequestElement> elements = new ArrayList<>();
		List<WebRequestElement> childElements = loopChildElements(document, nodes, elements, "/");
		return childElements;
	}

	private List<WebRequestElement> loopChildElements(Document document, NodeList nodes, List<WebRequestElement> elements, String xpath) {
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			WebRequestElement element = new WebRequestElement();
			if (nodes.item(idx).getNodeType() == Node.ELEMENT_NODE) {
				DeferredElementImpl childNode = (DeferredElementImpl) nodes.item(idx).getChildNodes();
				String elementName = WebserviceUtil.removeNameSpace(childNode.getNodeName());
				if(childNode.getLength()>1){
					String updatedpath = xpath+ "/" + elementName;
					loopChildElements(document, childNode, elements, updatedpath);
				}else {
					element.setElementName(elementName);
					//element.setElementValue(childNode.getTextContent());
					element.setXpathElementName(xpath+ "/" + elementName);
					elements.add(element);
				} 
			}
		}
		return elements;
	}

	private String getPrefix(String xml) throws XMLStreamException {
		NamespaceContext nsContext = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader evtReader = factory
				.createXMLEventReader(new StringReader(xml));
		while (evtReader.hasNext()) {
			XMLEvent event = evtReader.nextEvent();
			if (event.isStartElement()) {
				nsContext = ((StartElement) event)
						.getNamespaceContext();
				break;
			}
		}
		String prefix = nsContext.getPrefix(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
		if(StringUtils.isBlank(prefix)) {
			prefix = nsContext.getPrefix(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
		}
		return prefix;
	}
}
