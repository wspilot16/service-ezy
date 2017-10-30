/*package webservicesclient;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.reficio.ws.SoapContext;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;
import org.reficio.ws.client.core.SoapClient;
import org.reficio.ws.server.core.SoapServer;
import org.reficio.ws.server.responder.AutoResponder;
import org.xml.sax.SAXException;

public class SoapResponseTester {

	private static SoapServer server;
	private static final int port = 7643;
	private static final String contextPath = "/stockquote";

	private static final QName bindingName = new QName("http://reficio.org/stockquote.wsdl", "StockQuoteSoap");
	private static SoapBuilder builder;

	public static void main(String[] args) {
		try {
			startServer();
			invoke_tradePriceRequest_generatedMessages();
		} catch (WSDLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			server.stop();
		}

	}
	public static void startServer() throws WSDLException {
		server = SoapServer.builder()
				.httpPort(port)
				.build();
		server.start();

		AutoResponder responder = getAutoResponderForTestService();
		server.registerRequestResponder(contextPath, responder);
	}

	public static void stopServer() {
		server.stop();
	}

	public static AutoResponder getAutoResponderForTestService() throws WSDLException {
		SoapContext context = SoapContext.builder().exampleContent(false).build();
		Wsdl parser = Wsdl.parse("http://www.webservicex.net/geoipservice.asmx?WSDL");
		builder = parser.binding().localPart("StockQuoteSoap12").find();

		AutoResponder responder = new AutoResponder(builder, context);
		return responder;
	}

	*//**
	 * Here we're gonna generate the SOAP message using SoapBuilder and post it using SoapClient
	 *//*
	public static void invoke_tradePriceRequest_generatedMessages() throws Exception, SAXException, WSDLException {
		// construct the client
		String url = String.format("http://www.webservicex.net/geoipservice.asmx?WSDL");
		SoapClient client = SoapClient.builder()
				.endpointUri(url)
				.build();

		Wsdl parser = Wsdl.parse("http://www.webservicex.net/geoipservice.asmx?WSDL");
		SoapBuilder soapBuilder = parser.binding().localPart("GeoIPServiceSoap12").find();

		// get the operation to invoked -> assumption our operation is the first operation in the WSDL's
		SoapOperation operation = soapBuilder.operation().name("GetGeoIP").find();

		// construct the request
		String request = soapBuilder.buildInputMessage(operation);
		// post the request to the server
		System.out.println("Request>>>>"+request);
		String response = client.post(request);
		// get the response
		String expectedResponse = soapBuilder.buildOutputMessage(operation, SoapContext.NO_CONTENT);

		System.out.println("Response>>>>"+transformXml(response));
	}
	
	public static String transformXml(String soapResponseXml) throws TransformerFactoryConfigurationError, TransformerException {
		Source xmlInput = new StreamSource(new StringReader(soapResponseXml));
		StreamResult xmlOutput = new StreamResult(new StringWriter());

		// Configure transformer
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer(); // An identity transformer
//		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "testing.dtd");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(xmlInput, xmlOutput);
		return xmlOutput.getWriter().toString();
	}

	*//**
	 * Here we're gonna simply post SOAP hardcoded message using SoapClient
	 *//*
	public void invoke_tradePriceRequest_hardcodedMessages() throws IOException, SAXException {
		String url = String.format("http://localhost:%d%s", port, contextPath);
		SoapClient client = SoapClient.builder()
				.endpointUri(url)
				.build();

		String request =
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:stoc=\"http://reficio.org/stockquote.wsdl\" xmlns:stoc1=\"http://reficio.org/stockquote.xsd\">\n" +
						"   <soapenv:Header/>\n" +
						"   <soapenv:Body>\n" +
						"      <stoc:GetLastTradePrice>\n" +
						"         <stoc1:TradePriceRequest>\n" +
						"            <tickerSymbol>?</tickerSymbol>\n" +
						"         </stoc1:TradePriceRequest>\n" +
						"      </stoc:GetLastTradePrice>\n" +
						"   </soapenv:Body>\n" +
						"</soapenv:Envelope>";

		String response = client.post(request);

		String expectedResponse = "" +
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:stoc=\"http://reficio.org/stockquote.wsdl\" xmlns:stoc1=\"http://reficio.org/stockquote.xsd\">\n" +
				"   <soapenv:Header/>\n" +
				"   <soapenv:Body>\n" +
				"      <stoc:GetLastTradePriceResponse>\n" +
				"         <stoc1:TradePrice>\n" +
				"            <price>?</price>\n" +
				"         </stoc1:TradePrice>\n" +
				"      </stoc:GetLastTradePriceResponse>\n" +
				"   </soapenv:Body>\n" +
				"</soapenv:Envelope>";

	}


}
*/