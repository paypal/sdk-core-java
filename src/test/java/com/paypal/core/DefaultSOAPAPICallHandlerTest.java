package com.paypal.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.core.DefaultSOAPAPICallHandler.XmlNamespaceProvider;
import com.paypal.core.message.XMLMessageSerializer;

public class DefaultSOAPAPICallHandlerTest {

	DefaultSOAPAPICallHandler defaultHandler;
	
	private static Logger logger = Logger.getLogger(DefaultSOAPAPICallHandlerTest.class);

	@BeforeClass
	public void beforeClass() throws IOException {
		Properties props;
		props = new Properties();
		props.load(this.getClass()
				.getResourceAsStream("/sdk_config.properties"));
		Map<String, String> cMap = SDKUtil.constructMap(props);
		defaultHandler = new DefaultSOAPAPICallHandler(
				"requestEnvelope.errorLanguage=en_US&baseAmountList.currency(0).code=USD&baseAmountList.currency(0).amount=2.0&convertToCurrencyList.currencyCode(0)=GBP",
				"", "", cMap);
	}

	@AfterClass
	public void afterClass() {
		defaultHandler = null;
	}

	@Test
	public void getEndPointTest() {
		Assert.assertEquals("https://svcs.sandbox.paypal.com/",
				defaultHandler.getEndPoint());
	}

	@Test(dependsOnMethods = { "getEndPointTest" })
	public void getCredentialTest() {
		Assert.assertEquals(null, defaultHandler.getCredential());
	}

	@Test(dependsOnMethods = { "getCredentialTest" })
	public void getHeaderMapTest() {
		Assert.assertEquals(HashMap.class, defaultHandler.getHeaderMap()
				.getClass());
	}

	@Test(dependsOnMethods = { "getHeaderMapTest" })
	public void getHeaderStringTest() {
		Assert.assertEquals("", defaultHandler.getHeaderString());
	}

	@Test(dependsOnMethods = { "getHeaderStringTest" })
	public void getNamespacesTest() {
		Assert.assertEquals("", defaultHandler.getNamespaces());
	}

	@Test(dependsOnMethods = { "getNamespacesTest" })
	public void getPayLoadTest() {
		Assert.assertEquals(
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ><soapenv:Header></soapenv:Header><soapenv:Body>requestEnvelope.errorLanguage=en_US&baseAmountList.currency(0).code=USD&baseAmountList.currency(0).amount=2.0&convertToCurrencyList.currencyCode(0)=GBP</soapenv:Body></soapenv:Envelope>",
				defaultHandler.getPayLoad());
	}

	@Test(dependsOnMethods = { "getPayLoadTest" })
	public void setHeaderStringTest() {
		defaultHandler.setHeaderString("headerString");
		Assert.assertEquals("headerString", defaultHandler.getHeaderString());
	}

	@Test(dependsOnMethods = { "setHeaderStringTest" })
	public void setNamespacesTest() {
		defaultHandler.setNamespaces("namespaces");
		Assert.assertEquals("namespaces", defaultHandler.getNamespaces());
	}

	@Test(dependsOnMethods = { "setHeaderStringTest" }, expectedExceptions = IllegalArgumentException.class)
	public void domNullConfigMapTest() {
		BaseAPIContext baseAPIContext = new BaseAPIContext();
		Map<String, String> configurationMap = null;
		new DefaultSOAPAPICallHandler(new SampleBody(), baseAPIContext,
				configurationMap, "DoDirectPayment");
	}

	@Test(dependsOnMethods = { "domNullConfigMapTest" })
	public void domEndpointTest() {
		BaseAPIContext baseAPIContext = new BaseAPIContext();
		Map<String, String> configurationMap = new HashMap<String, String>();
		configurationMap.put("service.EndPoint",
				"https://api-3t.sandbox.paypal.com/2.0");
		DefaultSOAPAPICallHandler defHandler = new DefaultSOAPAPICallHandler(
				new SampleBody(), baseAPIContext, configurationMap,
				"DoDirectPayment");
		Assert.assertEquals("https://api-3t.sandbox.paypal.com/2.0",
				defHandler.getEndPoint());
	}

	@Test(dependsOnMethods = { "domEndpointTest" })
	public void domPayloadTest() {
		DefaultSOAPAPICallHandler
				.setXmlNamespaceProvider(new XmlNamespacePrefixProvider());
		BaseAPIContext baseAPIContext = new BaseAPIContext();
		baseAPIContext.setSOAPHeader(new SampleHeader());
		Map<String, String> configurationMap = new HashMap<String, String>();
		configurationMap.put("service.EndPoint",
				"https://api-3t.sandbox.paypal.com/2.0");
		baseAPIContext.setConfigurationMap(configurationMap);
		DefaultSOAPAPICallHandler defHandler = new DefaultSOAPAPICallHandler(
				new SampleBody(), baseAPIContext, null, "DoDirectPayment");
		String payload = defHandler.getPayLoad().trim();
		String expectedPayload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:cc=\"urn:ebay:apis:CoreComponentTypes\" xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\" xmlns:ed=\"urn:ebay:apis:EnhancedDataTypes\" xmlns:ns=\"urn:ebay:api:PayPalAPI\" xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\" xmlns:wsdlsoap=\"http://schemas.xmlsoap.org/wsdl/soap/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">" +
				"<soapenv:Header>" +
				"<ns:RequesterCredentials>" +
				"<ebl:Credentials>" +
				"<ebl:Username>jb-us-seller_api1.paypal.com</ebl:Username>" +
				"</ebl:Credentials>" +
				"</ns:RequesterCredentials>" +
				"</soapenv:Header>" +
				"<soapenv:Body>" +
				"<ns:DoDirectPaymentReq>" +
				"<ns:DoDirectPaymentRequest>" +
				"<ebl:Version>98.0</ebl:Version>" +
				"<ebl:DoDirectPaymentRequestDetails>" +
				"<ebl:CreditCard>" +
				"<ebl:CreditCardType>Visa</ebl:CreditCardType>" +
				"<ebl:CreditCardNumber>4202297003827029</ebl:CreditCardNumber>" +
				"<ebl:CVV2>962</ebl:CVV2>" +
				"</ebl:CreditCard>" +
				"</ebl:DoDirectPaymentRequestDetails>" +
				"</ns:DoDirectPaymentRequest>" +
				"</ns:DoDirectPaymentReq>" +
				"</soapenv:Body>" +
				"</soapenv:Envelope>";
		Assert.assertEquals(expectedPayload, payload);
	}
	
