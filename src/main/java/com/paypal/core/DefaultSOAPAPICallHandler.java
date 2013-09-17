package com.paypal.core;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.paypal.core.credential.ICredential;
import com.paypal.core.message.XMLMessageSerializer;
import com.paypal.exception.ClientActionRequiredException;

/**
 * <code>DefaultSOAPAPICallHandler</code> acts as a basic SOAP
 * {@link APICallPreHandler}. The interface method returns defaults for HTTP
 * headers which may be an empty {@link Map}. If SOAP:HEADERS are added as
 * headerString property and Namespace (to be added to SOAP:ENVELOPE) as
 * namespace property then DefaultSOAPAPICallHandler returns a String for
 * getPayLoad method that includes place-holders in the form {i} where i = 0, 1,
 * 2.. that can be used by any upper level classes to replace them with
 * appropriate values.
 * 
 */
public class DefaultSOAPAPICallHandler implements APICallPreHandler {

	/**
	 * XML Namespace provider for SOAP serialization
	 * 
	 * @author kjayakumar
	 * 
	 */
	public interface XmlNamespaceProvider {

		/**
		 * Return a {@link Map} of XML Namespaces with the entries in the format
		 * [prefix] = [namespace]
		 * 
		 * @return {@link Map} of XML Namespaces
		 */
		Map<String, String> getNamespaceMap();

	}

	/**
	 * XMLNamespaceProvider providing namespaces for SOAP Envelope
	 */
	private static XmlNamespaceProvider xmlNamespaceProvider;

	/**
	 * XMLNS attribute
	 */
	private static final String XMLNS_ATTRIBUTE_PREFIX = "xmlns:";

	/**
	 * SOAP Envelope qualified for namespace
	 * 'http://schemas.xmlsoap.org/soap/envelope/' with prefix soapenv
	 */
	private static final String SOAP_ENVELOPE_QNAME = "soapenv:Envelope";

	/**
	 * SOAP Header qualified for namespace
	 * 'http://schemas.xmlsoap.org/soap/envelope/' with prefix soapenv
	 */
	private static final String SOAP_HEADER_QNAME = "soapenv:Header";

	/**
	 * SOAP Body qualified for namespace
	 * 'http://schemas.xmlsoap.org/soap/envelope/' with prefix soapenv
	 */
	private static final String SOAP_BODY_QNAME = "soapenv:Body";

	/**
	 * SOAP Envelope Message Formatter String start
	 */
	private static final String SOAP_ENV_START = "<" + SOAP_ENVELOPE_QNAME
			+ " " + XMLNS_ATTRIBUTE_PREFIX
			+ "soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" {0}>";

	/**
	 * SOAP Envelope Message Formatter String end
	 */
	private static final String SOAP_ENV_END = "</" + SOAP_ENVELOPE_QNAME + ">";

	/**
	 * SOAP Header Message Formatter String start
	 */
	private static final String SOAP_HEAD_START = "<" + SOAP_HEADER_QNAME
			+ ">{1}";

	/**
	 * SOAP Header Message Formatter String end
	 */
	private static final String SOAP_HEAD_END = "</" + SOAP_HEADER_QNAME + ">";

	/**
	 * SOAP Body Message Formatter String start
	 */
	private static final String SOAP_BODY_START = "<" + SOAP_BODY_QNAME
			+ ">{2}";

	/**
	 * SOAP Body Message Formatter String end
	 */
	private static final String SOAP_BODY_END = "</" + SOAP_BODY_QNAME + ">";

	/**
	 * Namespace of SOAP Envelope
	 */
	private static final String SOAP_ENV_NS = "http://schemas.xmlsoap.org/soap/envelope/";

	/**
	 * Sets an implemenation of {@link XMLMessageSerializer}
	 * 
	 * @param xmlNamespaceProvider
	 */
	public static void setXmlNamespaceProvider(
			XmlNamespaceProvider xmlNamespaceProvider) {
		DefaultSOAPAPICallHandler.xmlNamespaceProvider = xmlNamespaceProvider;
	}

