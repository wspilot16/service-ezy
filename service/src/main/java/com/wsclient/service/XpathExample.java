package com.wsclient.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wsclient.model.WebRequestElement;

public class XpathExample {

	public static void main(String[] args) 
	{
		try 
		{
			String sb = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tem=\"http://tempuri.org/\" xmlns:tem2=\"http://tempuri.org/\">" +
					"   <soap:Header/>" +
					"   <soap:Body>" +
					"      <tem:Add>" +
					"         <tem:intA>6</tem:intA>" +
					"         <tem:intB>6</tem:intB>" +
					"      </tem:Add>" +
					"      <tem:Sub>" +
					"         <tem:intA>6</tem:intA>" +
					"         <tem:intB>6</tem:intB>" +
					"      </tem:Sub>" +
					"   </soap:Body>" +
					"</soap:Envelope>";
			/*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document document = factory.newDocumentBuilder().parse(
					new ByteArrayInputStream((sb).getBytes()));
			factory.setNamespaceAware(true);
			getChildTags(document);*/

			//String setSoapHeader = setSoapHeader(sb, "Test all");
			//System.out.println(setSoapHeader);
			String prefix = getPrefix(sb);
//			System.out.println(prefix);
			//This evaluates to true, hence you may as well just use the xpath //test1.
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	/*	String sb = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tem=\"http://tempuri.org/\" xmlns:tem2=\"http://tempuri.org/\">" +
				"   <soap:Header/>" +
				"   <soap:Body>" +
				"      <tem:Add>" +
				"         <tem:intA>6</tem:intA>" +
				"         <tem:intB>6</tem:intB>" +
				"      </tem:Add>" +
				"   </soap:Body>" +
				"</soap:Envelope>";
		List<WebRequestElement> elements = new ArrayList<>();
		WebRequestElement element = new WebRequestElement();
		element.setElementName("tem:intA");
		element.setXpathElementName("//Body/Add/intA");
		element.setElementValue("10");
		elements.add(element);
		try {
			String updateGeneratedXml = updateGeneratedXml(elements, sb);
			System.out.println(updateGeneratedXml);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private static String getXPath(Node root, String elementName, int nodeIndex){
		for (int i = 0; i < root.getChildNodes().getLength(); i++) {
			Node node = root.getChildNodes().item(i);

			if (node instanceof Element)
			{
				DeferredElementImpl childNode = (DeferredElementImpl)node;
				if (childNode.getNodeName().equals(elementName)
						&& childNode.getNodeIndex() == nodeIndex)
				{
					return "/" + childNode.getNodeName();
				}
				else if (childNode.getChildNodes().getLength() > 0)
				{
					String xpath = getXPath(childNode, elementName, nodeIndex);
					if (xpath != null)
					{
						return "/" + childNode.getNodeName() + xpath;
					}
				}
			}
		}

		return null;
	}

	private static String getXPath(Document document, String elementName, int nodeIndex) {
		return getXPath(document.getDocumentElement(), elementName, nodeIndex);
	}

	public static Map<String, String> getChildTags(Document document) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		//NodeList nodes =  (NodeList)xpath.compile("/Envelope/Body").evaluate(doc, XPathConstants.NODESET);
		NodeList nodes =  (NodeList)xpath.evaluate("/Envelope/Body", document, XPathConstants.NODESET);
		//		loopChildnodes(nodes);
		//operationMap.replace(operationName, writer.getBuffer().toString());
		Map<String, String> responseMap = new LinkedHashMap<String, String>();
		loopChildNodes(document, nodes, responseMap, "/");
		return responseMap;
	}


	private static Map<String, String> loopChildNodes(Document document, NodeList nodes, Map<String, String> elementMap, String xpath) {
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			if (nodes.item(idx).getNodeType() == Node.ELEMENT_NODE) {
				DeferredElementImpl childNode = (DeferredElementImpl) nodes.item(idx).getChildNodes();
				if(childNode.getLength()>1){
					String updatedpath = xpath+ "/" + childNode.getNodeName();
					loopChildNodes(document, childNode, elementMap, updatedpath);
				}else {
					elementMap.put(childNode.getNodeName(), childNode.getTextContent());
					//String xpath = "/" + getXPath(document, childNode.getNodeName(), childNode.getNodeIndex());
//					System.out.println(xpath+ "/" + childNode.getNodeName());      
				} 
			}
		}
		return elementMap;
	}
	
	private static String updateGeneratedXml(List<WebRequestElement> requestElements, String soapRequestXMl) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new InputSource(new ByteArrayInputStream(soapRequestXMl.getBytes("utf-8"))));
		XPath xpath = XPathFactory.newInstance().newXPath();
		String prefixname = xpath.getNamespaceContext().getPrefix("http://www.w3.org/2003/05/soap-envelope");
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
	
	private static String setSoapHeader(String soapRequestXml, String soapHeaderValue) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("<soap:Header>");
		sbr.append(soapHeaderValue);
		sbr.append("<soap:Header>");
		String replace = soapRequestXml.replace("<soap:Header/>", sbr.toString());
		return replace;
	}
	
	private static String getPrefix(String xml) throws XMLStreamException {
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
