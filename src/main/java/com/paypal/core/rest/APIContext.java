package com.paypal.core.rest;

import java.util.Map;
import java.util.UUID;

/**
 * <code>APIContext</code> wraps wire-level parameters for the API call.
 * AccessToken, which is essentially a OAuth token, is treated as a mandatory
 * parameter for (PayPal REST APIs). RequestId is generated if not supplied for
 * marking Idempotency of the API call. OAuth token can be generated using
 * {@link OAuthTokenCredential}
 * 
 * @author kjayakumar
 * 
 */
public class APIContext {

	/**
	 * OAuth Token
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
	 * Custom HTTP headers
	 */
	private Map<String, String> headersMap;
	
	/**
	 * Default Constructor
	 */
	public APIContext() {

	}

	/**
	 * APIContext, requestId is auto generated, calling setMaskRequestId(true)
	 * will override the requestId getter to return null
	 * 
	 * @param accessToken
	 *            OAuthToken required for the call. OAuth token used by the REST
	 *            API service. The token should be of the form 'Bearer xxxx..'.
	 *            See {@link OAuthTokenCredential} to generate OAuthToken
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
	 *            OAuthToken required for the call. OAuth token used by the REST
	 *            API service. The token should be of the form 'Bearer xxxx..'.
	 *            See {@link OAuthTokenCredential} to generate OAuthToken
	 * @param requestId
	 *            Unique requestId required for the call. Idempotency id,
	 *            Calling setMaskRequestId(true) will override the requestId
	 *            getter to return null, which can be used by the client (null
	 *            check) to forcibly not sent requestId in the API call.
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

	/**
	 * @return the headersMap
	 */
	public Map<String, String> getHeadersMap() {
		return headersMap;
	}

	/**
	 * @param headersMap the headersMap to set
	 */
	public void setHeadersMap(Map<String, String> headersMap) {
		this.headersMap = headersMap;
	}

}