	/**
	 * Raw payload from stubs
	 */
	private String rawPayLoad;

	/**
	 * Header element as String
	 */
	private String headerString;

	/**
	 * Namespace attributes as String
	 */
	private String namespaces;

	/**
	 * Map used for to override ConfigManager configurations
	 */
	private Map<String, String> configurationMap = null;

	/**
	 * SOAP API operation name
	 */
	private String methodName = null;

	/**
	 * {@link BaseAPIContext} instance
	 */
	private BaseAPIContext baseAPIContext;

	/**
	 * Instance of {@link XMLMessageSerializer} for SOAP Header
	 */
	private XMLMessageSerializer soapHeaderContent = null;

	/**
	 * Instance of {@link XMLMessageSerializer} for SOAP Body
	 */
	private XMLMessageSerializer soapBodyContent = null;

	/**
	 * @return the headerString
	 */
	public String getHeaderString() {
		return headerString;
	}

	/**
	 * @param headerString
	 *            the headerString to set
	 */
	public void setHeaderString(String headerString) {
		this.headerString = headerString;
	}

	/**
	 * @return the namespaces
	 */
	public String getNamespaces() {
		return namespaces;
	}

	/**
	 * @param namespaces
	 *            the namespaces to set
	 */
	public void setNamespaces(String namespaces) {
		this.namespaces = namespaces;
	}

	/**
	 * DefaultSOAPAPICallHandler acts as the base SOAPAPICallHandler.
	 * 
	 * @deprecated
	 * 
	 * @param rawPayLoad
	 *            Raw SOAP payload that goes into SOAP:BODY
	 * @param namespaces
	 *            Namespace attributes that should be appended to SOAP:ENVELOPE,
	 *            this argument can take any valid String value, empty String or
	 *            NULL. If the value is NULL {0} value is sandwiched between
	 *            SOAP:HEADER element that can be used for decorating purpose
	 * @param headerString
	 *            SOAP header String that should be appended to SOAP:HEADER,
	 *            this argument can take any valid String value, empty String or
	 *            NULL. If the value is NULL {1} value is sandwiched between
	 *            SOAP:HEADER element that can be used for decorating purpose
	 */
	public DefaultSOAPAPICallHandler(String rawPayLoad, String namespaces,
			String headerString) {
		super();
		this.rawPayLoad = rawPayLoad;
		this.namespaces = namespaces;
		this.headerString = headerString;
	}

	/**
	 * DefaultSOAPAPICallHandler acts as the base SOAPAPICallHandler.
	 * 
	 * @param rawPayLoad
	 *            Raw SOAP payload that goes into SOAP:BODY
	 * @param namespaces
	 *            Namespace attributes that should be appended to SOAP:ENVELOPE,
	 *            this argument can take any valid String value, empty String or
	 *            NULL. If the value is NULL, {0} value is added to
	 *            SOAP:ENVELOPE (ex: <soapenv:Envelope
	 *            {0}>...</soapenv:Envelope) element that can be used for
	 *            decorating purpose
	 * @param headerString
	 *            SOAP header String that should be appended to SOAP:HEADER,
	 *            this argument can take any valid String value, empty String or
	 *            NULL. If the value is NULL, {1} is placed between SOAP:HEADER
	 *            (ex: <soapenv:Header>{1}</soapenv:Header>)element that can be
	 *            used for decorating purpose
	 * @param configurationMap
	 *            {@link Map} used for Dynamic configuration, mandatory
	 *            parameter
	 */
	public DefaultSOAPAPICallHandler(String rawPayLoad, String namespaces,
			String headerString, Map<String, String> configurationMap) {
		if (configurationMap == null) {
			throw new IllegalArgumentException(
					"configurationMap cannot be null");
		}
		this.rawPayLoad = rawPayLoad;
		this.namespaces = namespaces;
		this.headerString = headerString;
		this.configurationMap = SDKUtil.combineDefaultMap(configurationMap);
	}

