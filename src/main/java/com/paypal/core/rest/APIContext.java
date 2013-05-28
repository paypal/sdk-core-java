package com.paypal.core.rest;

import java.util.Map;
import java.util.UUID;

/**
 * <code>APIContext</code> holds wire-level parameters for the API call.
 * AccessToken is treated as a mandatory parameter. RequestId is generated if
 * not supplied
 * 
 * @author kjayakumar
 * 
 */
public class APIContext {

	/**
	 * Access Token
	 */
	private String accessToken;

	/**
	 * Request Id
	 */
	private String requestId;

	/**
	 * Parameter to mask RequestId
	 */
	private boolean maskRequestId;

	/**
	 * Configuration Map used for dynamic configuration
	 */
	private Map<String, String> configurationMap;

	/**
	 * Default Constructor
	 */
	public APIContext() {

	}

	/**
	 * APIContext
	 * 
	 * @param accessToken
	 *            AccessToken required for the call.
	 */
	public APIContext(String accessToken) {
		if (accessToken == null || accessToken.length() <= 0) {
			throw new IllegalArgumentException("AccessToken cannot be null");
		}
		this.accessToken = accessToken;
	}

	/**
	 * APIContext
	 * 
	 * @param accessToken
	 *            AccessToken required for the call.
	 * @param requestId
	 *            Unique requestId required for the call.
	 */
	public APIContext(String accessToken, String requestId) {
		this(accessToken);
		if (requestId == null || requestId.length() <= 0) {
			throw new IllegalArgumentException("RequestId cannot be null");
		}
		this.requestId = requestId;
	}

	/**
	 * Returns the Access Token
	 * 
	 * @return Access Token
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Returns the unique requestId set during creation, if not available and if
	 * maskRequestId is set to false returns a generated one, else returns null.
	 * 
	 * @return requestId
	 */
	public String getRequestId() {
		String reqId = null;
		if (!maskRequestId) {
			if (requestId == null || requestId.length() <= 0) {
				requestId = UUID.randomUUID().toString();
			}
			reqId = requestId;
		}
		return reqId;
	}

	/**
	 * @return the configurationMap
	 */
	public Map<String, String> getConfigurationMap() {
		return configurationMap;
	}

	/**
	 * @param configurationMap
	 *            the configurationMap to set
	 */
	public void setConfigurationMap(Map<String, String> configurationMap) {
		this.configurationMap = configurationMap;
	}

	/**
	 * @param maskRequestId
	 *            the maskRequestId to set
	 */
	public void setMaskRequestId(boolean maskRequestId) {
		this.maskRequestId = maskRequestId;
	}

}
