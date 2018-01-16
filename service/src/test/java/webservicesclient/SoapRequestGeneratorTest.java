package webservicesclient;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes.Name;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.dom.DeferredElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Response;

import groovy.util.XmlSlurper;

public class SoapRequestGeneratorTest {
	public static void main(String[] args) throws Exception {
		WsdlProject project = new WsdlProject();
		WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, "http://www.dneonline.com/calculator.asmx?WSDL");
		WsdlInterface wsdl = wsdls[0];
		/*for (Operation operation : wsdl.getOperationList()) {
	            WsdlOperation op = (WsdlOperation) operation;
	            System.out.println("OP:"+op.getName());
	            System.out.println("Children...:"+op.getChildren());
	            System.out.println(op.createRequest(true));
	            System.out.println("Response:");
	            System.out.println(op.createResponse(true));
	        }*/

		WsdlOperation operation = (WsdlOperation) wsdl.getOperationByName("Add");

		WsdlRequest request = operation.addNewRequest("myRequest");
		//		request.setRequestHeaders(map);
		request.setRequestContent(CreateRequestAction());

		System.out.println("Request elemnts"+request.getChildren());

		// submit the request
		WsdlSubmit<WsdlRequest> submit = request.submit(new WsdlSubmitContext(
				request), false);

		// wait for the response
		Response response = submit.getResponse();

		// print the response
		String content = response.getContentAsString();
		Map<String, String> responseMap = updateGeneratedXml(content);
		System.out.println(responseMap.entrySet());

	}

	private static String CreateRequestAction(){
		StringBuilder sb = new StringBuilder();
		sb.append("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tem=\"http://tempuri.org/\">");
		sb.append("   <soap:Header/>");
		sb.append("   <soap:Body>");
		sb.append("      <tem:Add>");
		sb.append("         <tem:intA>5</tem:intA>");
		sb.append("         <tem:intB>5</tem:intB>");
		sb.append("      </tem:Add>");
		sb.append("   </soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}

	private static Map<String, String> updateGeneratedXml(String soapResponseXMl) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setIgnoringElementContentWhitespace(true);
		Document doc = builderFactory.newDocumentBuilder().parse(
				new InputSource(new ByteArrayInputStream(soapResponseXMl.getBytes())));
		XPath xpath = XPathFactory.newInstance().newXPath();
		//NodeList nodes =  (NodeList)xpath.compile("/Envelope/Body").evaluate(doc, XPathConstants.NODESET);
		NodeList nodes =  (NodeList)xpath.evaluate("/Envelope/Body", doc, XPathConstants.NODESET);
		//		loopChildnodes(nodes);
		//operationMap.replace(operationName, writer.getBuffer().toString());
		Map<String, String> responseMap = new HashMap<String, String>();
		loopChildNodes(nodes, responseMap);
		return responseMap;
	}

	private static Map<String, String> loopChildNodes(NodeList nodes, Map<String, String> elementMap) {
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			if (nodes.item(idx).getNodeType() == Node.ELEMENT_NODE) {
				DeferredElementImpl childNode = (DeferredElementImpl) nodes.item(idx).getChildNodes();
				if(childNode.getLength()>1){
					loopChildNodes(childNode, elementMap);
				}else {
					elementMap.put(childNode.getNodeName(), childNode.getTextContent());
				} 
			}
		}
		return elementMap;
	}
}
