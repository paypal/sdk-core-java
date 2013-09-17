package com.paypal.core;

import java.util.Map;

import com.paypal.core.message.XMLMessageSerializer;

public class BaseAPIContext {
	
	private Map<String, String> HTTPHeaders;
	
	private Map<String, String> configurationMap;
	
	private XMLMessageSerializer SOAPHeader;
	
	public BaseAPIContext() {
		
	}

	/**
	 * @return the hTTPHeaders
	 */
	public Map<String, String> getHTTPHeaders() {
		return HTTPHeaders;
	}

	/**
	 * @param httpHeaders the httpHeaders to set
	 */
	public void setHTTPHeaders(Map<String, String> httpHeaders) {
		HTTPHeaders = httpHeaders;
	}

	/**
	 * @return the configurationMap
	 */
	public Map<String, String> getConfigurationMap() {
		return configurationMap;
	}

	/**
	 * @param configurationMap the configurationMap to set
	 */
	public void setConfigurationMap(Map<String, String> configurationMap) {
		this.configurationMap = configurationMap;
	}

	/**
	 * @return the sOAPHeader
	 */
	public XMLMessageSerializer getSOAPHeader() {
		return SOAPHeader;
	}

	/**
	 * @param soapHeader the soapHeader to set
	 */
	public void setSOAPHeader(XMLMessageSerializer soapHeader) {
		SOAPHeader = soapHeader;
	}
	
}