	/**
	 * <code>DefaultSOAPAPICallHandler</code> taking
	 * {@link XMLMessageSerializer} instance for SOAP Body part. SOAP Header
	 * part is set in {@link BaseAPIContext} as Application Header property (The
	 * Application Header should be an instance of {@link XMLMessageSerializer}
	 * ). Dynamic configuration can be set using the configurationMap property
	 * of {@link BaseAPIContext} which will take higher precedence than the one
	 * set in the Service level. ConfigurationMap is treated as a mandatory
	 * parameter picked either from {@link BaseAPIContext}
	 * configurationMap parameter or configurationMap argument in that order of precedence.
	 * 
	 * @param soapBodyContent
	 *            SOAP Body Serializer
	 * @param baseAPIContext
	 *            {@link BaseAPIContext} instance
	 * @param configurationMap
	 *            ConfigurationMap used for Dynamic configuration
	 * @param methodName
	 *            SOAP API operation name
	 */
	public DefaultSOAPAPICallHandler(XMLMessageSerializer soapBodyContent,
			BaseAPIContext baseAPIContext,
			Map<String, String> configurationMap, String methodName) {
		Map<String, String> configMap = (baseAPIContext != null && baseAPIContext
				.getConfigurationMap() != null) ? baseAPIContext
				.getConfigurationMap() : configurationMap;

		if (configMap == null) {
			throw new IllegalArgumentException(
					"configurationMap cannot be null");
		}
		this.configurationMap = SDKUtil.combineDefaultMap(configMap);
		this.baseAPIContext = baseAPIContext;
		this.methodName = methodName;
		if (baseAPIContext != null) {
			this.soapHeaderContent = (XMLMessageSerializer) baseAPIContext
					.getSOAPHeader();
		}
		this.soapBodyContent = soapBodyContent;
	}

	public Map<String, String> getHeaderMap() {
		Map<String, String> headersMap = null;
		if (baseAPIContext != null) {
			headersMap = baseAPIContext.getHTTPHeaders();
		}
		if (headersMap == null) {
			headersMap = new HashMap<String, String>();
		}

		// Append HTTP Content-Type text/xml
		headersMap.put(Constants.HTTP_CONTENT_TYPE_HEADER,
				Constants.HTTP_CONTENT_TYPE_XML);
		return headersMap;
	}

	public String getPayLoad() {
		String payload = null;
		if (soapBodyContent != null) {
			try {
				payload = getSoapEnvelope();
			} catch (Exception e) {
				throw new RuntimeException("Exception ["
						+ e.getClass().getSimpleName()
						+ "] in creating PayLoad", e);
			}
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(getSoapEnvelopeStart());
			stringBuilder.append(getSoapHeaderStart());
			stringBuilder.append(getSoapHeaderEnd());
			stringBuilder.append(getSoapBodyStart());
			stringBuilder.append(getSoapBodyEnd());
			stringBuilder.append(getSoapEnvelopeEnd());
			payload = stringBuilder.toString();
		}

		return payload;
	}

	public String getEndPoint() {
		return this.configurationMap.get(Constants.ENDPOINT);
	}

	public ICredential getCredential() {
		return null;
	}

	public void validate() throws ClientActionRequiredException {
		return;
	}

	private String getSoapEnvelopeStart() {
		String envelope = null;
		if (namespaces != null) {
			envelope = MessageFormat.format(SOAP_ENV_START,
					new Object[] { namespaces });
		} else {
			envelope = SOAP_ENV_START;
		}
		return envelope;
	}

	private String getSoapEnvelopeEnd() {
		return SOAP_ENV_END;
	}

	private String getSoapHeaderStart() {
		String header = null;
		if (headerString != null) {
			header = MessageFormat.format(SOAP_HEAD_START, new Object[] { null,
					headerString });
		} else {
			header = SOAP_HEAD_START;
		}
		return header;
	}

	private String getSoapHeaderEnd() {
		return SOAP_HEAD_END;
	}

	private String getSoapBodyStart() {
		String body = null;
		if (rawPayLoad != null) {
			body = MessageFormat.format(SOAP_BODY_START, new Object[] { null,
					null, rawPayLoad });
		} else {
			body = SOAP_BODY_START;
		}
		return body;
	}