	@Test(dependsOnMethods = { "domPayloadTest" })
	public void domPayloadNoHeaderTest() {
		DefaultSOAPAPICallHandler
				.setXmlNamespaceProvider(new XmlNamespacePrefixProvider());
		BaseAPIContext baseAPIContext = new BaseAPIContext();
		Map<String, String> configurationMap = new HashMap<String, String>();
		configurationMap.put("service.EndPoint",
				"https://api-3t.sandbox.paypal.com/2.0");
		baseAPIContext.setConfigurationMap(configurationMap);
		DefaultSOAPAPICallHandler defHandler = new DefaultSOAPAPICallHandler(
				new SampleBody(), baseAPIContext, null, "DoDirectPayment");
		String payload = defHandler.getPayLoad().trim();
		Assert.assertEquals(true, payload.contains("<soapenv:Header/>"));
	}
	
	@Test(dependsOnMethods = { "domPayloadNoHeaderTest" }, expectedExceptions = RuntimeException.class)
	public void domErrBodyPayloadTest() {
		DefaultSOAPAPICallHandler
				.setXmlNamespaceProvider(new XmlNamespacePrefixProvider());
		BaseAPIContext baseAPIContext = new BaseAPIContext();
		baseAPIContext.setSOAPHeader(new SampleHeader());
		Map<String, String> configurationMap = new HashMap<String, String>();
		configurationMap.put("service.EndPoint",
				"https://api-3t.sandbox.paypal.com/2.0");
		baseAPIContext.setConfigurationMap(configurationMap);
		DefaultSOAPAPICallHandler defHandler = new DefaultSOAPAPICallHandler(
				new SampleNoNSBody(), baseAPIContext, null, "DoDirectPayment");
		defHandler.getPayLoad();
	}

	@Test(dependsOnMethods = { "domErrBodyPayloadTest" })
	public void getPayloadForEmptyRawPayloadTest() {
		defaultHandler = new DefaultSOAPAPICallHandler("", "", "",
				new HashMap<String, String>());
		Assert.assertEquals(
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ><soapenv:Header></soapenv:Header><soapenv:Body></soapenv:Body></soapenv:Envelope>",
				defaultHandler.getPayLoad());
	}

	private static class XmlNamespacePrefixProvider implements
			XmlNamespaceProvider {

		private Map<String, String> namespaceMap;

		public XmlNamespacePrefixProvider() {
			namespaceMap = new LinkedHashMap<String, String>();
			namespaceMap.put("xml", "http://www.w3.org/XML/1998/namespace");
			namespaceMap.put("ed", "urn:ebay:apis:EnhancedDataTypes");
			namespaceMap.put("cc", "urn:ebay:apis:CoreComponentTypes");
			namespaceMap.put("ebl", "urn:ebay:apis:eBLBaseComponents");
			namespaceMap.put("xs", "http://www.w3.org/2001/XMLSchema");
			namespaceMap.put("wsdlsoap",
					"http://schemas.xmlsoap.org/wsdl/soap/");
			namespaceMap.put("ns", "urn:ebay:api:PayPalAPI");
			namespaceMap.put("SOAP-ENC",
					"http://schemas.xmlsoap.org/soap/encoding/");
			namespaceMap.put("wsdl", "http://schemas.xmlsoap.org/wsdl/");
		}

		public Map<String, String> getNamespaceMap() {
			return namespaceMap;
		}
	}

	private static class SampleHeader implements XMLMessageSerializer {
		public String toXMLString() {
			return "<ns:RequesterCredentials><ebl:Credentials><ebl:Username>jb-us-seller_api1.paypal.com</ebl:Username></ebl:Credentials></ns:RequesterCredentials>";
		}
	}

	private static class SampleBody implements XMLMessageSerializer {

		public String toXMLString() {
			return "<ns:DoDirectPaymentReq><ns:DoDirectPaymentRequest><ebl:Version>98.0</ebl:Version><ebl:DoDirectPaymentRequestDetails><ebl:CreditCard><ebl:CreditCardType>Visa</ebl:CreditCardType><ebl:CreditCardNumber>4202297003827029</ebl:CreditCardNumber><ebl:CVV2>962</ebl:CVV2></ebl:CreditCard></ebl:DoDirectPaymentRequestDetails></ns:DoDirectPaymentRequest></ns:DoDirectPaymentReq>";
		}

	}
	
	private static class SampleNoNSBody implements XMLMessageSerializer {

		public String toXMLString() {
			return "<ns:DoDirectPaymentReq><ns:DoDirectPaymentRequest><ebl:Version>98.0</ebl:Version><ebl:DoDirectPaymentRequestDetails><ebl:CreditCard><ebl:CreditCardType>Visa</ebl:CreditCardType><ebl:CreditCardNumber>4202297003827029</ebl:CreditCardNumber><ebl:CVV2>962</ebl:CVV2></ebl:CreditCard></ebl:DoDirectPaymentRequestDetails></ns:DoDirectPaymentRequest>";
		}

	}
}