	private String getSoapBodyEnd() {
		return SOAP_BODY_END;
	}

	/**
	 * Returns the SOAP Envelope as a String
	 * 
	 * @return SOAP Envelope as String
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private String getSoapEnvelope()
			throws TransformerFactoryConfigurationError, TransformerException,
			ParserConfigurationException, SAXException, IOException {
		return nodeToString(getSoapEnvelopeAsNode());
	}

	/**
	 * Returns the SOAP Envelope as a {@link Node}
	 * 
	 * @return SOAP Envelope as a {@link Node}
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Node getSoapEnvelopeAsNode() throws ParserConfigurationException,
			SAXException, IOException {
		Node envelopeNode = null;
		Document soapDocument = getSoapEnvelopeAsDocument();
		if (soapHeaderContent != null) {
			Node headerContentNode = soapDocument.importNode(
					getNode(soapHeaderContent.toXMLString()), true);
			soapDocument
					.getDocumentElement()
					.getElementsByTagNameNS(
							SOAP_ENV_NS,
							SOAP_HEADER_QNAME.substring(SOAP_HEADER_QNAME
									.indexOf(':') + 1)).item(0)
					.appendChild(headerContentNode);
		}
		if (soapBodyContent != null) {
			Node bodyContentNode = soapDocument.importNode(
					getNode(soapBodyContent.toXMLString()), true);
			soapDocument
					.getDocumentElement()
					.getElementsByTagNameNS(
							SOAP_ENV_NS,
							SOAP_BODY_QNAME.substring(SOAP_BODY_QNAME
									.indexOf(':') + 1)).item(0)
					.appendChild(bodyContentNode);
		}
		envelopeNode = soapDocument.getDocumentElement();
		return envelopeNode;
	}

	/**
	 * Creates a {@link Document} for SOAP Envelope
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 */
	private Document getSoapEnvelopeAsDocument()
			throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DOMImplementation domImpl = factory.newDocumentBuilder()
				.getDOMImplementation();
		Document soapDocument = domImpl.createDocument(SOAP_ENV_NS,
				SOAP_ENVELOPE_QNAME, null);

		// Add any namespaces set in XmlNamespaceProvider implementation
		setNamespaces(soapDocument.getDocumentElement());
		Element soapHeader = soapDocument.createElementNS(SOAP_ENV_NS,
				SOAP_HEADER_QNAME);
		Element soapBody = soapDocument.createElementNS(SOAP_ENV_NS,
				SOAP_BODY_QNAME);
		soapDocument.getDocumentElement().appendChild(soapHeader);
		soapDocument.getDocumentElement().appendChild(soapBody);
		return soapDocument;
	}

	/**
	 * Adds namespaces to the provided {@link Element}
	 * 
	 * @param element
	 */
	private void setNamespaces(Element element) {
		if (element != null && xmlNamespaceProvider != null) {
			for (Entry<String, String> entry : xmlNamespaceProvider
					.getNamespaceMap().entrySet()) {
				element.setAttribute(XMLNS_ATTRIBUTE_PREFIX
						+ entry.getKey().trim(), entry.getValue().trim());
			}
		}
	}

	/**
	 * Returns a {@link Node} object by parsing the String content of the node
	 * 
	 * @param nodeAsString
	 *            String content of the node
	 * @return {@link Node} corresponding the String content
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Node getNode(String nodeAsString)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		InputSource inStream = new InputSource();
		inStream.setCharacterStream(new StringReader(nodeAsString));
		Document doc = dBuilder.parse(inStream);
		return doc.getChildNodes().item(0);
	}

	/**
	 * Returns the content of the {@link Node} object as a String
	 * 
	 * @param node
	 *            {@link Node} to be converted to String
	 * @return String representation of the {@link Node}
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	private String nodeToString(Node node)
			throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter stringWriter = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(node), new StreamResult(
				stringWriter));
		return stringWriter.toString();
	}

}
